package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.dao.front.UserMapper;
import cn.bb.sourceideamanage.entity.*;
import cn.bb.sourceideamanage.service.front.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class UserServiceImpl  implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public User findUserById(Integer userId) {
        return userMapper.findUserById(userId);
    }

    @Override
    public List<String> findUserAllRoleByUserId(Integer userId) {
        return userMapper.findUserAllRoleByUserId(userId);
    }

    @Override
    public List<String> findUserAllPermissionByUserId(Integer userId) {
        return userMapper.findUserAllPermissionByUserId(userId);
    }

    @Override
    public List<UserTeam> findUserAllTeam(Integer userId) {
        return userMapper.findUserAllTeam(userId);
    }

    @Override
    public List<Idea> findUserAllIdea(Integer userId) {
        return userMapper.findUserAllIdea(userId);
    }

    @Override
    public List<commentIdea> findUserAllCommentIdea(Integer userId) {
        return userMapper.findUserAllCommentIdea(userId);
    }

    @Override
    public User findUserByAccount(String account) {
        return userMapper.findUserByAccount(account);
    }


}
