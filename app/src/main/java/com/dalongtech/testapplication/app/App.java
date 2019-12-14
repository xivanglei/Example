package com.dalongtech.testapplication.app;

import android.app.Application;

import com.xianglei.analysis.AnalysisAgent;
import com.xianglei.analysis.AnalysysConfig;
import com.xianglei.analysis.EncryptEnum;

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
        config.setAutoInstallation(true);
        config.setAutoProfile(true);
        config.setChannel("testChannel");
        config.setEncryptType(EncryptEnum.EMPTY);
        AnalysisAgent.init(this, config);
        AnalysisAgent.setUploadURL(this, "http://www.baidu.com");
    }
}
