package com.xianglei.analysis.constants;

/**
 * Author:xianglei
 * Date: 2019-12-12 09:55
 * Description:易观之外的常数
 */
public interface ExtraConst {

    int USER_KEYS_LIMIT =  100;                             //用户传参最大条数 0表示不限制，不包含基础参数，只是另外传参

    String C_LIB = "$lib";
    String C_LIB_VERSION = "$lib_version";
    String C_PLATFORM = "$platform";
    String C_DEBUG = "$debug";
    String C_IS_LOGIN = "$is_login";

    String C_V_LIB = "Android";
    String C_V_PLATFORM = "Android";

}
