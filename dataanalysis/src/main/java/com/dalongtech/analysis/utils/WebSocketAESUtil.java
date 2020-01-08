package com.dalongtech.analysis.utils;

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
public class WebSocketAESUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String WEB_SOCKET_PASSWORD = "DlClientPost2019";

    public static String encryptAES(String data) {
        byte[] salt = getRandomBytes(8);
        byte[] key = getKeyBytes(WEB_SOCKET_PASSWORD, salt);
        byte[] encrypt = encrypt(data, Arrays.copyOfRange(key, 0, 32), Arrays.copyOfRange(key, 32, 48));
        byte[] extra = addByte("Salted__".getBytes(), salt);
        return new String(Base64.encode(addByte(extra, encrypt), Base64.DEFAULT));
    }

    public static String decryptAES(String data) {
        byte[] bytes = Base64.decode(data, Base64.DEFAULT);
        byte[] salt = Arrays.copyOfRange(bytes, 8, 16);
        bytes = Arrays.copyOfRange(bytes, 16, bytes.length);
        byte[] key = getKeyBytes(WEB_SOCKET_PASSWORD, salt);
        return decrypt(bytes, Arrays.copyOfRange(key, 0, 32), Arrays.copyOfRange(key, 32, 48));
    }

    private static byte[] getKeyBytes(String pw, byte[] salt) {
//        byte[] hash = md5(pw);
        byte[] hash = MD5Util.md5ToStr(pw).getBytes();
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
        return getAESTransResult(data.getBytes(), key, keyIv, true);
    }

    private static String decrypt(byte[] data, byte[] key, byte[] keyIv) {
        return new String(getAESTransResult(data, key, keyIv, false));
    }

    private static byte[] getAESTransResult(byte[] data, byte[] key, byte[] keyIv, boolean isEncrypt) {
        try {
            IvParameterSpec iv = new IvParameterSpec(keyIv);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, skeySpec, iv);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getRandomBytes(int length) {
        byte[] result = new byte[length];
        new Random().nextBytes(result);
        return result;
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
