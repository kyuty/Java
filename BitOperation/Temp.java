/**
 * Created by wangdong on 3/30/17.
 */

public class Temp {
    public static void main(String[] args) {
        goIn();
    }

    private static int states = 0;
    private static int num1 = 0x01;
    private static int num2 = 0x01 << 1;
    private static int num3 = 0x01 << 2;
    private static int num4 = 0x01 << 3;
    private static int num5 = 0x01 << 4;
    private static int[] allNums = new int[] {
            num1, num2, num3, num4, num5
    };

    static void goIn() {
        states |= num1; // open the 1
        states |= num5; // open the 5
        states |= num2; // open the 2

        for (int i = 0; i < allNums.length; i++) {
            int temp = allNums[i];
            if ((states & temp) == temp) {
                 System.out.println(i + 1);
            }
        }
    }
}

