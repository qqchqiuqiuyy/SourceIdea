package cn.bb.sourceideamanage.controller.front;

import cn.bb.sourceideamanage.service.front.LoginService;
import cn.bb.sourceideamanage.service.front.UserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/LoginC")
@Slf4j
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    JSONObject jsonObject;

    @Autowired
    UserService userService;

   /* @RequestMapping("/toLogin")
    public String toLogin(){
        return "pages/common/login";
    }

    @RequestMapping("/toRegister")
    public String toRegister(){
        return "pages/common/register";
}*/

    /**
     *     进行注册
     * @param account
     * @param password
     * @param repassword
     * @param username
     * @return
     */
    @RequestMapping("/register")
    public String register(String account, String password, String repassword, String username, Model model){
        Map<String, String> info = loginService.register(account, password, repassword, username);
        log.info("info={}",info);
        String isSuccess = info.get("success");
        if(isSuccess.equals("1")){
            return "forward:/UserC/toLogin";
        }else{
            model.addAttribute("info",info.get("msg"));
        }
        return  "/pages/front/html/user/reg";

    }

    @RequestMapping("/check")
    public String check(Model model,String account, String password, HttpServletRequest request, HttpServletResponse response){
        Map<String, String> info = loginService.check(account, password, request, response);
        String isSuccess = info.get("success");
        if(isSuccess.equals("1")){
            return "forward:/IndexC/toIndex";
        }else{
            String msg = info.get("msg");
            model.addAttribute("msg",msg);
            return "/pages/front/html/user/login";
        }
    }

    @RequestMapping("/toErr")
    public String toErr(){
        return "pages/examples/404";
    }

    @RequestMapping("/toLogout")
    public String toLogout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "forward:/IndexC/toIndex";
    }
}
