package com.dalongtech.magicmirror.utils;

import android.content.Context;
import android.os.Build;

import com.dalongtech.magicmirror.constants.Constants;
import com.dalongtech.magicmirror.constants.ExtraConst;
import com.dalongtech.magicmirror.process.ContextManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:xianglei
 * Date: 2019-12-12 17:46
 * Description:基本参数，自动添加的
 */
public class ParameterAddUtil {

    public static void putXContextInfo(Map<String, Object> map, String eventName) {
        Context context = ContextManager.getContext();
        putXContextBase(map, context);
        switch (eventName) {
            case Constants.STARTUP:
            case Constants.END:
                map.put(ExtraConst.C_IS_FIRST_DAY, CommonUtils.isFirstDay(context));
                map.put(ExtraConst.C_IS_FIRST_TIME, CommonUtils.isFirstStart(context));
                map.put(ExtraConst.SCREEN_WIDTH, CommonUtils.getScreenWidth(context));
                map.put(ExtraConst.SCREEN_HEIGHT, CommonUtils.getScreenHeight(context));
                map.put(Constants.USER, CommonUtils.getUserId(context));
                break;
            case Constants.LOGIN:
                map.put(ExtraConst.SYSTEM_VERSION, Build.VERSION.SDK_INT);
                map.put(Constants.USER, CommonUtils.getUserId(context));
                break;
        }
    }

    //基本参数，所有事件都带
    private static void putXContextBase(Map<String, Object> map, Context context) {
        map.put(ExtraConst.VIP_GRADE, CommonUtils.getVipGrade(context));
        map.put(ExtraConst.C_NETWORK, CommonUtils.networkType(context));
        map.put(ExtraConst.IS_REGISTER, InternalAgent.getLogin(context));
    }

    public static JSONObject getAgentInfo() {
        Context context = ContextManager.getContext();
        Map<String, Object> map = new HashMap<>();
        map.put(ExtraConst.IMEI, CommonUtils.getIMEI(context));
        map.put(ExtraConst.DEVICE_ID, CommonUtils.getDeviceId(context));
        map.put(ExtraConst.MAC_ADDRESS, CommonUtils.getMac(context));
        map.put(ExtraConst.C_PLATFORM, ExtraConst.C_V_PLATFORM);
        //"BRAND":"Xiaomi","MODEL":"MI 8 SE","VERSION.RELEASE":"8.1.0"
        map.put(ExtraConst.C_BRAND, Build.BRAND);
        map.put(ExtraConst.C_MODEL, Build.MODEL);
        map.put(ExtraConst.C_OS_VERSION, Build.VERSION.RELEASE);
        map.put(ExtraConst.C_OS, ExtraConst.C_V_OS);
        map.put(ExtraConst.C_NETWORK, CommonUtils.networkType(context));
        map.put(ExtraConst.C_CARRIER_NAME, CommonUtils.getCarrierName(context));
        map.put(ExtraConst.C_SCREEN_WIDTH, CommonUtils.getScreenWidth(context));
        map.put(ExtraConst.C_SCREEN_HEIGHT, CommonUtils.getScreenHeight(context));
        CommonUtils.clearEmptyValue(map);
        return new JSONObject(map);
    }


}
