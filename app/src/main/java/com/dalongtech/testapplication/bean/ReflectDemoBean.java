package com.dalongtech.testapplication.bean;

import com.dalongtech.testapplication.common.callback.CallbackDemo;
import com.dalongtech.testapplication.test.aaa;

/**
 * Author:xianglei
 * Date: 2020-03-04 10:26
 * Description:
 */
public class ReflectDemoBean extends TempBean implements CallbackDemo<aaa> {

    private GenericityBean<ContactBean> returnTest() {
//        LogUtil.d("返回执行了");
        return null;
    }
}
