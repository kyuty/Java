class SyncThread {
    private int count;

    public SyncThread() {
        count = 0;
    }

    public synchronized void methodA() {
        for (int i = 0; i < 5; i ++) {
            try {
                System.out.println(Thread.currentThread().getName() + "methodA :" + (count++));
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void methodB() {
        for (int i = 0; i < 5; i ++) {
            try {
                System.out.println(Thread.currentThread().getName() + "methodB :" + (count++));
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * 锁两个方法，调用不同的方法，看不同方法之间会不会有锁的效果
 */
public class Demo {

    public static void main(String args[]){
        SyncThread syncThread1 = new SyncThread();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                syncThread1.methodA();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                syncThread1.methodB();
            }
        });
        thread1.start();
        thread2.start();
    }

}