package com.xianglei.analysis.process;

import android.app.Application;
import android.content.Context;

import com.xianglei.analysis.AnalysysConfig;
import com.xianglei.analysis.AutomaticAcquisition;
import com.xianglei.analysis.constants.Constants;
import com.xianglei.analysis.utils.ANSThreadPool;
import com.xianglei.analysis.utils.CommonUtils;

/**
 * Author:xianglei
 * Date: 2019-12-11 16:34
 * Description:
 */
public class AgentProcess {

    private Application mApp = null;

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
     * Activity 回调注册
     */
    private void registerLifecycleCallbacks(Context context) {
            if (CommonUtils.isEmpty(mApp)) {
                mApp = (Application) context.getApplicationContext();
                mApp.registerActivityLifecycleCallbacks(new AutomaticAcquisition());
            }
    }

}
