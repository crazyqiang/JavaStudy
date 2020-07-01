package com.javastudy.MutiThread.Produce_Consume;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者-消费者 ReentrantLock-Condition方式实现
 * Created by mq on 2020/7/1 下午2:15
 * mqcoder90@gmail.com
 */
public class ReentrantLock_impl {

    private static final String THREAD_PRODUCE = "生产者";
    private static final String THREAD_CONSUME = "消费者";
    private static TaskResource resource = new TaskResource();

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(new TaskRunnable(THREAD_PRODUCE + i)).start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(new TaskRunnable(THREAD_CONSUME + i)).start();
        }

    }

    static class TaskRunnable implements Runnable {
        private String threadName;

        TaskRunnable(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (threadName.startsWith(THREAD_PRODUCE)) {
                    //生产者
                    resource.produce();
                } else if (threadName.startsWith(THREAD_CONSUME)) {
                    //消费者
                    resource.consume();
                }
            }
        }
    }

    static class TaskResource {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        private final int maxSize = 10;
        AtomicInteger resourceNum = new AtomicInteger(0);

        public void produce() {
            try {
                reentrantLock.lock();
                while (resourceNum.get() == maxSize) {
                    condition.await();
                }
                resourceNum.getAndIncrement();
                System.out.println("++" + Thread.currentThread().getName() + "生产一个，当前总数为：" + resourceNum.get());
                condition.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }

        public void consume() {
            try {
                reentrantLock.lock();
                while (resourceNum.get() <= 0) {
                    condition.await();
                }
                resourceNum.getAndDecrement();
                System.out.println("--" + Thread.currentThread().getName() + "消费一个，当前总数为：" + resourceNum.get());
                condition.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
