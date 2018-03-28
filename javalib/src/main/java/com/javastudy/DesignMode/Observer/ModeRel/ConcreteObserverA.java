package com.javastudy.DesignMode.Observer.ModeRel;

/**
 * Created by mq on 2017/9/24 下午11:58
 * mqcoder90@gmail.com
 * 具体观察者A
 */

public class ConcreteObserverA implements Observer {

    @Override
    public void update(Object obj) {
        if (obj instanceof ObserverBean) {
            ObserverBean bean = (ObserverBean) obj;
            System.out.print("ConcreteObserverA 网络类型："
                    + bean.netType + ",网络状态：" + bean.netStatus + "\n");
        }
    }
}
