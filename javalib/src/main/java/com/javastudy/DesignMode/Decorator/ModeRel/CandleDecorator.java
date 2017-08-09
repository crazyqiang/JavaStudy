package com.javastudy.DesignMode.Decorator.ModeRel;

/**
 *定义具体装饰着类
 * Created by MQ on 2017/4/16.
 */

public class CandleDecorator extends Decorator {
    Sweet sweet;

    public CandleDecorator(Sweet sweet) {
        this.sweet = sweet;
    }

    @Override
    public String getDescription() {
        return sweet.getDescription() + "一根蜡烛，";
    }

    @Override
    public double cost() {
        return sweet.cost() + 10;
    }
}
