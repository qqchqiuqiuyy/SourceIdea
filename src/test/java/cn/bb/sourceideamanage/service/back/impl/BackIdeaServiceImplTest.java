package cn.bb.sourceideamanage.service.back.impl;

import cn.bb.sourceideamanage.dto.back.BackIdea;
import cn.bb.sourceideamanage.service.back.BackIdeaService;
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
public class BackIdeaServiceImplTest {

    @Autowired
    BackIdeaService backIdeaService;
    @Test
    public void findBackIdeaByPage() {
        PageInfo<BackIdea> ideas = backIdeaService.findBackIdeaByPage(1, 1, "我想", "");
        List<BackIdea> list = ideas.getList();
        System.out.println(list.toString());
    }
}