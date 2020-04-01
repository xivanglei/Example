package com.dalongtech.testapplication.activity.thread;

import com.dalongtech.testapplication.utils.LogUtil;

// 售票窗口
public class ThreadSyncTrain implements Runnable{
    // 总共有100张火车票,开多个窗口同时卖票
    private int trainCount = 100;
    boolean flg = true;
    //可以创建一个对象放在同步代码块里当锁，synchronized (lock)
    final byte[] lock = new byte[]{};
    public void run() {
        if(flg){
            while (trainCount > 0) {
                try {
                    // 休眠50秒
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 执行同步代码块 this锁
                syncSale1();
            }
        }else {
            // 执行同步函数
            while (trainCount > 0){
                try {
                    // 休眠50秒
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 执行同步函数
                syncSale2();
            }
        }
    }

    // 卖票方法 同步函数等同于下面this锁，静态方法。
    // 如果是静态sync方法，就不是等同于synchronized (this)了，而是等同于synchronized (ThreadSyncTrain.class)，静态同步函数使用当前字节码文件（字节码文件就是当前class文件）
    // 还有一种方案是创建一个对象，两个方法都用这个对象的锁的同步块，对象就像上面的lock
    private synchronized void syncSale1(){
        if(trainCount > 0){
            LogUtil.d(Thread.currentThread().getName()+",出售第"+(100-trainCount+1)+"张票");
            trainCount--;
        }
    }

    // 卖票方法
    private void syncSale2(){
        // 同步代码块 synchronized 包裹需要线程安全的问题。this锁
        synchronized (this){
            if(trainCount > 0){
                LogUtil.d(Thread.currentThread().getName()+",出售第"+(100-trainCount+1)+"张票");
                trainCount--;
            }
        }
    }
}