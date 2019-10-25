package com.javastudy.DesignMode.Factory;


import com.javastudy.DesignMode.Factory.AbstractFactory.AFactory;
import com.javastudy.DesignMode.Factory.AbstractFactory.IBaseFactory;
import com.javastudy.DesignMode.Factory.AbstractFactory.IRaw;
import com.javastudy.DesignMode.Factory.AbstractFactory.IShape;
import com.javastudy.DesignMode.Factory.Bean.IDrink;
import com.javastudy.DesignMode.Factory.Bean.MilkDrink;
import com.javastudy.DesignMode.Factory.Bean.TeaDrink;
import com.javastudy.DesignMode.Factory.Factory.IFactory;
import com.javastudy.DesignMode.Factory.Factory.IceFactory;
import com.javastudy.DesignMode.Factory.SimpleFactory.DrinkFactory;

/**
 * Created by MQ on 2017/8/9.
 */

public class JavaExecutor {

    public static void main(String[] args) {

        System.out.println("--------------通过new实现，耦合性强--------------");
        drinkSomeThing("tea");

        System.out.println("--------------简单工厂模式--------------");
        //简单工厂模式
        IDrink drink = DrinkFactory.getDrink("milk");
        drink.drink();

        System.out.println("--------------工厂模式--------------");
        //工厂模式
        IFactory factory = new IceFactory();
//        IFactory factory = new TeaFactory();
        IDrink drink1 = factory.produce();
        drink1.drink();

        System.out.println("--------------抽象工厂模式--------------");
        //抽象工厂模式
        IRaw raw;
        IShape shape;
        IBaseFactory baseFactory;
        //大杯加果仁的
        baseFactory = new AFactory();
        //小杯加水果的
//        baseFactory = new BFactory();

        raw = baseFactory.buildRaw();
        shape = baseFactory.buildShape();
        shape.produceShape();
        raw.produceRaw();
    }

    private static void drinkSomeThing(String type) {
        IDrink drink = null;
        switch (type) {
            case "tea":
                drink = new TeaDrink();
                break;
            case "milk":
                drink = new MilkDrink();
                break;
            default:
                break;
        }
        drink.drink();
    }
}
