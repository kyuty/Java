https://www.jianshu.com/p/c13b5dbbe156



# Android中的LRU

- #### 什么是LRU

###### 简单来讲就是一种算法，在Android中一般用于集合。每次要移除成员时，集合会优先移除最近最久使用的成员。

- #### 为什么使用LRU

###### 为了在有限的内存中，让那些经常使用的对象一直存活在内存中。以达到当使用这些资源是更快的加载。

- #### Android中LRU的使用。

###### Android中使用LRU算法一般在存储图片方面，已达到节约内存同时加快加载图片的速度的目的。

###### Android中有一个LRU算法实现的集合LruCache。

```
public class LruCache<K, V> {
    private final LinkedHashMap<K, V> map;
}
```

###### 可以看出LruCache内部是使用LinkedHashMap存储数据的。其实LruCache主要依靠LinkedHashMap本身的LRU实现来实现的。LruCache只是进行了另一次封装。

```
    public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
//     这里指定了该集合的最大容量，一旦集合容量大于该容量则会调用trimToSize方法来减少容量。
        this.maxSize = maxSize;
//      这里创建了LinkedHashMap并且第三个参数指定为true.该参数为true时LinkedHashMap开启LRU算法。
        this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
    }
```

###### 在使用LruCache时要重写sizeOf方法，来指定成员的实际容量。否则默认返回1

```
protected int sizeOf(K key, V value) {
        return 1;
    }
```

###### LruCache的一个回调函数本身没有实现，可重新。在新增或移除变量时会被调用。第一个参数为true时是移除变量，false时是新增变量。

```
protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {}
```

###### 接下来简单说下LruCache的存放方法。

```
public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        V previous;
        synchronized (this) {
            putCount++;
            size += safeSizeOf(key, value);
            previous = map.put(key, value);
            if (previous != null) {
                size -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
//         新增时会调用的回调函数，本身没有具体实现需要使用时要自己重写
            entryRemoved(false, key, previous, value);
        }
//      这里是重点，当插入结束后会检查内存是否超标
        trimToSize(maxSize);
        return previous;
    }
/**
*简单来说就是会无限循环的来检测内存容量
*如果内存容量大于maxSize,则会移除最近最久使用的成员。
*然后继续循环，直到内存容量小于maxSize或者集合为null循环终止
**/
public void trimToSize(int maxSize) {
        while (true) {
            K key;
            V value;
            synchronized (this) {
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName()
                            + ".sizeOf() is reporting inconsistent results!");
                }

                if (size <= maxSize) {
                    break;
                }

                Map.Entry<K, V> toEvict = map.eldest();
                if (toEvict == null) {
                    break;
                }

                key = toEvict.getKey();
                value = toEvict.getValue();
                map.remove(key);
                size -= safeSizeOf(key, value);
                evictionCount++;
            }
//         移除时会调用的回调函数，本身没有具体实现需要使用时要自己重写
            entryRemoved(true, key, value, null);
        }
    }
public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
//     如果值存在则直接返回值，这里会依赖LinkedHashMap的LRU机制。
        V mapValue;
        synchronized (this) {
            mapValue = map.get(key);
            if (mapValue != null) {
                hitCount++;
                return mapValue;
            }
            missCount++;
        }

//      这里会调用create方法，这个方法本身默认返回null,即如果用户不重写get方法会返回null。
//      根据实际需求来编写。如果创建了新的对象则会存放进集合中。
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
//         新增时会调用的回调函数，本身没有具体实现需要使用时要自己重写
            entryRemoved(false, key, createdValue, mapValue);
            return mapValue;
        } else {
            trimToSize(maxSize);
            return createdValue;
        }
    }
```

###### LruCache中包含许多成员比如

```
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private int missCount;
```

###### 这些成员本身没有什么用处，只是记录了各种操作时的计数器。应该是方便调整设计的考量。

- #### JAVA中的LRU

###### 我们上边说过:LruCache主要依靠LinkedHashMap本身的LRU实现来实现的。LruCache只是进行了另一次封装。现在我们来看看JAVA的LinkedHashMap的LRU实现。

###### 重点就在于get方法的实现。

```
 public V get(Object key) {
        Node<K,V> e;
        if ((e = getNode(hash(key), key)) == null)
            return null;
//      关键部分，如果指定accessOrder为true则会调用afterNodeAccess方法。
        if (accessOrder)
            afterNodeAccess(e);
        return e.value;
    }
// 简单来说就是通过指针操作将e这个对象放到队尾（tail）
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
```

###### 这样每次使用成员时LinkedHashMap都会将成员放入队尾。每次移除成员时则从队头移除，这样就保证每次移除都移除最近最久的对象。

- #### Glide中的LRU实现。

###### 在观看郭霖大神写的解析Glide源码时发现，Glide会重写的LruCache。

###### 与Android不同的在于，<font color=red>当获取对象时Glide是直接从LinkedHashMap中移除，然后传递对象存放进一个临时的HashMap中。等到用完之后再将对象放回到LinkedHashMap中</font>。

###### 看评论说这样是防止内存紧张时当前使用的资源被直接干掉。出于安全方面考虑。

###### 后来自己想如果在一个图片应用很多的情况下，可以根据实际情况模拟一个JAVA内存中新生代、老年代的结构写2个LruCache。老年代用于存放那些全局经常使用的图片。新生代存放那些在某个模块经常使用的图片，当退出这个模块是会被清空。好处是可以保证（老年代）全局经常使用的图片加载更快，避免被挤出内存。不过这种情况应该很少遇到。
