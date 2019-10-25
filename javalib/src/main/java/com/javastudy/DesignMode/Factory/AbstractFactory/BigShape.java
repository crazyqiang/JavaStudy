package com.javastudy.DesignMode.Factory.AbstractFactory;

/**
 * Created by mq on 2019-10-25 17:30
 * mqcoder90@gmail.com
 */
public class BigShape implements IShape {
    @Override
    public void produceShape() {
        System.out.println("Shape:大杯");
    }
}
