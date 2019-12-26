package com.dalongtech.testapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.AES256;
import com.dalongtech.testapplication.utils.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends SimpleActivity {

    private String content = "{\"cmd\":\"test\",\"data\":{\"a\":\"cc\"},\"ext\":{}}";
    private String password = "DlClientPost2019";

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
        LogUtil.d("明文：" + content);
        LogUtil.d("key：" + password);

        encryptResult = AES256.AES_cbc_encrypt(content, password);
        LogUtil.d("密文：" + new String(encryptResult));
    }

    @OnClick(R.id.btn_test2)
    public void test2() {
        String decryptResult = AES256.AES_cbc_decrypt(encryptResult, password);
        LogUtil.d("解密：" + decryptResult);
    }
}
