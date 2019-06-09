package concurrent.countdownlatch.pump;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 抽水机机房,此处存放所有不同平台的抽水机(线程池)
 */
public class MachineRoom {
    //不同的平台机房存放各自的抽水机
    private static final ConcurrentHashMap<String,ThreadPoolExecutor> allMachineRooms = new ConcurrentHashMap<>();

    /**
     * 获取对应平台的线程池
     * @param platformName
     * @return
     */
    public static ThreadPoolExecutor getMachine(String platformName){
        if(null == allMachineRooms.get(platformName)){
            throw new RuntimeException("没有配置"+platformName+"平台的线程池!");
        }
        return allMachineRooms.get(platformName);
    }

    public static void initCreateAllPlatformMachines(){
        //TODO 配置所有平台的线程池
    }

    /**
     * 为不同的平台创建线程池
     *
     * @param platformName    平台名称
     * @param corePoolSize    最小线程数
     * @param maximumPoolSize 最大线程数
     * @param keepAliveSecond 线程是否收回检查时间
     * @param blockQueueSize  池容量
     * @return
     */
    private ThreadPoolExecutor createPlatformMachines(String platformName, int corePoolSize, int maximumPoolSize, long keepAliveSecond, int blockQueueSize) {
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(blockQueueSize);
        //自定义线程名称
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicLong poolNumber = new AtomicLong(1L);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(platformName + "-" + poolNumber.getAndIncrement());
                // 实现自定义线程类,每个该类对象都绑定一个http连接对象
                return t;
            }
        };
        //线程池过载直接跑出异常
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveSecond, unit,
                workQueue, threadFactory, handler);
    }
}
