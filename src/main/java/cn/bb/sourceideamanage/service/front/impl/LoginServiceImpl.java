package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.common.config.upload;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
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
      loginMapper.addRole(user.getUserId(),1);
    }

    @Override
    public void addRole(Integer userId, Integer roleId) {
        loginMapper.addRole(userId,roleId);
    }

    @Override
    public Map<String ,String> register(String account, String password, String repassword,String username) {
        Map<String ,String> hashMap = new HashMap<>();
        if(account.equals("") || password.equals("") || username.equals("") || repassword.equals("")){
            hashMap.put("msg","信息输入不完整不能空白");
            hashMap.put("isSuccess","0");
            log.error("信息输入不完整 msg={}",hashMap);
            return hashMap;
        }
        if(!password.equals(repassword)){
            hashMap.put("msg","两次密码不一致请重新输入");
            hashMap.put("isSuccess","0");
            log.error("两次密码不一致 !! p1={},p2={}",password, repassword);
            return hashMap;
        }
        User user = userService.findUserByAccount(account);
        if(null == user){
            user = new User();
            Md5Hash pwd = new Md5Hash(password, ByteSource.Util.bytes(account));
            user.setUserAccount(account).setUserPassword(pwd.toString()).setUserName(username).setUserMsg("这个人很懒...还没有个人简介");
            loginMapper.addUser(user);

            //新增用户直接赋值1
            loginMapper.addRole(user.getUserId(),1);

            hashMap.put("msg","success");
            hashMap.put("isSuccess","1");
        }else{
            hashMap.put("msg","账号已存在请重新输入");
            hashMap.put("isSuccess","0");
            log.error("账号已存在 account={}" ,account);
        }


        return hashMap;

    }

    @Override
    public Map<String,String> check(String account, String password, HttpServletRequest request, HttpServletResponse response){
        Map<String,String> info = new HashMap<>();
        //获取Subject
        Subject subject = SecurityUtils.getSubject();
        //封装用户数据
        Md5Hash pwd = new Md5Hash(password, ByteSource.Util.bytes(account));
        password = pwd.toString();
        UsernamePasswordToken token = new UsernamePasswordToken(account, password);
        try {
            subject.login(token);
            User user = userService.findUserByAccount(account);
            HttpSession session = request.getSession();
            String id = session.getId();
            log.info("session Id ={}",id);
            session.setAttribute("userName",user.getUserName());
            session.setAttribute("account",account);
            session.setAttribute("password",password);
            session.setAttribute("userId", user.getUserId());
            session.setMaxInactiveInterval(60*30);
            info.put("isSuccess","1");
            info.put("account",account);
        }catch (UnknownAccountException e){
            log.error("账号不存在={}", account);
            info.put("msg","用户不存在");
            info.put("isSuccess","0");
        }catch (IncorrectCredentialsException e){
            log.error("密码错误={}",password);
            info.put("msg","密码错误");
            info.put("isSuccess","0");
        }
        return info;
    }




}
