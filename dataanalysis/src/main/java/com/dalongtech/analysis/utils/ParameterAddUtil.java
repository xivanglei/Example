package com.dalongtech.analysis.utils;

import android.content.Context;
import android.os.Build;

import com.dalongtech.analysis.constants.Constants;
import com.dalongtech.analysis.constants.ExtraConst;
import com.dalongtech.analysis.process.ContextManager;

import java.util.Map;

/**
 * Author:xianglei
 * Date: 2019-12-12 17:46
 * Description:基本参数，自动添加的
 */
public class ParameterAddUtil {

    public static void putXContextInfo(Map<String, Object> map, String eventName) {
        Context context = ContextManager.getContext();
//        if(eventName.startsWith(Constants.PROFILE)) eventName = Constants.PROFILE;
        switch (eventName) {
            case Constants.STARTUP:
                map.put(ExtraConst.C_NETWORK, CommonUtils.networkType(context));
                map.put(ExtraConst.C_IS_FIRST_DAY, CommonUtils.isFirstDay(context));
                map.put(ExtraConst.C_IS_FIRST_TIME, CommonUtils.isFirstStart(context));
                map.put(ExtraConst.C_SCREEN_WIDTH, CommonUtils.getScreenWidth(context));
                map.put(ExtraConst.C_SCREEN_HEIGHT, CommonUtils.getScreenHeight(context));
                map.put(Constants.USER, CommonUtils.getUserId(context));
                map.put(ExtraConst.IS_REGISTER, InternalAgent.getLogin(context));
                break;
            case Constants.LOGIN:
                map.put(ExtraConst.C_NETWORK, CommonUtils.networkType(context));
                map.put(ExtraConst.SYSTEM_VERSION, Build.VERSION.SDK_INT);
                map.put(Constants.USER, CommonUtils.getUserId(context));
                map.put(ExtraConst.IS_REGISTER, InternalAgent.getLogin(context));
                map.put(ExtraConst.VIP_GRADE, CommonUtils.getVipGrade(context));
                break;
            case Constants.END:
                map.put(ExtraConst.C_NETWORK, CommonUtils.networkType(context));
                map.put(ExtraConst.C_IS_FIRST_DAY, CommonUtils.isFirstDay(context));
                map.put(ExtraConst.C_IS_FIRST_TIME, CommonUtils.isFirstStart(context));
                map.put(ExtraConst.C_SCREEN_WIDTH, CommonUtils.getScreenWidth(context));
                map.put(ExtraConst.C_SCREEN_HEIGHT, CommonUtils.getScreenHeight(context));
                map.put(Constants.USER, CommonUtils.getUserId(context));
                map.put(ExtraConst.IS_REGISTER, InternalAgent.getLogin(context));
                break;
//            case Constants.PROFILE:
//                map.put(ExtraConst.C_MAC, InternalAgent.getMac(context));
//                break;
        }
    }

    //基本参数，所有事件都带
    private static void putXContextBase(Map<String, Object> map) {

    }

    //通用参数，大部分事件带
    private static void putXContextCommon(Map<String, Object> map, Context context) {
//        map.put(ExtraConst.C_TIME_ZONE, TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
//        map.put(ExtraConst.C_MODEL, Build.MODEL);
//        map.put(ExtraConst.C_CARRIER_NAME, CommonUtils.getCarrierName(context));
//        map.put(ExtraConst.C_BRAND, Build.BRAND);
//        map.put(ExtraConst.C_LANGUAGE, Locale.getDefault().getLanguage());
//        map.put(ExtraConst.C_IS_TIME_CALIBRATED, Constants.isCalibration);
    }


}
