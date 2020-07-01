package com.javastudy.MutiThread.Produce_Consume;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 生产者-消费者 BlockingQueue方式
 */
public class BlockingQueue_Impl {

    private static final String THREAD_PRODUCE = "生产者";
    private static final String THREAD_CONSUME = "消费者";
    //阻塞队列中最多存5个
    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(5);

    public static void main(String[] args) {
        new Thread(new Task(THREAD_PRODUCE)).start();
        new Thread(new Task(THREAD_CONSUME)).start();
    }

    static class Task implements Runnable {
        private String threadName;

        Task(String threadName) {
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
                try {
                    if (threadName.equals(THREAD_PRODUCE)) {
                        //生产者
                        int produceNum = new Random().nextInt(1000);
                        System.out.println("++" + Thread.currentThread().getName() + "生产元素:" + produceNum);
                        queue.put(produceNum);
                    } else if (threadName.equals(THREAD_CONSUME)) {
                        //消费者
                        int consumeNum = queue.take();
                        System.out.println("--" + Thread.currentThread().getName() + "消费元素:" + consumeNum);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

}
