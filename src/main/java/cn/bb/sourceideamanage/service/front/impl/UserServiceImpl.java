package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.dao.front.ProjectMapper;
import cn.bb.sourceideamanage.dao.front.TeamMapper;
import cn.bb.sourceideamanage.dao.front.UserMapper;
import cn.bb.sourceideamanage.dto.front.*;
import cn.bb.sourceideamanage.entity.*;
import cn.bb.sourceideamanage.service.front.ProjectService;
import cn.bb.sourceideamanage.service.front.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    @Resource
    ProjectMapper projectMapper;

    @Resource
    TeamMapper teamMapper;

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
            //加入团队
            userMapper.joinTeam(userId,teamId);
            //团队人数+1
            userMapper.addMemberNums(teamId);
            //删除审核表
            userMapper.delApply(userId,teamId);
            //删除邀请表
            userMapper.delInvite(userId,teamId);
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
    @Transactional(rollbackFor = Exception.class)
    public String agree(Integer userId, Integer teamId) {
        try {
            //加入团队
            userMapper.joinTeam(userId,teamId);
            //团队人数+1
            userMapper.addMemberNums(teamId);
            //删除邀请表
            userMapper.delInvite(userId,teamId);
            //如果同时存在团队邀请 和 申请 继续删除 审核表
            userMapper.delApply(userId,teamId);
            log.info("成功进入团队!!");
            jsonObject.put("msg","成功进入团队!!");
            jsonObject.put("isSuccess","1");
        }catch (Exception e){
            log.warn("同意进团队错误!!");
            jsonObject.put("msg","同意进团队发生错误!!");
            jsonObject.put("isSuccess","0");
        }
        return jsonObject.toString();
    }

    @Override
    public List<Integer> getAllTeamRole(Integer userId, Integer teamId) {
        return userMapper.getAllTeamRole(userId,teamId);
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

    @Override
    public List<UserTag> getUsersForTag(String teamName) {
        Integer teamId = userMapper.getTeamId(teamName);
        List<UserTag> tags = projectMapper.getUsersForTag(teamId);
        return tags;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addTeamProject(String teamName, String projectName, String projectMsg) {
        try {
            Integer teamId = userMapper.getTeamId(teamName);
            Integer projectId = projectMapper.checkProjectName(projectName);
            if(projectId != null){
                log.error("已经存在该项目名!! 请勿重复");
                jsonObject.put("msg","已经存在该项目名!! 请勿重复");
                jsonObject.put("isSuccess","2");
                return jsonObject.toString();
            }
            projectMapper.addTeamProject(teamId,projectName,projectMsg);
            log.info("添加项目成功!!");
            jsonObject.put("msg","添加项目成功!!");
            jsonObject.put("isSuccess","1");
            jsonObject.put("successUrl","/UserC/toMyTeamMsg?teamName=" + teamName);
        }catch (Exception e){
            log.error("添加项目失败!!");
            jsonObject.put("msg","添加项目失败!!");
            jsonObject.put("isSuccess","0");
        }

        return jsonObject.toString();
    }

    @Override
    public PageInfo<frontUser> findAllFrontUser(int page, int size, String userName) {
        PageHelper.startPage(page,size);
        List<frontUser> allFrontUser = userMapper.findAllFrontUser(userName);
        return new PageInfo<>(allFrontUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String invite(Integer userId, String teamName) {
        try{
            Integer teamId = teamMapper.findTeamId(teamName);
            String s = userMapper.checkUserInInvite(userId, teamId);
            List<Integer> teamUserId = teamMapper.getTeamUserId(teamId);
            boolean contains = teamUserId.contains(userId);
            if(contains){
                log.error("错误 该用户已在此团队!");
                jsonObject.put("isSuccess","2");
                jsonObject.put("msg","错误 该用户已在此团队!");
            }else if(s == null){
                teamMapper.teamInvite(teamId,userId);
                log.info("邀请成功");
                jsonObject.put("isSuccess","1");
                jsonObject.put("msg","邀请成功");
            }
        }catch (Exception e){
            log.error("邀请发生错误!");
            jsonObject.put("msg","发生错误!");
            jsonObject.put("isSuccess","0");
        }

        return jsonObject.toString();
    }

    @Override
    public frontUser getUserMsg(Integer userId) {
        frontUser userMsg = userMapper.getUserMsg(userId);
        return userMsg;
    }

    /**
     * 如果邀请列表没有 就返回true  如果已经有了就返回false
     * @param userId
     * @param teamName
     * @return
     */
    @Override
    public Boolean checkUserInInvite(Integer userId, String teamName) {
        Integer teamId = teamMapper.findTeamId(teamName);
        String s = userMapper.checkUserInInvite(userId, teamId);
        return s == null ? true : false;
    }

    @Override
    public List<inviteUser> getUserInvite(Integer userId) {

        return userMapper.getUserInvite(userId);
    }




}
