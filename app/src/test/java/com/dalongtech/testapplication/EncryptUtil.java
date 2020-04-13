package com.dalongtech.testapplication;

import android.text.TextUtils;
import android.util.Base64;

import com.dalongtech.testapplication.utils.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by sunmoon on 2017/4/24 0024.
 */
public class EncryptUtil {

    /**
     * 官网 yun
     */
    public final static String TYPE_OFFICAL_NETWORK_SECRET = "officalNetworkSecret";

    /**
     * 总控 tech
     */
    public final static String TYPE_TOTAL_CONTROL_SECRET = "totalControlSecret";

    /**
     * 和驱动交互的key值
     */
    public final static String TYPE_DRIVE_SECRET = "type_drive_secret";

    /**
     * 换位加密
     */
    public static String encrypt(String str) {
        String strTemp = str;
        char arr[] = new char[str.length()];
        int length;
        for (int i = 0; i < strTemp.length(); i++) {
            arr[i] = (char) (strTemp.charAt(i) + 2);
        }
        length = strTemp.length() / 2;
        for (int j = 0; j < length; j++) {
            arr[j] = (char) (arr[j] ^ arr[j + length]);
            arr[j + length] = (char) (arr[j] ^ arr[j + length]);
            arr[j] = (char) (arr[j] ^ arr[j + length]);
        }
        return new String(arr).substring(0, strTemp.length());
    }


    /**
     * 换位解密
     */
    public static String decrypt(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String strTemp = str;
        char arr[] = new char[str.length()];
        for (int i = 0; i < strTemp.length(); i++) {
            arr[i] = strTemp.charAt(i);
        }
        int length;
        length = strTemp.length() / 2;
        for (int j = 0; j < length; j++) {
            arr[j] = (char) (arr[j] ^ arr[j + length]);
            arr[j + length] = (char) (arr[j] ^ arr[j + length]);
            arr[j] = (char) (arr[j] ^ arr[j + length]);
        }
        for (int i = 0; i < strTemp.length(); i++) {
            arr[i] = (char) (arr[i] - 2);
        }
        return new String(arr).substring(0, strTemp.length());
    }

    /**
     * md5加密
     * @param info
     * @return
     */
    public static byte[] md5Original(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            return md5.digest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * MD5加密(32位)
     */
    public static String MD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 大写MD5加密（32位）
     * @param info
     * @return
     */
    public static String MajusculeMd5(String info){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString().toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * base64 加密
     */
    public static String base64(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        try {
            str = "vjxd@" + str;
            byte[] encode = str.getBytes("UTF-8");
            // base64 加密
            return new String(Base64.encode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }

    /**
     * AES 加密
     * @param value
     * @return
     */
    public static String encryptAES(String value, String type) {
        byte[] requestSecretKey = new byte[0];
        if (type.equals(TYPE_OFFICAL_NETWORK_SECRET)) {
            requestSecretKey = md5Original("DlClientPost2018");
        } else if (type.equals(TYPE_TOTAL_CONTROL_SECRET)) {
            requestSecretKey = md5Original("DLSERVERCLIENT2018");
        } else if (type.equals(TYPE_DRIVE_SECRET)) {
            requestSecretKey = md5Original("ETWSYBZ");
        }

        try {
            IvParameterSpec iv = new IvParameterSpec(requestSecretKey);
            SecretKeySpec skeySpec = new SecretKeySpec(requestSecretKey, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.encodeToString(encrypted,Base64.DEFAULT);
        } catch (Exception ex) {
        }

        return "";
    }

    /**
     * AES 解密
     * @param encrypted
     * @return
     */
    public static String decryptAES(String encrypted, String type) {
        String jsonStr = "";
        byte[] responseSecretKey = new byte[0];
        if(type.equals(TYPE_OFFICAL_NETWORK_SECRET)) {
            responseSecretKey = md5Original("ServerReturnData2018");
        } else if(type.equals(TYPE_TOTAL_CONTROL_SECRET)) {
            responseSecretKey = md5Original("DLSERVERCLIENT2018");
        } else if (type.equals(TYPE_DRIVE_SECRET)) {
            responseSecretKey = md5Original("ETWSYBZ");
        }

        try {
            IvParameterSpec iv = new IvParameterSpec(responseSecretKey);
            SecretKeySpec skeySpec = new SecretKeySpec(responseSecretKey, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            jsonStr = new String(cipher.doFinal(Base64.decode(encrypted,Base64.DEFAULT)));
        } catch (Exception ex) {
        }

        return jsonStr;
    }

    /**
     * 请求参数加密（注：params.size > 1）
     * @param params
     * @return
     */
    public static Map<String,String> encryptParams(Map<String,String> params, String type) {
        try {
            //todo 这里解析成json字符串
            String jsonStr = JsonUtil.toJson(params);
            params.clear();
            params.put("en_w",MD5("AES2"));
            if(type.equals(TYPE_OFFICAL_NETWORK_SECRET)) {
                params.put("data",EncryptUtil.encryptAES(jsonStr, TYPE_OFFICAL_NETWORK_SECRET));
            } else if(type.equals(TYPE_TOTAL_CONTROL_SECRET)) {
                params.put("data",EncryptUtil.encryptAES(jsonStr, TYPE_TOTAL_CONTROL_SECRET));
            }
        } catch (Exception e) {
        }
        return params;
    }





}


