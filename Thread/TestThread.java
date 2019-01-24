
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangdong on 2/8/18.
 */

public class TestThread {

    public static final List<String> testList = new ArrayList<String>();

    public static void main(String[] args) {
        testList.add("1");
        testList.add("2");

        ThreadA a = new ThreadA();
        a.start();
        ThreadB b = new ThreadB();
        b.start();
    }

}

class ThreadA extends Thread {
    @Override
    public void run() {
        synchronized (TestThread.testList) {
            System.out.println("========================== ThreadA first");
            for (int i = 0; i < TestThread.testList.size(); i++) {
                System.out.println("i = " + TestThread.testList.get(i));
            }
            System.out.println("ThreadA size = " + TestThread.testList.size());
            TestThread.testList.clear();
            TestThread.testList.add("ThreadA 1");
            TestThread.testList.add("ThreadA 2");
            System.out.println("========================== ThreadA end");
        }
    }
}

class ThreadB extends Thread {
    @Override
    public void run() {
        synchronized (TestThread.testList) {
            System.out.println("++++++++++++++++++++++++++ ThreadB first");
            for (int i = 0; i < TestThread.testList.size(); i++) {
                System.out.println("i = " + TestThread.testList.get(i));
            }
            System.out.println("ThreadB size = " + TestThread.testList.size());
            TestThread.testList.clear();
            TestThread.testList.add("ThreadB 1");
            TestThread.testList.add("ThreadB 2");
            System.out.println("++++++++++++++++++++++++++ ThreadB end");
        }
    }
}

