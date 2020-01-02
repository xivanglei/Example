package com.dalongtech.testapplication.app;

import android.app.Application;

import com.dalongtech.analysis.AnalysisAgent;
import com.dalongtech.analysis.AnalysysConfig;

/**
 * Author:xianglei
 * Date: 2019-12-14 11:43
 * Description:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AnalysysConfig config = new AnalysysConfig();
        config.setAppKey("test-key");
        config.setAutoProfile(true);
        config.setChannel("testChannel");
//        config.setPartnerCode("随便传传");
        AnalysisAgent.init(this, config);
        AnalysisAgent.setUploadURL(this, "http://116.62.6.159:18306/v1");
        AnalysisAgent.setMaxCacheSize(this, 101);
        AnalysisAgent.setMaxEventSize(this, 10);
        AnalysisAgent.setIntervalTime(this, 10);
        AnalysisAgent.setDebugMode(this, 0);
    }
}
