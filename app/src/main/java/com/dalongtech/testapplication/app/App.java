package com.dalongtech.testapplication.app;

import android.annotation.SuppressLint;
import android.app.Application;

import com.dalongtech.analysis.AnalysisAgent;
import com.dalongtech.analysis.AnalysysConfig;
import com.dalongtech.analysis.BuildConfig;

import java.lang.reflect.InvocationTargetException;

/**
 * Author:xianglei
 * Date: 2019-12-14 11:43
 * Description:
 */
public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        AnalysysConfig config = new AnalysysConfig();
        config.setAppKey("test-key");
        config.setAutoProfile(true);
        config.setChannel("testChannel");
        config.setPartnerCode("随便传传");
        AnalysisAgent.init(this, config);
        AnalysisAgent.setUploadURL(this, "http://116.62.6.159:18306/v1");
        AnalysisAgent.setMaxCacheSize(this, 101);
        AnalysisAgent.setMaxEventSize(this, 10);
        AnalysisAgent.setIntervalTime(this, 10);
        AnalysisAgent.setDebugMode(this, BuildConfig.DEBUG ? 1 : 0);
    }

    public static synchronized App get() {
        if(instance != null) return instance;
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object at = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(at);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            instance = (App) app;
            return instance;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }
}
