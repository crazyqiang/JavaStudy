package com.javastudy.DesignMode.Strategy.ModeRel;

/**
 * Created by MQ on 2017/8/9.
 */

public class Context {

    //持有一个抽象策略对象
    private Strategy strategy;

    private Context() {
        //初始化具体策略角色
//        strategy = new GlideStrategy();
//        strategy = new PicassoStrategy();
//        strategy = new FrescoStrategy();
        strategy = new ImageLoaderStrategy();
    }

    /**
     * 执行具体策略角色中的方法
     */
    public void loadImage() {
        strategy.showImage();
    }

    /**
     * 通过单例模式获得唯一的一个Context
     *
     * @return Context
     */
    public static Context getInstance() {
        return InnerClass.context;
    }

    /**
     * 静态内部类来初始化Context
     */
    private static final class InnerClass {
        private static Context context = new Context();
    }
}
