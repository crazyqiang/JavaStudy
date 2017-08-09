**单例模式：用来创造独一无二的，只能有一个实例的对象设计模式。单例模式确保一个类只有一个实例，并提供一个全局访问点。** 相比于全局变量(对对象的静态引用)，单例模式可以延迟实例化，而且全局变量不能保证只有一个实例。

**UML图：**

![singleton.png](http://upload-images.jianshu.io/upload_images/587163-8793e658ac9d1078.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**如何保证只有一个实例呢？** 我们可以构造器的修饰符变为private，这样外部类就不能通过new来获取实例了，还记得修饰符对应的作用域吗？看下图：

![modifier.png](http://upload-images.jianshu.io/upload_images/587163-3ac5c99a424b7f15.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

确定了构造器为private之后，接着再定义一个static方法供全局访问来获得这个单例，首先可以先这样实现：
```
 private static SingleTon uniqueInstance;
 //私有构造器
 private SingleTon() {
 }
 //定义static供全局访问
 public static SingleTon getInstance() {
     if (uniqueInstance == null) {
        uniqueInstance = new SingleTon();
     }
     return uniqueInstance;
 }
```
当你正在得意单例模式愉快地搞定了的时候，殊不知代码有个致命的隐患：**多线程**。假如项目中不会用到多线程，那么上面的代码已经够用了。但如果项目中会用到多线程，比如项目中有2个线程，线程A执行到getInstance()方法中的if (uniqueInstance == null)了，这时CPU去执行线程B，而线程B恰巧也执行到getInstance()方法中的if (uniqueInstance == null)了，此时uniqueInstance 还没有被初始化，所以线程A和线程B都会去初始化类，导致单例失效，存在多个实例，解决方法就是加同步锁：**synchronized**。下面给出几种不同的实现方式：

1、如果对性能要求不高，可以直接简单粗暴地加到getInstance()前面：
```
    private static SingleTon uniqueInstance;
    private SingleTon() {
    }
    public static synchronized SingleTon getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SingleTon();
        }
        return uniqueInstance;
    }
```
如果将getInstance()运行在频繁运行的地方，上面的单例会让执行效率大大下降，接着看下面的方式。

2、饿汉式单例：
```
 //在静态初始化器中创建实例
 private static SingleTon uniqueInstance = new SingleTon();
 private SingleTon() {
 }
 public static SingleTon getInstance() {
     //已经有了实例，直接使用
     return uniqueInstance;
 }
```
在静态初始化器中创建单例，这样就保证了线程安全，在JVM加载这个类时马上创建了唯一的实例，这样就保证了任何线程访问uniqueInstance静态变量之前，一定先创建了此实例。饿汉式单例优点是不用使用同步锁，保证了线程安全；缺点也很明显，类加载时就初始化了实例，假如getInstance()没有使用，浪费了内存。

3、懒汉式单例(双重加锁式)：
```
 private volatile static SingleTon uniqueInstance;
 private SingleTon() {
 }
 public static SingleTon getInstance() {
     //检查实例，如果不存在就进入同步区
     if (uniqueInstance == null) {
          //只有第一次才会执行同步锁块
         synchronized (SingleTon.class) {
             //进入同步块内，实例仍是null的时候才去创建实例
             if (uniqueInstance == null) {
                 uniqueInstance = new SingleTon();
             }
         }
     }
     return uniqueInstance;
 }
```
**volatile关键字：**保证数据的可见性，即当一个共享变量被volatile修饰时，它会保证修改的值会立即被更新到主存，当有其他线程需要读取时，它会去内存中读取最新的值。也就是说两个线程同时操作一个变量，那么一个线程A对这个变量的写操作一定先行发生于另一个线程B对这个变量的读操作。所以当uniqueInstance变量被初始化成SingleTon实例时，volatile能保证多个线程正确的处理uniqueInstance变量。
双重加锁式保证了只有第一次调用时才会执行同步锁块，后面再调用就不会执行同步锁块了，相比于1中的方式，性能大大提升。

(2017.08.06更新)

4、静态内部类：
```
public class InnerSingleTon {

    private InnerSingleTon() {
    }

    public static InnerSingleTon getInstance() {
        return InnerSingle.instance;
    }

    private static class InnerSingle {
        private static InnerSingleTon instance = new InnerSingleTon();
    }

}
```
由于静态内部类只加载一次，所以这种方式是线程安全的！

**针对不同情况，可以选择上面的一种来实现单例模式！**
