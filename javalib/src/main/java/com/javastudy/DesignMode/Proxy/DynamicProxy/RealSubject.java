package com.javastudy.DesignMode.Proxy.DynamicProxy;

/**
 * Created by mq on 2019-10-24 16:18
 * mqcoder90@gmail.com
 */
public class RealSubject implements ISubject {
    @Override
    public void say() {
        System.out.println("我是动态代理对象!");
    }
}
