package com.dalongtech.analysis.utils;

import android.text.TextUtils;

/**
 * Author:xianglei
 * Date: 2019-12-30 15:56
 * Description:捕获解析报错
 */
public class ParseUtil {

    public static int toInt(String s) {
        if(TextUtils.isEmpty(s)) return 0;
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long toLong(String s) {
        if(TextUtils.isEmpty(s)) return 0;
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
