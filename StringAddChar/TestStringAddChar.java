public class TestStringAddChar {
    public static void main(String[] args) {
        char startc = 2;
        String b = "" + startc + "#*";
        System.out.println("b = " + b);

        char endc = 3;
        String a = "" + "*#" + endc;
        System.out.println("a = " + a);

        temp1();

        //Object object = "asdfdas";
        //int index = Integer.parseInt(String.valueOf(object));
        //System.out.println("index = " + index);

        String a111 = "111";
        String b222 = "111";
        System.out.println("" + a111 == b222);
    }
    static void temp1() {
        String b = "" + "#*";
        System.out.println("b = " + b);

        String a = "" + "*#";
        System.out.println("a = " + a);
    }
}
