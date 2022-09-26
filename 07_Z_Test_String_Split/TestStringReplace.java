public class TestStringReplace {
    public static void main(String[] args) {
        // A&0;10;20;&0,1;10,2;20,3;30,4;
        // A&&0,1;10,2;20,3;30,4;
        // &0,1;10,2;20,3;30,4;
        String test0 = "aaaaa";
        String test1 = test0 + "bbbbb";

        String replace = test1.replace(test0, "");
        System.out.println("test0 = " + test0);
        System.out.println("test1 = " + test1);
        System.out.println("replace = " + replace);
    }
}
