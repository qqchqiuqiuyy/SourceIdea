package cn.bb.sourceideamanage.common.redis;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.Jedis;

/**
 * redis配置类
 * @date   2018年6月6日

 */
@Configuration
public class RedisConfig {
    @Value("${jedis.port}")
    private String port;

    @Bean("jedis")
    public Jedis jedis(){
        return new Jedis(port,6379);
    }
}

