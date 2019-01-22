package cn.bb.sourceideamanage.service.front.impl;

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
 * @author s
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


    @Override
    public void addUser(User user) {
      loginMapper.addUser(user);
        //新增用户直接赋值1
      loginMapper.addRole(user.getUserId(), Roles.UserCommon.getRoleId());
    }

    @Override
    public void addRole(Integer userId, Integer roleId) {
        loginMapper.addRole(userId,roleId);
    }

    @Override
    public String register(String account, String password, String repassword,String username) {
        if("".equals(account)|| "".equals(password)  || "".equals(username)  || "".equals(repassword) ){

            jsonObject.put(ModelMsg.MSG.getMsg(),"信息输入不完整不能空白");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),"0");
            log.error("信息输入不完整 msg={}",jsonObject);
            return jsonObject.toString();
        }
        if(!password.equals(repassword)){
            jsonObject.put(ModelMsg.MSG.getMsg(),"两次密码不一致请重新输入");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),"0");
            log.error("两次密码不一致 !! p1={},p2={}",password, repassword);
            return jsonObject.toString();
        }
        User user = userService.findUserByAccount(account);
        if(null == user){
            user = new User();
            Md5Hash pwd = new Md5Hash(password, ByteSource.Util.bytes(account));
            user.setUserAccount(account).setUserPassword(pwd.toString()).setUserName(username).setUserMsg("这个人很懒...还没有个人简介");
            loginMapper.addUser(user);
            //新增用户直接赋值1
            loginMapper.addRole(user.getUserId(),1);
            jsonObject.put(ModelMsg.MSG.getMsg(),"注册成功!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),"1");
        }else{
            jsonObject.put(ModelMsg.MSG.getMsg(),"账号已存在请重新输入");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),"0");
            log.error("账号已存在 account={}" ,account);
        }


        return jsonObject.toString();

    }

    @Override
    public String check(String account, String password, HttpServletRequest request, HttpServletResponse response){
        //获取Subject
        Subject subject = SecurityUtils.getSubject();
        //封装用户数据 加密
        Md5Hash pwd = new Md5Hash(password, ByteSource.Util.bytes(account));
        password = pwd.toString();
        UsernamePasswordToken token = new UsernamePasswordToken(account, password);
        try {
            subject.login(token);
            User user = userService.findUserByAccount(account);
            HttpSession session = request.getSession();
            String id = session.getId();
            log.info("session Id ={}",id);
            session.setAttribute(ModelMsg.USER_NAME.getMsg(),user.getUserName());
            session.setAttribute(ModelMsg.USER_ID.getMsg(), user.getUserId());
            session.setMaxInactiveInterval(60*30);
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),"1");
            jsonObject.put(ModelMsg.MSG.getMsg(),"登录成功!!");
        }catch (UnknownAccountException e){
            log.error("账号不存在={}", account);
            jsonObject.put(ModelMsg.MSG.getMsg(),"用户不存在");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),"0");
        }catch (IncorrectCredentialsException e){
            log.error("密码错误={}",password);
            jsonObject.put(ModelMsg.MSG.getMsg(),"密码错误");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),"0");
        }
        return jsonObject.toString();
    }




}
