package com.xianglei.analysis.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.xianglei.analysis.constants.Constants;
import com.xianglei.analysis.constants.ExtraConst;
import com.xianglei.analysis.process.ContextManager;

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Author:xianglei
 * Date: 2019-12-12 17:46
 * Description:基本参数，自动添加的
 */
public class ParameterAddUtil {

    public static void putXContextInfo(Map<String, Object> map, String eventName) {
        Context context = ContextManager.getContext();
        putXContextBase(map);
        if(TextUtils.equals(eventName, Constants.STARTUP)
                || TextUtils.equals(eventName, Constants.END)
                || TextUtils.equals(eventName, Constants.TRACK)
                ||TextUtils.equals(eventName, Constants.PAGE_VIEW)
                || TextUtils.equals(eventName, Constants.FIRST_INSTALL)) {
            putXContextCommon(map, context);
        }
        switch (eventName) {
            case Constants.STARTUP:
                map.put(ExtraConst.C_IS_FIRST_TIME, CommonUtils.isFirstStart(context));
                break;
            case Constants.ALIAS:
            case Constants.PROFILE:
                map.put(ExtraConst.C_IMEI, InternalAgent.getIMEI(context));
                map.put(ExtraConst.C_MAC, InternalAgent.getMac(context));
                break;
        }
    }

    //基本参数，所有事件都带
    private static void putXContextBase(Map<String, Object> map) {
        map.put(ExtraConst.C_LIB, ExtraConst.C_V_LIB);
        map.put(ExtraConst.C_PLATFORM, ExtraConst.C_V_PLATFORM);
        map.put(ExtraConst.C_LIB_VERSION, Constants.DEV_SDK_VERSION);
        map.put(ExtraConst.C_DEBUG, CommonUtils.getDebugMode(ContextManager.getContext()));
        map.put(ExtraConst.C_IS_LOGIN, CommonUtils.getLogin(ContextManager.getContext()));
    }

    //通用参数，大部分事件带
    private static void putXContextCommon(Map<String, Object> map, Context context) {
        map.put(ExtraConst.C_CHANNEL, CommonUtils.getChannel(context));
        map.put(ExtraConst.C_TIME_ZONE, TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
        map.put(ExtraConst.C_MANUFACTURER, Build.MANUFACTURER);
        map.put(ExtraConst.C_APP_VERSION, CommonUtils.getVersionName(context));
        map.put(ExtraConst.C_MODEL, Build.MODEL);
        map.put(ExtraConst.C_OS, ExtraConst.C_V_OS);
        map.put(ExtraConst.C_OS_VERSION, Constants.DEV_SYSTEM + " " + Build.VERSION.RELEASE);
        map.put(ExtraConst.C_NETWORK, CommonUtils.networkType(context));
        map.put(ExtraConst.C_CARRIER_NAME, CommonUtils.getCarrierName(context));
        map.put(ExtraConst.C_SCREEN_WIDTH, CommonUtils.getScreenWidth(context));
        map.put(ExtraConst.C_SCREEN_HEIGHT, CommonUtils.getScreenHeight(context));
        map.put(ExtraConst.C_BRAND, Build.BRAND);
        map.put(ExtraConst.C_LANGUAGE, Locale.getDefault().getLanguage());
        map.put(ExtraConst.C_IS_FIRST_DAY, CommonUtils.isFirstDay(context));
        map.put(ExtraConst.C_IS_TIME_CALIBRATED, Constants.isCalibration);
    }


}
