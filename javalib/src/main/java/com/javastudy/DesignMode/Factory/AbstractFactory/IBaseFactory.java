package com.javastudy.DesignMode.Factory.AbstractFactory;

/**
 * Created by mq on 2019-10-25 17:27
 * mqcoder90@gmail.com
 */
public interface IBaseFactory {

    IShape buildShape();

    IRaw buildRaw();

}
