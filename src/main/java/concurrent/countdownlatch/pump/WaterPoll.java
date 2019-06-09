package concurrent.countdownlatch.pump;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 水池,所有平台抽取的临时数据暂存在这里
 */
public class WaterPoll {
    //小水池,从不同平台抽过来的水都在这里
    private static ConcurrentHashMap<String, List> waterPoll = new ConcurrentHashMap<>();

    /**
     * 数据流入
     *
     * @param platformName
     * @param startTime
     * @param endTime
     * @param water
     */
    public static void inFlow(String platformName, String startTime, String endTime, List water) {
        String key = platformName + ":" + startTime + ":" + endTime;
        if (null == waterPoll.get(key)) {
            synchronized (waterPoll) {
                if (null == waterPoll.get(key)) {
                    waterPoll.put(key, new ArrayList(water.size()));
                }
            }
        }
        waterPoll.get(key).addAll(water);
        System.out.println(key + "流入" + water.size() + "条数据");
    }

    /**
     * 数据流出
     *
     * @param platformName
     * @param startTime
     * @param endTime
     * @return
     */
    public static List outFlow(String platformName, String startTime, String endTime) {
        String key = platformName + ":" + startTime + ":" + endTime;
        if (null != waterPoll.get(key)) {
            List water = waterPoll.remove(key);
            System.out.println(key + "流出" + water.size() + "条数据");
            return water;
        }
        System.out.println(key + "流出0条数据");
        return new ArrayList(0);
    }
}
