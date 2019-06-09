package concurrent.countdownlatch.pump;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DaTangPumping extends PumpingController {

    // 系统启动->初始化所有平台线程池-
    // 执行调度->获取水位线数据->1.1抽取所有数据到水池
    public Map getWaterLine(String platformName, String startTime, String endTime) {
        //TODO 将数据放置在WaterPoll中
        return null;
    }

    @Override
    public void concurrentPumping(String platformName, String startTime, String endTime, Map<String, String> params) {
        Integer count = new Integer(params.get("pageCount"));
        CountDownLatch cdl = new CountDownLatch(count);
        //如果在并发执行下,某个线程出现了异常,则本次抽取任务失败
        AtomicBoolean hasException = new AtomicBoolean(false);
        //获取对应平台的线程池
        ThreadPoolExecutor executor = MachineRoom.getMachine(platformName);
        try {
            for (int i = 0; i < count; i++) {
                executor.execute(() -> {
                    try {
                        //TODO 执行业务
                        //TODO 将数据放置在WaterPoll中
                        cdl.countDown();
                        System.out.println("执行一次");
                    } catch (Exception ex) {
                        if (hasException.get() != true) {
                            hasException.set(true);
                        }
                    }

                });
            }
            //本次请求总等待时间(最好不要超过调度间隔时间)
            cdl.await(5, TimeUnit.SECONDS);

            if (cdl.getCount() != 0) {
                System.out.println("超时");
            } else {
                System.out.println("执行完成");
            }
        }catch (Exception ex){
            System.out.println("执行异常"+ex.getStackTrace());
        }finally {
            if(hasException.get()){
                throw new RuntimeException("并发请求时异常!");
            }
        }
    }


}
