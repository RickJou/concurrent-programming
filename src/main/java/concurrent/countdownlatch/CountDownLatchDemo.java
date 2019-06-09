package concurrent.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {


        CountDownLatch cdl = new CountDownLatch(10);

        for (int i=0;i<10;i++){
            Thread t = new Thread(()->{
                cdl.countDown();
                System.out.println(cdl.getCount());
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                }
            });
            t.start();
        }
        cdl.await();

        System.out.println(cdl.getCount());
    }
}
