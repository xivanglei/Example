package com.dalongtech.testapplication.test;

import java.lang.reflect.Method;

public class ccc extends bbb {
        public void zzz() {
            try {
                Class clazz = aaa.class;
                Method m1 = clazz.getDeclaredMethod("zzz");
                m1.invoke(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }