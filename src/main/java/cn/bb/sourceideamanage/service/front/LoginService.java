package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface LoginService {

    public void addUser(User user);

    public void addRole( Integer userId, Integer roleId);
    public Map<String ,String> register(String account, String password, String repassword, String username);
    public Map<String,String> check(String account, String password, HttpServletRequest request, HttpServletResponse response);
}
