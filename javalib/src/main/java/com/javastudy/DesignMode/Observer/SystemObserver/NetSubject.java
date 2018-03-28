package com.javastudy.DesignMode.Observer.SystemObserver;

import com.javastudy.DesignMode.Observer.ModeRel.ObserverBean;

import java.util.Observable;

/**
 * Created by mq on 2017/9/25 上午12:58
 * mqcoder90@gmail.com
 * 被观察者
 */

public class NetSubject extends Observable {

    public void setNetChanged(ObserverBean bean){
        setChanged();
        notifyObservers(bean);
    }
}
