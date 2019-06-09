package concurrent;


public class volatileTest extends Thread{
    private static volatile   int count;

    private static void addCount() {
        for (int i=0;i<100;i++){
            count++;
            //Thread.sleep(1);
        }
        System.out.println(count);
    }

    @Override
    public void run(){
        try {
            addCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
       volatileTest [] arr = new volatileTest[10];
       for (int i=0;i<10;i++){
           arr[i] = new volatileTest();
       }

       for (int i=0;i<10;i++){
           arr[i].start();
       }
    }
}
