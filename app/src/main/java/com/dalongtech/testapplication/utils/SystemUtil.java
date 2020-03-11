package com.dalongtech.testapplication.utils;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import com.dalongtech.testapplication.app.App;

import java.util.Iterator;
import java.util.List;

/**
 * Author:xianglei
 * Date: 2018/12/23 12:31 PM
 * Description:
 */
public class SystemUtil {

    /**
     * 获取版本名
     */
    public static String getVersionName() {
        PackageManager packageManager = App.get().getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(App.get().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
    /**
     * 获取版本号
     */
    public static int getVersionCode() {
        PackageManager packageManager = App.get().getPackageManager();
        PackageInfo packageInfo;
        int versionCode = 0;
        try {
            packageInfo = packageManager.getPackageInfo(App.get().getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 判断是否是主进程
     *
     * 如果app启用了远程的service，此application:onCreate会被调用2次
     * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
     */
    public static boolean isMainProcess() {
        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase(App.get().getPackageName())) {
            // 则此application的onCreate 是被service 调用的，返回false
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param pid 进程的id
     * @return 返回进程的名字
     */
    private static String getAppName(int pid) {
        String processName = null;
        ActivityManager activityManager =
                (ActivityManager) App.get().getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info =
                    (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName;
                    // 返回当前进程名
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }

    /**
     * @方法说明:判断当前应用程序是否后台运行
     * @方法名称:isBackground
     * @param context
     * @return
     * @返回值:boolean
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    // 后台运行
                    return true;
                } else {
                    // 前台运行
                    return false;
                }
            }
        }
        return false;
    }

    //获取设备唯一号
    public static String getEquipmentId(){
        String androidID = Settings.Secure.getString(App.get().getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        return id;
    }

    //回到桌面
    public static void backHome(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }



    private static String getJenkinsBuildNum() {
        ApplicationInfo appInfo = null;
        try {
            appInfo = App.get().getPackageManager().getApplicationInfo(App.get().getPackageName(), PackageManager.GET_META_DATA);
            int msg = appInfo.metaData.getInt("JENKINS_BUILD_NUMBER");
            return String.valueOf(msg);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void copyToClip(String content) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) App.get().getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("learn", content);
// 将ClipData内容放到系统剪贴板里。
        if( cm != null) cm.setPrimaryClip(mClipData);
    }

}
