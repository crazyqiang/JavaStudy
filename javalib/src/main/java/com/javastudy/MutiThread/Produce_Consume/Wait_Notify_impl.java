package com.javastudy.MutiThread.Produce_Consume;

/**
 * 生产者-消费者模式 Wait-Notify模式
 */
public class Wait_Notify_impl {

    private static final String THREAD_PRODUCE = "生产者";
    private static final String THREAD_CONSUME = "消费者";
    //公共资源
    private static PublicResource resource = new PublicResource();

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
                    if (THREAD_PRODUCE.equals(threadName)) {
                        //生产者
                        resource.increase();
                    } else if (THREAD_CONSUME.equals(threadName)) {
                        //消费者
                        resource.decrease();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class PublicResource {
        private int number = 0;//当前总共生产个数
        private final int size = 5;//最大产品个数
        private final Object object = new Object();

        //生产者生产产品
        public void increase() throws InterruptedException {
            synchronized (object) {
                //如果产品个数大于最大个数 等待
                while (number >= size) {
                    object.wait();
                }
                Thread.sleep(500);
                number++;
                System.out.println("++" + Thread.currentThread().getName() + ":生产了一个，总共有" + number);
                object.notifyAll();
            }
        }

        //消费者消费产品
        public void decrease() throws InterruptedException {
            synchronized (object) {
                //如果产品个数小于等于0 等待
                while (number <= 0) {
                    object.wait();
                }
                Thread.sleep(1000);
                number--;
                System.out.println("--" + Thread.currentThread().getName() + ":消费了一个，总共有" + number);
                object.notifyAll();
            }
        }
    }

}
