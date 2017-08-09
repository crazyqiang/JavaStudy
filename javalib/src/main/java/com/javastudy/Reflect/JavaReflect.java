package com.javastudy.Reflect;

import java.lang.reflect.Method;

/**
 * Created by MQ on 2017/8/7.
 */

public class JavaReflect {

    public static void main(String[] args) {
        try {
            Class<?> cls = Class.forName("com.javastudy.Reflect.ReflectBean");
            Method setName = cls.getDeclaredMethod("setName", String.class);
            Object obj = cls.newInstance();
            setName.invoke(obj, "Î÷¹Ï");
            Method setPrice = cls.getDeclaredMethod("setPrice", double.class);
            setPrice.invoke(obj, 1.5);

            Method getName = cls.getDeclaredMethod("getName");
            System.out.println(getName.invoke(obj));

            Method getPrice = cls.getDeclaredMethod("getPrice");
            System.out.println(getPrice.invoke(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
