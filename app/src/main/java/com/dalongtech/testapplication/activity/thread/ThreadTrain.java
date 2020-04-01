package com.dalongtech.testapplication.activity.thread;

import com.dalongtech.testapplication.utils.LogUtil;

// 售票窗口
public class ThreadTrain implements Runnable{
    // 总共有30张火车票,开多个窗口同时卖票
    private int trainCount = 100;
    public void run() {
        while (trainCount > 0) {
            try {
                // 休眠50秒
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 执行同步代码块 this锁
            sale();
        }
    }

    // 卖票方法 同步函数等同于this锁，静态方法
    private void sale(){
        if(trainCount > 0){
            LogUtil.d(Thread.currentThread().getName()+",出售第"+(100-trainCount+1)+"张票");
            trainCount--;
        }
    }
}