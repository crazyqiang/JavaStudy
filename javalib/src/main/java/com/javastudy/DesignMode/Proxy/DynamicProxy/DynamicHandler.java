package com.javastudy.DesignMode.Proxy.DynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * InvocationHandler是代理类的辅助类
 */
public class DynamicHandler implements InvocationHandler {

    private ISubject target;

    public DynamicHandler(ISubject target) {
        this.target = target;
    }

    public Object bind() {
        //通过反射得到代理类实例
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
        //调用被代理类对应的方法
        return method.invoke(target, objects);
    }
}
