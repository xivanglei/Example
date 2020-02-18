package com.dalongtech.testapplication.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.utils.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class NotWaitService extends Service {

    private Disposable mDisposable;
    private NotificationManager notificationManager;
    private String notificationId = "serviceid";
    private String notificationName = "servicename";

    @Override
    public void onCreate() {
        LogUtil.d("onCreate");
        super.onCreate();
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        //创建NotificationChannel
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel channel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(channel);
//        }
//        startForeground(1,getNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("startCommand");
        if(mDisposable != null) {
            mDisposable.dispose();
        }
        Observable.interval(3000, TimeUnit.MILLISECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }
            @Override
            public void onNext(Long aLong) {
                LogUtil.d(aLong);
            }
            @Override
            public void onError(Throwable e) { }

            @Override
            public void onComplete() { }
        });
        return super.onStartCommand(intent, flags, startId);

    }

    public NotWaitService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        LogUtil.d("onDestroy");
        super.onDestroy();
        if(mDisposable != null) mDisposable.dispose();
    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("title")
                .setContentText("text")
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationId);
        }
        Notification notification = builder.build();
        return notification;
    }
}
