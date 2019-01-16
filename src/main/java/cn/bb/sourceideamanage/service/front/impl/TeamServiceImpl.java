package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.dao.front.TeamMapper;
import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.front.FrontTeam;
import cn.bb.sourceideamanage.dto.front.MyTeamMember;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Project;
import cn.bb.sourceideamanage.entity.User;
import cn.bb.sourceideamanage.entity.UserTeam;
import cn.bb.sourceideamanage.service.front.TeamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.util.List;
@Service
@Slf4j
public class TeamServiceImpl implements TeamService {
    @Resource
    TeamMapper teamMapper;

    @Autowired
    JSONObject jsonObject;

    @Override
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
    public PageInfo<FrontTeam> findAllFrontTeam(int page, int size, String teamName) {
        PageHelper.startPage(page,size);
        List<FrontTeam> teams = teamMapper.findAllFrontTeam(teamName);
        return new PageInfo<>(teams);
    }

    @Override
    public BackTeam findAllTeamMember(String teamName) {
        return teamMapper.findAllTeamMember(teamName);
    }

    @Override
    public String joinTeam(Integer userId, Integer teamId) {
        String checkTeamMember = teamMapper.checkTeamMember(userId, teamId);
        if(null == checkTeamMember){
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
    public List<String> findAllTeam(Integer userId) {
        return teamMapper.findAllTeam(userId);
    }

    @Override
    public PageInfo<FrontTeam> findAllMyTeam(int page, int size, String teamName, Integer userId) {
        PageHelper.startPage(page,size);
        List<FrontTeam> teams = teamMapper.findAllMyTeam(teamName,userId);
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
    public Integer getTeamId(String teamName) {
        return teamMapper.getTeamId(teamName);
    }
}
