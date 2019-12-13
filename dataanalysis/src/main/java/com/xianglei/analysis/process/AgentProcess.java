package com.xianglei.analysis.process;

import android.app.Application;
import android.content.Context;

import com.xianglei.analysis.AnalysysConfig;
import com.xianglei.analysis.AutomaticAcquisition;
import com.xianglei.analysis.constants.Constants;
import com.xianglei.analysis.network.UploadManager;
import com.xianglei.analysis.utils.ANSThreadPool;
import com.xianglei.analysis.utils.CommonUtils;
import com.xianglei.analysis.utils.LogPrompt;
import com.xianglei.analysis.utils.SharedUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Author:xianglei
 * Date: 2019-12-11 16:34
 * Description:
 */
public class AgentProcess {

    private Application mApp = null;

    private static long cacheMaxCount = 0;

    public static AgentProcess getInstance(Context context) {
        ContextManager.setContext(context);
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final AgentProcess INSTANCE = new AgentProcess();
    }

    /**
     * 初始化接口 config,不调用初始化接口: 获取不到key/channel,页面自动采集失效,电池信息采集失效
     */
    public void init(final AnalysysConfig config) {
        final Context context = ContextManager.getContext();
        registerLifecycleCallbacks(context);
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (context != null) {
                        saveKey(context, config.getAppKey());
                        saveChannel(context, config.getChannel());
                        if (CommonUtils.isMainProcess(context)) {
                            // 同时设置 UploadUrl/WebSocketUrl/ConfigUrl
                            setBaseUrl(context, config.getBaseUrl());
                            // 设置首次启动是否发送
                            Constants.isAutoProfile = config.isAutoProfile();
                            // 设置加密类型
                            Constants.encryptType = config.getEncryptType().getType();
                            // 设置渠道归因是否开启
                            Constants.autoInstallation = config.isAutoInstallation();
                            // 重置PV计数器值
                            CommonUtils.resetCount(context.getFilesDir().getAbsolutePath());
                            long MaxDiffTimeInterval = config.getMaxDiffTimeInterval();
                            if (0 <= MaxDiffTimeInterval) {
                                // 用户忽略最大时间差值
                                Constants.ignoreDiffTime = config.getMaxDiffTimeInterval();
                            }
                        }
                        // 设置时间校准是否开启
                        Constants.isTimeCheck = config.isTimeCheck();
                        if (Constants.autoHeatMap) {
                            SystemIds.getInstance(context).parserId();
                        }
                        LifeCycleConfig.initUploadConfig(context);
                        LogPrompt.showInitLog(true);
                    } else {
                        LogPrompt.showInitLog(false);
                    }
                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * 页面启动，或从后台进入（判断依据是之前activity数量为0）
     */
    public void appStart(final boolean isFromBackground, long startTime) {
        try {
            Context context = ContextManager.getContext();
            if (context == null) {
                return;
            }
            HashMap<String, Object> startUpMap = new HashMap<>();
            startUpMap.put(Constants.DEV_IS_FROM_BACKGROUND, isFromBackground);
            JSONObject eventData = DataAssemble.getInstance(context).getEventData(
                    Constants.API_APP_START, Constants.STARTUP, null, startUpMap);
            eventData.put(Constants.X_WHEN, startTime);
            trackEvent(context, Constants.API_APP_START, Constants.STARTUP, eventData);
            if (CommonUtils.isFirstStart(context)) {
                sendProfileSetOnce(context, 0);
                if (Constants.autoInstallation) {
                    sendFirstInstall(context);
                }
            }
        } catch (Throwable throwable) {
        }
    }

    /**
     * 存储channel
     */
    private void saveChannel(Context context, String channel) {
        String appChannel = getNewChannel(context, channel);
        if (!CommonUtils.isEmpty(appChannel)) {
            SharedUtil.setString(context, Constants.SP_CHANNEL, appChannel);
            LogPrompt.showChannelLog(true, channel);
        } else {
            LogPrompt.showChannelLog(false, channel);
        }
    }

    /**
     * 获取 xml channel和 init channel，优先xml
     */
    private String getNewChannel(Context context, String channel) {
        String xmlChannel = CommonUtils.getManifestData(context, Constants.DEV_CHANNEL);
        if (CommonUtils.isEmpty(xmlChannel) && !CommonUtils.isEmpty(channel)) {
            return channel;
        }
        return xmlChannel;
    }

    /**
     * 接口数据处理
     */
    private void trackEvent(Context context, String apiName,
                            String eventName, JSONObject eventData) {
        if (!CommonUtils.isEmpty(eventName) && checkoutEvent(eventData)) {
            // 此处重置重传传次数，解决出于重传状态时，触发新事件重传次数不够三次
            //SharedUtil.remove(mContext, Constants.SP_FAILURE_COUNT);
            if (LogBean.getCode() == Constants.CODE_SUCCESS) {
                LogPrompt.showLog(apiName, true);
            }
            UploadManager.getInstance(context).sendManager(eventName, eventData);
        }
    }

    /**
     * 获取最大缓存条数
     */
    public long getMaxCacheSize() {
        long count;
        count = cacheMaxCount;
        if (count < 1) {
            count = Constants.MAX_CACHE_COUNT;
        }
        return count;
    }

    /**
     * 设置最大缓存条数
     */
    public void setMaxCacheSize(final long count) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (100 < count) {
                    cacheMaxCount = count;
                }
            }
        });
    }

    /**
     * 校验数据是否符合上传格式
     */
    private boolean checkoutEvent(JSONObject eventData) {
        if (CommonUtils.isEmpty(eventData.optString(Constants.APP_ID))) {
            LogPrompt.keyFailed();
            return false;
        }
        return true;
    }

    /**
     * 首次安装后是否发送profile_set_once
     * type 0 表示设置 1表示清除
     */
    private void sendProfileSetOnce(Context context, int type) throws Exception {
        if (Constants.isAutoProfile) {
            Map<String, Object> profileInfo = new HashMap<>();
            if (type == 0) {
                profileInfo.put(Constants.DEV_FIRST_VISIT_TIME,
                        CommonUtils.getFirstStartTime(context));
                profileInfo.put(Constants.DEV_FIRST_VISIT_LANGUAGE,
                        Locale.getDefault().getLanguage());
            } else if (type == 1) {
                profileInfo.put(Constants.DEV_RESET_TIME,
                        CommonUtils.getTime(context));
            } else {
                return;
            }
            JSONObject eventData = DataAssemble.getInstance(context).getEventData(
                    Constants.API_PROFILE_SET_ONCE,
                    Constants.PROFILE_SET_ONCE, null, profileInfo);
            trackEvent(context,
                    Constants.API_PROFILE_SET_ONCE, Constants.PAGE_VIEW, eventData);
        }
    }

    /**
     * 渠道归因
     */
    private void sendFirstInstall(Context context) throws Exception {
        if (context != null) {
            JSONObject eventData = DataAssemble.getInstance(context).getEventData(
                    Constants.API_FIRST_INSTALL, Constants.FIRST_INSTALL,
                    null, Constants.utm);
            trackEvent(context, Constants.API_FIRST_INSTALL,
                    Constants.FIRST_INSTALL, eventData);
        }
    }

    /**
     * 是否自动采集
     */
    public void automaticCollection(final boolean isAuto) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Context context = ContextManager.getContext();
                if (context != null) {
                    SharedUtil.setBoolean(context, Constants.SP_IS_COLLECTION, isAuto);
                }
            }
        });
    }

    /**
     * 获取自动采集开关状态
     */
    public boolean getAutomaticCollection() {
        Context context = ContextManager.getContext();
        if (context != null) {
            return SharedUtil.getBoolean(
                    context, Constants.SP_IS_COLLECTION, true);
        }
        return true;
    }

    /**
     * 获取忽略自动采集的页面
     */
    public List<String> getIgnoredAutomaticCollection() {
        Context context = ContextManager.getContext();
        if (context != null) {
            String activities = SharedUtil.getString(
                    context, Constants.SP_IGNORED_COLLECTION, null);
            if (!CommonUtils.isEmpty(activities)) {
                return CommonUtils.toList(activities);
            }
        }
        return new ArrayList<>();
    }

    /**
     * 忽略多个页面自动采集
     */
    public void setIgnoredAutomaticCollection(final List<String> activitiesName) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context == null) {
                        return;
                    }
                    if (CommonUtils.isEmpty(activitiesName)) {
                        SharedUtil.remove(context, Constants.SP_IGNORED_COLLECTION);
                        return;
                    }
                    String activities = SharedUtil.getString(context,
                            Constants.SP_IGNORED_COLLECTION, null);
                    if (CommonUtils.isEmpty(activities)) {
                        SharedUtil.setString(
                                context, Constants.SP_IGNORED_COLLECTION,
                                CommonUtils.toString(activitiesName));
                    } else {
                        Set<String> pageNames = CommonUtils.toSet(activities);
                        if (pageNames == null) {
                            pageNames = new HashSet<>();
                        }
                        pageNames.addAll(activitiesName);
                        SharedUtil.setString(context,
                                Constants.SP_IGNORED_COLLECTION,
                                CommonUtils.toString(pageNames));
                    }

                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * 页面信息处理
     */
    public void autoCollectPageView(final Map<String, Object> pageInfo) throws Exception {
        Context context = ContextManager.getContext();
        if (context != null) {
            JSONObject eventData = DataAssemble.getInstance(context).getEventData(
                    Constants.API_PAGE_VIEW, Constants.PAGE_VIEW, pageInfo, null);
            trackEvent(context, Constants.API_PAGE_VIEW, Constants.PAGE_VIEW, eventData);
        }
    }


    /**
     * Activity 回调注册
     */
    private void registerLifecycleCallbacks(Context context) {
            if (CommonUtils.isEmpty(mApp)) {
                mApp = (Application) context.getApplicationContext();
                mApp.registerActivityLifecycleCallbacks(new AutomaticAcquisition());
            }
    }

}
