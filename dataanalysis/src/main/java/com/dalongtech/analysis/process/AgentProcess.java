package com.dalongtech.analysis.process;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dalongtech.analysis.AnalysysConfig;
import com.dalongtech.analysis.AutomaticAcquisition;
import com.dalongtech.analysis.constants.Constants;
import com.dalongtech.analysis.database.TableAllInfo;
import com.dalongtech.analysis.network.UploadManager;
import com.dalongtech.analysis.utils.ANSLog;
import com.dalongtech.analysis.utils.ANSThreadPool;
import com.dalongtech.analysis.utils.CheckUtils;
import com.dalongtech.analysis.utils.CommonUtils;
import com.dalongtech.analysis.utils.InternalAgent;
import com.dalongtech.analysis.utils.LogPrompt;
import com.dalongtech.analysis.utils.LogUtil;
import com.dalongtech.analysis.utils.NumberFormat;
import com.dalongtech.analysis.utils.SharedUtil;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
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
    private String mTitle = "", mUrl = "";
    private Map<String, Object> properties;

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
     * 应用关闭或回到桌面
     */
    public void appEnd(String eventTime, final JSONObject realTimeField) {
        try {
            long time = NumberFormat.convertToLong(eventTime);
            if (time > 0) {
                Context context = ContextManager.getContext();
                if (context != null && realTimeField != null) {
                    JSONObject endData = DataAssemble.getInstance(context).getEventData(
                            Constants.API_APP_END, Constants.END, null, null);
                    endData.put(Constants.X_WHEN, time);
                    JSONObject xContData = endData.optJSONObject(Constants.X_CONTEXT);
                    CommonUtils.mergeJson(realTimeField, xContData);
                    endData.put(Constants.X_CONTEXT, xContData);
                    trackEvent(context, Constants.API_APP_END, Constants.END, endData);
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
            LogUtil.d(eventName + "----" + eventData.toString());
            UploadManager.getInstance(context).sendManager(eventName, eventData);
        }
    }

    /**
     * 存储key
     * 使用缓存的key，与传入的key对比
     */
    private void saveKey(Context context, String key) {
        String appKey = getNewKey(context, key);
        String spKey = CommonUtils.getAppKey(context);
        if (!CommonUtils.isEmpty(appKey)) {
            SharedUtil.setString(context, Constants.SP_APP_KEY, appKey);
            if (!CommonUtils.isEmpty(spKey) && !TextUtils.equals(spKey, appKey)) {
                resetUserInfo(context);
            }
            LogPrompt.showKeyLog(true, key);
        } else {
            LogPrompt.showKeyLog(false, key);
        }
    }

    /**
     * reset 接口 重置所有属性
     */
    private void resetUserInfo(Context context) {
        // 重置首次访问
        SharedUtil.remove(context, Constants.SP_FIRST_START_TIME);
        // 重置首日访问
        SharedUtil.remove(context, Constants.DEV_IS_FIRST_DAY);
        // 重置 通用属性
        SharedUtil.remove(context, Constants.SP_SUPER_PROPERTY);
        // 重置 alias id
        CommonUtils.setIdFile(context, Constants.SP_ALIAS_ID, "");
        // 重置identify
        CommonUtils.setIdFile(context, Constants.SP_DISTINCT_ID, "");
        // 修改 isLogin
        SharedUtil.remove(context, Constants.SP_IS_LOGIN);
        // 重置original id
        SharedUtil.remove(context, Constants.SP_ORIGINAL_ID);
        //  清空数据库
        TableAllInfo.getInstance(context).deleteAll();
    }

    /**
     * 读取XML和init中的key，校验后返回
     */
    private String getNewKey(Context context, String key) {
        String xmlKey = CommonUtils.getManifestData(context, Constants.DEV_APPKEY);
        if (!CommonUtils.isEmpty(key)) {
            if (!CommonUtils.isEmpty(xmlKey)) {
                return TextUtils.equals(xmlKey, key) ? key : null;
            } else {
                return key;
            }
        } else {
            return xmlKey;
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
                    Constants.API_PROFILE_SET_ONCE, Constants.PROFILE_SET_ONCE, eventData);
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
     * debug 信息处理
     */
    public void setDebug(final int debug) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Context context = ContextManager.getContext();
                if (context == null || debug < 0 || 2 < debug) {
                    Log.w("analysys", Constants.API_SET_DEBUG_MODE + ": set failed!");
                    return;
                }
                debugResetUserInfo(context, debug);
                SharedUtil.setInt(context, Constants.SP_USER_DEBUG, debug);
                if (debug != 0) {
                    ANSLog.isShowLog = true;
                }
                LogPrompt.showLog(Constants.API_SET_DEBUG_MODE, true);
            }
        });
    }

    /**
     * debug切换判断是否需要重置发送
     */
    private void debugResetUserInfo(Context context, int debugNum) {
        int debug = SharedUtil.getInt(context, Constants.SP_USER_DEBUG, 0);
        if (debug == 1 && (debugNum == 0 || debugNum == 2)) {
            resetUserInfo(context);
        }
    }

    /**
     * 存储url
     */
    public void setUploadURL(final String url) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context != null && !CommonUtils.isEmpty(url)) {
                        String getUrl;
                        if (url.startsWith(Constants.HTTP)) {
                            //去除空格去除/
                            getUrl = CommonUtils.checkUrl(url);
                        } else if (url.startsWith(Constants.HTTPS)) {
                            getUrl = CommonUtils.checkUrl(url);
                        } else {
                            LogPrompt.showLog(Constants.API_SET_UPLOAD_URL, false);
                            return;
                        }
                        if (!CommonUtils.isEmpty(getUrl)) {
                            saveUploadUrl(context, getUrl + "/up");
                        } else {
                            LogPrompt.showErrLog(LogPrompt.URL_ERR);
                        }
                    }
                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * 存储 upload url
     */
    private void saveUploadUrl(Context context, String uploadUrl) throws MalformedURLException {
        // 判断是否进行时间校准且为主进程
        if (Constants.isTimeCheck && CommonUtils.isMainProcess(context)) {
            UploadManager.getInstance(context).sendGetTimeMessage();
        }
        changeUrlResetUser(context, uploadUrl);
        SharedUtil.setString(context, Constants.SP_USER_URL, uploadUrl);
    }

    /**
     * 修改URL重置用户属性
     */
    private void changeUrlResetUser(Context context, String url) throws MalformedURLException {
        String spServiceUrl = SharedUtil.getString(
                context, Constants.SP_SERVICE_URL, null);
        if (CommonUtils.isEmpty(spServiceUrl)) {
            String spUserUrl = SharedUtil.getString(
                    context, Constants.SP_USER_URL, null);
            if (CommonUtils.isEmpty(spUserUrl)) {
                return;
            }
            String urlHost = new URL(url).getHost();
            String userUrlHost = new URL(spUserUrl).getHost();
            if (!CommonUtils.isEmpty(urlHost)
                    && !CommonUtils.isEmpty(userUrlHost)
                    && !urlHost.equals(userUrlHost)) {
                resetUserInfo(context);
            }
        }
    }

    /**
     * 用户自定义上传间隔时间
     */
    public void setIntervalTime(final long time) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Context context = ContextManager.getContext();
                if (context != null && 1 < time) {
                    SharedUtil.setLong(
                            context, Constants.SP_USER_INTERVAL_TIME, time * 1000);
                }
            }
        });
    }

    /**
     * 用户自定义上传条数
     */
    public void setMaxEventSize(final long count) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Context context = ContextManager.getContext();
                if (context != null && 1 < count) {
                    SharedUtil.setLong(
                            context, Constants.SP_USER_EVENT_COUNT, count);
                }
            }
        });
    }

    /**
     * 用户调用上传接口
     */
    public void flush() {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Context context = ContextManager.getContext();
                if (context != null) {
                    UploadManager.getInstance(context).flushSendManager();
                }
            }
        });
    }

    /**
     * alias id
     */
    public void alias(final String aliasId, final String originalId) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context != null) {
                        if (!CheckUtils.checkIdLength(aliasId)) {
                            LogPrompt.showLog(Constants.API_ALIAS, LogBean.getLog());
                            return;
                        }
                        if (!CommonUtils.isEmpty(originalId)
                                && !CheckUtils.checkOriginalIdLength(originalId)) {
                            LogPrompt.showLog(Constants.API_ALIAS, LogBean.getLog());
                            return;
                        }
                        String original = originalId;
                        if (CommonUtils.isEmpty(original)) {
                            original = CommonUtils.getDistinctId(context);
                        } else {
                            SharedUtil.setString(context, Constants.SP_ORIGINAL_ID, original);
                        }
                        CommonUtils.setIdFile(context, Constants.SP_ALIAS_ID, aliasId);
                        SharedUtil.setInt(context, Constants.SP_IS_LOGIN, 1);

                        Map<String, Object> aliasMap = new HashMap<>();
                        aliasMap.put(Constants.ORIGINAL_ID, original);

                        JSONObject eventData = DataAssemble.getInstance(context).getEventData(
                                Constants.API_ALIAS, Constants.ALIAS, aliasMap, null);

                        if (!CommonUtils.isEmpty(eventData)) {
                            trackEvent(context, Constants.API_ALIAS, Constants.ALIAS, eventData);
                            sendProfileSetOnce(context, 0);
                        } else {
                            LogPrompt.showLog(Constants.API_ALIAS, false);
                        }
                    }
                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * distinct id 存储
     */
    public void identify(final String distinctId) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Context context = ContextManager.getContext();
                if (context == null) {
                    return;
                }
                if (!CheckUtils.checkIdLength(distinctId)) {
                    LogPrompt.showLog(Constants.SP_DISTINCT_ID, LogBean.getLog());
                    return;
                }
                CommonUtils.setIdFile(context, Constants.SP_DISTINCT_ID, distinctId);

            }
        });
    }

    /**
     * 获取distinctId
     */
    public String getDistinctId() {
        Context context = ContextManager.getContext();
        if (context != null) {
            return CommonUtils.getDistinctId(context);
        }
        return "";
    }

    /**
     * 清除本地所有id、超级属性、重新生成id
     */
    public void reset() {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context != null) {
                        resetUserInfo(context);
                        CommonUtils.setIdFile(context, Constants.SP_UUID, "");
                        LogPrompt.showLog(Constants.API_RESET, true);
                        sendProfileSetOnce(context, 1);
                    }
                } catch (Throwable throwable) {
                    LogPrompt.showLog(Constants.API_RESET, false);
                }
            }
        });
    }

    /**
     * 多个事件信息处理
     */
    public void track(final String eventName, final Map<String, Object> eventDetail) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> eventInfo = CommonUtils.deepCopy(eventDetail);
                    Context context = ContextManager.getContext();
                    if (context == null) {
                        LogPrompt.showLog(Constants.API_TRACK, false);
                        return;
                    }
                    JSONObject eventData = DataAssemble.getInstance(context).getEventData(
                                Constants.API_TRACK, Constants.TRACK,
                                eventInfo, null, eventName);
                    trackEvent(context, Constants.API_TRACK, eventName, eventData);
                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * 页面信息处理
     */
    public void pageView(final Context context,
                         final String pageName, final Map<String, Object> pageDetail) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            mUrl = activity.getClass().getCanonicalName();
            mTitle = String.valueOf(activity.getTitle());
        }
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context == null) {
                        return;
                    }
                    Map<String, Object> pageInfo = CommonUtils.deepCopy(pageDetail);
                    pageInfo.put(Constants.EVENT_PAGE_NAME, pageName);

                    Map<String, Object> autoCollectPageInfo = new HashMap<>();
                    if (!pageInfo.containsKey(Constants.PAGE_URL)) {
                        autoCollectPageInfo.put(Constants.PAGE_URL, mUrl);
                    }
                    if (!pageInfo.containsKey(Constants.PAGE_TITLE)) {
                        autoCollectPageInfo.put(Constants.PAGE_TITLE, mTitle);
                    }
                    JSONObject eventData = DataAssemble.getInstance(context).getEventData(
                            Constants.API_PAGE_VIEW, Constants.PAGE_VIEW,
                            pageInfo, autoCollectPageInfo);

                    trackEvent(context, Constants.API_PAGE_VIEW, Constants.PAGE_VIEW, eventData);
                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * 注册单条通用属性
     */
    public void registerSuperProperty(final String key, final Object value) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context == null) {
                        return;
                    }
                    Map<String, Object> property = new HashMap<>();
                    property.put(key, value);

                    if (CheckUtils.checkParameter(Constants.API_REGISTER_SUPER_PROPERTY,
                            property)) {
                        if (LogBean.getCode() == Constants.CODE_SUCCESS) {
                            LogPrompt.showLog(Constants.API_REGISTER_SUPER_PROPERTY, true);
                        }
                        saveSuperProperty(context, property);
                    } else {
                        LogPrompt.showLog(Constants.API_REGISTER_SUPER_PROPERTY, LogBean.getLog());
                    }
                } catch (Throwable throwable) {
                }

            }
        });
    }

    /**
     * 注册多条公共属性
     */
    public void registerSuperProperties(final Map<String, Object> propertyDetail) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context == null) {
                        LogPrompt.showLog(
                                Constants.API_REGISTER_SUPER_PROPERTIES, LogBean.getLog());
                        return;
                    }
                    Map<String, Object> propertyInfo = CommonUtils.deepCopy(propertyDetail);
                    if (CheckUtils.checkParameter(
                            Constants.API_REGISTER_SUPER_PROPERTIES, propertyInfo)) {
                        if (LogBean.getCode() == Constants.CODE_SUCCESS) {
                            LogPrompt.showLog(
                                    Constants.API_REGISTER_SUPER_PROPERTIES, true);
                        }
                        saveSuperProperty(context, propertyInfo);
                    }
                } catch (Throwable throwable) {
                }
            }

        });
    }

    /**
     * 存储通用属性
     */
    private void saveSuperProperty(Context context,
                                   Map<String, Object> superProperty) throws Exception {
        JSONObject propertyInfo;
        if (!CommonUtils.isEmpty(superProperty)) {
            String sharedProperty = SharedUtil.getString(
                    context, Constants.SP_SUPER_PROPERTY, null);
            if (!CommonUtils.isEmpty(sharedProperty)) {
                propertyInfo = new JSONObject(sharedProperty);
                CommonUtils.mergeJson(new JSONObject(superProperty), propertyInfo);
            } else {
                propertyInfo = new JSONObject(superProperty);
            }
            SharedUtil.setString(
                    context, Constants.SP_SUPER_PROPERTY, String.valueOf(propertyInfo));
        }
    }

    /**
     * 删除超级属性
     */
    public void unregisterSuperProperty(final String superPropertyName) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context != null && !CommonUtils.isEmpty(superPropertyName)) {
                        String property = SharedUtil.getString(
                                context, Constants.SP_SUPER_PROPERTY, null);

                        if (!CommonUtils.isEmpty(property)) {
                            JSONObject json = new JSONObject(property);
                            json.remove(superPropertyName);
                            SharedUtil.setString(
                                    context, Constants.SP_SUPER_PROPERTY, String.valueOf(json));
                            LogPrompt.showLog(
                                    Constants.API_UNREGISTER_SUPER_PROPERTY, true);
                        }
                    }
                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * 删除所有超级属性
     */
    public void clearSuperProperty() {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context != null) {
                        SharedUtil.remove(context, Constants.SP_SUPER_PROPERTY);
                        LogPrompt.showLog(
                                Constants.API_CLEAR_SUPER_PROPERTIES, true);
                    }
                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * 用户获取super Property
     */
    public Map<String, Object> getSuperProperty() {
        try {
            Context context = ContextManager.getContext();
            if (context == null) {
                return new HashMap<>();
            }
            String superProperty = SharedUtil.getString(context,
                    Constants.SP_SUPER_PROPERTY, null);
            if (!CommonUtils.isEmpty(superProperty)) {
                return CommonUtils.jsonToMap(new JSONObject(superProperty));
            }
        } catch (Throwable throwable) {
        }
        return new HashMap<>();
    }

    /**
     * 用户获取超级属性
     */
    public Object getSuperProperty(String propertyKey) {
        try {
            Context context = ContextManager.getContext();
            if (context != null && !CommonUtils.isEmpty(propertyKey)) {
                String superProperty = SharedUtil.getString(
                        context, Constants.SP_SUPER_PROPERTY, null);
                if (!CommonUtils.isEmpty(superProperty)) {
                    return new JSONObject(superProperty).opt(propertyKey);
                }
            }
        } catch (Throwable throwable) {
        }
        return null;
    }

    /**
     * profile set 键值对
     */
    public void profileSet(final String propertyKey, final Object propertyValue) {
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put(propertyKey, propertyValue);
        profileSet(propertyMap);
    }

    /**
     * profile set json
     */
    public void profileSet(final Map<String, Object> profileDetail) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context != null) {
                        Map<String, Object> profileInfo = CommonUtils.deepCopy(profileDetail);
                        JSONObject eventData = DataAssemble.getInstance(context).getEventData(
                                Constants.API_PROFILE_SET,
                                Constants.PROFILE_SET, profileInfo, null);
                        trackEvent(context, Constants.API_PROFILE_SET,
                                Constants.PROFILE_SET, eventData);
                    }
                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * profileSetOnce json
     */
    public void profileSetOnce(final Map<String, Object> profileDetail) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context != null) {
                        Map<String, Object> profileInfo = CommonUtils.deepCopy(profileDetail);
                        JSONObject eventData = DataAssemble.getInstance(context).getEventData(
                                Constants.API_PROFILE_SET_ONCE,
                                Constants.PROFILE_SET_ONCE, profileInfo, null);

                        trackEvent(context, Constants.API_PROFILE_SET_ONCE,
                                Constants.PROFILE_SET_ONCE, eventData);
                    } else {
                        LogPrompt.showLog(Constants.API_PROFILE_SET_ONCE, false);
                    }
                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * profile set 键值对
     */
    public void profileSetOnce(final String profileKey, final Object profileValue) {
        Context context = ContextManager.getContext();
        if (context != null) {
            Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put(profileKey, profileValue);
            profileSetOnce(propertyMap);
        } else {
            LogPrompt.showLog(Constants.API_PROFILE_SET_ONCE, false);
        }
    }

    /**
     * profile increment
     */
    public void profileIncrement(final Map<String, ? extends Number> profileDetail) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context != null) {
                        Map<String, ? extends Number> profileInfo =
                                CommonUtils.deepCopy(profileDetail);
                        JSONObject eventData = DataAssemble.getInstance(context).getEventData(
                                Constants.API_PROFILE_INCREMENT,
                                Constants.PROFILE_INCREMENT, profileInfo, null);

                        trackEvent(context, Constants.API_PROFILE_INCREMENT,
                                Constants.PROFILE_INCREMENT, eventData);
                    } else {
                        LogPrompt.showLog(Constants.API_PROFILE_INCREMENT, false);
                    }
                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * profile increment
     */
    public void profileIncrement(final String profileKey, final Number profileValue) {
        Context context = ContextManager.getContext();
        if (context != null) {
            Map<String, Number> profileMap = new HashMap<>();
            profileMap.put(profileKey, profileValue);
            profileIncrement(profileMap);
        } else {
            LogPrompt.showLog(Constants.API_PROFILE_INCREMENT, false);
        }
    }

    /**
     * 给一个列表类型的Profile增加一个元素
     */
    public void profileAppend(final Map<String, Object> profileDetail) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context != null) {
                        Map<String, Object> profileInfo = CommonUtils.deepCopy(profileDetail);
                        JSONObject eventData = DataAssemble.getInstance(context).getEventData(
                                Constants.API_PROFILE_APPEND,
                                Constants.PROFILE_APPEND, profileInfo, null);

                        trackEvent(context, Constants.API_PROFILE_APPEND,
                                Constants.PROFILE_APPEND, eventData);
                    } else {
                        LogPrompt.showLog(Constants.API_PROFILE_APPEND, false);
                    }
                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * profile append
     */
    public void profileAppend(final String propertyName, final Object propertyValue) {
        Context context = ContextManager.getContext();
        if (context != null) {
            Map<String, Object> properMap = new HashMap<>();
            properMap.put(propertyName, propertyValue);
            profileAppend(properMap);
        } else {
            LogPrompt.showLog(Constants.API_PROFILE_APPEND, false);
        }
    }

    /**
     * 给一个列表类型的Profile增加一个或多个元素
     */
    public void profileAppend(final String profileKey, final List<Object> profileValue) {
        Context context = ContextManager.getContext();
        if (context != null) {
            Map<String, Object> profileMap = new HashMap<>();
            profileMap.put(profileKey, profileValue);
            profileAppend(profileMap);
        } else {
            LogPrompt.showLog(Constants.API_PROFILE_APPEND, false);
        }
    }

    /**
     * 删除单个用户属性
     */
    public void profileUnset(final String propertyKey) {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context != null && !CommonUtils.isEmpty(propertyKey)) {
                        Map<String, Object> profileMap = new HashMap<>();
                        profileMap.put(propertyKey, "");

                        JSONObject eventData = DataAssemble.getInstance(context).getEventData(
                                Constants.API_PROFILE_UNSET,
                                Constants.PROFILE_UNSET, profileMap, null);

                        trackEvent(context, Constants.API_PROFILE_UNSET,
                                Constants.PROFILE_UNSET, eventData);

                    } else {
                        LogPrompt.showLog(Constants.API_PROFILE_UNSET, false);
                    }
                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * 清除所有用户属性
     */
    public void profileDelete() {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = ContextManager.getContext();
                    if (context == null) {
                        return;
                    }
                    JSONObject eventData = DataAssemble.getInstance(context).getEventData(
                            Constants.API_PROFILE_DELETE,
                            Constants.PROFILE_DELETE, null, null);

                    trackEvent(context, Constants.API_PROFILE_DELETE,
                            Constants.PROFILE_DELETE, eventData);

                } catch (Throwable throwable) {
                }
            }
        });
    }

    /**
     * 获取预置属性
     */
    public Map<String, Object> getPresetProperties() {
        try {
            Context context = ContextManager.getContext();
            if (context != null) {
                if (properties == null) {
                    properties = new HashMap<>();
                    properties.put("$time_zone", InternalAgent.getTimeZone(context));
                    properties.put("$platform", "Android");
                    properties.put("$app_version", InternalAgent.getVersionName(context));
                    properties.put("$language", InternalAgent.getDeviceLanguage(context));
                    properties.put("$lib_version", InternalAgent.getLibVersion(context));
                    properties.put("$lib", "Android");
                    properties.put("$screen_width", InternalAgent.getScreenWidth(context));
                    properties.put("$screen_height", InternalAgent.getScreenHeight(context));
                    properties.put("$network", InternalAgent.getNetwork(context));
                    properties.put("$manufacturer", InternalAgent.getManufacturer(context));
                    properties.put("$mac", InternalAgent.getMac(context));
                    properties.put("$imei", InternalAgent.getIMEI(context));
                    properties.put("$first_visit_time", CommonUtils.getFirstStartTime(context));
                    properties.put("$brand", InternalAgent.getBrand(context));
                    properties.put("$model", InternalAgent.getDeviceModel(context));
                    properties.put("$os", "Android");
                    return properties;
                } else {
                    return properties;
                }
            }
        } catch (Throwable throwable) {
        }
        return new HashMap<>();
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
