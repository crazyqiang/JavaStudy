package com.javastudy.DesignMode.Observer.ModeRel;

/**
 * Created by mq on 2017/9/24 下午8:41
 * mqcoder90@gmail.com
 * 主题（被观察者接口）
 */

public interface Subject {

    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();

}
