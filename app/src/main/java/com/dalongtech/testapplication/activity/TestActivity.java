package com.dalongtech.testapplication.activity;

import android.os.Bundle;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.AESUtil;

import butterknife.OnClick;

public class TestActivity extends SimpleActivity {

    private String content = "{\"cmd\":\"test\",\"data\":{\"a\":\"cc\"},\"ext\":{}}";
    private String password = "DlClientPost2019";

    private String mData;

    private byte[] encryptResult;


    @Override
    protected int getLayoutById() {
        return R.layout.activity_test;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {




    }

    @OnClick(R.id.btn_test)
    public void test() {
        mData = AESUtil.encryptAES(content);

    }

    @OnClick(R.id.btn_test2)
    public void test2() {
        String deData = AESUtil.decryptAES(mData);
    }


}
