package com.javastudy.DesignMode.Factory.Factory;

import com.javastudy.DesignMode.Factory.Bean.IDrink;
import com.javastudy.DesignMode.Factory.Bean.IceCreamDrink;

/**
 * Created by mq on 2019-10-25 16:02
 * mqcoder90@gmail.com
 */
public class IceFactory implements IFactory {

    @Override
    public IDrink produce() {

        return new IceCreamDrink();
    }
}
