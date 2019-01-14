package cn.bb.sourceideamanage.service.back.impl;

import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.back.BackTeamMember;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Project;
import cn.bb.sourceideamanage.entity.User;
import cn.bb.sourceideamanage.service.back.BackTeamService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class BackTeamServiceImplTest {

    @Autowired
    BackTeamService backTeamService;

    @Test
    public void findAllProjectByTeamId() {
        List<Project> projects = backTeamService.findAllProjectByTeamId(1);
        System.out.println(projects.toString());
    }

    @Test
    public void findAllMemberByTeamId() {
        List<BackTeamMember> btm = backTeamService.findAllMemberByTeamId(1);
        System.out.println(btm.toString());
    }

    @Test
    public void findAllTeamIdea() {
        List<Idea> allTeamIdea = backTeamService.findAllTeamIdea(1);
        System.out.println(allTeamIdea.toString());
    }

    @Test
    public void findTeamCaptainByTeamId() {
        String teamCaptainByTeamId = backTeamService.findTeamCaptainByTeamId(2);
        System.out.println(teamCaptainByTeamId);
    }

    @Test
    public void findAllBackTeamMember() {
        List<User> list = new ArrayList<User>();
        System.out.println(list.size());
    }
}