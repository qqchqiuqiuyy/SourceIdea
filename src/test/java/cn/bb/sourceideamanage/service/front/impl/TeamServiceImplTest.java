package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.front.FrontTeam;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Project;
import cn.bb.sourceideamanage.entity.User;
import cn.bb.sourceideamanage.entity.UserTeam;
import cn.bb.sourceideamanage.service.front.TeamService;
import com.github.pagehelper.PageInfo;
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
public class TeamServiceImplTest {

    @Autowired
    TeamService teamService;

    @Test
    public void findAllProject() {
        List<Project> allProject = teamService.findAllProject(1);
        log.warn(allProject.toString());
    }

    @Test
    public void findAllIdea() {
        List<Idea> allIdea = teamService.findAllIdea(1);
        log.warn(allIdea.toString());
    }

    @Test
    public void findAllMembers() {
        List<User> members = teamService.findAllMembers(1);
        log.warn(members.toString());
    }

    @Test
    public void findSearchTeam() {
        List<UserTeam> c = teamService.findSearchTeam("产");
        log.warn(c.toString());
    }

    @Test
    public void findFrontTeam(){
        PageInfo<FrontTeam> r = teamService.findAllFrontTeam(1,1,"R");
        System.out.println(r.getList());
    }

    @Test
    public void findAllTeamMember(){
        BackTeam team = teamService.findAllTeamMember("生产大队");
        System.out.println(team);

    }

    @Test
    public void checkTeamMember(){
        String s = teamService.checkTeamMember(43, 11);
        System.out.println(s);
    }

    @Test
    public void findAllTeam(){
        List<String> allTeam = teamService.findAllTeam(1);
        System.out.println(allTeam.toString());
    }
}