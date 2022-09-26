import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

class TestMapAddAll {
    public static void main(String[] args) {
        String s1 = "1sdfads";
        String s2 = "2";
        String s3 = "3";
        String s4 = "4";
        String s5 = "5";

        {
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

            System.out.println("size = " + al3.size());
            System.out.println(Arrays.toString(al3.toArray()));
        }

        {
            ArrayList<String> al1 = new ArrayList<String>();
            al1.add(s1);
            al1.add(s2);

            ArrayList<String> al2 = new ArrayList<String>();
            al2.add(s1);
            al2.add(s1);

            System.out.println("al1 = " + Arrays.toString(al1.toArray()) + " hashcode " + al1.hashCode());
            System.out.println("al2 = " + Arrays.toString(al2.toArray()) + " hashcode " + al2.hashCode());
            System.out.println("containsAll " + al1.containsAll(al2));
            System.out.println("equals " + al1.equals(al2));


            al2.clear();
            al2.add(s2);
            al2.add(s1);
            System.out.println("al1 = " + Arrays.toString(al1.toArray()) + " hashcode " + al1.hashCode());
            System.out.println("al2 = " + Arrays.toString(al2.toArray()) + " hashcode " + al2.hashCode());
            System.out.println("containsAll " + al1.containsAll(al2));
            System.out.println("equals " + al1.equals(al2));
        }

        {

            ArrayList<String> al1 = new ArrayList<String>();
            al1.add(s1);
            al1.add(s2);
            System.out.println("al1 = " + al1 + " " + al1.hashCode());
            ArrayList<String> clone1 = (ArrayList<String>) al1.clone();
            System.out.println("clone1 = " + clone1 + " " + clone1.hashCode());

            System.out.println("" + (al1 == clone1));


            ArrayList<String> al2 = new ArrayList<String>();
            al2.add(s1);
            al2.add(s2);
            System.out.println("" + (al1 == al2));
            System.out.println("" + (al1.equals(al2)));




        }

        {
            ArrayList<String> xxx = xxx();
            for (String s : xxx) {
                System.out.println("s " + s);
                ArrayList<String> xxx1 = xxx();
            }
        }

    }

    private final static ArrayList<String> mXXX = new ArrayList<>();

    public static ArrayList<String> xxx() {
        mXXX.clear();
        mXXX.add("1");
        mXXX.add("1");
        mXXX.add("1");
        mXXX.add("1");
        return mXXX;
    }

}
