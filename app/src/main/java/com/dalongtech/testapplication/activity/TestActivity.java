package com.dalongtech.testapplication.activity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.CheckBox;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.AES;
import com.dalongtech.testapplication.utils.AES256;
import com.dalongtech.testapplication.utils.LogUtil;
import com.dalongtech.testapplication.utils.StringUtil;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends SimpleActivity {

    private String content = "{\"cmd\":\"test\",\"data\":{\"a\":\"cc\"},\"ext\":{}}";
    private String password = "DlClientPost2019";

    private byte[] encryptResult;


    @Override
    protected int getLayoutById() {
        return R.layout.activity_test;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {




    }

    @OnClick(R.id.btn_test)
    public void test() {
        encrypt();
    }

    @OnClick(R.id.btn_test2)
    public void test2() {
        String decryptResult = AES256.AES_cbc_decrypt(encryptResult, password);
        LogUtil.d("解密：" + decryptResult);
    }

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private void encrypt() {
        byte[] hash = md5(password);
        LogUtil.d("DlClientPost2019  toMd5:  " + hash);
//        int salt = new Random().nextInt(8);
        byte[] salt = new byte[]{6};
        byte[] salted;
        byte[] dx = new byte[]{};
        dx = md5(addByte(hash , salt));
        salted = dx;
        print("第一次", salted);
        while (salted.length < 48) {
            byte[] temp = addByte(dx, hash);
            dx = md5(addByte(temp, salt));
            salted = addByte(salted, dx);
        }
        byte[] key = subByte(salted, 0, 32);
        byte[] iv = subByte(salted,32, 16);
        LogUtil.d("salted:  " + salted);
        LogUtil.d("key:  " + key);
        LogUtil.d("iv:  " + iv);

        String srcData = "123";
        print("salted",salted);
        print("key", key);
        print("iv", iv);
        byte[] encrypt = new AES().encrypt(content.getBytes(), key, iv);
        LogUtil.d(byteArrayToHexString(encrypt));
        LogUtil.d(new String(Base64.encode(encrypt, Base64.DEFAULT)));
    }

    private void encryptJS() {
        String hash = md5(password, true);
        LogUtil.d("DlClientPost2019  toMd5:  " + hash);
        LogUtil.d(new String(hash.getBytes()));
        print("hash", hash.getBytes());
//        byte[] encrypt = new AES().encrypt(content.getBytes(), hash.getBytes(), hash.getBytes());
//        LogUtil.d(byteArrayToHexString(encrypt));

    }

    private void print(String tag, byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder(tag);
        stringBuilder.append("-----");
        for(byte b : bytes) {
            stringBuilder.append(b).append(",");
        }
        LogUtil.d(stringBuilder.toString());
    }

    private byte[] addByte(byte[] a, byte[] b) {
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

    private byte[] subByte(byte[] bytes, int start, int length) {
        byte[] result = new byte[length];
        for(int i = start; i < start + length; i++) {
            result[i - start] = bytes[i];
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
}
