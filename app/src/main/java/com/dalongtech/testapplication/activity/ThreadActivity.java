package com.dalongtech.testapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.LogUtil;

import java.util.Random;

import butterknife.OnClick;

public class ThreadActivity extends SimpleActivity {

    private volatile Thread mThread;
    private Handler mHandler;
    private Random mRandom;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_thread;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        initThreadHandler();
        mRandom = new Random();
    }

    @OnClick(R.id.btn_start_thread)
    public synchronized void startThread() {
            if (mThread == null) {
                LogUtil.d("new了一个");
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.d(Thread.currentThread().getName());
                        for (int i = 0; i < 100; i++) {
                            try {
                                if (i == 5) {
                                    //            调用interrupt()方法仅仅是在当前线程中打了一个停止的标记，并不是真正的停止线程
//                                Thread.currentThread().interrupt();
                                }
//                            如果一个线程处于了阻塞状态（如线程调用了thread.sleep、thread.join、thread.wait）调用线程的interrupt会中断异常，
//                            然后再把状态改为false相当于没中断，所以需要依赖中断状态时一定不要sleep的时候调用interrupt
                                Thread.sleep(100);
                                interruptThread();
                                startThread();
//                            interrupted()测试当前线程是否已经是中断状态，执行后具有清除中断状态flag的功能
//                            isInterrupted()测试线程Thread对象是否已经是中断状态，但不清除中断状态flag
                                //这里是true，不改变状态
//                            LogUtil.d(mThread.getName() + "开始了:" + i + "中断状态:" + mThread.isInterrupted());
                                //这里是true，并且改变状态了，之后都会是false
//                            LogUtil.d(mThread.getName() + "开始了:" + i + "中断状态:" + Thread.interrupted());
                                //这里状态被改变了，变为false,之后都是false了
//                            LogUtil.d(mThread.getName() + "开始了:" + i + "中断状态:" + mThread.isInterrupted());
                            } catch (Exception e) {
//                            LogUtil.d("异常了");
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            try {
                //如果线程执行完毕再开始就会IllegalThreadStateException异常
                LogUtil.d("开始");
                mThread.start();
            } catch (IllegalThreadStateException e) {
                LogUtil.d("线程已经结束，不用开了");
                e.printStackTrace();
            }
    }

    @OnClick(R.id.btn_restart_thread)
    public void restartThread() {
        mHandler.sendEmptyMessage(1);
    }

    @OnClick(R.id.btn_main_start_thread)
    public void mainStartThread() {
        for(int i = 0; i < 10; i++) {
            interruptThread();
            startThread();
        }
    }

    private synchronized void interruptThread() {
        if(mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
    }

    private void initThreadHandler() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                mHandler = new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        LogUtil.d(Thread.currentThread().getName());
                        if(msg.what == 1) {
                            for(int i = 0; i < 100; i++) {
                                try {
                                    Thread.sleep(mRandom.nextInt(1000));
                                    interruptThread();
                                    startThread();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                };
                Looper.loop();
            }
        }).start();
    }


}
