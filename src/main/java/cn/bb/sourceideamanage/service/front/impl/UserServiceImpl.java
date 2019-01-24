package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.common.cacheConstant.CacheConstant;
import cn.bb.sourceideamanage.common.enums.IsDelete;
import cn.bb.sourceideamanage.common.enums.ModelMsg;
import cn.bb.sourceideamanage.common.enums.ProjectArchive;
import cn.bb.sourceideamanage.common.enums.Roles;
import cn.bb.sourceideamanage.dao.front.ProjectMapper;
import cn.bb.sourceideamanage.dao.front.TeamMapper;
import cn.bb.sourceideamanage.dao.front.UserMapper;
import cn.bb.sourceideamanage.dto.front.*;
import cn.bb.sourceideamanage.entity.*;
import cn.bb.sourceideamanage.service.front.TeamService;
import cn.bb.sourceideamanage.service.front.UserService;
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
/**
 * @author bobo
 */
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

    @Autowired
    TeamService teamService;

    /**
     * 根据用户id查找该用户所有信息
     * @param userId 用户id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_USER_BY_ID} ,key = "'findUserById=[userId='+#userId+']'")
    public User findUserById(Integer userId) {
        return userMapper.findUserById(userId,IsDelete.NOTDELETE.getState());
    }

    /**
     * 根据用户id 查找该用户所有角色
     * @param userId 用户id
     * @return
     */
    @Override
    public List<String> findUserAllRoleByUserId(Integer userId) {
        return userMapper.findUserAllRoleByUserId(userId);
    }


    /**
     * 根据用户id 返回该用户所有操作
     * @param userId 用户id
     * @return
     */
    @Override
    public List<String> findUserAllPermissionByUserId(Integer userId) {
        return userMapper.findUserAllPermissionByUserId(userId);
    }

    /**
     * 根据用户id 查找该用户所有团队
     * @param userId 用户id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_USER_ALL_TEAM} ,key = "'findUserAllTeam=[userId='+#userId+']'")
    public List<UserTeam> findUserAllTeam(Integer userId) {
        return userMapper.findUserAllTeam(userId,IsDelete.NOTDELETE.getState());
    }


    /**
     * 根据用户id 查找该用户所有想法
     * @param userId 用户id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_USER_ALL_IDEA} , key = "'findUserAllIdea=[userId='+#userId+']'")
    public List<Idea> findUserAllIdea(Integer userId) {
        return userMapper.findUserAllIdea(userId,IsDelete.NOTDELETE.getState());
    }


    /**
     * 根据用户id查找该用户所有评论
     * @param userId    用户id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_USER_ALL_COMMENT_IDEA} ,key = "'findUserAllCommentIdea=[userId'+#userId+']'")
    public List<CommentIdea> findUserAllCommentIdea(Integer userId) {
        return userMapper.findUserAllCommentIdea(userId,IsDelete.NOTDELETE.getState());
    }


    /**
     * 根据账号  查找该用户信息
     * @param account   账号
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_USER_BY_ACCOUNT} ,key = "'findUserByAccount=[account='+#account+']'")
    public User findUserByAccount(String account) {
        return userMapper.findUserByAccount(account,IsDelete.NOTDELETE.getState());
    }


    /**
     * 根据用户id  团队名 删除该成员
     * @param userId    被删除用户id
     * @param teamName  团队名
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CacheConstant.MY_TEAM_MEMBER} ,key = "'myTeamMember=[teamName='+#teamName+']'")
    public String delMember(Integer userId, String teamName) {
        try {
            Integer teamId = teamService.getTeamId(teamName);
            userMapper.delMember(userId,teamName,IsDelete.DELETE.getState());
            userMapper.decrMemberNums(teamId,IsDelete.NOTDELETE.getState());
            jsonObject.put(ModelMsg.MSG.getMsg(),"删除成功!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
        }catch (Exception e){
            log.error("删除错误 e={}",e.getMessage());
            jsonObject.put(ModelMsg.MSG.getMsg(),"删除错误 请重试");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }

        return jsonObject.toString();
    }


    /**
     * 根据团队名  查找申请该团队的所有用户信息
     * @param teamName  团队名
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.GET_ALL_APPY} ,key = "'getAllAppy=[teamName'+#teamName+']'")
    public List<ApplyUser> getAllAppy(String teamName) {
        return userMapper.getAllAppy(teamName,IsDelete.NOTDELETE.getState());
    }


    /**
     * 根据用户id 团队名 同意该用户进入团队
     * @param userId    被同意用户id
     * @param teamName  团队名
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CacheConstant.MY_TEAM_MEMBER,CacheConstant.GET_ALL_APPY},allEntries = true)
    public String agreeMember(Integer userId,String teamName){
        try {

            Integer teamId = userMapper.getTeamId(teamName,IsDelete.NOTDELETE.getState());
            //加入团队
            userMapper.joinTeam(userId,teamId, Roles.UserProjectMember.getRoleId(),Roles.UserProjectMember.getRoleName()+":"+teamId);
            userMapper.joinTeam(userId,teamId, Roles.UserTeamMember.getRoleId(),Roles.UserTeamMember.getRoleName()+":"+teamId);
            log.info("加入团队后的role={}",Roles.UserTeamMember.getRoleName()+":"+teamId);
            //团队人数+1
            userMapper.addMemberNums(teamId);
            //删除审核表
            userMapper.delApply(userId,teamId,IsDelete.DELETE.getState());
            //删除邀请表
            userMapper.delInvite(userId,teamId,IsDelete.DELETE.getState());
            jsonObject.put(ModelMsg.MSG.getMsg(),"同意成功!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
        }catch (Exception e){
            log.warn("同意进团队错误!!");
            jsonObject.put(ModelMsg.MSG.getMsg(),"同意进团队发生错误!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }

        return jsonObject.toString();
    }


    /**
     *  该用户同意进入该团队
     * @param userId    用户id
     * @param teamId    团队id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CacheConstant.INVITE_LIST},key = "'inviteList=[userId='+#userId+']'")
    public String agree(Integer userId, Integer teamId) {
        try {
            //加入团队 授予roleID 和 roleName + teamId
            userMapper.joinTeam(userId,teamId, Roles.UserProjectMember.getRoleId(),Roles.UserProjectMember.getRoleName()+":"+teamId);
            userMapper.joinTeam(userId,teamId, Roles.UserTeamMember.getRoleId(),Roles.UserTeamMember.getRoleName()+":"+teamId);
            log.info("加入团队后的role={}",Roles.UserTeamMember.getRoleName()+":"+teamId);
            //团队人数+1
            userMapper.addMemberNums(teamId);
            //删除邀请表
            userMapper.delInvite(userId,teamId,IsDelete.DELETE.getState());
            //如果同时存在团队邀请 和 申请 继续删除 审核表
            userMapper.delApply(userId,teamId,IsDelete.DELETE.getState());
            log.info("成功进入团队!!");
            jsonObject.put(ModelMsg.MSG.getMsg(),"成功进入团队!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
        }catch (Exception e){
            log.warn("同意进团队错误!!");
            jsonObject.put(ModelMsg.MSG.getMsg(),"同意进团队发生错误!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }
        return jsonObject.toString();
    }

    /**
     * 根据用户id 团队id 返回该用户在这个团队的所有角色id
     * @param userId    用户id
     * @param teamId    团队id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.GET_ALL_TEAM_ROLE} ,key = "'getAllTeamRole=[userId'+#userId+'][teamId'+#teamId+']' ")
    public List<Integer> getAllTeamRole(Integer userId, Integer teamId) {
        return userMapper.getAllTeamRole(userId,teamId,IsDelete.NOTDELETE.getState());
    }


    /**
     * 根据团队名 查找到该团对所有项目
     * @param teamName  团队名
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.MY_PROJECT}, key = "'myProject=[teamName='+#teamName+']'")
    public List<FrontProject> findProjects(String teamName) {
        return userMapper.findProjects(teamName,IsDelete.NOTDELETE.getState());
    }


    /**
     * 根据项目id  团队名 删除该团队的这个项目
     * @param projectId 被删除的项目id
     * @param teamName  团队名
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CacheConstant.MY_PROJECT},key = "'myProject=[teamName='+#teamName+']'")
    public String delProject(Integer projectId,String teamName) {
        try {
            userMapper.delProject(projectId,IsDelete.DELETE.getState());
            log.info("删除项目成功!");
            jsonObject.put(ModelMsg.MSG.getMsg(),"删除成功");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
        }catch (Exception e){
            log.warn("删除项目失败!");
            jsonObject.put(ModelMsg.MSG.getMsg(),"删除项目失败!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }
        return jsonObject.toString();
    }


    /**
     * 根据团队名 查找到该团队所有团员信息
     * @param teamName 团队名
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.GET_USERS_FOR_TAG} ,key = "'getUsersForTag=[teamName=' + #teamName+']'")
    public List<UserTag> getUsersForTag(String teamName) {
        Integer teamId = userMapper.getTeamId(teamName,IsDelete.NOTDELETE.getState());
        List<UserTag> tags = projectMapper.getUsersForTag(teamId);
        return tags;
    }

    /**
     * 对该团队增加一个项目
     * @param teamName  团队名
     * @param projectName   项目名
     * @param projectMsg    项目信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CacheConstant.MY_PROJECT}, key = "'myProject=[teamName='+#teamName+']'")
    public String addTeamProject(String teamName, String projectName, String projectMsg) {
        try {
            Integer teamId = userMapper.getTeamId(teamName,IsDelete.NOTDELETE.getState());
            Integer projectId = projectMapper.checkProjectName(projectName);
            if(projectId != null){
                log.error("已经存在该项目名!! 请勿重复");
                jsonObject.put(ModelMsg.MSG.getMsg(),"已经存在该项目名!! 请勿重复");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.ERROR);
                return jsonObject.toString();
            }
            projectMapper.addTeamProject(teamId,projectName,projectMsg, ProjectArchive.NOTFINISH.getArchive());
            log.info("添加项目成功!!");
            jsonObject.put(ModelMsg.MSG.getMsg(),"添加项目成功!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
            jsonObject.put(ModelMsg.SUCCESS_URL.getMsg(),"/userC/toMyTeamMsg/" + teamName);
        }catch (Exception e){
            log.error("添加项目失败!!");
            jsonObject.put(ModelMsg.MSG.getMsg(),"添加项目失败!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }

        return jsonObject.toString();
    }



    /**
     * 分页显示 所有用户
     * @param page  当前页
     * @param size  每页数
     * @param userName  用户名
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_ALL_FRONT_USER} ,key = "'findAllFrontUser=[page='+#page+'][size='+#size+'][userName='+#userName+']'")
    public PageInfo<FrontUser> findAllFrontUser(int page, int size, String userName) {
        PageHelper.startPage(page,size);
        List<FrontUser> allFrontUser = userMapper.findAllFrontUser(userName,IsDelete.NOTDELETE.getState());
        return new PageInfo<>(allFrontUser);
    }

    /**
     * 邀请用户进入团队
     * @param userId    用户id
     * @param teamName  团队名
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CacheConstant.USER_MSG},key = "'userMsg=[userId='+#userId+']'")
    public String invite(Integer userId, String teamName) {
        try{
            Integer teamId = teamMapper.findTeamId(teamName, IsDelete.NOTDELETE.getState());
            String s = userMapper.checkUserInInvite(userId, teamId,IsDelete.NOTDELETE.getState());
            List<Integer> teamUserId = teamMapper.getTeamUserId(teamId,IsDelete.NOTDELETE.getState());
            boolean contains = teamUserId.contains(userId);
            if(contains){
                log.error("错误 该用户已在此团队!");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.ERROR);
                jsonObject.put(ModelMsg.MSG.getMsg(),"错误 该用户已在此团队!");
            }else if(s == null){
                teamMapper.teamInvite(teamId,userId);
                log.info("邀请成功");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
                jsonObject.put(ModelMsg.MSG.getMsg(),"邀请成功");
            }
        }catch (Exception e){
            log.error("邀请发生错误!");
            jsonObject.put(ModelMsg.MSG.getMsg(),"发生错误!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }

        return jsonObject.toString();
    }


    /**
     * 根据用户id  返回用户信息
     * @param userId    团队id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.USER_MSG},key = "'userMsg=[userId='+#userId+']'")
    public FrontUser getUserMsg(Integer userId) {
        FrontUser userMsg = userMapper.getUserMsg(userId,IsDelete.NOTDELETE.getState());
        return userMsg;
    }

    /**
     *  检查该用户是否在该团队邀请列表内
     * @param userId    用户id
     * @param teamName  团队名
     * @return
     */
    @Override
    public Boolean checkUserInInvite(Integer userId, String teamName) {
        Integer teamId = teamMapper.findTeamId(teamName,IsDelete.NOTDELETE.getState());
        String s = userMapper.checkUserInInvite(userId, teamId,IsDelete.NOTDELETE.getState());
        return s == null ? true : false;
    }

    /**
     * 根据用户id  查找该用户所有邀请请求
     * @param userId
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.INVITE_LIST},key = "'inviteList=[userId='+#userId+']'")
    public List<InviteUser> getUserInvite(Integer userId) {

        return userMapper.getUserInvite(userId,IsDelete.NOTDELETE.getState());
    }


    /**
     * 对该用户授予项目管理员角色
     * @param userId    用户id
     * @param teamName  团队名
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CacheConstant.MY_TEAM_MEMBER} ,key = "'myTeamMember=[teamName='+#teamName+']'")
    public String awardManager(Integer userId, String  teamName){
        try{
            Integer teamId = teamService.getTeamId(teamName);
            //判断 该用户所在团队 是不是项目管理员
            Integer roleId = teamService.checkManager(userId, teamId,Roles.UserProjectManager.getRoleId());
            if(null != roleId && roleId.equals(Roles.UserProjectManager.getRoleId())){
                log.info("该成员已经是项目管理员!");
                jsonObject.put(ModelMsg.MSG.getMsg(),"该成员已经是项目管理员!");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
                return jsonObject.toString();
            }
            //对用户所在团队 的团员职位进行授权为项目管理员
            teamMapper.awardManager(userId,teamId,Roles.UserTeamMember.getRoleId(),Roles.UserProjectManager.getRoleId(),Roles.UserProjectManager.getRoleName()+":"+teamId);
            log.info("授予项目管理员成功!!");
            jsonObject.put(ModelMsg.MSG.getMsg(),"授予项目管理员成功!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
        }catch (Exception e){
            log.error("授予项目管理员失败!! {}",e.getMessage());
            jsonObject.put(ModelMsg.MSG.getMsg(),"授予项目管理员失败!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }
        return jsonObject.toString();

    }


    /**
     * 根据用户id 返回该用户名
     * @param userId    用户id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.USER_NAME},key = "'userName=[userId='+#userId+']'")
    public String getUserName(Integer userId){
        return userMapper.getUserName(userId,IsDelete.NOTDELETE.getState());
    }
}
