package cn.bb.sourceideamanage.common.timer;

import cn.bb.sourceideamanage.common.enums.IdeaSupportsKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 异步任务 5秒钟调用一次
 */
@Component
public class RedisTimer {

    @Autowired
    Jedis jedis;
    private static final String userSetKey = IdeaSupportsKey.UserSetKey.getKey();
    private static final String supportsKey = IdeaSupportsKey.SupportsKey.getKey();

    @Autowired

    /**
     * 定时任务 5秒执行一次
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void redisToDb(){
        ScanParams scanParams = new ScanParams();
        ScanResult<String> keys = jedis.scan("0", scanParams.match(userSetKey+"*"));
        List<String> results = keys.getResult();
        Integer ideaId = 0;
        Long supports = 0L;
        String[] split = null;
        Map<Integer,Long> map = new HashMap<>();
        for (String key : results){
            split = key.split(":");
            ideaId = Integer.parseInt(split[1]);
            supports = jedis.scard(key);
            map.put(ideaId,supports);
        }
        //TODO 刷入刷数据库
    }
}
