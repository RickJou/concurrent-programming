package concurrent.count;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCount {
    private AtomicInteger count = new AtomicInteger();

    //public synchronized void add10k(){
    public void add10k() {
        int i = 0;
        while (i++ < 10000) {
            count.incrementAndGet();
            count.set(count.get()-1);
            count.incrementAndGet();
        }
    }


    public AtomicCount() {
    }

    public long getCount() {
        return count.get();
    }

    public static void main(String[] args) {
        AtomicCount count = new AtomicCount();
        Thread t1 = new Thread(()->{
            count.add10k();
        });
        Thread t2 = new Thread(() -> {
            count.add10k();
        });

        t1.start();
        t2.start();

        try {
            t1.join(10000);
            t2.join(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(count.getCount());
    }
}
