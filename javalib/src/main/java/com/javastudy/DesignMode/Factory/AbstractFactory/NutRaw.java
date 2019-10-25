package com.javastudy.DesignMode.Factory.AbstractFactory;

/**
 * Created by mq on 2019-10-25 17:53
 * mqcoder90@gmail.com
 */
public class NutRaw implements IRaw {

    @Override
    public void produceRaw() {
        System.out.println("produceRaw：添加果仁");
    }
}
