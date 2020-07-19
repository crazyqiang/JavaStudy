package com.javastudy.MutiThread.JUC;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock的基本使用 & condition：await、signal/signalAll的使用
 * Created by mq on 2019-07-15 12:35
 * mqcoder90@gmail.com
 */
public class ReentrantLock_Demo {

    public static void main(String[] args) throws InterruptedException {
        //初始化 ReentrantLock
        ReentrantLock reentrantLock = new ReentrantLock();

        //lock加锁 unlock解锁
        MyRunnable runnable = new MyRunnable(reentrantLock);
        Thread threadA = new Thread(runnable,"threadA");
        Thread threadB = new Thread(runnable,"threadB");
        threadA.start();
        threadB.start();


        //condition：await、signal/signalAll通知
//        Condition condition = reentrantLock.newCondition();
//        ThreadC threadC = new ThreadC(reentrantLock, condition);
//        threadC.start();
//        Thread.sleep(2000);
//        ThreadD threadD = new ThreadD(reentrantLock, condition);
//        threadD.start();

    }


    static class MyRunnable implements Runnable {

        private ReentrantLock reentrantLock;

        MyRunnable(ReentrantLock reentrantLock) {
            this.reentrantLock = reentrantLock;
        }

        @Override
        public void run() {
            try {
                reentrantLock.lock();
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + "," + i);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }
    }


    static class ThreadC extends Thread {
        private ReentrantLock reentrantLock;
        private Condition condition;

        ThreadC(ReentrantLock reentrantLock, Condition condition) {
            this.reentrantLock = reentrantLock;
            this.condition = condition;
            setName("ThreadC");
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": 开始运行");
            try {
                reentrantLock.lock();
                System.out.println(Thread.currentThread().getName() + ": 通过condition.await中断运行并放弃锁");
                condition.await();
                System.out.println(Thread.currentThread().getName() + ": 重新获取锁，恢复执行");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
                System.out.println(Thread.currentThread().getName() + ": 释放锁");
            }
        }
    }

    static class ThreadD extends Thread {
        private ReentrantLock reentrantLock;
        private Condition condition;

        ThreadD(ReentrantLock reentrantLock, Condition condition) {
            this.reentrantLock = reentrantLock;
            this.condition = condition;
            setName("ThreadD");
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": 开始运行");
            try {
                reentrantLock.lock();
                System.out.println(Thread.currentThread().getName() + ": 通过condition.signal去唤醒ThreadC");
                condition.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
                System.out.println(Thread.currentThread().getName() + ": 释放锁");
            }
        }
    }

}
