package concurrent.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CountDownLatchDemo2 {
    public static void main(String[] args) throws InterruptedException {

        Executor executor = Executors.newFixedThreadPool(2);
        CountDownLatch cdl = new CountDownLatch(10);
        final long count = cdl.getCount();


        Long startTime = System.currentTimeMillis();
        for (int i=0;i<count;i++){
            executor.execute(()->{
                try {
                    Thread.sleep(999L);
                } catch (InterruptedException e) {
                }
                cdl.countDown();
                System.out.println("执行一次");
            });
        }
        cdl.await(5, TimeUnit.SECONDS);
        Long endTime = System.currentTimeMillis();

        if(cdl.getCount()!=0){
            System.out.println("超时,耗时:"+(endTime-startTime));
        }else{
            System.out.println("执行完成");
        }
    }
}
