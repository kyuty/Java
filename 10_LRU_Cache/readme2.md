https://juejin.im/post/5b98b2ad6fb9a05d290edd29

# Android缓存机制-LRU cache原理与用法

在使用Android图片加载框架时，经常会提到三级缓存，其中主要的是内存缓存和文件缓存。 两个缓存都是用到了LruCache算法，在Android分别对应：[LruCache](https://link.juejin.im/?target=https%3A%2F%2Fdeveloper.android.com%2Freference%2Fandroid%2Futil%2FLruCache)和[DiskLruCache](https://link.juejin.im/?target=https%3A%2F%2Fgithub.com%2FJakeWharton%2FDiskLruCache)。

## LRU算法

操作系统中进行内存管理中时采用一些页面置换算法，如LRU、LFU和FIFO等。
其中LRU(Least recently used,最近最少使用)算法，核心思想是当缓存达到上限时，会先淘汰最近最少使用的缓存。这样可以保证缓存处于一种可控的状态,有效的防止OOM的出现。

## LruCache用法

[LruCache](https://link.juejin.im/?target=https%3A%2F%2Fdeveloper.android.com%2Freference%2Fandroid%2Futil%2FLruCache)是从Android3.1开始支持，目前已经在[androidx.collection](https://link.juejin.im/?target=https%3A%2F%2Fdeveloper.android.com%2Freference%2Fandroidx%2Fcollection%2FLruCache)支持。

### 初始化

LruCache初始化:

```
int maxCache = (int) (Runtime.getRuntime().maxMemory() / 1024);
//初始化大小:内存的1/8
int cacheSize = maxCache / 8;
memorySize = new LruCache<String, Bitmap>(512) {
    @Override
    protected int sizeOf(String key, Bitmap value) {
        //重写该方法,计算每张要缓存的图片大小
        return value.getByteCount() / 1024;
    }
};
复制代码
```

### 方法

LruCache结构图

![img](/Users/wangdong/github/Java/10_LRU_Cache/md4.png)



| 方法        | 描述               |
| ----------- | ------------------ |
| get(K)      | 通过K获取缓存      |
| put(K,V)    | 设置K的值为V       |
| remove(K)   | 删除K缓存          |
| evictAll()  | 清除缓存           |
| resize(int) | 设置最大缓存大小   |
| snapshot()  | 获取缓存内容的镜像 |

## LruCache实现原理

LruCache初始化只要声明 `new LruCache(maxSize)`，这个过程主要做了那些功能，看源码：

```
public LruCache(int maxSize) {
    if (maxSize <= 0) {
        throw new IllegalArgumentException("maxSize <= 0");
    }
    //声明最大缓存大小
    this.maxSize = maxSize;
    //重点,LinkedHashMap,LruCache的实现依赖
    this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
}
复制代码
```

LruCache核心思想就是淘汰最近最少使用的缓存，需要维护一个缓存对象列表，排列方式按照访问顺序实现。
把最近访问过的对象放在队头，未访问的对象放在队尾，当前列表达到上限的时候，优先会淘汰队尾的对象。
如图(盗用下图片)：

![img](/Users/wangdong/github/Java/10_LRU_Cache/md5.png)

在Java中，

```
LinkedHashMap
```

比较适合实现这种算法。



### LinkedHashMap

`LinkedHashMap`是一个关联数组、哈希表，它是线程不安全的,继承自`HashMap`，实现`Map<K,V>`接口。
内部维护了一个双向链表，在插入数据、访问，修改数据时，会增加节点、或调整链表的节点顺序，双向链表结构可以实现插入顺序或者访问顺序。 在LruCache初始化定义了`this.map = new LinkedHashMap<K, V>(0, 0.75f, true)` 。
`LinkedHashMap`的构造函数：

```
public LinkedHashMap(int initialCapacity,
                        float loadFactor,
                        boolean accessOrder) {
    super(initialCapacity, loadFactor);
    this.accessOrder = accessOrder;
}
复制代码
```

布尔变量`accessOrder`定义了输出的顺序，`true`为按照访问顺序，`false`为按照插入顺序。`accessOrder`设置为`true`正好满足LRU算法的核心思想。

## LruCache源码分析

既然LruCache底层使用LinkedHashMap，下面我们来看看它怎么实现缓存的操作的。 源码分析是在Android API 28版本，

### put

```
public final V put(K key, V value) {
    if (key == null || value == null) {
        throw new NullPointerException("key == null || value == null");
    }

    V previous;
    synchronized (this) {
        putCount++;
        //增加缓存大小
        size += safeSizeOf(key, value);
        //使用LinkedHashMap的put方法
        previous = map.put(key, value);
        if (previous != null) {
            //如果previous存在，减少对应缓存大小
            size -= safeSizeOf(key, previous);
        }
    }

    if (previous != null) {
        entryRemoved(false, key, previous, value);
    }

    //检查缓存大小，删除最近最少使用的缓存
    trimToSize(maxSize);
    return previous;
}
复制代码
```

put方法主要是添加缓存对象后，调用`trimToSize`方法，保证缓存大小，删除最近最少使用的缓存。具体的添加缓存，通过`LinkedHashMap`put方法实现。 `LinkedHashMap`继承自`HashMap`，没有重写put方法，调用的是`HashMap`的put方法，在`HashMap`的putVal方法中有调用创建新节点的`newNode`方法，`LinkedHashMap`重写了该方法。

```
Node<K,V> newNode(int hash, K key, V value, Node<K,V> e) {
    LinkedHashMapEntry<K,V> p =
        new LinkedHashMapEntry<K,V>(hash, key, value, e);
    linkNodeLast(p);
    return p;
}
// link at the end of list
private void linkNodeLast(LinkedHashMapEntry<K,V> p) {
    LinkedHashMapEntry<K,V> last = tail;
    tail = p;
    if (last == null)
        head = p;
    else {
        p.before = last;
        last.after = p;
    }
}
复制代码
```

其中LinkedHashMapEntry定义：

```
static class LinkedHashMapEntry<K,V> extends HashMap.Node<K,V> {
    LinkedHashMapEntry<K,V> before, after;
    LinkedHashMapEntry(int hash, K key, V value, Node<K,V> next) {
        super(hash, key, value, next);
    }
}

/**
* The head (eldest) of the doubly linked list.
*/
transient LinkedHashMap.Entry<K,V> head;

/**
* The tail (youngest) of the doubly linked list.
*/
transient LinkedHashMap.Entry<K,V> tail;
复制代码
```

`LinkedHashMapEntry`用来存储数据，定义了`before`,`after`显示上一个元素和下一个元素。
下面的操作：

```
LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>(10, 0.75f, true);
map.put(1,1);
复制代码
```

![img](/Users/wangdong/github/Java/10_LRU_Cache/md6.png)

再执行

```
map.put(2,2)
```

![img](/Users/wangdong/github/Java/10_LRU_Cache/md7.png)

继续执行

```
map.put(3,3)
```

![img](/Users/wangdong/github/Java/10_LRU_Cache/md8.png)

如果put了相同key的话，会做什么操作。这个一起放到get方法中讲解。



### get

```
public final V get(K key) {
    if (key == null) {
        throw new NullPointerException("key == null");
    }

    V mapValue;
    synchronized (this) {
        mapValue = map.get(key);
        if (mapValue != null) {
            hitCount++;
            return mapValue;
        }
        missCount++;
    }

    //基本不会执行到这里,除了重写create方法
    /*
        * Attempt to create a value. This may take a long time, and the map
        * may be different when create() returns. If a conflicting value was
        * added to the map while create() was working, we leave that value in
        * the map and release the created value.
        */

    V createdValue = create(key);
    if (createdValue == null) {
        return null;
    }

    synchronized (this) {
        createCount++;
        mapValue = map.put(key, createdValue);

        if (mapValue != null) {
            // There was a conflict so undo that last put
            map.put(key, mapValue);
        } else {
            size += safeSizeOf(key, createdValue);
        }
    }

    if (mapValue != null) {
        entryRemoved(false, key, createdValue, mapValue);
        return mapValue;
    } else {
        trimToSize(maxSize);
        return createdValue;
    }
}
复制代码
```

这里主要就是调用`LinkedHashMap`的get方法。 `LinkedHashMap`的get方法：

```
public V get(Object key) {
    Node<K,V> e;
    if ((e = getNode(hash(key), key)) == null)
        return null;
    if (accessOrder)
        afterNodeAccess(e);
    return e.value;
}
复制代码
```

`afterNodeAccess`方法：

```
void afterNodeAccess(Node<K,V> e) { // move node to last
    LinkedHashMapEntry<K,V> last;
    if (accessOrder && (last = tail) != e) {
        LinkedHashMapEntry<K,V> p =
            (LinkedHashMapEntry<K,V>)e, b = p.before, a = p.after;
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
复制代码
```

`afterNodeAccess`方法会将当前被访问的节点e，移动到内部双向链表的尾部。 在put方法，map已经有三个数据。 现在操作`map.get(1)`，具体的逻辑在`afterNodeAccess`方法，看下每步操作后值的变化。

```
if (accessOrder && (last = tail) != e) {
        LinkedHashMapEntry<K,V> p =
            (LinkedHashMapEntry<K,V>)e, b = p.before, a = p.after;
        p.after = null;
        ...
}        
复制代码
```

| 变量 | 值      | before | after |
| ---- | ------- | ------ | ----- |
| head | 1       | null   | 2     |
| tail | 3       | 2      | null  |
| last | (tail)3 | 2      | null  |
| p    | 1       | null   | null  |
| b    | null    |        |       |
| a    | 2       | 1      | 3     |

```
if (b == null)
    head = a;
else
    b.after = a;
if (a != null)
    a.before = b;
else
    last = b;
复制代码
```

| 变量 | 值   | before | after |
| ---- | ---- | ------ | ----- |
| head | (a)2 | null   | 3     |
| tail | 3    | 2      | null  |
| last | 3    | 2      | null  |
| p    | 1    | null   | null  |
| b    | null |        |       |
| a    | 2    | null   | 3     |

```
if (last == null)
    head = p;
else {
    p.before = last;
    last.after = p;
}
tail = p;
复制代码
```

| 变量 | 值   | before  | after |
| ---- | ---- | ------- | ----- |
| head | 2    | null    | 3     |
| tail | 1    | 3       | null  |
| last | 3    | 2       | (p)1  |
| p    | 1    | (last)3 | null  |
| b    | null |         |       |
| a    | 2    | null    | 3     |

最后的操作结果:

![img](/Users/wangdong/github/Java/10_LRU_Cache/md9.png)



### trimToSize

`get`和`put`方法都会调用到

```
public void trimToSize(int maxSize) {
    while (true) {
        K key;
        V value;
        synchronized (this) {
            if (size < 0 || (map.isEmpty() && size != 0)) {
                throw new IllegalStateException(getClass().getName()
                        + ".sizeOf() is reporting inconsistent results!");
            }

            if (size <= maxSize || map.isEmpty()) {
                break;
            }

            Map.Entry<K, V> toEvict = map.entrySet().iterator().next();
            key = toEvict.getKey();
            value = toEvict.getValue();
            map.remove(key);
            size -= safeSizeOf(key, value);
            evictionCount++;
        }

        entryRemoved(true, key, value, null);
    }
}
复制代码
```

当`map`的`size`大于`maxSize`，会一直循环删除最近最少使用的缓存对象，直到缓存大小小于 `maxSize`。

以上就是LruCache基本原理，理解了`LinkedHashMap`,可以更加轻松地理解LruCache原理。
`DiskLruCache`内部实现也有一部分基于`LinkedHashMap`。