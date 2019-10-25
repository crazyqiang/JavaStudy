package com.javastudy.DesignMode.Factory.SimpleFactory;

import com.javastudy.DesignMode.Factory.Bean.IDrink;
import com.javastudy.DesignMode.Factory.Bean.MilkDrink;
import com.javastudy.DesignMode.Factory.Bean.TeaDrink;

/**
 * Created by mq on 2019-10-25 15:28
 * mqcoder90@gmail.com
 */
public class DrinkFactory {

    public static IDrink getDrink(String type) {
        switch (type) {
            case "tea":
                return new TeaDrink();
            case "milk":
                return new MilkDrink();
            default:
                break;
        }
        return null;
    }
}
