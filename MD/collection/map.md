@[toc]

HashMap、ConcurrentHashMap、LinkedHashMap、TreeMap与Map的关系：

![map.png](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy81ODcxNjMtODBhY2EzZTI2OGMwZTJjMi5wbmc?x-oss-process=image/format,png)

他们都实现了Map接口，都以key-value形式保存元素，同时各自又有不同的特点，下面会一一分析。

## HashMap
HashMap在项目中使用很频繁，底层实现是数组+链表(jdk1.8版本加入了红黑树)。我们知道数组的优点是查找快，增加或删除元素慢；链表的优点是增加或删除快，查找慢。HashMap结合了两者的特点，增删改查都有不错的性能，下面就来了解一下HashMap的内部机制。

### HashMap的存储结构
![hashmap.png](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy81ODcxNjMtY2EzMzQyMzQwMDk4MDc3Ni5wbmc?x-oss-process=image/format,png)

从图中可以看出，HashMap是由数组+链表(jdk1.8在链表长度大于8时转换成红黑树)组成。HashMap存储的是键值对，Node<K,V>即是每个节点中存储键值对的映射(Node<K,V>的内部结构后面分析)。

- 当存储元素时，Key值经过hash方法算出key对应table数组的索引位置，如果该位置没有元素，则直接将该key、value生成Node并放入该索引位置；如果该位置有元素，利用头结点方式放入该索引所在的链表中，当链表的长度超过8时，链表转换成红黑树，这样即使元素多也能保证高效性。
- 当添加元素时发现HashMap的元素个数超过阈值后，会将数组大小翻倍并对所有元素重新进行hash计算并放入新的数组中。
- 取元素时，依然是先通过key找到对应table数组中的索引，并最终找到该索引中对应的链表(或红黑树)中对应的值value。

以下源码分析是基于jdk1.8版本的，跟1.7版本相比，1.8中有几处优化，后面会列出。

### 初始化
```
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;//初始化大小，默认是16

static final float DEFAULT_LOAD_FACTOR = 0.75f;//负载因子默认值

static final int MAXIMUM_CAPACITY = 1 << 30;//最大容量

static final int TREEIFY_THRESHOLD = 8;//链表
static final int UNTREEIFY_THRESHOLD = 6;
static final int MIN_TREEIFY_CAPACITY = 64;

transient Node<K,V>[] table;//哈希桶数组
transient Set<Map.Entry<K,V>> entrySet;
transient int size;//key-value键值对的个数
transient int modCount;//HashMap内部结构发生变化次数
int threshold;//扩容阈值 threshold
final float loadFactor;//负载因子

//1
public HashMap() {
    this.loadFactor = DEFAULT_LOAD_FACTOR;
}

//2
public HashMap(int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
}

//3
public HashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " +
                                           initialCapacity);
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: "+loadFactor);

    this.loadFactor = loadFactor;
    //如果传入的容量不是2的倍数 通过tableSizeFor方法返回一个最接近initialCapacity的值。
    this.threshold = tableSizeFor(initialCapacity);
}

//4
public HashMap(Map<? extends K, ? extends V> m) {
    this.loadFactor = DEFAULT_LOAD_FACTOR;
    putMapEntries(m, false);
}

static final int tableSizeFor(int cap) {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}
```
Node是HashMap中的一个内部类，是一个保存key-value的映射。Node的内部结构：
```
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;

    Node(int hash, K key, V value, Node<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public final K getKey()        { return key; }
    public final V getValue()      { return value; }
    public final String toString() { return key + "=" + value; }

    public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    public final V setValue(V newValue) {
        V oldValue = value;
        value = newValue;
        return oldValue;
    }

    public final boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof Map.Entry) {
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
            if (Objects.equals(key, e.getKey()) &&
                Objects.equals(value, e.getValue()))
                return true;
        }
        return false;
    }
}
```
Node中有4个成员变量：

- hash：哈希值，用来确定Node在table数组中的索引位置
- key: Key值
- value: value值
- next: HashMap使用哈希表(一维数组)存储元素，当出现哈希冲突(即不同元素对应上table数组中的同一索引位置)时，HashMap采用链地址法解决冲突，可以简单认为table数组中的每个存储都是一个链表结构，链表的节点是Node，Node.next指向的是链表中的下一个Node节点.

### put & get

#### put元素
```
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);

static final int hash(Object key) {
    int h;
    //hashCode的高16位也参与运算 增加Node元素在哈希数组分布的均匀性
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    //如果table数组未初始化 调用resize去初始化数组
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
     //当前key的hashCode与数组大小取模取得在table数组中的索引位置，如果当前位置没有元素，说明没有Hash冲突，直接将当前key、value生成Node并放入当前位置中
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        //如果该索引位置有元素(Hash冲突)且key、key的hashCode相等，直接用新值覆盖旧值并返回旧值
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        //当前节点属于红黑树的节点，则按红黑树规则添加
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            //当前节点属于链表，将Key、value封装成Node放入链表的尾部(1.7是放入链表的头部)
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1)
                        //链表长度超过阈值，转化成红黑树
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
}
```
画一个大概的流程图：
![put.png](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy81ODcxNjMtNzliMzYyOGY4MTEyNDNlYS5wbmc?x-oss-process=image/format,png)

#### get元素
```
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}

//获取key经过hash算法之后的值
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    //table数组不为空且key在table数组中索引位置处不能为空
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        //如果table数组索引位置(可能是红黑树或链表结构)的key及hash值相等，直接返回索引位置对应的value
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        if ((e = first.next) != null) {
             //存在下一个元素且是红黑树结构，按红黑树方式返回查找值
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            do {
                //按链表方式遍历并返回查找值
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```
get(key)对应的流程图：

![get.png](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy81ODcxNjMtMTQ3NTU5ZjM4M2VjNDQ0MC5wbmc?x-oss-process=image/format,png)

### 扩容
扩容指的是当向HashMap中添加元素时，Node元素总数超过了设定的阈值，那么HashMap就会进行扩容(扩大到2倍)，那么这个设定的阈值跟哪些变量相关呢？上面HashMap源码中有这几个变量：

- loadFactor：负载因子(默认是0.75)。哈希数组大小固定的前提下，负载因子越大那么HashMap容纳的Node元素越多，反之容纳的越少。但不是说loadFactor越大越好，loadFactor越大，意味着哈希冲突的可能性越大，查找需要的时间也就更多；反之loadFactor越小，哈希冲突可能性减小，但是同时哈希数组的空间利用率也就越小。默认值0.75是对空间和时间的一个平衡选择，一般不需要改动。
- Node<K,V>[] table：哈希数组，
- threshold：扩容阈值(即HashMap能容纳的最大数量的Node值)，当HashMap中的元素Node个数超过此阈值时，需要进行扩容(resize)。**threshold = table.length * loadFactor**

### 遍历Map
```
HashMap<String, String> hashMap = new HashMap<>();
hashMap.put("111", "222");
hashMap.put("333", "444");
hashMap.put("555", "666");

//1、通过entrySet遍历map
for (Map.Entry<String, String> entry : hashMap.entrySet()) {
    System.out.println("entrySet方式，key:" + entry.getKey() + ",value:" + entry.getValue());
}
//2、通过keySet遍历map
for (String key : hashMap.keySet()) {
    System.out.println("keySet 方 式，key:" + key + ",value:" + hashMap.get(key));
}
```
执行结果:
```
entrySet方式，key:111,value:222
entrySet方式，key:333,value:444
entrySet方式，key:555,value:666

keySet 方 式，key:111,value:222
keySet 方 式，key:333,value:444
keySet 方 式，key:555,value:666
```

### jdk1.8中的优化
相比于jdk1.7的HashMap，jdk1.8中优化了下面几个方面：

- hash算法：hash算法的好坏决定了Node元素在哈希表中的分布均匀情况，分布的越均匀，那么生成链表的可能性越小，查找的效率越高(数组查找效率>链表)，在1.7中通过取模运算(hash = h & (table.length -1))来使Node元素均匀分布；在1.8中优化了hash算法，hashCode是通过hashCode的高16位异或低16位实现的((h = k.hashCode()) ^ (h >>> 16)),保证了高低位都参与到hashCode的计算中，增加了离散性，使得哈希数组中的Node元素分布的更均匀。

- 红黑树的引入：极端情况下，所有的键值对应的哈希值都是一样的，那么经过链地址法后最后在哈希数组的某个位置上形成了一个链表，此时不管是空间性能还是时间性能都是糟糕的，HashMap已经退化成一个链表。基于此在1.8中对链表做了优化，当链表中的Node元素个数大于8时，将链表优化成红黑树，利用红黑树可以快速增删改查的特点提高HashMap的性能。

## ConcurrentHashMap
Hashmap中没有任何同步操作，HashMap在单线程下使用没有问题，但是当在多线程中使用时，可能会导致数据不一致问题，因此在多线程下改用JUC中的ConcurrentHashMap。

注：除了ConcurrentHashMap，还可以使用Hashtable或Collections.synchronizedMap(hashmap),但是他们都是直接在最上层加锁，不管是put还是get操作都需要进行同步加锁，效率并不高。

### jdk1.7版本
ConcurrentHashMap在1.7版本中使用了分段锁形式来存取元素，如下图所示：

![1.7.png](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy81ODcxNjMtZWRiOTU3YTJhM2ZjYzI4YS5wbmc?x-oss-process=image/format,png)

ConcurrentHashMap在1.7版本的大致流程：

- 当put元素时，首先通过key的hashcode获取所在的Segment, 其中Segment的父类是ReentrantLock，当对其中一个Segment进行put或get同步操作时，其他的Segment是不受影响的。在定位到的Segment中根据key的hashCode找到HashEntry，然后进行遍历，如果key值相等，用新值覆盖旧值；如果不相等新建一个HashEntry放入链表中。
- get元素时，将key经过Hash后定位到对应的Segment，再经过遍历后取得key对应的value。

ConcurrentHashMap相对于Hashtable或Collections.synchronizedMap(hashmap)来说是高效的，尤其是get元素时，不需要加锁，但是如果链表中的元素比较多时，遍历效率还是比较低(跟HashMap类似)，接着看在1.8版本中做了哪些结构变化。

### jdk1.8版本

#### put元素
```
public V put(K key, V value) {
    return putVal(key, value, false);
}

/** Implementation for put and putIfAbsent */
final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    //根据key获取hashCode
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh;
        //table数组为空，先初始化数组
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            //key对应的索引位置没有Node元素,直接通过CAS方式尝试写入，失败则进行自旋至执行成功
            if (casTabAt(tab, i, null,
                         new Node<K,V>(hash, key, value, null)))
                break;                   // no lock when adding to empty bin
        }
        else if ((fh = f.hash) == MOVED)
            //扩容
            tab = helpTransfer(tab, f);
        else {
            V oldVal = null;
            //加锁 遍历链表 有相应key的话value直接覆盖旧value，没有的话添加到链表的尾部
            synchronized (f) {
                if (tabAt(tab, i) == f) {
                    if (fh >= 0) {
                        binCount = 1;
                        for (Node<K,V> e = f;; ++binCount) {
                            K ek;
                            if (e.hash == hash &&
                                ((ek = e.key) == key ||
                                 (ek != null && key.equals(ek)))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent)
                                    e.val = value;
                                break;
                            }
                            Node<K,V> pred = e;
                            if ((e = e.next) == null) {
                                pred.next = new Node<K,V>(hash, key,
                                                          value, null);
                                break;
                            }
                        }
                    }
                    //如果是红黑树 按红黑树方式添加
                    else if (f instanceof TreeBin) {
                        Node<K,V> p;
                        binCount = 2;
                        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                       value)) != null) {
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                }
            }
            if (binCount != 0) {
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    addCount(1L, binCount);
    return null;
}

static final <K,V> boolean casTabAt(Node<K,V>[] tab, int i,
                                    Node<K,V> c, Node<K,V> v) {
    return U.compareAndSwapObject(tab, ((long)i << ASHIFT) + ABASE, c, v);
}

static final int spread(int h) {
    return (h ^ (h >>> 16)) & HASH_BITS;
}
```
#### get元素
```
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    //根据key计算出对应的hashCode
    int h = spread(key.hashCode());
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {
        if ((eh = e.hash) == h) {
            //如果key在table数组中，直接取得对应的value并返回
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val;
        }
        else if (eh < 0)
            return (p = e.find(h, key)) != null ? p.val : null;
        //遍历链表 获取key对应的value
        while ((e = e.next) != null) {
            if (e.hash == h &&
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;
        }
    }
    return null;
}
```

### 1.8中的变化
- 1.7中同步策略采用的是Segment分段锁(内部实现是ReentrantLock)，1.8中采用的是CAS+synchronized，效率更高；
- 1.8中引入了红黑树(跟HashMap一样)，当链表过长时直接将链表转换成红黑树，查找效率从O(n)提升到O(logn)。

## LinkedHashMap
LinkedHashMap继承自HashMap,其内部结构是**散列表+双向链表**,其中对散列表部分的put、get操作跟HashMap一样，没有变化。区别于HashMap的是，LinkedHashMap内部还维护了一个双向链表(内部保存了所有元素)，我们都知道HashMap基于Key-Value保存数据，但是HashMap不能按put顺序去遍历元素，如果想顺序遍历元素，可以使用HashMap的子类LinkedHashMap,LinkedHashMap维护了两种迭代顺序：

- 按插入顺序迭代: 默认设置，链表是按插入顺序添加的，那么遍历时也是按插入顺序访问的。
- 按访问顺序迭代： 调用get方法后，会将这次访问的元素移动到链表的尾部，只有设置accessOrder=true时才会生效。如果设置了最大容量(capacity)并重写removeEldestEntry方法(返回true),当新添加元素时，会将链表中最老的元素移除。

### 内部结构
![LinkedHashMap.png](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy81ODcxNjMtOTk5OTI0MWQ1NmFhZWU3MC5wbmc?x-oss-process=image/format,png)

- 基于插入顺序访问：

```
Map<String, String> linkedHashMap = new LinkedHashMap<>();

linkedHashMap.put("A", "1");
linkedHashMap.put("B", "2");
linkedHashMap.put("C", "3");
linkedHashMap.put("D", "4");
linkedHashMap.put("E", "5");

for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {
    System.out.println("key: " + entry.getKey() + ",value: " + entry.getValue());
}
```
执行结果:
```
key: A,value: 1
key: B,value: 2
key: C,value: 3
key: D,value: 4
key: E,value: 5
```

- 基于访问顺序遍历：
```
int initialCapacity = 10;//初始化容量
float loadFactor = 0.75f;//负载因子，一般设置为0.75
boolean accessOrder = true;//false 基于插入顺序  true 基于访问顺序

Map<String, String> linkedHashMap = new LinkedHashMap<String, String>(initialCapacity, loadFactor, accessOrder);

linkedHashMap.put("A", "1");
linkedHashMap.put("B", "2");
linkedHashMap.put("C", "3");
linkedHashMap.put("D", "4");
linkedHashMap.put("E", "5");

for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {
    System.out.println("访问前：key: " + entry.getKey() + ",value: " + entry.getValue());
}

linkedHashMap.get("C");//调用了get方法，执行完后此元素将被加入到链表尾部

System.out.println("-------------------");
for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {
    System.out.println("访问后：key: " + entry.getKey() + ",value: " + entry.getValue());
}

linkedHashMap.put("F", "6");

System.out.println("-------------------");
for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {
    System.out.println("插入后：key: " + entry.getKey() + ",value: " + entry.getValue());
}
```
执行结果：
```
访问前：key: A,value: 1
访问前：key: B,value: 2
访问前：key: C,value: 3
访问前：key: D,value: 4
访问前：key: E,value: 5
-------------------
访问后：key: A,value: 1
访问后：key: B,value: 2
访问后：key: D,value: 4
访问后：key: E,value: 5
访问后：key: C,value: 3
-------------------
插入后：key: A,value: 1
插入后：key: B,value: 2
插入后：key: D,value: 4
插入后：key: E,value: 5
插入后：key: C,value: 3
插入后：key: F,value: 6
```
LinkedHashMap初始化时传入了loadFactor并且设置为true，当添加完元素后，通过`linkedHashMap.get("C")`访问了其中的某个元素，之后再遍历LinkedHashMap，发现被访问的元素已经被放到链表的尾部了。

- 移除最近最少使用的元素

```
int initialCapacity = 10;//初始化容量
float loadFactor = 0.75f;//负载因子，一般设置为0.75
boolean accessOrder = true;//false 基于插入顺序  true 基于访问顺序

Map<String, String> linkedHashMap = new LinkedHashMap<String, String>(initialCapacity, loadFactor, accessOrder){
    @Override
    protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
        return size() > 3;
    }
};

linkedHashMap.put("A", "1");
linkedHashMap.put("B", "2");
linkedHashMap.put("C", "3");
linkedHashMap.put("D", "4");
linkedHashMap.put("E", "5");

for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {
    System.out.println("key: " + entry.getKey() + ",value: " + entry.getValue());
}
```
执行结果:
```
key: C,value: 3
key: D,value: 4
key: E,value: 5
```
我们添加了5个元素，但是最后遍历时只显示了3个元素，这是因为重写了removeEldestEntry方法，当元素的size>3时，此方法会返回true, 当每次添加元素时，会判断removeEldestEntry方法返回值是否为true， 是true的话会去除链表中最近最少使用的元素。

### put元素
LinkedHashMap中没有重写put方法，直接使用父类HashMap#put方法，具体实现可以参看上面HashMap的介绍，LinkedHashMap#put绝大部分跟HashMap#put实现一致，在HashMap#put方法中，调用了newNode方法，目的是生成一个新节点Node，而在LinkedhashMap中重写了这个方法：

```
//当散列表中没有key对应的Node节点时，新生成一个key对应的Node节点，并向链表的尾部添加元素
Node<K,V> newNode(int hash, K key, V value, Node<K,V> e) {
    LinkedHashMap.Entry<K,V> p =
        new LinkedHashMap.Entry<K,V>(hash, key, value, e);
    linkNodeLast(p);
    return p;
}

//向链表的尾部添加元素
private void linkNodeLast(LinkedHashMap.Entry<K,V> p) {
    //定义了head tail双向链表
    LinkedHashMap.Entry<K,V> last = tail;
    tail = p;
    if (last == null)
        head = p;
    else {
        p.before = last;
        last.after = p;
    }
}

//如果key已经在散列表中，散列表中的旧值会被覆盖，同时链表中对应的节点也会移动到链表的尾部
void afterNodeAccess(Node<K,V> e) { // move node to last
    LinkedHashMap.Entry<K,V> last;
    if (accessOrder && (last = tail) != e) {
        LinkedHashMap.Entry<K,V> p =
            (LinkedHashMap.Entry<K,V>)e, b = p.before, a = p.after;
        p.after = null;
        if (b == null)
            head = a;
        else
            b.after = a;
        if (a != null)
            a.before = b;
        else
            last = b;
        if (last == null)
            head = p;
        else {
            p.before = last;
            last.after = p;
        }
        tail = p;
        ++modCount;
    }
}
```
可见在LinkedhashMap#put方法中调用newNode()时，除了新生成一个节点之外，还将此节点元素加入到一个双向链表中，从而可以按一定顺序去遍历元素。

### get元素
```
public V get(Object key) {
    Node<K,V> e;
    //调用父类HashMap中的getNode获取key对应的value元素
    if ((e = getNode(hash(key), key)) == null)
        return null;
    if (accessOrder)
        afterNodeAccess(e);
    return e.value;
}
```
get方法调用父类HashMap中的getNode获取key对应的value元素，同时如果accessOrde为true，同样会调afterNodeAccess方法将元素添加到链表的尾部，实现按访问顺序排序。

### 移除最近最少使用元素
在HashMap#put中，调用了afterNodeInsertion()方法，意思是在插入新元素后做一些处理，但是在HashMap中该方法只是一个空实现，该方法在LinkedHashMaP中被重写：

```
void afterNodeInsertion(boolean evict) { // possibly remove eldest
    LinkedHashMap.Entry<K,V> first;
    if (evict && (first = head) != null && removeEldestEntry(first)) {
        K key = first.key;
        //移除元素
        removeNode(hash(key), key, null, false, true);
    }
}

protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
    return false;
}
```
LinkedHashMap#removeEldestEntry默认返回了false，所以默认往LinkedHashMap中put元素时，并不会移除链表中头部的元素(最近最少使用)，当我们重写removeEldestEntry方法并符合条件时返回true，那么便会触发移除操作，移除掉最近最少使用的元素，这也是LRUCache的原理。

## TreeMap
- TreeMap实现了Map接口，可以存储**有序key-value集合**，内部结构是红黑树
- TreeMap能比较元素大小，对传入的k也进行大小排序，可以使用自然顺序，也可以自定义比较器(实现Comparable接口并重写compareTo方法)进行排序

### 使用元素自然排序
- key是String类型：
```
public static void main(String[] args) {

TreeMap<String, Integer> treeMap = new TreeMap<>();
treeMap.put("D", 4);
treeMap.put("A", 1);
treeMap.put("C", 3);
treeMap.put("B", 2);

//遍历TreeMap
for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
    System.out.println("key: " + entry.getKey() + ",value: " + entry.getValue());
 }
}
```
执行结果:
```
key: A,value: 1
key: B,value: 2
key: C,value: 3
key: D,value: 4
```
- key是Integer类型
```
public static void main(String[] args) {

  TreeMap<Integer, Integer> treeMap = new TreeMap<>();
  treeMap.put(4, 4);
  treeMap.put(1, 1);
  treeMap.put(3, 3);
  treeMap.put(2, 2);

  for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
      System.out.println("key: " + entry.getKey() + ",value: " + entry.getValue());
  }
}
```
执行结果:
```
key: 1,value: 1
key: 2,value: 2
key: 3,value: 3
key: 4,value: 4
```
String、Integer类内部都实现了Comparable接口并在重写的compareTo方法中比较大小。

### 使用自定义排序

```
public static void main(String[] args) {

  Person person1 = new Person("张三", 20);
  Person person2 = new Person("李四", 10);
  Person person3 = new Person("王五", 30);
  Person person4 = new Person("赵六", 40);

  TreeMap<Person, Integer> treeMap = new TreeMap<>();
    treeMap.put(person1, person1.getAge());
    treeMap.put(person2, person2.getAge());
    treeMap.put(person3, person3.getAge());
    treeMap.put(person4, person4.getAge());

    //遍历treeMap，其中key是自定义Person类型，Person内部是按age从小到大排序
    for (Map.Entry<Person, Integer> entry : treeMap.entrySet()) {
        System.out.println("key: " + entry.getKey() + ",value: " + entry.getValue());
    }
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
            //年龄大的元素放在后面
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
key: [person(name:李四,age:10)],value: 10
key: [person(name:张三,age:20)],value: 20
key: [person(name:王五,age:30)],value: 30
key: [person(name:赵六,age:40)],value: 40
```
最终遍历结果是按Person#age从小到大排序的，这里需要注意一点，放入TreeMap中的Key对象必须实现了Comparable接口，否则会抛出`java.lang.ClassCastException: Collection._Map$xxx cannot be cast to java.lang.Comparable`异常信息。

关于TreeMaP的源码，可以参考下面两篇文章：
- https://www.jianshu.com/p/2dcff3634326
- https://www.cnblogs.com/skywang12345/p/3310928.html

## 参考
【1】Java8系列之重新认识HashMap：https://mp.weixin.qq.com/s/oIE4Nnqs5_lOE1D-r9xXWg
【2】https://crossoverjie.top/2018/07/23/java-senior/ConcurrentHashMap/
【3】LinkedHashMap: https://www.cnblogs.com/yulinfeng/p/8590010.html