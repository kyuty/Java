public class TestLRUCache {
    public static void main(String[] args) {
        int cacheSize = 10;
        {
            LRU<String, String> lruMap = new LRU<String, String>(cacheSize);
            String key = "key";
            String value = "value";
            for (int i = 0; i < cacheSize; i++) {
                lruMap.put(key + i, value + i);
            }
            lruMap.print();
            String getKey = key + (cacheSize / 2);
            String getValue = lruMap.get(getKey);
            System.out.println("key = " + getKey + " value = " + getValue);
            System.out.println("lruMap size = " + lruMap.usedSize());
            lruMap.print();

            for (int i = 0; i < cacheSize / 2; i++) {
                lruMap.put(key + (cacheSize + i), value + (cacheSize + i));
            }
            System.out.println("lruMap size = " + lruMap.usedSize());
            lruMap.print();
            getValue = lruMap.get(getKey);
            System.out.println("key = " + getKey + " value = " + getValue);
            lruMap.print();
        }
    }
}
/*
value0 value1 value2 value3 value4 value5 value6 value7 value8 value9
key = key5 value = value5
lruMap size = 10
value0 value1 value2 value3 value4 value6 value7 value8 value9 value5
lruMap size = 10
value6 value7 value8 value9 value5 value10 value11 value12 value13 value14
key = key5 value = value5
value6 value7 value8 value9 value10 value11 value12 value13 value14 value5
 */