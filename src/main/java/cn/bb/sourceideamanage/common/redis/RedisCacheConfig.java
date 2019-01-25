package cn.bb.sourceideamanage.common.redis;

import cn.bb.sourceideamanage.common.cacheConstant.CacheConstant;
import cn.bb.sourceideamanage.common.enums.TimeOut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * redisCache的配置类
 * @author bobo
 */
@Configuration
@Slf4j
public class RedisCacheConfig {

    @Autowired
    Jedis jedis;

    /**
     * 配置缓存序列化器
     * @param redisConnectionFactory
     * @return
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){

        RedisTemplate<Object,Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer());
        redisTemplate.setHashKeySerializer(keySerializer());
        redisTemplate.setValueSerializer(valueSerializer());
        redisTemplate.setHashValueSerializer(valueSerializer());
        return redisTemplate;
    }

    /**
     * 缓存管理
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager cacheManager(@Autowired RedisTemplate redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        // 设置缓存过期时间  默认60秒
        redisCacheManager.setDefaultExpiration(TimeOut.TEST.getTime());
        //对不同场景设定缓存时间
           Map<String, Long> expires = new ConcurrentHashMap<>(16);
        //对于缓存名 设置时间
        //对于tag 可以设置很长
        expires.put(CacheConstant.FIND_TAG,TimeOut.FOREVER.getTime());
        expires.put(CacheConstant.ALL_TAG,TimeOut.FOREVER.getTime());
        //想法排行
        expires.put(CacheConstant.GET_IDEA_SUPPORTS,TimeOut.SHORTEST.getTime());
        expires.put(CacheConstant.IDEA_PAGE,TimeOut.SHORTEST.getTime());

        //头脑风暴的时间
        expires.put(CacheConstant.ALL_BRAIN_TIME,TimeOut.FOREVER.getTime());
        expires.put(CacheConstant.BRAIN_TIME,TimeOut.FOREVER.getTime());

         redisCacheManager.setExpires(expires);
        return redisCacheManager;
    }

    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }





}