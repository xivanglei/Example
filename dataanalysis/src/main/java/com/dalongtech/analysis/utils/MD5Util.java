package com.dalongtech.analysis.utils;

import java.security.MessageDigest;

/**
 * Author:xianglei
 * Date: 2019-12-30 17:05
 * Description:
 */
public class MD5Util {

    private final static String[] hexDigits = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};

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
    
    public static String md5ToStr(String info) {
        return byteArrayToHexString(md5(info));
    }

    /**
     * 轮换字节数组为十六进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for(int i=0;i<b.length;i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    //将一个字节转化成十六进制形式的字符串
    private static String byteToHexString(byte b){
        int n = b;
        if(n<0)
            n=256+n;
        int d1 = n/16;
        int d2 = n%16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
