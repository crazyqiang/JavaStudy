package com.javastudy.DesignMode.Proxy.StaticProxy;

/**
 * Created by mq on 2019-10-24 16:18
 * mqcoder90@gmail.com
 */
public class RealSSubject implements ISSubject {
    @Override
    public void say() {
        System.out.println("我是静态代理对象!");
    }
}
