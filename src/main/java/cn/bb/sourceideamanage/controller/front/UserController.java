package cn.bb.sourceideamanage.controller.front;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/UserC")
public class UserController {

    @RequestMapping("/toHome")
    public String toHome(HttpServletRequest request){
        Integer userId = (Integer)request.getSession().getAttribute("userId");

        return "pages/front/html/home";
    }
    @RequestMapping("/toMyIdea")
    public String toMyIdea(){
        return "/pages/front/html/myIdea";
    }

    @RequestMapping("/toMyProject")
    public String toMyProject(){
        return "/pages/front/html/myProject";
    }

    @RequestMapping("/toMyTeam")
    public String toMyTeam(){
        return "/pages/front/html/myTeam";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "/pages/front/html/user/login";
    }
    @RequestMapping("/toReg")
    public String toReg(){
        return "/pages/front/html/user/reg";
    }

}
