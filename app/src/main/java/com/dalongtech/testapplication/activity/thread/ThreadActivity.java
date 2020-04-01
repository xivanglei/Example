package com.dalongtech.testapplication.activity.thread;

import android.os.Bundle;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.LogUtil;

import butterknife.OnClick;

public class ThreadActivity extends SimpleActivity {

    @Override
    protected int getLayoutById() {
        return R.layout.activity_thread;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {

    }

    //不同步方法，数据会错乱
    @OnClick(R.id.btn_no_sync_thread)
    public void noSyncThread() {
        //这里是不同步，票会多卖
        ThreadTrain threadTrain = new ThreadTrain();
        Thread t1 = new Thread(threadTrain, "窗口1");
        Thread t2 = new Thread(threadTrain, "窗口2");
        t1.start();
        t2.start();
    }

    //同步方法，数据正常
    @OnClick(R.id.btn_sync_thread)
    public void syncThread() {
//        //这里是同步
        ThreadSyncTrain syncTrain = new ThreadSyncTrain();
        Thread tSync1 = new Thread(syncTrain);
        Thread tSync2 = new Thread(syncTrain);
        tSync1.start();
        try {
            Thread.sleep(40);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //这里为了让t2窗口走syncSale2方法,证明syncSale1与syncSale2两种同步方法相同，能实现同步，这里只是举例，其实只走一个方法也能同步
        syncTrain.flg = false;
        tSync2.start();
    }

    @OnClick(R.id.btn_interrupt)
    public void interrupt() {
        Thread thread = new Thread(new Runnable() {
            private long i;
            @Override
            public void run() {
                LogUtil.d(Thread.currentThread().getName());
//                Thread.interrupted()测试当前线程是否已经是中断状态，执行后具有清除中断状态flag的功能
//                Thread.currentThread().isInterrupted()测试线程Thread对象是否已经是中断状态，但不清除中断状态flag
                while (!Thread.currentThread().isInterrupted())
                    try {
                        LogUtil.d(++i);
                        //sleep的时候走interrupt方法会报错InterruptedException
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        //在sleep的时候如果执行interrupt中断，会抛出异常，状态重置为false,所以这里需要再次中断
                        Thread.currentThread().interrupt();
                    }
                }
        });
    }
}
