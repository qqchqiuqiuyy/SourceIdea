package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.common.enums.Roles;
import cn.bb.sourceideamanage.dao.front.IdeaMapper;
import cn.bb.sourceideamanage.dao.front.TeamMapper;
import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.front.*;
import cn.bb.sourceideamanage.entity.*;
import cn.bb.sourceideamanage.service.front.TeamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
@CacheConfig(cacheNames = "teamServiceImpl")
public class TeamServiceImpl implements TeamService {
    @Resource
    TeamMapper teamMapper;

    @Autowired
    JSONObject jsonObject;

    @Override
    @Cacheable(key = "'findAllProject=[teamId='+#teamId+']'")
    public List<Project> findAllProject(Integer teamId){
        return teamMapper.findAllProject(teamId);
    }

    @Override
    public List<Idea> findAllIdea(Integer ideaId) {
        return teamMapper.findAllIdea(ideaId);
    }

    @Override
    public List<User> findAllMembers(Integer team_id) {
        return teamMapper.findAllMembers(team_id);
    }

    @Override
    public List<UserTeam> findSearchTeam(String teamName) {
        return teamMapper.findSearchTeam(teamName);
    }

    @Override
    @Cacheable( key = "'findAllFrontTeam=[page='+#page+']'+'[size='+#size+']'+'[teamName='+#teamName+']' ")
    public PageInfo<FrontTeam> findAllFrontTeam(int page, int size, String teamName) {
        PageHelper.startPage(page,size);
        List<FrontTeam> teams = teamMapper.findAllFrontTeam(teamName,Roles.UserTeamManager.getRoleId());
        return new PageInfo<>(teams);
    }

    @Override
    @Cacheable(key = "'findAllTeamMember=[teamName='+#teamName+']'")
    public BackTeam findAllTeamMember(String teamName) {
        return teamMapper.findAllTeamMember(teamName);
    }

    @Override
    @Cacheable(key = "'findTeamMsg=[teamName='+#teamName+']'")
    public TeamMsg findTeamMsg(String teamName){
        return teamMapper.findTeamMsg(teamName);
    }
    /**
     * 申请加入团队
     * @param userId
     * @param teamId
     * @return
     */
    @Override
    public String joinTeam(Integer userId, Integer teamId) {
        String checkTeamMember = teamMapper.checkTeamMember(userId, teamId);
        if(null == checkTeamMember){
            //检查是否已经在申请表
            String checkApplyList = teamMapper.checkApply(userId,teamId);
            if(null == checkApplyList){
                teamMapper.apply(userId, teamId);
                log.info("申请加入团队成功!!");
                jsonObject.put("msg","申请加入团队成功!!等待团长审批!!");
                jsonObject.put("isSuccess","1");
            }else{
                log.warn("已申请!!等待审批中 请勿重复申请");
                jsonObject.put("msg","已申请 等待审批 请勿重复申请");
                jsonObject.put("isSuccess","0");
            }
        }else{
            log.info("申请加入失败!! 已在此团队");
            jsonObject.put("msg","已在此团队,请勿重复加入!!");
            jsonObject.put("isSuccess","0");
        }

        return jsonObject.toString();
    }

    @Override
    public String checkTeamMember(Integer userId, Integer teamId) {
        return teamMapper.checkTeamMember(userId,teamId);
    }

    @Override
    @Cacheable(key = "'findAllTeam=[userId='+#userId+']'")
    public List<String> findAllTeam(Integer userId) {
        return teamMapper.findAllTeam(userId);
    }

    @Override
    @Cacheable(key = "'findAllMyTeam=[page='+#page+'][size='+#size+'][teamNmae='+#teamName+'][userId='+#userId+']'")
    public PageInfo<FrontTeam> findAllMyTeam(int page, int size, String teamName, Integer userId) {
        PageHelper.startPage(page,size);
        List<FrontTeam> teams = teamMapper.findAllMyTeam(teamName,userId,Roles.UserTeamManager.getRoleId());
        return new PageInfo<>(teams);
    }

    @Override
    public List<MyTeamMember> findAllMyTeamMember(String teamName) {
        return teamMapper.findAllMyTeamMember(teamName);
    }

    @Override
    public List<String> findTeamRoleByTeamNameAndUserId(String teamName, Integer userId) {
        return teamMapper.findTeamRoleByTeamNameAndUserId(teamName,userId);
    }

    @Override
    @Cacheable(key = "'getTeamId=[teamName='+#teamName+']'" )
    public Integer getTeamId(String teamName) {
        return teamMapper.getTeamId(teamName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addTeam(String teamName, String teamMsg, Integer userId) {
        try {
            //判断是否已经有这个团队
            Integer tId = teamMapper.getTeamId(teamName);
            if(tId != null){
                log.error("新建团队错误!! 已有重复团队");
                jsonObject.put("msg","新建团队发生错误!!已有重复团队");
                jsonObject.put("isSuccess","0");

            }else{
                NewTeam newTeam = new NewTeam();
                newTeam.setTeamMsg(teamMsg).setTeamName(teamName);
                //添加到team表
                teamMapper.addTeam(newTeam);
                //返回刚才的teamId
                Integer teamId = newTeam.getTeamId();
                log.info("teamId = {}",teamId);
                //团队队长
                Role role5 = new Role().setRoleId(Roles.UserTeamManager.getRoleId()).
                                        setRoleName(Roles.UserTeamManager.getRoleName()+":"+teamId);

                int successNums = teamMapper.addTeamRoles(role5, teamId,userId);
                log.info("successNums={}",successNums);
                jsonObject.put("msg","创建团队成功!!");
                jsonObject.put("isSuccess","1");
                jsonObject.put("successUrl","/UserC/toMyTeamMsg?teamName=" + teamName);
            }


        }catch (Exception e){
            log.error("新建团队错误!!exception={}",e.toString());
            jsonObject.put("msg","新建团队发生错误!!");
            jsonObject.put("isSuccess","0");
        }
        return jsonObject.toString();
    }

    @Override
    public String getTeamMsg(Integer teamId) {
        return "";
    }

    @Override
    @Cacheable(key = "'findAllMemberByTeamId=[teamId='+#teamId+']'")
    public List<TeamMember> findAllMemberByTeamId(Integer teamId) {
        return teamMapper.findAllMemberByTeamId(teamId);
    }


    @Override
    @Cacheable(key = "'findAllTeamIdeas=[teamId='+#teamId+']'")
    public List<Idea> findAllTeamIdeas(Integer teamId){
        return teamMapper.findAllIdea(teamId);
    }
}
