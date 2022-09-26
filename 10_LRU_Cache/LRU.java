import java.util.LinkedHashMap;
import java.util.Map;

public class LRU<K, V> {

    private static final float hashLoadFactory = 0.75f;
    private LinkedHashMap<K, V> mMap;
    private int mCacheSize;

    public LRU(int cacheSize) {
        this.mCacheSize = cacheSize;
        int capacity = (int) Math.ceil(cacheSize / hashLoadFactory) + 1;
        mMap = new LinkedHashMap<K, V>(capacity, hashLoadFactory, true) {
            private static final long serialVersionUID = 1;

            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > LRU.this.mCacheSize;
            }
        };
    }

    public synchronized V get(K key) {
        return mMap.get(key);
    }

    public synchronized void put(K key, V value) {
        mMap.put(key, value);
    }

    public synchronized void clear() {
        mMap.clear();
    }

    public synchronized int usedSize() {
        return mMap.size();
    }

    public void print() {
        for (Map.Entry<K, V> entry : mMap.entrySet()) {
            System.out.print(entry.getValue() + " ");
        }
        System.out.println();
    }
}