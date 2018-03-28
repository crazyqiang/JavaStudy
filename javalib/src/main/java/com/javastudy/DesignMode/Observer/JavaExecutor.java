package com.javastudy.DesignMode.Observer;


import com.javastudy.DesignMode.Observer.ModeRel.ObserverBean;
import com.javastudy.DesignMode.Observer.SystemObserver.NetSubject;
import com.javastudy.DesignMode.Observer.SystemObserver.SystemObserverA;
import com.javastudy.DesignMode.Observer.SystemObserver.SystemObserverB;

/**
 * Created by MQ on 2017/8/9.
 */

public class JavaExecutor {

    public static void main(String[] args) {

//        //第一种实现方式：
//        //初始化主题(被观察者)
//        NetConcreteSubject subject = new NetConcreteSubject();
//        //初始化观察者A
//        ConcreteObserverA observerA = new ConcreteObserverA();
//        //初始化观察者B
//        ConcreteObserverB observerB = new ConcreteObserverB();
//        //注册观察者A
//        subject.registerObserver(observerA);
//        //注册观察者B
//        subject.registerObserver(observerB);
//        //通知所有观察者数据有更新
//        ObserverBean bean = new ObserverBean("wifi", "有网");
//        subject.setStatusChanged(bean);


        //第二种实现方式：
        //初始化主题(被观察者)
        NetSubject subject = new NetSubject();
        //初始化观察者A
        SystemObserverA observerA = new SystemObserverA();
        //初始化观察者B
        SystemObserverB observerB = new SystemObserverB();
        //注册观察者A
        subject.addObserver(observerA);
        //注册观察者B
        subject.addObserver(observerB);
        //通知观察者有更新
        ObserverBean bean = new ObserverBean("4G流量", "有网");
        subject.setNetChanged(bean);

    }
}
