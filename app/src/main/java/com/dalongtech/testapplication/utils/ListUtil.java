package com.dalongtech.testapplication.utils;

import java.util.List;

/**
 * Author:xianglei
 * Date: 2018/12/26 3:21 PM
 * Description:
 */
public class ListUtil {
    public static boolean isEmpty(List list) {
        if(list != null && list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }
}
