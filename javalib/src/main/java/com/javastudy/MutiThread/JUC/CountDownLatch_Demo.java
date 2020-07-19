package com.javastudy.MutiThread.JUC;

import com.javastudy.MutiThread.ThreadConsts;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch通过计数器实现一个线程等待其他若干线程执行完后，本线程再继续执行的功能。
 * 本例中模拟主线程必须等待另外2个线程执行完后才能执行，否则阻塞等待
 * Created by mq on 2019-08-29 10:54
 * mqcoder90@gmail.com
 */
public class CountDownLatch_Demo {

    public static void main(String[] args) throws InterruptedException {
        //CountDownLatch传入数量n 主线程执行await()后 阻塞等待两个子线程执行
        // 当子线程执行完毕时会调用countDown() 此时n会减一 当n减至0时主线程会重新执行
        CountDownLatch latch = new CountDownLatch(2);
        MyRunnable runnable = new MyRunnable(latch);
        Thread thread1 = new Thread(runnable, ThreadConsts.THREAD_1);
        Thread thread2 = new Thread(runnable, ThreadConsts.THREAD_2);
        thread1.start();
        thread2.start();

        System.out.println("Main Thread: 开始等待其他线程执行");
        latch.await();
        System.out.println("Main Thread: 继续执行");

    }


    static class MyRunnable implements Runnable {

        private CountDownLatch latch;

        MyRunnable(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ":开始执行");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":执行完毕");
            latch.countDown();
        }
    }

}
