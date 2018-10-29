import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HashMapTest {
    public static void main(String[] args) {
       HashMap<String, String> hashMap = new HashMap<String, String>();
       hashMap.put("name", "1");
       hashMap.put("name", "2");
       System.out.println("hashMap.size() = " + hashMap.size());
       for (int i = 0; i < hashMap.size(); i++) {
            Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                String key = next.getKey();
                System.out.println("wangdong Texture size name = " + key);
            }
        }

       int index = 1;
       index = ~index;
       System.out.println("index = " + index);

       index = ~index;
       System.out.println("index = " + index);

    }
}
