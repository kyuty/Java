public class TestRecursive {
    public static void main(String[] args) {

        int n = 3;
        int[] a = new int[n*n];
        set(n, a, 0, 0, 0);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(" " + a[i * n + j]);
            }
            System.out.println();
        }

    }

    public static void set(int n, int[] a, int startI, int startJ, int startValue) {
        int currentValue = startValue;
        int index = startI * n + startJ;
        for (int i = 0; i < n; i++) {
            a[index] = currentValue + 1;
            currentValue++;
            index++;
        }
        index = n - 1;
        for (int i = 0; i < n; i++) {
            a[index] = currentValue + 1;
            currentValue++;
            index = index + n;
        }

        index = n * n - 1;
        for (int i = 0; i < n; i++) {

            a[index] = currentValue + 1;
            currentValue++;
            index--;
        }

        index = n * n - n;
        for (int i = 0; i < n; i++) {

            a[index] = currentValue + 1;
            currentValue++;
            index = index - n + 1;
        }
    }
}
