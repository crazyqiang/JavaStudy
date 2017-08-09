package com.javastudy.DesignMode.Strategy.ModeRel;

/**
 * Created by MQ on 2017/8/9.
 */

public class Context {

    Strategy strategy;

    private Context() {
        //在这里切换让谁去加载图片
//        strategy = new GlideStrategy();
//        strategy = new PicassoStrategy();
//        strategy = new FrescoStrategy();
        strategy = new ImageLoaderStrategy();
    }

    public void loadImage() {
        strategy.showImage();
    }

    public static Context getInstance() {
        return InnerClass.context;
    }

    private static final class InnerClass {
        private static Context context = new Context();
    }
}
