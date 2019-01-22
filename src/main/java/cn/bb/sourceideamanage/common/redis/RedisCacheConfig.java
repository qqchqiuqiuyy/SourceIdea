package cn.bb.sourceideamanage.common.redis;

import cn.bb.sourceideamanage.common.CacheConstant.CacheConstant;
import cn.bb.sourceideamanage.common.enums.IdeaSupportsKey;
import cn.bb.sourceideamanage.common.enums.TimeOut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
@Slf4j
public class RedisCacheConfig {



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
        // 设置缓存过期时间
        redisCacheManager.setDefaultExpiration(TimeOut.MEDIUM.getTime());
        //对不同场景设定缓存时间
           Map<String, Long> expires = new ConcurrentHashMap<>(32);
        //对于缓存名 设置时间
        //对于tag 可以设置很长
        expires.put(CacheConstant.FIND_TAG,TimeOut.FOREVER.getTime());
        expires.put(CacheConstant.ALL_TAG,TimeOut.FOREVER.getTime());

        //想法排行
        expires.put(CacheConstant.GET_IDEA_SUPPORTS,TimeOut.SHORTEST.getTime());

        //头脑风暴
        expires.put(CacheConstant.ALL_BRAIN_TIME,TimeOut.FOREVER.getTime());
        expires.put(CacheConstant.BRAIN_TIME,TimeOut.FOREVER.getTime());


        //teamServoceImpl
        /*expires.put(CacheConstant.TEAM_PAGE, TimeOut.MEDIUM.getTime());
        expires.put(CacheConstant.FIND_ALL_TEAM_MEMBER, TimeOut.MEDIUM.getTime());
        expires.put(CacheConstant.ALL_TEAM_MSG, TimeOut.MEDIUM.getTime());
        expires.put(CacheConstant.ALL_TEAM_BY_USERID, TimeOut.MEDIUM.getTime());
        expires.put(CacheConstant.MY_TEAMS, TimeOut.MEDIUM.getTime());
        expires.put(CacheConstant.MY_TEAM_MEMBER, TimeOut.MEDIUM.getTime());*/

       /* //想法page
        expires.put(TimeOut.IdeaPage.getCacheName(),TimeOut.IdeaPage.getTime());
        //项目page
        expires.put(TimeOut.ProjectPage.getCacheName(),TimeOut.ProjectPage.getTime());
        //点赞排行
        expires.put(TimeOut.IdeaSupportsList.getCacheName(),TimeOut.IdeaSupportsList.getTime());
        //标签
        expires.put(TimeOut.Tags.getCacheName(),TimeOut.Tags.getTime());
        //想法msg
        expires.put(TimeOut.IdeaMsg.getCacheName(),TimeOut.IdeaMsg.getTime());
        //我的想法
        expires.put(TimeOut.MyIdeas.getCacheName(),TimeOut.MyIdeas.getTime());
        //想法评论
        expires.put(TimeOut.Comments.getCacheName(),TimeOut.Comments.getTime());
        //用户简介
        expires.put(TimeOut.UserMsg.getCacheName(),TimeOut.UserMsg.getTime());
        //邀请列表
        expires.put(TimeOut.InviteList.getCacheName(),TimeOut.UserMsg.getTime());
        //我参与的团队
        expires.put(TimeOut.MyTeams.getCacheName(),TimeOut.MyTeams.getTime());
        //我参与的项目
        expires.put(TimeOut.MyProjects.getCacheName(),TimeOut.MyProjects.getTime());
        //我参与项目信息
        expires.put(TimeOut.ProjectMsg.getCacheName(),TimeOut.ProjectMsg.getTime());
        //管理员页面 ideaList
        expires.put(TimeOut.BackIdeasPage.getCacheName(),TimeOut.BackIdeasPage.getTime());
        //TeamS 里的查找所有project
        expires.put(TimeOut.AllProject.getCacheName(),TimeOut.AllProject.getTime());
        //通过userId查找projects
        expires.put(TimeOut.AllProjectsByUserId.getCacheName(),TimeOut.AllProjectsByUserId.getTime());
        //通过teamId查找member
        expires.put(TimeOut.AllMemberByteamId.getCacheName(),TimeOut.AllMemberByteamId.getTime());
        //团队的所有idea
        expires.put(TimeOut.AllteamIdeas.getCacheName(),TimeOut.AllteamIdeas.getTime());
        //根据userId查找team
        expires.put(TimeOut.AllTeamByUserId.getCacheName(),TimeOut.AllTeamByUserId.getTime());
        //查找我的团队所有成员
        expires.put(TimeOut.MyTeamMember.getCacheName(),TimeOut.MyTeamMember.getTime());
        //
        expires.put(TimeOut.TeamRole.getCacheName(),TimeOut.TeamRole.getTime());
        expires.put(TimeOut.TeamId.getCacheName(),TimeOut.TeamId.getTime());
        expires.put(TimeOut.AllTeamMsg.getCacheName(),TimeOut.AllTeamMsg.getTime());
        expires.put(TimeOut.TeamIdeas.getCacheName(),TimeOut.TeamIdeas.getTime());
        //我的项目
        expires.put(TimeOut.MyProject.getCacheName(),TimeOut.MyProject.getTime());
        //头脑风暴时间
        expires.put(TimeOut.AllBrainTime.getCacheName(),TimeOut.AllBrainTime.getTime());*/

       redisCacheManager.setExpires(expires);
        return redisCacheManager;
    }

    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Autowired
    Jedis jedis;

    /*private static final String userSetKey = IdeaSupportsKey.UserSetKey.getKey();
    private static final String supportsKey = IdeaSupportsKey.SupportsKey.getKey();*/


}