import org.w3c.dom.css.Rect;

import java.util.ArrayList;

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


        ArrayList<Rect> list = new ArrayList<Rect>(19);
        System.out.println("size = " + list.size());
        for (int i = 0, size  = list.size(); i < size; i++) {
            Rect rect = list.get(i);
            System.out.println("rect i " + i + " " + rect);
        }
    }

    static void temp1() {
        String b = "" + "#*";
        System.out.println("b = " + b);

        String a = "" + "*#";
        System.out.println("a = " + a);
    }
}
