package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bobo
 */
public interface LoginService {

    /**
     * 增加一个user
     * @param user  封装用户信息的对象
     */
    public void addUser(User user);

    /**
     * 增加一个角色
     * @param userId 用户id
     * @param roleId 角色id
     */
    public void addRole( Integer userId, Integer roleId);

    /**
     * 进行注册
     * @param account 账号
     * @param password 密码
     * @param repassword 重复密码
     * @param username  用户名
     * @return
     */
    public String register(String account, String password, String repassword, String username);

    /**
     * 检查账号是否正确
     * @param account  账号
     * @param password 密码
     * @param request
     * @param response
     * @return
     */
    public String check(String account, String password, HttpServletRequest request, HttpServletResponse response);


}
