package concurrent.count;

/**
 * count+=1,在执行时不是一个原子性操作(1:读取主存到cpu缓存,2:在缓存中+1,3:将+1的结果写入主存),中间可能会出现同时读,同时写的情况
 * 结论:volatile只能保证共享变量在线程之间的可见性,和执行代码时一定的有序性,不能保证任何原子性
 * volatile应用场景1:配合synchronized的或者lock保证共享变量在修改时的安全性
 * volatile应用场景2:根据volatile关键字和传递性 happens-before原则,可以用volatile来保证其他语句有序性,拒绝编译优化改变执行顺序
 */
public class Count {
    private long count=0;

    //public synchronized void add10k(){
    public void add10k(){
        int i=0;
        while(i++<10000){
            synchronized (this){//加锁,同时只能执行一次这个操作
                count+=1;
            }
        }
    }

    public Count() {
    }

    public long getCount() {
        return count;
    }

    public static void main(String[] args) {
        Count count = new Count();
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
