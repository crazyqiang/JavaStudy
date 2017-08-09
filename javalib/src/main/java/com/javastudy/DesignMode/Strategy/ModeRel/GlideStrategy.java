package com.javastudy.DesignMode.Strategy.ModeRel;

/**
 * Created by MQ on 2017/8/9.
 */

public class GlideStrategy implements Strategy{
    @Override
    public void showImage() {
        System.out.println("Glide ShowImage");
    }
}
