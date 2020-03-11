package com.dalongtech.testapplication.activity;

import android.os.Bundle;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.bean.ReflectDemoBean;
import com.dalongtech.testapplication.utils.LogUtil;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import butterknife.OnClick;

public class ReflectDemoActivity extends SimpleActivity {

    @Override
    protected int getLayoutById() {
        return R.layout.activity_reflect_demo;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        test();
    }

    @OnClick(R.id.btn_test)
    public void test() {
        Class<?> clazz = ReflectDemoBean.class;
        Type genericSuperclassType = clazz.getGenericSuperclass();
//        LogUtil.d(genericSuperclassType); //返回带泛型的父类 class com.dalongtech.testapplication.bean.TempBean
        //上面父类没有泛型，下面演示父类的父类有泛型
        Class<?> superClazz = clazz.getSuperclass();  //也就是TempBean.class
        Type superGenericSuperclassType = superClazz.getGenericSuperclass();
//        LogUtil.d(superGenericSuperclassType);//同样返回带泛型的父类，不过有泛型 com.dalongtech.testapplication.bean.GenericityBean<com.dalongtech.testapplication.bean.ContactBean>
        Type[] genericInterfacesType = clazz.getGenericInterfaces();    //返回带泛型的接口 com.dalongtech.testapplication.common.callback.CallbackDemo<com.dalongtech.testapplication.test.aaa>
//        printTypeArray(genericInterfacesType);
        Type[] interfaceType = clazz.getInterfaces(); //返回不带泛型的接口 interface com.dalongtech.testapplication.common.callback.CallbackDemo
//        printTypeArray(interfaceType);
        try {
            Method method = clazz.getDeclaredMethod("returnTest");  //能获得私有方法 getMethod只能获取公开方法
//            LogUtil.d(method);      //private com.dalongtech.testapplication.bean.GenericityBean com.dalongtech.testapplication.bean.ReflectDemoBean.returnTest()
            method.setAccessible(true);     //私有方法也能执行
            method.invoke(new ReflectDemoBean());
            Class<?> returnClass = method.getReturnType();      //获取返回类型
//            LogUtil.d(returnClass);                 //class com.dalongtech.testapplication.bean.GenericityBean
            Type genericReturnType = method.getGenericReturnType();     //获取带泛型的类型
//            LogUtil.d(genericReturnType);       //com.dalongtech.testapplication.bean.GenericityBean<com.dalongtech.testapplication.bean.ContactBean>
            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;        //带泛型的通常是这个类，可以操作泛型了
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();        //获取所有的泛型
//            LogUtil.d(actualTypeArguments[0]);      //class com.dalongtech.testapplication.bean.ContactBean
            Type rowType = parameterizedType.getRawType();      //获取类型，不包含泛型
//            LogUtil.d(rowType);     //class com.dalongtech.testapplication.bean.GenericityBean


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printTypeArray(Type[] types) {
        if(types != null) {
            for(Type type : types) {
                LogUtil.d(type);
            }
        }
    }
}
