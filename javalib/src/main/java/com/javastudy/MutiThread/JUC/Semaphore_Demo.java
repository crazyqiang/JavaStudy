package com.javastudy.MutiThread.JUC;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mq on 2019-08-29 19:22
 * mqcoder90@gmail.com
 */
public class Semaphore_Demo {

    public static void main(String[] args) throws InterruptedException {

        //银行一共有2个柜台 true表示公平锁 false是非公平锁
        Semaphore semaphore = new Semaphore(2, true);

        //一共有10个顾客来办理业务
        for (int i = 0; i < 10; i++) {
            Client client = new Client(semaphore, "client" + i);
            client.start();
            Thread.sleep(10);
        }
    }

    private static class Client extends Thread {
        private Semaphore semaphore;
        private String threadName;

        Client(Semaphore semaphore, String threadName) {
            this.semaphore = semaphore;
            this.threadName = threadName;
        }

        @Override
        public void run() {
            try {
                //acquire()获取一次使用权限
                semaphore.acquire();
                System.out.println(threadName + "开始办理业务，当前可使用许可数(空闲柜台数):" + semaphore.availablePermits());
                Thread.sleep(1000);
                //acquire()释放一次使用权限
                semaphore.release();
                System.out.println("==>" + threadName + "结束办理业务，当前可使用许可数(空闲柜台数):" + semaphore.availablePermits());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 可以用ReentrantLock模拟Semaphore的功能 但是每次只允许一个线程进行操作
     */
    //SemaphoreOnLock semaphore = new SemaphoreOnLock(2);
    private static class SemaphoreOnLock {
        private final Lock lock = new ReentrantLock();
        private final Condition permitsAvailable = lock.newCondition();
        private int permits;

        SemaphoreOnLock(int permits) {
            lock.lock();
            try {
                this.permits = permits;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void acquire() throws InterruptedException {
            lock.lock();
            try {
                while (permits <= 0) permitsAvailable.await();
                --permits;
            } finally {
                lock.unlock();
            }
        }

        public void release() {
            lock.lock();
            try {
                ++permits;
                permitsAvailable.signal();
            } finally {
                lock.unlock();
            }
        }

        public int availablePermits() {
            return permits;
        }

    }

}
