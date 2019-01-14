package cn.bb.sourceideamanage.controller;

import cn.bb.sourceideamanage.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test")
public class test {
    @RequestMapping("/toTest")
    public String test(Model model){
        List<User> users = new ArrayList<>();
        User user = new User();

        users.add(user);
        model.addAttribute("users",users);
        return "/test";
    }
}
