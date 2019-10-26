package com.javastudy.DesignMode.Proxy;

import com.javastudy.DesignMode.Proxy.DynamicProxy.DynamicHandler;
import com.javastudy.DesignMode.Proxy.DynamicProxy.ISubject;
import com.javastudy.DesignMode.Proxy.DynamicProxy.RealSubject;
import com.javastudy.DesignMode.Proxy.StaticProxy.RealSSubject;
import com.javastudy.DesignMode.Proxy.StaticProxy.StaticProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理模式 包括静态代理&动态代理
 */

public class JavaExecutor {

    public static void main(final String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        //静态代理
        StaticProxy staticProxy = new StaticProxy(new RealSSubject());
        staticProxy.say();

        //1、动态代理
        DynamicHandler handler = new DynamicHandler(new RealSubject());
        ISubject subject = (ISubject) handler.bind();
        subject.say();


        //2、动态代理
        //new 目标对象
        final ISubject target = new RealSubject();
        Class proxyClass = Proxy.getProxyClass(target.getClass().getClassLoader(), target.getClass().getInterfaces());
        //得到带参数的构造器
        Constructor constructor = proxyClass.getConstructor(InvocationHandler.class);
        //反射创建代理实例
        ISubject impl = (ISubject) constructor.newInstance(new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

                //指定目标对象对应的方法
                Object result = method.invoke(target, args);
                //返回目标对象执行的结果
                return result;
            }
        });
        impl.say();

    }


}
