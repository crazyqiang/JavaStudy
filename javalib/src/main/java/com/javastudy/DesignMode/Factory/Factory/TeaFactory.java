package com.javastudy.DesignMode.Factory.Factory;

import com.javastudy.DesignMode.Factory.Bean.IDrink;
import com.javastudy.DesignMode.Factory.Bean.TeaDrink;

/**
 * Created by mq on 2019-10-25 16:01
 * mqcoder90@gmail.com
 */
public class TeaFactory implements IFactory {
    @Override
    public IDrink produce() {

        return new TeaDrink();
    }
}
