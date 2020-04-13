package com.dalongtech.testapplication.activity.thread;

import com.dalongtech.testapplication.utils.LogUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Author:xianglei
 * Date: 2020-04-11 14:25
 * Description:阻塞队列介绍
 */
public class QueueTest {

//    插入方法：
//
//    add(E e) : 添加成功返回true，失败抛IllegalStateException异常
//    offer(E e) : 成功返回 true，如果此队列已满，则返回 false。
//    put(E e) :将元素插入此队列的尾部，如果该队列已满，则一直阻塞
//    删除方法:
//
//    remove(Object o) :移除指定元素,成功返回true，失败返回false
//    poll() : 获取并移除此队列的头元素，若队列为空，则返回 null
//    take()：获取并移除此队列头元素，若没有元素则一直阻塞。
//    检查方法
//
//    element() ：获取但不移除此队列的头元素，没有元素则抛异常
//    peek() :获取但不移除此队列的头；若队列为空，则返回 null。
    BlockingQueue mBlockingQueue;

    private final static LinkedBlockingQueue<Apple> queue= new LinkedBlockingQueue<>(1);
    private final static ArrayBlockingQueue<Apple> arrayQueue = new ArrayBlockingQueue<>(1);
    private static int i = 0;


    public static void start(){
        new Thread(new Producer(queue)).start();
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }





    static class Apple {
        private int i;
        public Apple(int i){
            this.i = i;
        }
        public int getI() {
            return i;
        }
    }

    /**
     * 生产者线程
     */
    static class Producer implements Runnable{
        private final LinkedBlockingQueue<Apple> mAbq;
        Producer(LinkedBlockingQueue<Apple> arrayBlockingQueue){
            this.mAbq = arrayBlockingQueue;
        }

        @Override
        public void run() {
            while (true) {
                Produce();
            }
        }

        private void Produce(){
            synchronized (this) {
                try {
                    Apple apple = new Apple(i++);
                    mAbq.put(apple);
                    mAbq.offer(apple);
                    LogUtil.d("生产:" + apple.getI());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 消费者线程
     */
    static class Consumer implements Runnable{

        private LinkedBlockingQueue<Apple> mAbq;
        Consumer(LinkedBlockingQueue<Apple> arrayBlockingQueue){
            this.mAbq = arrayBlockingQueue;
        }

        @Override
        public void run() {
            while (true){
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    comsume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void comsume() throws InterruptedException {
            synchronized (this) {
                Apple apple = mAbq.take();
                LogUtil.d("消费Apple=" + apple.getI());
            }
        }
    }
}
