package com.dalongtech.magicmirror.utils;

import android.util.Log;

/**
 * Author:xianglei
 * Date: 2018/12/23 11:37 AM
 * Description:
 */
public class LogUtil {

    private static boolean IS_ERR = true;
    private static boolean IS_DEBUG = true;
    private static boolean IS_WARN = true;
    private static boolean IS_INFO = true;
    private static boolean IS_VERBOSE = true;

    private static final String TAG = "DL_MM";

    private static final int START_STACK_INDEX = 4;
    private static final int PRINT_STACK_COUNT = 4;
    private static boolean LOG_DETAIL = false;

    public static void setDebugMode(boolean debug) {
        if (!debug) {
            IS_ERR = false;
            IS_DEBUG = false;
            IS_WARN = false;
            IS_INFO = false;
            IS_VERBOSE = false;
        }
    }

    public static void setDebugDetail(boolean needDetail) {
        LOG_DETAIL = needDetail;
    }

    /**
     * 得到tag（所在类.方法（L:行））
     * @return
     */
    private static String generateTag(int startStackIndex) {
        return generateTag(TAG, startStackIndex);
    }

    private static String generateTag(String tag, int startStackIndex) {
        if(tag == null) tag = TAG;
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[startStackIndex];
        String callerClazzName = stackTraceElement.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        String result = "%s.%s(L:%d)";
        result = String.format(result, new Object[]{callerClazzName, stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber())});
        //给tag设置前缀
        result = tag + ":" + result;
        return result;
    }

    private static String appendStack(int startStackIndex) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        if (stacks != null && stacks.length > startStackIndex) {
            int lastIndex = Math.min(stacks.length-1, startStackIndex + PRINT_STACK_COUNT);
            for (int i=lastIndex; i >= startStackIndex; i--) {
                if (stacks[i] == null) {
                    continue;
                }
                String fileName = stacks[i].getFileName();
                if (fileName != null) {
                    int dotIndx = fileName.indexOf('.');
                    if (dotIndx > 0) {
                        fileName = fileName.substring(0, dotIndx);
                    }
                }

                sb.append(fileName);
                sb.append('(');
                sb.append(stacks[i].getLineNumber());
                sb.append(")");
                sb.append("->");
            }
            sb.append(stacks[startStackIndex].getMethodName());
        }
        return sb.toString();
//        sb.append('\n');
    }

    public static void v(String msg) {
        String tag = generateTag(START_STACK_INDEX);
        Log.v(tag, msg);
    }

    public static void v(String msg, Throwable tr) {
        String tag = generateTag(START_STACK_INDEX);
        Log.v(tag, msg, tr);
    }

    public static void d(String tag, Object msg, int startStackIndex) {
        if (IS_DEBUG) {
            String logTag = generateTag(tag, startStackIndex);
            String content = cleanStr(msg);
            if(LOG_DETAIL) content = "[" + appendStack(startStackIndex) + "]---->: " + content;
            Log.d(logTag, content);
        }
    }

    public static void d(String tag, Object msg) {
        d(tag, msg, START_STACK_INDEX + 1);
    }

    public static void d(Object msg) {
        d(null, msg, START_STACK_INDEX + 1);
    }

    public static void d() {
        d(null, "......", START_STACK_INDEX + 1);
    }

    public static void i(String msg) {
        String tag = generateTag(START_STACK_INDEX);
        Log.i(tag, msg);
    }

    public static void i(String msg, Throwable tr) {
        String tag = generateTag(START_STACK_INDEX);
        Log.i(tag, msg, tr);
    }

    public static void w(String msg) {
        String tag = generateTag(START_STACK_INDEX);
        Log.w(tag, msg);
    }

    public static void w(String msg, Throwable tr) {
        String tag = generateTag(START_STACK_INDEX);
        Log.w(tag, msg, tr);
    }

    public static void e(String msg) {
        String tag = generateTag(START_STACK_INDEX);
        Log.e(tag, msg);
    }

    public static void e(String msg, Throwable tr) {
        String tag = generateTag(START_STACK_INDEX);
        Log.e(tag, msg, tr);
    }

    public static void wtf(String msg) {
        String tag = generateTag(START_STACK_INDEX);
        Log.wtf(tag, msg);
    }

    public static void wtf(String msg, Throwable tr) {
        String tag = generateTag(START_STACK_INDEX);
        Log.wtf(tag, msg, tr);
    }

    private static String cleanStr(Object msg) {
        String result = msg == null ? "null" : msg.toString().equals("") ? "空字符" : msg.toString();
        return result.length() > 3000 ? result.substring(0, 3000) : result;

    }
}