public class ClassD {
    public static void main(String args[]){
        ClassA a = new ClassA();
        ClassB b = new ClassB();
        ClassC c = new ClassC();

        b.ba = a;
        c.ca = a;
        a = null;
        System.out.println("b.ba = " + b.ba);
        System.out.println("c.ca = " + c.ca);
    }
}
