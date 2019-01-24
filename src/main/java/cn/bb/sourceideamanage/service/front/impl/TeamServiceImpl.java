package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.common.cacheConstant.CacheConstant;
import cn.bb.sourceideamanage.common.enums.IsDelete;
import cn.bb.sourceideamanage.common.enums.ModelMsg;
import cn.bb.sourceideamanage.common.enums.Roles;
import cn.bb.sourceideamanage.dao.front.TeamMapper;
import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.front.*;
import cn.bb.sourceideamanage.entity.*;
import cn.bb.sourceideamanage.service.front.TeamService;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service
@Slf4j
public class TeamServiceImpl implements TeamService {
    @Resource
    TeamMapper teamMapper;

    @Autowired
    JSONObject jsonObject;

    /**
     * 根据团队id查找该团队所有项目
     * @param teamId   团队id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.ALL_PROJECT},key = "'allProject=[teamId='+#teamId+']'")
    public List<Project> findAllProject(Integer teamId){
        return teamMapper.findAllProject(teamId,IsDelete.NOTDELETE.getState());
    }


    /**
     *  根据想法id 查找所有
     * @param ideaId    想法id
     * @return
     */
    @Override
    public List<Idea> findAllIdea(Integer ideaId) {
        return teamMapper.findAllIdea(ideaId,IsDelete.NOTDELETE.getState());
    }


    /**
     * 根据团队id查找该团队所有成员
     * @param teamId    团队id
     * @return
     */
    @Override
    public List<User> findAllMembers(Integer teamId) {
        return teamMapper.findAllMembers(teamId,IsDelete.NOTDELETE.getState());
    }


    /**
     * 根据团名查找
     * @param teamName  团队名
     * @return
     */
    @Override
    public List<UserTeam> findSearchTeam(String teamName) {
        return teamMapper.findSearchTeam(teamName,IsDelete.NOTDELETE.getState());
    }


    /**
     * 根据以下参数返回 团队模块前台分页
     * @param page  当前页
     * @param size  每页长度
     * @param teamName  团队名
     * @return
     */
    @Override
    @Cacheable( cacheNames = {CacheConstant.TEAM_PAGE},key = "'findAllFrontTeam=[page='+#page+'][size='+#size+'][teamName='+#teamName+']' ")
    public PageInfo<FrontTeam> findAllFrontTeam(int page, int size, String teamName) {
        PageHelper.startPage(page,size);
        List<FrontTeam> teams = teamMapper.findAllFrontTeam(teamName,Roles.UserTeamManager.getRoleId(), IsDelete.NOTDELETE.getState());
        return new PageInfo<>(teams);
    }


    /**
     * 根据团队名查找团队成员
     * @param teamName 团队名
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_ALL_TEAM_MEMBER} ,key = "'findAllTeamMember=[teamName='+#teamName+']'")
    public BackTeam findAllTeamMember(String teamName) {
        return teamMapper.findAllTeamMember(teamName,IsDelete.NOTDELETE.getState());
    }


    /**
     * 加入团队,
     * @param teamName 团队名
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.ALL_TEAM_MSG},key = "'allTeamMsg=[teamName='+#teamName+']'")
    public TeamMsg findTeamMsg(String teamName){
        return teamMapper.findTeamMsg(teamName,IsDelete.NOTDELETE.getState());
    }


    /**
     * 根据userId  团队Id 判断该user是否是团队管理员
     * @param userId    用户id
     * @param teamId    团队id
     * @param roleId    角色id
     * @return
     */
    @Override
    public Integer checkManager(Integer userId, Integer teamId,Integer roleId) {
        return teamMapper.checkManager(userId,teamId,roleId,IsDelete.NOTDELETE.getState());
    }

    /**
     * 加入团队,
     * @param userId    用户id
     * @param teamId    需要加入的团队
     * @return
     */
    @Override
    public String joinTeam(Integer userId, Integer teamId) {
        String checkTeamMember = teamMapper.checkTeamMember(userId, teamId,IsDelete.NOTDELETE.getState());
        if(null == checkTeamMember){
            //检查是否已经在申请表
            String checkApplyList = teamMapper.checkApply(userId,teamId,IsDelete.NOTDELETE.getState());
            if(null == checkApplyList){
                teamMapper.apply(userId, teamId);
                log.info("申请加入团队成功!!");
                jsonObject.put("msg","申请加入团队成功!!等待团长审批!!");
                jsonObject.put("success","1");
            }else{
                log.warn("已申请!!等待审批中 请勿重复申请");
                jsonObject.put("msg","已申请 等待审批 请勿重复申请");
                jsonObject.put("success","0");
            }
        }else{
            log.info("申请加入失败!! 已在此团队");
            jsonObject.put("msg","已在此团队,请勿重复加入!!");
            jsonObject.put("success","0");
        }

        return jsonObject.toString();
    }


    /**
     * 根据用户id 和团队id 检查是否在该团队
     * @param userId    用户id
     * @param teamId    团队id
     * @return
     */
    @Override
    public String checkTeamMember(Integer userId, Integer teamId) {
        return teamMapper.checkTeamMember(userId,teamId,IsDelete.NOTDELETE.getState());
    }


    /**
     * 根据用户id查找该用户所有团队
     * @param userId    用户id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.ALL_TEAM_BY_USERID},key = "'allTeamByUserId=[userId='+#userId+']'")
    public List<String> findAllTeam(Integer userId) {
        return teamMapper.findAllTeam(userId,IsDelete.NOTDELETE.getState());
    }


    /**
     *  通过以下参数 返回我的团队分页信息
     * @param page  当前页
     * @param size  每页长度
     * @param teamName  团队名
     * @param userId    当前用户id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.MY_TEAMS},key = "'myTeams=[page='+#page+'][size='+#size+'][teamNmae='+#teamName+'][userId='+#userId+']'")
    public PageInfo<FrontTeam> findAllMyTeam(int page, int size, String teamName, Integer userId) {
        PageHelper.startPage(page,size);
        List<FrontTeam> teams = teamMapper.findAllMyTeam(teamName,userId,Roles.UserTeamManager.getRoleId(),IsDelete.NOTDELETE.getState());
        return new PageInfo<>(teams);
    }


    /**
     * 根据团队名查找我的团队所有团员
     * @param teamName  团队名
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.MY_TEAM_MEMBER} ,key = "'myTeamMember=[teamName='+#teamName+']'")
    public List<MyTeamMember> findAllMyTeamMember(String teamName) {
        return teamMapper.findAllMyTeamMember(teamName,IsDelete.NOTDELETE.getState());
    }

    /**
     * 通过团队名和userId 查找团队角色
     * @param teamName  团队名
     * @param userId    用户id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.TEAM_ROLE} ,key = "'teamRole=[teamName='+#teamName+'][userId='+#userId+']'")
    public List<String> findTeamRoleByTeamNameAndUserId(String teamName, Integer userId) {
        return teamMapper.findTeamRoleByTeamNameAndUserId(teamName,userId,IsDelete.NOTDELETE.getState());
    }


    /**
     * 根据团队名 查找团队Id
     * @param teamName  团队名
     * @return
     */
    @Override
    @Cacheable( cacheNames = {CacheConstant.TEAM_ID},key = "'teamName=['+#teamName+']'" )
    public Integer getTeamId(String teamName) {
        return teamMapper.getTeamId(teamName,IsDelete.NOTDELETE.getState());
    }


    /**
     * 新增一个团队
     * @param teamName  团队名
     * @param teamMsg   团队信息
     * @param userId    当前用户id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CacheConstant.MY_TEAMS},allEntries = true)
    public String addTeam(String teamName, String teamMsg, Integer userId) {
        try {
            //判断是否已经有这个团队
            Integer tId = teamMapper.getTeamId(teamName,IsDelete.NOTDELETE.getState());
            if(tId != null){
                log.error("新建团队错误!! 已有重复团队");
                jsonObject.put(ModelMsg.MSG.getMsg(),"新建团队发生错误!!已有重复团队");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),"0");

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
                jsonObject.put(ModelMsg.MSG.getMsg(),"创建团队成功!!");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),"1");
                jsonObject.put(ModelMsg.SUCCESS_URL.getMsg(),"/userC/toMyTeamMsg/" + teamName);
            }
        }catch (Exception e){
            log.error("新建团队错误!!exception={}",e.toString());
            jsonObject.put(ModelMsg.MSG.getMsg(),"新建团队发生错误!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),"0");
        }
        return jsonObject.toString();
    }


    /**
     * 根据团队id 查找所有的团队队员
     * @param teamId    团队id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.ALL_MEMBER_BY_TEAMID},key = "'allMemberByteamId=[teamId='+#teamId+']'")
    public List<TeamMember> findAllMemberByTeamId(Integer teamId) {
        return teamMapper.findAllMemberByTeamId(teamId);
    }


    /**
     *  根据团队id 查找该团队所有想法
     * @param teamId 团队id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.ALL_TEAM_IDEAS},key = "'allteamIdeas=[teamId='+#teamId+']'")
    public List<Idea> findAllTeamIdeas(Integer teamId){
        return teamMapper.findAllIdea(teamId,IsDelete.NOTDELETE.getState());
    }
}
