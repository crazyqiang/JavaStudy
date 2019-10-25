package com.javastudy.DesignMode.Factory.Bean;

/**
 * Created by mq on 2019-10-25 15:26
 * mqcoder90@gmail.com
 */
public class TeaDrink implements IDrink {

    @Override
    public void drink() {
        System.out.println("drink: TeaDrink");
    }
}
