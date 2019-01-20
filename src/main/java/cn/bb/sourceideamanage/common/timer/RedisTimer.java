package cn.bb.sourceideamanage.common.timer;

import cn.bb.sourceideamanage.common.enums.IdeaSupportsKey;
import cn.bb.sourceideamanage.service.front.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.*;
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
    IdeaService ideaService;

    /**
     * 定时任务 10秒执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void redisToDb(){
        ScanParams scanParams = new ScanParams();
        ScanResult<String> keys = jedis.scan("0", scanParams.match(userSetKey+"*"));
        List<String> results = keys.getResult();
        Integer ideaId = 0;
        Long supports = 0L;
        String[] split = null;
        for (String key : results){
            split = key.split(":");
            ideaId = Integer.parseInt(split[1]);
            supports = jedis.scard(key);
            ideaService.durSupports(ideaId,supports);
        }

    }
}
