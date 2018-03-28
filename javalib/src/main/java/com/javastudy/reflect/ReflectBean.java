package com.javastudy.reflect;

/**
 * Created by MQ on 2017/8/7.
 */

public class ReflectBean {

    private String name;
    protected double price;
    int age;
    public String sex;

    public ReflectBean() {
    }

    private ReflectBean(String name) {
        this.name = name;
    }

    protected ReflectBean(String name, double price) {
        this.name = name;
        this.price = price;
    }

    ReflectBean(double price) {
        this.price = price;
    }


    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    private void setPrice(double price) {
        this.price = price;
    }

    protected void showName(){}

    @Override
    public String toString() {
        return "ReflectBean [name=" + name +
                ",price=" + price +
                ",age=" + age +
                "，sex=" + sex + "]";
    }
}
