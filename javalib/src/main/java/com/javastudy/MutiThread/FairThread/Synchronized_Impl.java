package com.javastudy.MutiThread.FairThread;

/**
 * Synchronized+wait/notify实现公平锁
 * Created by mq on 2019-11-02 16:36
 * mqcoder90@gmail.com
 */
public class Synchronized_Impl {
    private static final String FLAG_THREAD_1 = "Synchronized_Thread1";
    private static final String FLAG_THREAD_2 = "Synchronized_Thread2";
    private static final String FLAG_THREAD_3 = "Synchronized_Thread3";

    public static void main(String[] args) {
        FairRunnable runnable = new FairRunnable();
        new Thread(runnable, FLAG_THREAD_1).start();
        new Thread(runnable, FLAG_THREAD_2).start();
        new Thread(runnable, FLAG_THREAD_3).start();
    }

    static class FairRunnable implements Runnable {
        private volatile static int flag = 1;
        private final Object object = new Object();

        @Override
        public void run() {
            while (true) {
                synchronized (object) {
                    //如果当前情况是：线程1&flag!=1、线程2&flag!=2、线程3&flag!=3 那当前线程通过object.wait挂起
                    switch (Thread.currentThread().getName()) {
                        case FLAG_THREAD_1:
                            while (flag != 1) {
                                try {
                                    object.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case FLAG_THREAD_2:
                            while (flag != 2) {
                                try {
                                    object.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case FLAG_THREAD_3:
                            while (flag != 3) {
                                try {
                                    object.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                    }
                    //线程任务开始执行
                    System.out.println(Thread.currentThread().getName() + "开始执行");
                    try {
                        //模拟线程任务执行
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    switch (Thread.currentThread().getName()) {
                        case FLAG_THREAD_1:
                            //接下来该去执行线程2
                            flag = 2;
                            break;
                        case FLAG_THREAD_2:
                            //接下来该去执行线程3
                            flag = 3;
                            break;
                        case FLAG_THREAD_3:
                            //接下来该去执行线程1
                            flag = 1;
                            break;
                    }
                    //唤醒所有线程
                    object.notifyAll();
                }
            }
        }
    }

}
