package com.dalongtech.testapplication.activity;

import android.content.Intent;
import android.os.Bundle;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.LogUtil;
import com.dalongtech.testapplication.utils.WebSocketAESUtil;

import java.nio.ByteBuffer;
import java.util.Arrays;

import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

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
        setRxJavaErrorHandler();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void aaa(String... params) {
        LogUtil.d(params.length);
        LogUtil.d(-3 % 2);
        LogUtil.d(0 % 2);
        LogUtil.d(5 % 2);
    }

    @OnClick(R.id.btn_test)
    public void test() {
        String text = "{\"cmd\":\"heart\",\"data\":{},\"ext\":{}}";
        String encryptData = WebSocketAESUtil.encryptAES(text);
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + encryptData.length());
        byteBuffer.putInt(encryptData.length());
        byteBuffer.put(encryptData.getBytes());
        byte[] enBytes = byteBuffer.array();
        String enText = new String(enBytes);
        LogUtil.d(encryptData);
        byte[] bytes = "U2FsdGVkX1+VqAAAAAAAAGgA+1QC4HKqxKtCTmyq9toC2Nnma2UmZqSuNEQE2/FLgXPH/d5arjpumxMZDLAewOWbXohGUD/eUnLkh0AQtrvCvCMauBiOzA==".getBytes();
        String data = WebSocketAESUtil.decryptAES(new String(Arrays.copyOfRange(bytes, 0, bytes.length)));
        LogUtil.d(data);
    }

    @OnClick(R.id.btn_test2)
    public void test2() {

    }

    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtil.d("全局出错了");
            }
        });
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    private static byte charToByte(char c) {
        byte result = (byte) "0123456789ABCDEF".indexOf(c);
        LogUtil.d(result);
        return result;
    }

}
