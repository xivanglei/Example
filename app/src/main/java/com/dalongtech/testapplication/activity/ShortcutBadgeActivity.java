package com.dalongtech.testapplication.activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.dalongtech.shortcutbadget.ShortcutBadger;
import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ShortcutBadgeActivity extends SimpleActivity {

    @BindView(R.id.et_num)
    EditText et_num;
    @BindView(R.id.tv_home_package)
    TextView tv_home_package;

    private static final String NOTIFICATION_CHANNEL = "net.xianglei.shortcutbadget.ShortcutBadger.example";

    private NotificationManager mNotificationManager;
    private int notificationId = 0;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_shortcut_badge;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        findHomeLauncherPackage();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    //设置角标，除了小米，其余手机都要先设置，再发送通知，小米是直接发送通知附带角标数
    @OnClick(R.id.btn_set_badge)
    public void setBadge() {
        int badgeCount = 0;
        try {
            badgeCount = Integer.parseInt(et_num.getText().toString());
        } catch (NumberFormatException e) {
        }
        if(badgeCount == 0) badgeCount = 66;
        boolean success = ShortcutBadger.applyCount(mContext, badgeCount);
        LogUtil.d("数量=" + badgeCount + "成功？" + success);
    }

    @OnClick(R.id.btn_set_badge_by_notification)
    public void setBadgeByNotification() {
        int badgeCount = 0;
        try {
            badgeCount = Integer.parseInt(et_num.getText().toString());
        } catch (NumberFormatException e) { }
        if(badgeCount == 0) badgeCount = 66;
//        new Thread(() -> {
            mNotificationManager.cancel(notificationId);
            notificationId++;

            Notification.Builder builder = new Notification.Builder(getApplicationContext())
                    .setContentTitle("角标测试")
                    .setContentText("角标")
                    .setSmallIcon(R.mipmap.ic_launcher);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupNotificationChannel();

                builder.setChannelId(NOTIFICATION_CHANNEL);
            }

            Notification notification = builder.build();
            ShortcutBadger.applyNotification(getApplicationContext(), notification, 66);
            mNotificationManager.notify(notificationId, notification);
//        }).start();
        finish();
//        startService(
//                new Intent(mContext, BadgeIntentService.class).putExtra("badgeCount", badgeCount)
//        );

    }

    @OnClick(R.id.btn_remove_badge)
    public void removeBadge() {
        boolean success = ShortcutBadger.removeCount(mContext);
        LogUtil.d("移出成功?" + success);
    }

    private void findHomeLauncherPackage() {
        //find the home launcher Package
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        String currentHomePackage = "none";
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        // in case of duplicate apps (Xiaomi), calling resolveActivity from one will return null
        if (resolveInfo != null) {
            currentHomePackage = resolveInfo.activityInfo.packageName;
        }
        tv_home_package.setText("launcher:" + currentHomePackage);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void setupNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, "xianglei ShortcutBadger Sample",
                NotificationManager.IMPORTANCE_DEFAULT);

        mNotificationManager.createNotificationChannel(channel);
    }
}
