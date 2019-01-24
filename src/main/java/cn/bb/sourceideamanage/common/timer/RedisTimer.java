package cn.bb.sourceideamanage.common.timer;

import cn.bb.sourceideamanage.common.enums.IdeaSupportsKey;
import cn.bb.sourceideamanage.service.front.IdeaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.*;

/**
 * 异步任务 5秒钟调用一次
 */
@Component
@Slf4j
public class RedisTimer {

    @Autowired
    Jedis jedis;
    /**
     * ideaSupportUser:
     */
    private static final String USER_SET_KEY = IdeaSupportsKey.UserSetKey.getKey();

    @Autowired
    IdeaService ideaService;
    /**
     * 游标
     */
    private static String CURSOR = "0";
    /**
     * 定时任务 10秒执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void redisToDb(){
        ScanParams scanParams = new ScanParams();
        //匹配所有符合前缀的key                          ideaSupportUser:*
        ScanResult<String> keys = jedis.scan(CURSOR, scanParams.match(USER_SET_KEY +"*"));
        //获得游标
        CURSOR = keys.getStringCursor();
        //得到key集合
        List<String> results = keys.getResult();
        Integer ideaId = 0;
        Long supports = 0L;
        String[] split;
        for (String key : results){
            //ideaSupportUser:id
            split = key.split(":");
            //得到id
            ideaId = Integer.parseInt(split[1]);
            //计算该key的元素个数 即可得到点赞数
            supports = jedis.scard(key);
            //持久化回数据库
            ideaService.durSupports(ideaId,supports);
        }
    }
}
