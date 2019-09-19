package com.javastudy.MutiThread.ThreadLocal;

/**
 * Created by mq on 2019-09-17 22:45
 * mqcoder90@gmail.com
 */
public class _ThreadLocal {

    public static void main(String[] args) throws InterruptedException {
        final ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("AAA");

        new Thread(new Runnable() {
            @Override
            public void run() {
                threadLocal.set("BBB");
                System.out.println("get in " + Thread.currentThread().getName() + " " + threadLocal.get());
            }
        }).start();

        Thread.sleep(1000);
        System.out.println("get in main thread " + threadLocal.get());
    }


}
