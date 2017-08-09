package com.javastudy.DesignMode.Decorator;

import com.javastudy.DesignMode.Decorator.ModeRel.Cake;
import com.javastudy.DesignMode.Decorator.ModeRel.CandleDecorator;
import com.javastudy.DesignMode.Decorator.ModeRel.FruitDecorator;

/**
 * Created by MQ on 2017/8/9.
 */

public class JavaExecutor {

    public static void main(String[] args) {
        Cake cake = new Cake();
        System.out.println(cake.getDescription() + "总共花费" + cake.cost());
        FruitDecorator fruitDecorator = new FruitDecorator(cake);
        System.out.println(fruitDecorator.getDescription() + "总共花费" + fruitDecorator.cost());
        CandleDecorator candleDecorator = new CandleDecorator(fruitDecorator);
        System.out.println(candleDecorator.getDescription() + "总共花费" + candleDecorator.cost());
    }
}
