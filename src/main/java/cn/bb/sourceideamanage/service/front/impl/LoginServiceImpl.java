package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.common.cacheConstant.CacheConstant;
import cn.bb.sourceideamanage.common.enums.ModelMsg;
import cn.bb.sourceideamanage.common.enums.Roles;
import cn.bb.sourceideamanage.dao.front.LoginMapper;
import cn.bb.sourceideamanage.entity.User;
import cn.bb.sourceideamanage.service.front.LoginService;
import cn.bb.sourceideamanage.service.front.UserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author bobo
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    JSONObject jsonObject;

    @Resource
    LoginMapper loginMapper;

    @Autowired
    UserService userService;

    @Autowired
    Jedis jedis;

    /**
     * 增加一个user
     * @param user  封装用户信息的对象
     */
    @Override
    public void addUser(User user) {
      loginMapper.addUser(user);
        //新增用户直接赋值1
      loginMapper.addRole(user.getUserId(), Roles.UserCommon.getRoleId());
    }

    /**
     * 增加一个角色
     * @param userId 用户id
     * @param roleId 角色id
     */
    @Override
    public void addRole(Integer userId, Integer roleId) {
        loginMapper.addRole(userId,roleId);
    }


    /**
     * 进行注册
     * @param account 账号
     * @param password 密码
     * @param repassword 重复密码
     * @param username  用户名
     * @return
     */
    @Override
    public String register(String account, String password, String repassword,String username) {
        if("".equals(account)|| "".equals(password)  || "".equals(username)  || "".equals(repassword) ){

            jsonObject.put(ModelMsg.MSG.getMsg(),"信息输入不完整不能空白");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(), CacheConstant.SUCCESS);
            log.error("信息输入不完整 msg={}",jsonObject);
            return jsonObject.toString();
        }
        if(!password.equals(repassword)){
            jsonObject.put(ModelMsg.MSG.getMsg(),"两次密码不一致请重新输入");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
            log.error("两次密码不一致 !! p1={},p2={}",password, repassword);
            return jsonObject.toString();
        }
        User user = userService.findUserByAccount(account);
        if(null == user){
            user = new User();
            //用户名加盐  MD5加密输入进来的密码
            Md5Hash pwd = new Md5Hash(password, ByteSource.Util.bytes(account));
            user.setUserAccount(account).
                    setUserPassword(pwd.toString()).
                    setUserName(username).
                    setUserMsg(CacheConstant.NEW_USER_DEFAULT_MSG);
            //存入数据库
            loginMapper.addUser(user);
            //新增用户给予user:common角色
            loginMapper.addRole(user.getUserId(), Roles.UserCommon.getRoleId());

            jsonObject.put(ModelMsg.MSG.getMsg(),"注册成功!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
        }else{
            jsonObject.put(ModelMsg.MSG.getMsg(),"账号已存在请重新输入");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
            log.error("账号已存在 account={}" ,account);
        }


        return jsonObject.toString();

    }

    /**
     * 检查账号是否正确
     * @param account  账号
     * @param password 密码
     * @param request
     * @param response
     * @return
     */
    @Override
    public String check(String account, String password, HttpServletRequest request, HttpServletResponse response){
        // 每个请求创建一个Subject 获取Subject
        Subject subject = SecurityUtils.getSubject();
        //封装用户数据 用 用户名作为盐 md5 加密密码
        Md5Hash pwd = new Md5Hash(password, ByteSource.Util.bytes(account));
        password = pwd.toString();
        //存取账号密码
        UsernamePasswordToken token = new UsernamePasswordToken(account, password);
        try {
            subject.login(token);
            //找到账号
            User user = userService.findUserByAccount(account);
            HttpSession session = request.getSession();
            String id = session.getId();
            log.info("session Id ={}",id);
            session.setAttribute(ModelMsg.USER_NAME.getMsg(),user.getUserName());
            session.setAttribute(ModelMsg.USER_ID.getMsg(), user.getUserId());
            session.setMaxInactiveInterval(CacheConstant.SESSION_ALIVE);
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
            jsonObject.put(ModelMsg.MSG.getMsg(),"登录成功!!");
        }catch (UnknownAccountException e){
            log.error("账号不存在={}", account);
            jsonObject.put(ModelMsg.MSG.getMsg(),"用户不存在");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }catch (IncorrectCredentialsException e){
            log.error("密码错误={}",password);
            jsonObject.put(ModelMsg.MSG.getMsg(),"密码错误");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }
        return jsonObject.toString();
    }


}
