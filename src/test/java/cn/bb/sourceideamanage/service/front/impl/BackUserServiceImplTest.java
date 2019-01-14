package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.dto.back.BackUser;
import cn.bb.sourceideamanage.entity.Team;
import cn.bb.sourceideamanage.service.back.BackUserService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BackUserServiceImplTest {

    @Autowired
    BackUserService backUserService;

    @Test
    public void findAllTeamByUserId(){
        List<Team> teams = backUserService.findAllTeamByUserId(43);
        System.out.println(teams);
    }
    @Test
    public void findAllBackUser() {
        List<BackUser> allBackUser = backUserService.findAllBackUser("");
        System.out.println(allBackUser);
    }

    @Test
    public void findUserByPage(){
        PageInfo<BackUser> pages = backUserService.findUserByPage(2, 1,"");
        List<BackUser> list = pages.getList();
      //  JSONObject jsonObject = JSONObject.fromObject(pages);
        System.out.println(pages);
        System.out.println(list);
    }



    @Autowired
    Jedis jedis;
    @Test
    public void searchUserByName(){
        jedis.set("name","bobo");
        System.out.println(jedis.get("name"));
    }



}