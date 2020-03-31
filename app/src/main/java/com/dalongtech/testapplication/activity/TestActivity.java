package com.dalongtech.testapplication.activity;

import android.content.Intent;
import android.os.Bundle;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.LogUtil;

import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class TestActivity extends SimpleActivity {

    private String content = "{\"cmd\":\"test\",\"data\":{\"a\":\"cc\"},\"ext\":{}}";
    private String password = "DlClientPost2019";

    private String mData;

    private byte[] encryptResult;

    private Thread mThread;


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
        interrupt();
        startThread();
    }

    @OnClick(R.id.btn_test2)
    public void test2() {
        interrupt();
    }

    private void interrupt() {
        if(mThread != null) {
//            调用interrupt()方法仅仅是在当前线程中打了一个停止的标记，并不是真正的停止线程
            mThread.interrupt();
        }
    }

    private void startThread() {
        if(mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i < 100; i++) {
                        try {
//                            如果一个线程处于了阻塞状态（如线程调用了thread.sleep、thread.join、thread.wait）调用线程的interrupt会中断异常，
//                            然后再把状态改为false相当于没中断，所以需要依赖中断状态时一定不要sleep的时候调用interrupt
//                            Thread.sleep(1000);
//                            interrupted()测试当前线程是否已经是中断状态，执行后具有清除中断状态flag的功能
//                            isInterrupted()测试线程Thread对象是否已经是中断状态，但不清除中断状态flag
                            LogUtil.d(mThread.getName() + "开始了:" + i + "中断状态:" + mThread.isInterrupted());
                            LogUtil.d(mThread.getName() + "开始了:" + i + "中断状态:" + mThread.isInterrupted());
                            LogUtil.d(mThread.getName() + "开始了:" + i + "中断状态:" + mThread.isInterrupted());
                            LogUtil.d(mThread.getName() + "开始了:" + i + "中断状态:" + Thread.interrupted());
                            LogUtil.d(mThread.getName() + "开始了:" + i + "中断状态:" + Thread.interrupted());
                            LogUtil.d(mThread.getName() + "开始了:" + i + "中断状态:" + mThread.isInterrupted());
                        } catch (Exception e) {
                            LogUtil.d("异常了");
                            e.printStackTrace();
                        }
                    }
                }
            });
            //如果
            mThread.start();
        }
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
