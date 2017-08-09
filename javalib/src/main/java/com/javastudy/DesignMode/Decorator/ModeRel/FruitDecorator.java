package com.javastudy.DesignMode.Decorator.ModeRel;

/**
 * 定义装饰者类
 * Created by MQ on 2017/4/16.
 */

public class FruitDecorator extends Decorator {
    Sweet sweet;

    public FruitDecorator(Sweet sweet) {
        this.sweet = sweet;
    }

    @Override
    public String getDescription() {
        return sweet.getDescription() + "水果，";
    }

    @Override
    public double cost() {
        return sweet.cost() + 10;
    }
}
