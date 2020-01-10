package com.dalongtech.testapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.dalongtech.testapplication.component.DialogHelper;
import com.dalongtech.testapplication.utils.LogUtil;

/**
 * Author:xianglei
 * Date: 2020-01-10 16:57
 * Description:
 */
public class TestService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d("onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("onStartCommand");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogHelper.getInstance().show();
            }
        }, 10000);
        return super.onStartCommand(intent, flags, startId);
    }
}
