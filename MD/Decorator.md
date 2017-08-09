**装饰者模式：**
动态的给对象添加一些额外的属性或行为。相比于使用继承，装饰者模式更加灵活。

**UML图：**
![装饰者.png](http://upload-images.jianshu.io/upload_images/587163-697df51451031108.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

一般来说装饰者模式有下面几个参与者：
- Component：装饰者和被装饰者共同的父类，是一个接口或者抽象类，用来定义基本行为
- ConcreteComponent：定义具体对象，即被装饰者
- Decorator：抽象装饰者，继承自Component，从外类来扩展ConcreteComponent。对于ConcreteComponent来说，不需要知道Decorator的存在，Decorator是一个接口或抽象类
- ConcreteDecorator：具体装饰者，用于扩展ConcreteComponent

注：装饰者和被装饰者对象有相同的超类型，因为装饰者和被装饰者必须是一样的类型，**这里利用继承是为了达到类型匹配，而不是利用继承获得行为。**

利用继承设计子类，只能在编译时静态决定，并且所有子类都会继承相同的行为；利用组合的做法扩展对象，就可以在运行时动态的进行扩展。装饰者模式遵循开放-关闭原则：**类应该对扩展开放，对修改关闭。**利用装饰者，我们可以实现新的装饰者增加新的行为而不用修改现有代码，而如果单纯依赖继承，每当需要新行为时，还得修改现有的代码。

**实例：**

假设一家甜品店，出售蛋糕，除了蛋糕外，还可以在蛋糕上布置水果，蜡烛等，但是水果和蜡烛需要额外收费，**假设一个蛋糕的价格是66元，水果和蜡烛分别需要额外付10元，那么怎么样来动态的计算价格呢？**
首先，定义组件类，也是装饰者和被装饰者的超类Sweet .java：
```
public abstract class Sweet {
    String description = "Sweet";

    public String getDescription() {
        return description;
    }

    public abstract double cost();
}
```
定义被装饰者蛋糕类，Cake .java：
```
public class Cake extends Sweet {
    @Override
    public String getDescription() {
        return "一个蛋糕";
    }

    @Override
    public double cost() {
        return 66;
    }
}
```
定义抽象装饰者类Decorator.java：
```
public abstract class Decorator extends Sweet {
    public abstract String getDescription();
}
```
定义具体装饰者水果类，FruitDecorator.java：
```
public class FruitDecorator extends Decorator {
    Sweet sweet;

    public FruitDecorator(Sweet sweet) {
        this.sweet = sweet;
    }

    @Override
    public String getDescription() {
        return sweet.getDescription() + "，水果";
    }

    @Override
    public double cost() {
        return sweet.cost() + 10;
    }
}
```
定义具体装饰者蜡烛类，CandleDecorator.java：
```
public class CandleDecorator extends Decorator {
    Sweet sweet;

    public CandleDecorator(Sweet sweet) {
        this.sweet = sweet;
    }

    @Override
    public String getDescription() {
        return sweet.getDescription() + "，蜡烛";
    }

    @Override
    public double cost() {
        return sweet.cost() + 10;
    }
}
```
最后根据不同的选择来结算价格：
```
public static void main(String[] args) {

    Cake cake = new Cake();
    System.out.println(cake.getDescription() + "总共花费" + cake.cost());

    FruitDecorator fruitDecorator = new FruitDecorator(cake);
    System.out.println(fruitDecorator.getDescription() + "总共花费" + fruitDecorator.cost());

    CandleDecorator candleDecorator = new CandleDecorator(fruitDecorator);
    System.out.println(candleDecorator.getDescription() + "总共花费" + candleDecorator.cost());

    }
```
执行结果：
```
一个蛋糕，总共花费66.0
一个蛋糕，水果，总共花费76.0
一个蛋糕，水果，一根蜡烛，总共花费86.0
```
可见，**装饰者模式可以非常灵活地动态地给被装饰者添加新行为，它的缺点也显现出来了，那就是必须管理好更多的对象，但是，装饰者模式可以和工厂模式或生成器这样的模式一块使用来避免这个问题。**

PS：我们使用java.IO类时类似于下面的这种写法：
```
new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream( "io.txt" )));
```
java.IO类用到的也是装饰者模式！是不是一下清楚了好多~

**总结：**
1、装饰者和被装饰者对象有相同的超类型，所以在任何需要原始对象（被装饰者）的场合，都可以用装饰过得对象代替原始对象。

2、可以用一个或多个装饰者包装一个对象（被装饰者）

3、**装饰者可以在所委托的装饰者行为之前或之后加上自己的行为，以达到特定的目的**

4、被装饰者可以在任何时候被装饰，所以可以在运行时动态地、不限量地用你喜欢的装饰者来装饰对象。

5、装饰者会导致出现很多小对象，如果过度使用，会让程序变得复杂。
