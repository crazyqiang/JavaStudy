package com.javastudy.DesignMode.Factory.Bean;

/**
 * Created by mq on 2019-10-25 16:03
 * mqcoder90@gmail.com
 */
public class IceCreamDrink implements IDrink {

    @Override
    public void drink() {
        System.out.println("drink: IceCream");
    }
}
