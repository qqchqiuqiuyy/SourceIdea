package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.entity.User;
import cn.bb.sourceideamanage.service.front.LoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginServiceImplTest {

    @Autowired
    LoginService service;
    @Test
    public void addUser() {
        User user = new User();
        user.setUserAccount("bobaobo");
        user.setUserPassword("bbb");
        user.setUserName("bbb");
        service.addUser(user);
        System.out.println(user);
    }
}