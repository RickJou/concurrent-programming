package concurrent.account;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Account {
    private String ident;
    private Integer balance;//账户余额
    private String pwd;//账户密码

    private final Object balLock = new Object();
    private final Object pwdLock = new Object();

    public Account(String ident, Integer balance) {
        this.ident = ident;
        this.balance = balance;
    }

    private void transfer(Account targetAccount, Integer amt) {
        if (null == this.ident || null == targetAccount.ident) {
            throw new RuntimeException("转账必须设置账户");
        }
        if (this.ident.hashCode() == targetAccount.ident.hashCode()) {
            throw new RuntimeException("两个账户HashCode一致");
        }
        Object firstLock;
        Object secondLock;
        if (this.ident.hashCode() > targetAccount.ident.hashCode()) {
            firstLock = this.balLock;
            secondLock = targetAccount.balLock;
        } else {
            firstLock = targetAccount.balLock;
            secondLock = this.balLock;
        }

        synchronized (firstLock) {
            synchronized (secondLock) {
                if (this.balance > amt) {

                    try {//模拟业务耗时
                        long time = (long) (Math.random() * 1000);
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    this.balance -= amt;
                    targetAccount.balance += amt;
                    System.out.println(System.currentTimeMillis()+"-----"+Thread.currentThread().getName() + "线程->" + this.ident + "转账给" + targetAccount.ident + ":" + amt);
                }
            }
        }

    }

    //取款
    private void withDraw(Integer amt) {
        synchronized (balLock) {
            if (this.balance > amt) {
                this.balance -= amt;
            }
        }
    }

    //查看余额
    private Integer getBalance() {
        synchronized (balLock) {
            return this.balance;
        }
    }

    //修改密码
    private void updatePwd(String pwd) {
        synchronized (pwdLock) {
            this.pwd = pwd;
        }
    }

    //查看密码
    private String getPwd() {
        synchronized (pwdLock) {
            return this.pwd;
        }
    }

    public static void main(String[] args) {
        Account act1 = new Account("act1", 10000);
        Account act2 = new Account("act2", 10000);
        Account act3 = new Account("act3", 10000);
        Account act4 = new Account("act4", 10000);

        Long startTime = System.currentTimeMillis();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                act1.transfer(act2, 10);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                act2.transfer(act3, 10);
            }
        });
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                act3.transfer(act4, 10);
            }
        });
        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                act4.transfer(act1, 10);
            }
        });
        t1.setName("t1");
        t2.setName("t2");
        t3.setName("t3");
        t4.setName("t4");


        try {
            /*
            性能差: 假设t1开始执行,act2的hashCode比act1大,所以先持有act2的锁和act1的锁
                        t2开始执行,act3的hashCode比act2大,所以先持有act3的锁和尝试获取act1的锁(获取失败,t1持有),阻塞
                        t3开始执行,act4的hashCode比act3大,所以先持有act4的锁和尝试获取act3的锁(获取失败,t2持有),阻塞
                        t4开始执行,act4的hashCode比act1大,所以尝试获取act4的锁和尝试获取act1的锁(获取失败,t3/t1持有),阻塞
                    这种情况下只有t1能够继续获得锁,等到t1完全执行完了之后,t2才有机会获取执行,然后是t3,最后是t4
            t1.start();
            t2.start();
            t3.start();
            t4.start();

            t1.join();
            t2.join();
            t3.join();
            t4.join();*/

            //性能反而最高:属于人工逻辑干涉
            t1.start();
            t3.start();
            t1.join();
            t3.join();

            t2.start();
            t4.start();
            t2.join();
            t4.join();

            System.out.println("t1:" + act1.getBalance());//10000
            System.out.println("t2:" + act2.getBalance());//10000
            System.out.println("t3:" + act3.getBalance());//10000
            System.out.println("t4:" + act4.getBalance());//10000
            System.out.println(act1.getBalance() + act2.getBalance() + act3.getBalance());//30000
            System.out.println("耗时:"+(System.currentTimeMillis()-startTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

        }


    }

}
