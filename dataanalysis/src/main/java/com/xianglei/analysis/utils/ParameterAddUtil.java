package com.xianglei.analysis.utils;

import com.xianglei.analysis.constants.Constants;
import com.xianglei.analysis.constants.ExtraConst;
import com.xianglei.analysis.process.ContextManager;

import java.util.Map;

/**
 * Author:xianglei
 * Date: 2019-12-12 17:46
 * Description:基本参数，自动添加的
 */
public class ParameterAddUtil {

    public static void putXContextInfo(Map<String, Object> map, String eventName) {
        putXContextBase(map);
        switch (eventName) {
            case Constants.STARTUP:
                map.put(ExtraConst.C_LIB, ExtraConst.C_V_LIB);
                break;
            case Constants.END:

                break;
            case Constants.TRACK:

                break;
            case Constants.PAGE_VIEW:

                break;
            case Constants.ALIAS:

                break;
            case Constants.PROFILE:

                break;
        }
    }

    private static void putXContextBase(Map<String, Object> map) {
        map.put(ExtraConst.C_LIB, ExtraConst.C_V_LIB);
        map.put(ExtraConst.C_PLATFORM, ExtraConst.C_V_PLATFORM);
        map.put(ExtraConst.C_LIB_VERSION, Constants.DEV_SDK_VERSION);
        map.put(ExtraConst.C_DEBUG, CommonUtils.getDebugMode(ContextManager.getContext()));
        map.put(ExtraConst.C_IS_LOGIN, CommonUtils.getLogin(ContextManager.getContext()));
    }


}
