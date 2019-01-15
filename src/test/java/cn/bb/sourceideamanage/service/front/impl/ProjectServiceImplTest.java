package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.dto.front.FrontProject;
import cn.bb.sourceideamanage.dto.front.FrontProjectMsg;
import cn.bb.sourceideamanage.service.front.ProjectService;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectServiceImplTest {

    @Autowired
    ProjectService projectService;
    @Test
    public void findAllFrontProject() {
        PageInfo<FrontProject> info = projectService.findAllFrontProject(0, 0, "");
        List<FrontProject> list = info.getList();
        System.out.println(list);
    }

    @Test
    public void getFrontProjectMsg(){
        FrontProjectMsg projectMsgByProjectId = projectService.getProjectMsgByProjectId(1);
        System.out.println(projectMsgByProjectId);
    }

    @Test
    public void getAllProject(){
        PageInfo<FrontProject> allMyProject = projectService.findAllMyProject(1,2,"", 43);
        System.out.println(allMyProject.getList().toString());
    }


}