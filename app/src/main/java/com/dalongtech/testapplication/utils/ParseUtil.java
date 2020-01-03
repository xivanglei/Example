package com.dalongtech.testapplication.utils;

/**
 * Author:xianglei
 * Date: 2019-12-30 15:56
 * Description:捕获解析报错
 */
public class ParseUtil {

    public static int toInt(String s) {
        if(s == null) return 0;
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
