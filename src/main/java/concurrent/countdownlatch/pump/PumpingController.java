package concurrent.countdownlatch.pump;

import java.util.Map;

public abstract class PumpingController {
    public void control(String platformName, String startTime, String endTime, Map<String,String> params){
        Map result = getWaterLine(platformName, startTime, endTime);
        if(null != result){
            concurrentPumping(platformName, startTime, endTime,  result);
        }
        //TODO kafka 写入
    }

    /**
     * 第一次抽取,获取总水位线
     * @param platformName
     * @param startTime
     * @param endTime
     * @return notNull:进行并发分页抽取;null:不进行并发分页抽取
     */
    public abstract Map getWaterLine(String platformName, String startTime, String endTime);

    /**
     * 并发抽取
     * @param platformName
     * @param startTime
     * @param endTime
     * @param params
     */
    public abstract void concurrentPumping(String platformName, String startTime, String endTime, Map<String,String> params);

}
