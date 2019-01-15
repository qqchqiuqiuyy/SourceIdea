package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.dao.front.UserMapper;
import cn.bb.sourceideamanage.dto.front.ApplyUser;
import cn.bb.sourceideamanage.dto.front.FrontProject;
import cn.bb.sourceideamanage.entity.*;
import cn.bb.sourceideamanage.service.front.UserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service
@Slf4j
public class UserServiceImpl  implements UserService {

    @Resource
    UserMapper userMapper;

    @Autowired
    JSONObject jsonObject;

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

    @Override
    @Transactional
    public String delMember(Integer userId, String teamName) {
        try {
            userMapper.delMember(userId,teamName);
            jsonObject.put("msg","删除成功!");
            jsonObject.put("isSuccess","1");
        }catch (Exception e){
            log.error("删除错误");
            jsonObject.put("msg","删除错误 请重试");
            jsonObject.put("isSuccess","0");
        }

        return jsonObject.toString();
    }

    @Override
    public List<ApplyUser> getAllAppy(String teamName) {
        return userMapper.getAllAppy(teamName);
    }

    @Override
    @Transactional
    public String agreeMember(Integer userId,String teamName){
        try {
            Integer teamId = userMapper.getTeamId(teamName);
            userMapper.joinTeam(userId,teamId);
            userMapper.delApply(userId,teamId);
            jsonObject.put("msg","同意成功!!");
            jsonObject.put("isSuccess","1");
        }catch (Exception e){
            log.warn("同意进团队错误!!");
            jsonObject.put("msg","同意进团队发生错误!!");
            jsonObject.put("isSuccess","0");
        }

        return jsonObject.toString();
    }

    @Override
    public List<FrontProject> findProjects(String teamName) {
        return userMapper.findProjects(teamName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delProject(Integer projectId) {
        try {
            userMapper.delProject(projectId);
            log.info("删除项目成功!");
            jsonObject.put("msg","删除成功");
            jsonObject.put("isSuccess","1");
        }catch (Exception e){
            log.warn("删除项目失败!");
            jsonObject.put("msg","删除项目失败!");
            jsonObject.put("isSuccess","0");
        }
        return jsonObject.toString();
    }

}
