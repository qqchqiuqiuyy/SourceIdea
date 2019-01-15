package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.dto.front.FrontIdea;
import cn.bb.sourceideamanage.dto.front.IdeaMsg;
import cn.bb.sourceideamanage.dto.front.comment;
import cn.bb.sourceideamanage.entity.*;
import cn.bb.sourceideamanage.service.front.IdeaService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class IdeaServiceImplTest {

    @Autowired
    IdeaService ideaService;

    @Autowired
    Jedis jedis;

    @Test
    public void findTeam() {
        Team team = ideaService.findTeam(1);
        log.warn("team={}",team);
    }

    @Test
    public void findUser() {
        User user = ideaService.findUser(1);
        log.warn("user={}",user);
    }

    @Test
    public void findTag() {
        Tag tag = ideaService.findTag(1);
        log.warn("tag={}",tag);
    }

    @Test
    public void findComment() {
        List<commentIdea> comment = ideaService.findComment(1);
        log.warn("coment={}",comment);
    }

    @Test
    public void findAllFrontIdea(){
        PageInfo<FrontIdea> info = ideaService.findAllFrontIdea(1, 5, "", "");
        List<FrontIdea> list = info.getList();
        System.out.println(list);
    }

    @Test
    public void getIdeaMsg(){
        IdeaMsg ideaMsg = ideaService.getIdeaMsg(6);
        System.out.println(ideaMsg);
    }

    @Test
    public void getIdeasSupports(){
        List<Idea> ideaSupports = ideaService.getIdeaSupports();
        System.out.println(ideaSupports);
    }

    @Test
    public void testJedis(){

        Boolean sismember = jedis.sismember("ideaId:6", "43");
        log.error("有没有43 ={}",sismember);
        Set<String> smembers = jedis.smembers("ideaId:6");
        for(String s : smembers){
            System.out.println(s);
        }
        /*
        set KEY  value 排重
         */
        /*jedis.del("ideaId:"+1);
        jedis.sadd("ideaId:"+1, "43");
        jedis.sadd("ideaId:"+1, "42");
        jedis.sadd("ideaId:"+1, "44");
        jedis.sadd("ideaId:"+1, "45");
        Set<String> keys = jedis.smembers("ideaId:" + 1);
        Boolean sismember = jedis.sismember("ideaId:" + 1, "42");

        log.error("有没有42? = {}",sismember);
        for(String s : keys){
            System.out.println("key Name "+s);
            String[] split = s.split("ideaId:");
            System.out.println("split "+ Arrays.asList(split).toString());
        }
        log.error("set size={}",jedis.scard("ideaId"));
        log.error("set members={}",jedis.smembers("ideaId"));
        log.error("set ideas={}",keys);*/
    }

    @Test
    public void testRedisMap(){

    }

    @Test
    public void findAllIdea(){
        List<FrontIdea> allIdea = ideaService.findAllIdea(43);

        System.out.println(allIdea);
    }

    @Test
    public void getAllComment(){
        List<comment> allComment = ideaService.getAllComment(3);
        System.out.println(allComment);
    }
}