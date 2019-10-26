package com.javastudy.DesignMode.Proxy.StaticProxy;


/**
 * Created by mq on 2019-10-25 23:17
 * mqcoder90@gmail.com
 */
public class StaticProxy implements ISSubject {

    ISSubject realSubject;

    public StaticProxy(ISSubject realSubject) {
        this.realSubject = realSubject;
    }

    @Override
    public void say() {
        realSubject.say();
    }
}
