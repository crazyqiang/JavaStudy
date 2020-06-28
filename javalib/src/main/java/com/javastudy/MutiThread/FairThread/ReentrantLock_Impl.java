package com.javastudy.MutiThread.FairThread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock+Condition实现公平锁
 * Created by mq on 2019-11-02 16:02
 * mqcoder90@gmail.com
 */
public class ReentrantLock_Impl {

    private static final String FLAG_THREAD_1 = "ReentrantLock_Thread1";
    private static final String FLAG_THREAD_2 = "ReentrantLock_Thread2";
    private static final String FLAG_THREAD_3 = "ReentrantLock_Thread3";

    public static void main(String[] args) throws InterruptedException {
        //ReentrantLock构造方法传入true为公平锁 false为非公平锁
        ReentrantLock lock = new ReentrantLock(true);
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        Condition condition3 = lock.newCondition();
        FairRunnable runnable = new FairRunnable(lock, condition1, condition2, condition3);

        new Thread(runnable, FLAG_THREAD_1).start();
        new Thread(runnable, FLAG_THREAD_2).start();
        new Thread(runnable, FLAG_THREAD_3).start();

    }

    static class FairRunnable implements Runnable {
        private ReentrantLock lock;
        private Condition condition1;
        private Condition condition2;
        private Condition condition3;

        public FairRunnable(ReentrantLock lock, Condition condition1, Condition condition2, Condition condition3) {
            this.lock = lock;
            this.condition1 = condition1;
            this.condition2 = condition2;
            this.condition3 = condition3;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + "开始执行");
                    Thread.sleep(1000);
                    switch (Thread.currentThread().getName()) {
                        case FLAG_THREAD_1:
                            //唤醒线程2 自身线程挂起阻塞
                            condition2.signal();
                            condition1.await();
                            break;
                        case FLAG_THREAD_2:
                            //唤醒线程3 自身线程挂起阻塞
                            condition3.signal();
                            condition2.await();
                            break;
                        case FLAG_THREAD_3:
                            //唤醒线程1 自身线程挂起阻塞
                            condition1.signal();
                            condition3.await();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
