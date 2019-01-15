package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.dto.front.ApplyUser;
import cn.bb.sourceideamanage.entity.*;
import cn.bb.sourceideamanage.service.front.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceImplTest {

    @Autowired
    UserService userService ;

    @Test
    public void findUserAllRoles() {
        List<String> userAllRoleByUserId = userService.findUserAllRoleByUserId(1);
        log.warn(userAllRoleByUserId.toString());
    }

    @Test
    public void findUserAllPermission(){
        List<String> permissions = userService.findUserAllPermissionByUserId(1);
        log.warn("userPermissions = {} " ,permissions.toString());
    }

    @Test
    public void findUserAllTeam(){
        List<UserTeam> userTeams = userService.findUserAllTeam(1);
        log.warn("userTeams = {} " ,userTeams.toString());
    }

    @Test
    public void findUserAllIdea(){
        List<Idea> userAllIdea = userService.findUserAllIdea(1);
        log.warn("userAllIdea = {} " ,userAllIdea.toString());
    }

    @Test
    public void findUserAllCommentIdea(){
        List<commentIdea> commentIdeas = userService.findUserAllCommentIdea(1);
        log.warn("commentIdeas = {} " , commentIdeas.toString());
    }

    @Test
    public void findUserByAccount(){
        User bb = userService.findUserByAccount("bb");
        log.warn("bb = {} " ,bb.toString());
    }

    @Test
    public void getAllAppy(){
        List<ApplyUser> applys = userService.getAllAppy("生产大队");
        System.out.println(applys);
    }
}