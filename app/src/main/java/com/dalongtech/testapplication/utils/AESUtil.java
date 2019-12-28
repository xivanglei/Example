package com.dalongtech.testapplication.utils;

import android.util.Base64;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Author:xianglei
 * Date: 2019-12-28 10:40
 * Description:AES加密工具
 */
public class AESUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String WEB_SOCKET_PASSWORD = "DlClientPost2019";


    public static String encryptAES(String data) {
        byte[] salt = getRandomBytes(8);
        byte[] key = getKeyBytes(WEB_SOCKET_PASSWORD, salt);
        byte[] encrypt = encrypt(data, Arrays.copyOfRange(key, 0, 32), Arrays.copyOfRange(key, 32, 48));
        LogUtil.d("第一次加密" + byteArrayToHexString(encrypt));
        byte[] extra = addByte("Salted__".getBytes(), salt);
        String result = new String(Base64.encode(addByte(extra, encrypt), Base64.DEFAULT));
        LogUtil.d(result);
        return result;
    }

    public static String decryptAES(String data) {
        byte[] bytes = Base64.decode(data, Base64.DEFAULT);
        byte[] salt = Arrays.copyOfRange(bytes, 8, 16);
        bytes = Arrays.copyOfRange(bytes, 16, bytes.length);
        LogUtil.d("第一次解密" + byteArrayToHexString(bytes));
        byte[] key = getKeyBytes(WEB_SOCKET_PASSWORD, salt);
        String result = decrypt(bytes, Arrays.copyOfRange(key, 0, 32), Arrays.copyOfRange(key, 32, 48));
        LogUtil.d(result);
        return result;

    }

    private static byte[] getKeyBytes(String pw, byte[] salt) {
        byte[] hash = md5(pw);
        byte[] salted;
        byte[] dx;
        byte[] tp = addByte(hash , salt);
        dx = md5(tp);
        salted = dx;
        while (salted.length < 48) {
            byte[] temp = addByte(dx, hash);
            dx = md5(addByte(temp, salt));
            salted = addByte(salted, dx);
        }
        return salted;
    }

    private static byte[] encrypt(String data, byte[] key, byte[] keyIv) {
        try {
            IvParameterSpec iv = new IvParameterSpec(keyIv);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            return cipher.doFinal(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String decrypt(byte[] data, byte[] key, byte[] keyIv) {
        try {
            IvParameterSpec iv = new IvParameterSpec(keyIv);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            return new String(cipher.doFinal(data));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static byte[] getRandomBytes(int length) {
        byte[] result = new byte[length];
        new Random().nextBytes(result);
        print("random", result);
        return result;
    }

    private static void print(String tag, byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder(tag);
        stringBuilder.append("-----");
        for(byte b : bytes) {
            stringBuilder.append(b).append(",");
        }
        LogUtil.d(stringBuilder.toString());
    }

    private static byte[] addByte(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        for(int i = 0; i < result.length; i++) {
            if(i < a.length) {
                result[i] = a[i];
            } else {
                result[i] = b[i - a.length];
            }
        }
        return result;
    }


    /**
     * md5加密
     * @param info
     * @return
     */
    public static String md5(String info, boolean isToStr) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] digest = md5.digest();
            return isToStr ? byteArrayToHexString(digest) : new String(digest);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * md5加密
     * @param info
     * @return
     */
    public static byte[] md5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] digest = md5.digest();
            return digest;
        } catch (Exception e) {
            return null;
        }
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
            byte[] digest = md5.digest();
            return digest;
        } catch (Exception e) {
            return null;
        }
    }

    private final static String[] hexDigits = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};

    /**
     * 轮换字节数组为十六进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b){
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

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    private static byte charToByte(char c) {
        byte result = (byte) "0123456789ABCDEF".indexOf(c);
        LogUtil.d(result);
        return result;
    }
}
