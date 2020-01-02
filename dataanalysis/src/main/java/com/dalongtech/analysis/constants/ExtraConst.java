package com.dalongtech.analysis.constants;

/**
 * Author:xianglei
 * Date: 2019-12-12 09:55
 * Description:易观之外的常数
 */
public interface ExtraConst {

    int USER_KEYS_LIMIT =  100;                             //用户传参最大条数 0表示不限制，不包含基础参数，只是另外传参
    String UN_KNOW = "Unknown";                             //无法获取某些参数是传这个
    String X_CONTEXT = "$content";

    //这部分基本属性键
    String C_SDK_VERSION = "$sdk_version";
    String PLATFORM = "$platform";
    String C_DEBUG = "$debug";
    String C_IS_LOGIN = "$is_login";

    String C_CHANNEL = "$channel_code";
    String C_TIME_ZONE = "$time_zone";
    String C_MANUFACTURER = "$manufacturer";
    String C_APP_VERSION = "$app_version";
    String C_AGENT = "$c_agent";
    String C_PARTNER_CODE = "$partner_code";
    String C_MODEL = "$model";
    String C_OS = "$os";
    String C_OS_VERSION = "$os_version";

    String C_NETWORK = "nstatus";
    String C_CARRIER_NAME = "$carrier_name";
    String C_SCREEN_WIDTH = "$screen_width";
    String C_SCREEN_HEIGHT = "$screen_height";
    String C_BRAND = "$brand";
    String C_LANGUAGE = "$language";
    String C_IS_FIRST_DAY = "install_first_day";
    String C_SESSION_ID = "$session_id";
    String C_IS_FIRST_TIME = "install_first_open";
    String C_IS_FROM_BACKGROUND = "$is_from_background";
    String C_IS_TIME_CALIBRATED = "$is_time_calibrated";
    String C_PAGENAME = "$pagename";
    String C_CID = "$cid";
    String C_MAC = "$mac";

    String C_V_PLATFORM = "Android";
    String C_V_OS = "Android";
    String IDENT_KEY = "ident_key";
    String IDENT_VALUE = "ident_value";

    String URL_EVENT = "/report";       //用户事件上报
    String URL_GET_CID = "/client";     //获取cid
    String URL_LOGIN = "/client/user";  //登录
    String SYSTEM_VERSION = "system_version";       //系统版本
    String VIP_GRADE = "vip_grade";         //会员等级
    String IS_REGISTER = "is_register";     //是否注册
    String UID = "uid";

}
