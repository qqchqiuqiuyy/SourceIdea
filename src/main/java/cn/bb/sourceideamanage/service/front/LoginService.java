package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface LoginService {

    /**
     * 增加一个user
     * @param user
     */
    public void addUser(User user);

    /**
     * 增加一个角色
     * @param userId
     * @param roleId
     */
    public void addRole( Integer userId, Integer roleId);

    /**
     * 进行注册
     * @param account
     * @param password
     * @param repassword
     * @param username
     * @return
     */
    public String register(String account, String password, String repassword, String username);

    /**
     * 检查是否正确
     * @param account
     * @param password
     * @param request
     * @param response
     * @return
     */
    public String check(String account, String password, HttpServletRequest request, HttpServletResponse response);
}
