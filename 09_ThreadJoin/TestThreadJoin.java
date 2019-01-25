/**
 * https://www.journaldev.com/1024/java-thread-join-example
 *
 * public final void join():
 * This java thread join method puts the current thread on wait until the thread on which it’s called is dead.
 * If the thread is interrupted, it throws InterruptedException.
 *
 * public final synchronized void join(long millis):
 * This java thread join method is used to wait for the thread on which it’s called to be dead or wait for specified milliseconds.
 * Since thread execution depends on OS implementation, it doesn’t guarantee that the current thread will wait only for given time.
 *
 * public final synchronized void join(long millis, int nanos):
 * This java thread join method is used to wait for thread to die for given milliseconds plus nanoseconds.
 *
 * 如何确保main()方法所在的线程是Java程序最后结束的线程？
 * 我们可以使用Thread类的join()方法来确保所有程序创建的线程在main()方法退出前结束
 */
public class TestThreadJoin {

    public static void main(String[] args) {
        Thread t1 = new Thread(new MyRunnable(), "t1");
        Thread t2 = new Thread(new MyRunnable(), "t2");
        Thread t3 = new Thread(new MyRunnable(), "t3");

        System.out.println(Thread.currentThread().getId() + "\t t1.start time = " + System.currentTimeMillis());
        t1.start();

        // start second thread after waiting for 2 seconds or if it's dead
        // 等待2s或者t1线程dead，再往下执行，去start t2线程
        try {
            System.out.println(Thread.currentThread().getId() + "\t t1 join time = " + System.currentTimeMillis());
            t1.join(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getId() + "\t t2.start time = " + System.currentTimeMillis());
        t2.start();

        // start third thread only when first thread is dead
        // 一直等t1线程dead，dead后再往下执行，去start t3线程
        try {
            System.out.println(Thread.currentThread().getId() + "\t t1.join time = " + System.currentTimeMillis());
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getId() + "\t t3.start time = " + System.currentTimeMillis());
        t3.start();

        // let all threads finish execution before finishing main thread
        // 一直等，等t1/t2/t3线程全部dead，才往下执行。
        try {
            System.out.println(Thread.currentThread().getId() + "\t t1 t2 t3 join time = " + System.currentTimeMillis());
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getId() + "\t All threads are dead, exiting main thread");
    }

}

class MyRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getId() + "\t Thread started:::" + Thread.currentThread().getName());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getId() + "\t Thread ended:::" + Thread.currentThread().getName());
    }

}

/*
1	 t1.start time = 1548316125160
1	 t1 join time = 1548316125184
13	 Thread started:::t1
1	 t2.start time = 1548316127187
14	 Thread started:::t2
1	 t1.join time = 1548316127188
13	 Thread ended:::t1
1	 t3.start time = 1548316129201
15	 Thread started:::t3
1	 t1 t2 t3 join time = 1548316129202
14	 Thread ended:::t2
15	 Thread ended:::t3
1	 All threads are dead, exiting main thread
 */