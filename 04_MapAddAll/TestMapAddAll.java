import java.util.ArrayList;

class TestMapAddAll {
    public static void main(String[] args) {
        String s1 = "1";
        String s2 = "2";
        String s3 = "3";
        String s4 = "4";
        String s5 = "5";

        ArrayList<String> al1 = new ArrayList<String>();
        al1.add(s1);
        al1.add(s2);
        al1.add(s3);

        ArrayList<String> al2 = new ArrayList<String>();
        al2.add(s2);
        al2.add(s4);
        al2.add(s5);

        ArrayList<String> al3 = new ArrayList<String>();
        al3.addAll(al1);
        al3.addAll(al2);

        System.out.println(al3.size());

    }
}
