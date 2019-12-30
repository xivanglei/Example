package com.dalongtech.customkeyboard.util;

import java.security.MessageDigest;

/**
 * Author:xianglei
 * Date: 2019-12-30 17:05
 * Description:
 */
public class MD5Util {

    /**
     * md5加密
     * @param info
     * @return
     */
    public static byte[] md5(String info) {
        return md5(info.getBytes());
    }

    /**
     * md5加密
     * @param info
     * @return
     */
    public static byte[] md5(byte[] info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info);
            return md5.digest();
        } catch (Exception e) {
            return null;
        }
    }
}
