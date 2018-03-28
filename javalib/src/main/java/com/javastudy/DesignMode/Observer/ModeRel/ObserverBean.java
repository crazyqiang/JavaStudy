package com.javastudy.DesignMode.Observer.ModeRel;

/**
 * Created by mq on 2017/9/24 下午11:54
 * mqcoder90@gmail.com
 */

public class ObserverBean {

    public String netStatus;
    public String netType;

    public ObserverBean(String netType, String netStatus) {
        this.netStatus = netStatus;
        this.netType = netType;
    }

}
