package com.dalongtech.testapplication.test;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;

import com.dalongtech.testapplication.utils.JsonUtil;
import com.dalongtech.testapplication.utils.LogUtil;
import com.dalongtech.testapplication.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:xianglei
 * Date: 2020-04-10 10:35
 * Description:
 */
public class LoginUtil {

//    基本地址
//if ("test".equals(BuildConfig.ENVIRONMENT)) {
//        apiUrl = "http://dltech.test.dalongtech.com/";
////            apiUrl ="https://dltech.exam.dalongtech.com/";
//    } else if (("pre").equals(BuildConfig.ENVIRONMENT)) {
//        apiUrl = "http://dltech.wap.pre.dalongtech.com/";
//    } else if ("rc".equals(BuildConfig.ENVIRONMENT)) {
//        apiUrl = "http://zkrc.dalongyun.com/";
//    } else {
//        //release线
//        apiUrl = "http://zkwap.dalongyun.com/";
//    }


    public static void login(Context context, String userName, String userPsw) {
        //    基本地址
//if ("test".equals(BuildConfig.ENVIRONMENT)) {
//        apiUrl = "http://dltech.test.dalongtech.com/";
////            apiUrl ="https://dltech.exam.dalongtech.com/";
//    } else if (("pre").equals(BuildConfig.ENVIRONMENT)) {
//        apiUrl = "http://dltech.wap.pre.dalongtech.com/";
//    } else if ("rc".equals(BuildConfig.ENVIRONMENT)) {
//        apiUrl = "http://zkrc.dalongyun.com/";
//    } else {
//        //release线
//        apiUrl = "http://zkwap.dalongyun.com/";
//    }

        final String psw = EncryptUtil.encrypt(userPsw);
//        用户名密码登录
//        api/index.php/user/login_app
//        接口文档
//        http://wiki.dalongyun.com:90/index.php?s=/13&page_id=48
        Map<String, String> loginParams = getLoginParams(context, userName, psw);
        //todo 这里获取到响应数据后解析成LoginBean
        ApiResponse<LoginBean> response = new ApiResponse<>();
        if(!response.isSuccess()) return;
        LoginBean loginBean = response.getData();
        Map<String, String> checkLoginParams = getCheckLoginParam(context, loginBean, psw);



        //        if ("test".equals(BuildConfig.ENVIRONMENT)) {
//            yunApi = "http://waptest.dalongyun.com/";
////            yunApi ="https://dlyun.exam.dalongyun.com/";
//        } else if ("pre".equals(BuildConfig.ENVIRONMENT)) {
//            yunApi = "http://dlyun.wap.pre.dalongyun.com/";
////            yunApi ="https://dlyun.exam.dalongyun.com/";
//        } else if ("rc".equals(BuildConfig.ENVIRONMENT)) {
//            yunApi = "http://waprc.dalongyun.com/";
//        } else {
//            yunApi = "http://dlyun.wap.slb.dalongyun.com/";
//        }

//        登录成功后调用，后续用来获取token等其它验证
//        api/apk/client_login.php
//        接口文档
//        http://wiki.dalongyun.com:90/index.php?s=/13&page_id=314
        //todo 这里获取到响应数据后解析成BaseEncryptData
        BaseEncryptData encryptData = new BaseEncryptData();
        String jsonData = EncryptUtil.decryptAES(encryptData.getData(), EncryptUtil.TYPE_OFFICAL_NETWORK_SECRET);
        ApiResponse resTemp = JsonUtil.fromJson(jsonData, ApiResponse.class);
        if(resTemp.isSuccess()) {
            ApiResponse<CheckLoginBean> res = new Gson().fromJson(jsonData, new TypeToken<ApiResponse<CheckLoginBean>>() {}.getType());
//            token就在这
            LogUtil.d(res.getData().getToken());
        }
    }



    //生成参数
    public static Map<String, String> getLoginParams(Context context, String userName, String userPsw) {
        Map<String, String> params = new HashMap<>();
        int v = getVersionCode(context, context.getPackageName());
        params.put("uname", userName);
        params.put("upasswd", userPsw);
        params.put("mark", "1");
        params.put("versioncode", "" + v);
        params.put("mac", getMac(context));
        params.put("platform", "0");
            String token = encrypt("17d1d6d22dcfcf2806b0d353ab890ff9,1220817001");
            params.put("channel_code", "dalong_android");
            params.put("token", "" + token);
        params.put("auth", MD5(getAuth(params)));
        return params;
    }
    //版本号
    public static int getVersionCode(Context context, String strPackageName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(strPackageName, 0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            return 0;
        }
    }
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

    public static String getAuth(Map<String, String> params) {
        if (params == null) {
            return "";
        }

        String AuthValue = "ckjal234uivhu453yiva2342342fajiova";

        String[] keySet = new String[params.size()];
        params.keySet().toArray(keySet);
        Arrays.sort(keySet);
        StringBuilder sbAuth = new StringBuilder();
        for (String key : keySet) {
            String value = params.get(key);
            String item = key + "=" + value;
            sbAuth.append(item);
        }
        sbAuth.append(AuthValue);
        return sbAuth.toString();
    }

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

    public static String getMac(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();

        if (null != info) {
            String wifiMac = info.getMacAddress();
            if (wifiMac != null) {
                return wifiMac;
            } else {
                return "null";
            }
        } else {
            return "null";
        }
    }



    public static Map<String, String> getCheckLoginParam(Context context, LoginBean loginBean, String userPsw) {
        Map<String, String> params = new HashMap<>();
        params.put("uname", loginBean.getUname());
        params.put("pwd", userPsw);
        params.put("udid", getDeviceId(context));
        params.put("app_vision", getVersionCode(context, context.getPackageName()) + "");
        params.put("identity", "0");
        params.put("platform", "android");
        params.put("auth", MD5(getAuth(params)));
        return EncryptUtil.encryptParams(params, EncryptUtil.TYPE_OFFICAL_NETWORK_SECRET);
    }

    public static String getDeviceId(Context var0) {
        try {
            String var1 = Settings.Secure.getString(var0.getContentResolver(), "android_id");
            String var2 = Build.SERIAL;
            return MD5(var1 + var2);
        } catch (Exception var3) {
            return "";
        }
    }

    public static class LoginBean {

        private String token;
        private String push_tag;
        private String uname;
        private String type;

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getPush_tag() {
            return push_tag;
        }

        public void setPush_tag(String push_tag) {
            this.push_tag = push_tag;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }


        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("uname=").append(this.uname).append("&type=").append(type);
            return stringBuilder.toString();
        }


    }

    public static class BaseEncryptData {


        @SerializedName("en_w")
        private String enW;
        private String data;


        public String getEnW() {
            return enW;
        }

        public void setEnW(String enW) {
            this.enW = enW;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public boolean isSuccess() {
            return StringUtil.isNotBlank(data);
        }


    }

    /**
     * 网络请求返回数据的数据接口
     */
    public static class ApiResponse<T> {

        // 新数据
        public static final int Status_New = 100;
        // 使用旧数据
        public static final int Status_Old = 101;

        private boolean success;
        private int status;
        private String msg;
        private String show;
        private String last_modify_time;
        public T data;

        public String getShow() {
            return show;
        }

        public void setShow(String show) {
            this.show = show;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public String getLast_modify_time() {
            return last_modify_time;
        }

        public void setLast_modify_time(String last_modify_time) {
            this.last_modify_time = last_modify_time;
        }

    }

    public static class CheckLoginBean {

        private String token;
        private String saltkey;
        private String auth;
        private String wss_token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getSaltkey() {
            return saltkey;
        }

        public void setSaltkey(String saltkey) {
            this.saltkey = saltkey;
        }

        public String getAuth() {
            return auth;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }

        public String getWss_token() {
            return wss_token;
        }

        public void setWss_token(String wss_token) {
            this.wss_token = wss_token;
        }
    }

}
