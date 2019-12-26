package com.dalongtech.analysis.constants;

/**
 * Author:xianglei
 * Date: 2019-12-12 09:55
 * Description:易观之外的常数
 */
public interface ExtraConst {

    int USER_KEYS_LIMIT =  100;                             //用户传参最大条数 0表示不限制，不包含基础参数，只是另外传参
    String UN_KNOW = "Unknown";                             //无法获取某些参数是传这个
    String X_CONTEXT = "xcontext";

    //这部分基本属性键
    String C_LIB = "$lib";
    String C_LIB_VERSION = "$lib_version";
    String C_PLATFORM = "$platform";
    String C_DEBUG = "$debug";
    String C_IS_LOGIN = "$is_login";

    String C_CHANNEL = "$channel";
    String C_TIME_ZONE = "$time_zone";
    String C_MANUFACTURER = "$manufacturer";
    String C_APP_VERSION = "$app_version";
    String C_MODEL = "$model";
    String C_OS = "$os";
    String C_OS_VERSION = "$os_version";
    String C_NETWORK = "$network";
    String C_CARRIER_NAME = "$carrier_name";
    String C_SCREEN_WIDTH = "$screen_width";
    String C_SCREEN_HEIGHT = "$screen_height";
    String C_BRAND = "$brand";
    String C_LANGUAGE = "$language";
    String C_IS_FIRST_DAY = "$is_first_day";
    String C_SESSION_ID = "$session_id";
    String C_IS_FIRST_TIME = "$is_first_time";
    String C_IS_FROM_BACKGROUND = "$is_from_background";
    String C_IS_TIME_CALIBRATED = "$is_time_calibrated";
    String C_PAGENAME = "$pagename";
    String C_IMEI = "$imei";
    String C_MAC = "$mac";

    String C_V_LIB = "Android";
    String C_V_PLATFORM = "Android";
    String C_V_OS = "Android";

}
