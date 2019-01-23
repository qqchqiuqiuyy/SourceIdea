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
@RequestMapping("/loginC")
@Slf4j
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    JSONObject jsonObject;

    @Autowired
    UserService userService;

    /**
     *     进行注册
     * @param account
     * @param password
     * @param repassword
     * @param username
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public String register(String account, String password, String repassword, String username, Model model){
        String info  = loginService.register(account, password, repassword, username);
        log.info("info={}",info);
        return info;
    }

    /**
     * 检查登录信息
     * @param model
     * @param account   账号
     * @param password  密码
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/check")
    @ResponseBody
    public String check(Model model,String account, String password, HttpServletRequest request, HttpServletResponse response){
        String info = loginService.check(account, password, request, response);
        return info;
    }

    /**
     * 退出
     * @return
     */
    @GetMapping("/toLogout")
    public String toLogout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "forward:/indexC/toIndex";
    }
}
