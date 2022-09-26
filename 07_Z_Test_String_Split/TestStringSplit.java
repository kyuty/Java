public class TestStringSplit {
    public static void main(String[] args) {
        // A&0;10;20;&0,1;10,2;20,3;30,4;
        // A&&0,1;10,2;20,3;30,4;
        // &0,1;10,2;20,3;30,4;
        String test0 = "A&0;10;20;&0,1;10,2;20,3;30,4;";
        String test1 = "A&&0,1;10,2;20,3;30,4;";
        String test2 = "&0,1;10,2;20,3;30,4;";

        String[] split = test0.split("&");
        System.out.println("split0 size = " + split.length);
        for (String sp : split) {
            System.out.println("sp = " + sp);
        }


        split = test1.split("&");
        System.out.println("split1 size = " + split.length);
        for (String sp : split) {
            System.out.println("sp = " + sp);
            String[] split1 = sp.split(";");
            System.out.println("----------- split1 size = " + split1.length);
            for (String aaa : split1) {
                System.out.println("aaa = " + aaa);
            }
        }

        split = test2.split("&");
        System.out.println("split2 size = " + split.length);
        for (String sp : split) {
            System.out.println("sp = " + sp);
        }
    }
}
