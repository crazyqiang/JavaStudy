package com.javastudy.MutiThread.JUC;

import com.javastudy.MutiThread.ThreadConsts;

import java.util.concurrent.CyclicBarrier;

/**
 * 意为循环栅栏，可以实现一组线程等待至某个状态之后再全部继续执行。当所有线程执行完毕后，CyclicBarrier还可以继续被重用
 * Created by mq on 2020/7/19 下午1:09
 * mqcoder90@gmail.com
 */
public class CyclicBarrier_Demo {
    public static void main(String[] args) {
        System.out.println("CyclicBarrier例子：");
        //构造方法中传入的Runnable是由最后通过栅栏的线程去执行,如本例中栅栏数声明为3，当最后一个线程执行await()时，最后这个线程会再去执行这里声明的Runnable任务
        //这里Runnable是非必须的，不声明的话不会执行，同时CyclicBarrier将允许所有线程继续执行，
        CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "是最后一个通过栅栏的线程，它继续执行CyclicBarrier构造方法中的Runnable任务(如有)");
            }
        });
        for (int i = 0; i < 3; i++) {
            new Thread(new MyTask(barrier), ThreadConsts.THREAD + i).start();
        }
    }

    static class MyTask implements Runnable {
        CyclicBarrier barrier;

        MyTask(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + ":开始执行");
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + ":执行完毕，等待其他线程执行");
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + "：通过栅栏，所有任务执行完毕");
            }
        }
    }
}
