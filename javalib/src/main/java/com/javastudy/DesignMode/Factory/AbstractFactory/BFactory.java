package com.javastudy.DesignMode.Factory.AbstractFactory;

/**
 * Created by mq on 2019-10-25 17:29
 * mqcoder90@gmail.com
 */
public class BFactory implements IBaseFactory {

    @Override
    public IShape buildShape() {

        return new SmallShape();
    }

    @Override
    public IRaw buildRaw() {

        return new FruitRaw();
    }
}
