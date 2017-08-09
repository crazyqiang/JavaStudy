\####定义

策略模式属于对象的行为模式。其用意是针对一组算法，将每一个算法封装到具有共同接口的独立的类中，从而使得它们可以相互替换。策略模式使得算法可以在不影响客户端的情况下发生变化。



####UML图

![strategy.png](http://upload-images.jianshu.io/upload_images/587163-a3ff907dd76eced2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 环境(Context)角色：持有一个Strategy的引用，最终给客户端调用。
- 抽象策略(Strategy)角色：这是一个抽象角色，通常由一个接口或抽象类实现。此角色给出所有的具体策略类所需的接口。
- 具体策略(ConcreteStrategy)角色：包装了相关的算法或行为。

####实例

假设我们现在需要一个图片加载框架，我们可以选择Glide、Fresco、ImageLoader、Picasso其中的一个，比如选择ImageLoader实现了图片加载，正当我们美滋滋地实现了这个功能的时候，突然有一天Boss让你把加载框架换成Glide，如果很多类都去调用了ImageLoader，那么就得一个一个地去替换，替换到怀疑人生，如果之前使用到策略模式，那么替换过程将会变得非常简单。

首先来定义相应的角色：

1、环境角色：
```
public class Context {

    Strategy strategy;

    private Context() {
        //在这里切换让谁去加载图片
        // strategy = new GlideStrategy();
        // strategy = new PicassoStrategy();
        // strategy = new FrescoStrategy();
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
```

2、抽象策略角色
```
public interface Strategy {
    void showImage();
}
```

3、具体策略角色

```
//Glide
public class GlideStrategy implements Strategy{
    @Override
    public void showImage() {
        System.out.println("Glide ShowImage");
    }
}

//Fresco
public class FrescoStrategy implements Strategy {

    @Override
    public void showImage() {
        System.out.println("Fresco ShowImage");
    }
}

//ImageLoader
public class ImageLoaderStrategy implements Strategy {

    @Override
    public void showImage() {
        System.out.println("ImageLoader ShowImage");
    }

}

//Picasso
public class PicassoStrategy implements Strategy {
    @Override
    public void showImage() {
        System.out.println("Picasso ShowImage");
    }
}
```

最后可以直接这么调用：
```
Context.getInstance().loadImage();
```
这样我们在Context的构造函数中初始化的哪个策略角色就会调用它的方法去加载图片，从而让图片加载框架独立于调用者而灵活变化。
完整代码地址：[策略模式](https://github.com/crazyqiang/JavaStudy)

####策略模式的优缺点
1、优点
-  对客户隐藏具体策略(算法)的实现细节，彼此完全独立。具体策略类实现自同一个接口，他们之间可以自由切换
- 易于扩展，如果需要添加新的策略类，基本不用改变现有代码
- 使用策略模式可以避免使用多重条件转移语句。多重转移语句不易维护，它把采取哪一种算法或采取哪一种行为的逻辑与算法或行为的逻辑混合在一起，统统列在一个多重转移语句里面，比使用继承的办法还要原始和落后。

2、缺点
- 维护各个策略类会给带来额外开销，策略类太多时，消耗更大。
- 客户端必须知道所有的策略类，并自行决定使用哪一个策略类。这就意味着客户端必须知道这些算法的区别并决定使用哪个策略类。

####适用场景

- 几个类的主要逻辑相同，只在部分逻辑的算法和行为上稍有区别的情况。
- 有几种相似的行为，或者说算法，客户端需要动态地决定使用哪一种，那么可以使用策略模式，将这些算法封装起来供客户端调用。
