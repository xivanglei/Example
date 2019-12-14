package com.xianglei.analysis.utils;

import android.util.Log;

/**
 * Author:xianglei
 * Date: 2018/12/23 11:37 AM
 * Description:
 */
public class LogUtil {

    private static final String TAG = "祥雷测试";
    /**
     * 得到tag（所在类.方法（L:行））
     * @return
     */
    private static String generateTag() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        String callerClazzName = stackTraceElement.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        String result = "%s.%s(L:%d)";
        result = String.format(result, new Object[]{callerClazzName, stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber())});
        //给tag设置前缀
        result = TAG + ":" + result;
        return result;
    }

    public static void v(String msg) {
            String tag = generateTag();
            Log.v(tag, msg);
    }

    public static void v(String msg, Throwable tr) {
            String tag = generateTag();
            Log.v(tag, msg, tr);
    }

    public static void d(String msg) {
            String tag = generateTag();
            Log.d(tag, msg == null ? "空" : msg);
    }
    public static void d(Object msg) {
            String tag = generateTag();
            Log.d(tag, msg == null ? "空" : msg.toString());
    }

    public static void d(String msg, Throwable tr) {
            String tag = generateTag();
            Log.d(tag, msg, tr);
    }

    public static void i(String msg) {
            String tag = generateTag();
            Log.i(tag, msg);
    }

    public static void i(String msg, Throwable tr) {
            String tag = generateTag();
            Log.i(tag, msg, tr);
    }

    public static void w(String msg) {
            String tag = generateTag();
            Log.w(tag, msg);
    }

    public static void w(String msg, Throwable tr) {
            String tag = generateTag();
            Log.w(tag, msg, tr);
    }

    public static void e(String msg) {
            String tag = generateTag();
            Log.e(tag, msg);
    }

    public static void e(String msg, Throwable tr) {
            String tag = generateTag();
            Log.e(tag, msg, tr);
    }

    public static void wtf(String msg) {
            String tag = generateTag();
            Log.wtf(tag, msg);
    }

    public static void wtf(String msg, Throwable tr) {
            String tag = generateTag();
            Log.wtf(tag, msg, tr);
    }
}
