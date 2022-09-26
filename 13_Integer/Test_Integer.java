public class Test_Integer {

    public static void main(String[] args) {
        Integer i = 0;
        System.out.println("i = " + i + " " + System.identityHashCode(i));
        i++;
        System.out.println("i = " + i + " " + System.identityHashCode(i));
        add(i);
        System.out.println("i = " + i + " " + System.identityHashCode(i));
    }

    public static void add(Integer i) {
        System.out.println("i = " + i + " " + System.identityHashCode(i));
        i = i + 1;
        System.out.println("i = " + i + " " + System.identityHashCode(i));

    }

}
