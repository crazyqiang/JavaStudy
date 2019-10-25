package com.javastudy.DesignMode.Factory.Bean;

/**
 * Created by mq on 2019-10-25 15:27
 * mqcoder90@gmail.com
 */
public class MilkDrink implements IDrink {
    @Override
    public void drink() {
        System.out.println("drink: MilkDrink");
    }
}
