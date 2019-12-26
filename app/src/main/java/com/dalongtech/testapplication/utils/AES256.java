package com.dalongtech.testapplication.utils;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * java实现AES256加密解密 
 * 依赖说明： 
 * bcprov-jdk15-133.jar：PKCS7Padding 
 * javabase64-1.3.1.jar：base64 
 * local_policy.jar 和 US_export_policy.jar需添加到%JAVE_HOME%\jre\lib\security中（lib中版本适合jdk1.7） 
 */  
  
public class AES256 {

    static {
        /*AES 加密默认128位的key，这里改成256位的（类在下面粘出来了）*/
        UnlimitedKeyStrengthJurisdictionPolicy.ensure();
    }

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    //加密
    public static byte[] AES_cbc_encrypt(String srcData,String password) {
        byte[] key = md5Original(password);
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(key));
            byte[] encData = cipher.doFinal(srcData.getBytes());
            return Base64.encode(encData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //解密
    public static String AES_cbc_decrypt(byte[] encData, String password) {
        byte[] data = Base64.decode(encData, Base64.DEFAULT);
        byte[] key = md5Original(password);
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(key));
            byte[] decbbdt = cipher.doFinal(data);
            return new String(decbbdt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
  
    public static byte[] encrypt(String content, String password) {
        try {  
            //"AES"：请求的密钥算法的标准名称  
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            //256：密钥生成参数；securerandom：密钥生成器的随机源  
            SecureRandom securerandom = new SecureRandom(tohash256Deal(md5Original(password)));
            kgen.init(256, securerandom);  
            //生成秘密（对称）密钥  
            SecretKey secretKey = kgen.generateKey();
            //返回基本编码格式的密钥  
            byte[] enCodeFormat = secretKey.getEncoded();  
            //根据给定的字节数组构造一个密钥。enCodeFormat：密钥内容；"AES"：与给定的密钥内容相关联的密钥算法的名称  
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            //创建一个实现指定转换的 Cipher对象，该转换由指定的提供程序提供。  
            //"AES/ECB/PKCS7Padding"：转换的名称；"BC"：提供程序的名称
            IvParameterSpec iv = new IvParameterSpec(md5Original(password));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
  
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] byteContent = content.getBytes();
            byte[] cryptograph = cipher.doFinal(byteContent);  
            return cryptograph;
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    public static String decrypt(byte[] cryptograph, String password) {  
        try {  
            KeyGenerator kgen = KeyGenerator.getInstance("AES");  
            SecureRandom securerandom = new SecureRandom(tohash256Deal(md5Original(password)));
            kgen.init(256, securerandom);  
            SecretKey secretKey = kgen.generateKey();  
            byte[] enCodeFormat = secretKey.getEncoded();  
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            IvParameterSpec iv = new IvParameterSpec(md5Original(password));
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] content = cipher.doFinal(cryptograph);
            LogUtil.d(content);
            return new String(content);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < buf.length; i++) {  
            String hex = Integer.toHexString(buf[i] & 0xFF);  
            if (hex.length() == 1) {  
                hex = '0' + hex;  
            }  
            sb.append(hex.toUpperCase());  
        }  
        return sb.toString();  
    }  
  
    /*private static byte[] parseHexStr2Byte(String hexStr) { 
        if (hexStr.length() < 1) 
            return null; 
        byte[] result = new byte[hexStr.length()/2]; 
        for (int i = 0;i< hexStr.length()/2; i++) { 
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16); 
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16); 
            result[i] = (byte) (high * 16 + low); 
        } 
        return result; 
    }*/  
      
    private static byte[] tohash256Deal(byte[] datastr) {
        try {  
            MessageDigest digester= MessageDigest.getInstance("SHA-256");
            digester.update(datastr);
            byte[] hex=digester.digest();  
            return hex;   
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());    
        }  
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
}  