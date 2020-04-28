<!-- TOC -->

- [Set接口](#Set接口)
- [HashSet](#HashSet)
    - [HashSet的使用&遍历](#HashSet的使用&遍历)
    - [源码浅析](#源码浅析)
       - [构造函数](#构造函数)
       - [HashSet常用方法](#HashSet常用方法)
       - [遍历](#遍历)
- [LinkedHashSet](#LinkedHashSet)
    - [LinkedHashSet的使用&遍历](#LinkedHashSet的使用&遍历)
    - [源码浅析](#源码浅析)
- [TreeSet](#TreeSet)
    - [TreeSet的使用&遍历](#TreeSet的使用&遍历)
- [总结](#总结)
- [参考](#参考)

<!-- /TOC -->

# Set接口

整体Collection&Map的实现关系：
![Collection&Map.png](https://upload-images.jianshu.io/upload_images/587163-f77963ba48fe412f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

```
public interface Set<E> extends Collection<E> {
  
  int size();

  boolean isEmpty();

  boolean contains(Object o);

  Iterator<E> iterator();

  Object[] toArray();

  <T> T[] toArray(T[] a);

  boolean add(E e);

  boolean remove(Object o);

  boolean containsAll(Collection<?> c);

  boolean addAll(Collection<? extends E> c);

  boolean retainAll(Collection<?> c);

  boolean removeAll(Collection<?> c);

  void clear();

  boolean equals(Object o);

  int hashCode();

  @Override
  default Spliterator<E> spliterator() {
      return Spliterators.spliterator(this, Spliterator.DISTINCT);
   }

}
```

# HashSet

HashSet基于key-value形式保存元素，底层是基于HashMap实现的`(默认容量是16 负载因子是0.75)`，HashSet的相关操作基本都是调用HashMap来完成的，Hashset存储元素具有无序、不可重复的特点。

- 无序: 因为底层数据结构是HashMap并且利用HashMap去存储元素，所以HashSet存储的元素也是无序的。
- 不可重复：HashSet#add添加元素时，底层利用HashMap将添加的元素当成HashMap中的key,并新建一个默认值PRESENT(Object类型,final修饰不可修改)当做HashMap中的value，利用HashMap中key值相等会覆盖的特点，从而实现HashSet不能存储重复元素(当添加元素不重复时，HashSet#add方法添加成功并返回true；如果添加的元素重复，则添加失败，并返回false)。

## HashSet的使用&遍历

```
Set<String> hashSet = new HashSet<>();
hashSet.add("A");
hashSet.add("C");
hashSet.add("B");
hashSet.add("D");
hashSet.add("A");

System.out.println("使用for循环遍历：");
for (String str : hashSet) {
    System.out.println("元素：" + str);
}

System.out.println("-------------");

System.out.println("使用Iterator循环遍历：");
Iterator<String> iterator = hashSet.iterator();
while (iterator.hasNext()) {
    System.out.println("元素：" + iterator.next());
}
```
执行结果：
```
元素：A
元素：B
元素：C
元素：D

-------------

使用Iterator循环遍历：
元素：A
元素：B
元素：C
元素：D
```
我们往HashSet中添加了2次"A"元素，通过结果发现并没有存储2个"A"，并且元素存储顺序并不是按添加顺序排序的。所以HashSet存储的是无序、不可重复的元素。

## 源码浅析

### 构造函数

```
//默认无参构造函数 内部初始化一个HashMap  默认容量是16 负载因子0.75
public HashSet() {
    map = new HashMap<>();
}

//构造一个包含指定collection的set
public HashSet(Collection<? extends E> c) {
    map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
    addAll(c);
}

//构造一个指定容量和负载因子的HashSet
public HashSet(int initialCapacity, float loadFactor) {
    map = new HashMap<>(initialCapacity, loadFactor);
}

//构造函数中指定容量
public HashSet(int initialCapacity) {
    map = new HashMap<>(initialCapacity);
}

//构造函数仅能在包内访问 此构造函数是为了支持LinkedHashSet的初始化
HashSet(int initialCapacity, float loadFactor, boolean dummy) {
    map = new LinkedHashMap<>(initialCapacity, loadFactor);
}
```
### HashSet常用方法

```
//hashSet中元素个数
public int size() {
    return map.size();
}
//是否为空 为空返回true 不为空返回false
public boolean isEmpty() {
    return map.isEmpty();
}
//hashSet中是否包含元素o，包含返回true；不包含返回false
public boolean contains(Object o) {
    return map.containsKey(o);
}
//添加元素到hashSet中 成功返回true 失败返回false
public boolean add(E e) {
    return map.put(e, PRESENT)==null;
}
//删除hashSet中的元素 成功返回true 失败返回false
public boolean remove(Object o) {
    return map.remove(o)==PRESENT;
}
//清空hashSet中的元素
public void clear() {
    map.clear();
}
//拷贝副本
@SuppressWarnings("unchecked")
public Object clone() {
    try {
        HashSet<E> newSet = (HashSet<E>) super.clone();
        newSet.map = (HashMap<E, Object>) map.clone();
        return newSet;
    } catch (CloneNotSupportedException e) {
        throw new InternalError(e);
    }
}
```

### 遍历
```
public Iterator<E> iterator() {
    return map.keySet().iterator();
}
```
可以看到调用的是HashMap#keySet方法，所以也证明了上面的结论：**Hashset中的元素时保存在HashMap中的key中，具有无序、不可重复的特点；value 则是使用的 PRESENT对象，该对象被static final修饰，不可更改**。

# LinkedHashSet

**LinkedHashSet继承于HashSet,所以存放的元素不可重复；又因为底层是通过LinkedHashMap(内部维护一个双向链表)实现的，所以LinkedHashSet可以按顺序遍历(按插入顺序排序)。**

## LinkedHashSet的使用&遍历

```
LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
linkedHashSet.add("A");
linkedHashSet.add("C");
linkedHashSet.add("B");
linkedHashSet.add("D");
linkedHashSet.add("A");

for (String s : linkedHashSet) {
    System.out.println("value is " + s);
}

System.out.println("-------------");

Iterator<String> iterator = linkedHashSet.iterator();
while (iterator.hasNext()) {
    System.out.println("value is " + iterator.next());
}
```
执行结果：
```
value is A
value is C
value is B
value is D
-------------
value is A
value is C
value is B
value is D
```
添加元素时，分别在第一次和最后一次尝试添加元素"A"，通过结果看只有第一次添加成功。通过结果可以看到LinkedHashSet可以按插入顺序遍历元素，并且存储的元素是不能重复的。

## 源码浅析

LinkedHashSet的源码很简单：

```
public class LinkedHashSet<E> extends HashSet<E>
              implements Set<E>, Cloneable, java.io.Serializable {

    private static final long serialVersionUID = -2851667679971038690L;

    public LinkedHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor, true);
    }

    public LinkedHashSet(int initialCapacity) {
        super(initialCapacity, .75f, true);
    }

    public LinkedHashSet() {
        super(16, .75f, true);
    }

    public LinkedHashSet(Collection<? extends E> c) {
        super(Math.max(2*c.size(), 11), .75f, true);
        addAll(c);
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.DISTINCT | Spliterator.ORDERED);
    }
}
```
只有几个构造参数并通过super调用了父类的构造函数，还记得父类HashSet中的那个构造函数:
```
//HashSet构造函数
HashSet(int initialCapacity, float loadFactor, boolean dummy) {
    map = new LinkedHashMap<>(initialCapacity, loadFactor);
}
```
这个构造函数就是供LinkedHashSet构造函数调用super时被调用的，所以LinkedHashSet内部就是通过LinkedHashMap及父类HashSet实现的，LinkedHashSet存储的是有序、不可重复的元素。

# TreeSet

TreeSet内部是通过TreeMap(红黑树)实现的，因为TreeMap可以按自然顺序或自定义顺序排序，所以TreeSet同样可以对元素进行排序存储。

## TreeSet的使用&遍历

- 按自然顺序排序：

```
TreeSet<String> treeSet = new TreeSet<>();
treeSet.add("A");
treeSet.add("C");
treeSet.add("B");
treeSet.add("D");
treeSet.add("A");

for (String s : treeSet) {
    System.out.println("value is " + s);
}

System.out.println("-------------");

Iterator<String> iterator = treeSet.iterator();
while (iterator.hasNext()) {
    System.out.println("value is " + iterator.next());
}
```
执行结果:
```
value is A
value is B
value is C
value is D
-------------
value is A
value is B
value is C
value is D
```
放入的元素是String类型，内部实现了Comparable接口，TreeSet对元素进行了排序并且放入的元素时不能重复。

- 自定义排序：

```
Person person1 = new Person("张三", 10);
Person person2 = new Person("李四", 30);
Person person3 = new Person("王五", 20);
Person person4 = new Person("赵六", 10);

TreeSet<Person> treeSet = new TreeSet<>();
treeSet.add(person1);
treeSet.add(person2);
treeSet.add(person3);
treeSet.add(person4);

for (Person person : treeSet) {
    System.out.println("value is " + person);
}

System.out.println("-------------");

Iterator<Person> iterator = treeSet.iterator();
while (iterator.hasNext()) {
    System.out.println("value is " + iterator.next());
}



static class Person implements Comparable<Person> {

    private String Name;
    private int age;

    Person(String name, int age) {
        this.Name = name;
        this.age = age;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int compareTo(Person o) {
        if (age >= o.getAge()) {
            //年龄大的或相等的元素放在后面
            return 1;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "[person(name:" + getName() + ",age:" + getAge() + ")]";
    }
}
```
执行结果：
```
value is [person(name:张三,age:10)]
value is [person(name:赵六,age:10)]
value is [person(name:王五,age:20)]
value is [person(name:李四,age:30)]
-------------
value is [person(name:张三,age:10)]
value is [person(name:赵六,age:10)]
value is [person(name:王五,age:20)]
value is [person(name:李四,age:30)]
```
TreeSet<Person>放入的是Person类型，在Person类中实现了Comparable接口并重写其中的compareTo方法，在compareTo方法中根据age进行排序，age大的放在后面，所以最后遍历的结果可以看到age是从小到大输出的。

# 总结

- HashSet内部几乎都是通过HashMap实现的，LinkedHashSet内部是通过LinkedHashMap实现的，TreeSet内部是通过TreeMap实现的；
- HashSet存储无序、不可重复元素；LinkedHashSet存储有序(按插入顺序排序)、不可重复元素；TreeSet内部是TreeMap(红黑树)，内部元素可以按自然顺序或自定义顺序排序。

# 参考
【1】https://wiki.jikexueyuan.com/project/java-enhancement/java-twentyfour.html

【2】TreeSet: https://wiki.jikexueyuan.com/project/java-enhancement/java-twentyeight.html

【3】TreeSet: https://www.cnblogs.com/skywang12345/p/3311268.html

