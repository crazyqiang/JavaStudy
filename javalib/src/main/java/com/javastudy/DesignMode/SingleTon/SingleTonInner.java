package com.javastudy.DesignMode.SingleTon;

/**
 * 静态内部类方式，由于静态内部类只初始化一次，所以这种方式是线程安全的
 * Created by MQ on 2017/8/7.
 */

public class SingleTonInner {

    private SingleTonInner() {
    }

    public static SingleTonInner getInstance() {
        return InnerSingle.instance;
    }

    private static class InnerSingle {
        private static SingleTonInner instance = new SingleTonInner();
    }

}
