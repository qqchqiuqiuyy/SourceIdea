package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.dto.front.*;
import cn.bb.sourceideamanage.entity.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {
    public User findUserById(Integer userId);
    public List<String> findUserAllRoleByUserId(Integer userId);
    public List<String> findUserAllPermissionByUserId(Integer userId);
    public List<UserTeam> findUserAllTeam(Integer userId);
    public List<Idea> findUserAllIdea(Integer userId);
    public List<CommentIdea> findUserAllCommentIdea(Integer userId);
    public User findUserByAccount( String account);
    public String delMember(Integer userId , String teamName);
    public List<ApplyUser> getAllAppy(String teamName);
    public String agreeMember(Integer userId,String teamName);
    public List<FrontProject> findProjects(String teamName);
    public String delProject(Integer projectId);
    public List<UserTag> getUsersForTag(String teamName );
    public String addTeamProject(String teamName,String projectName,String projectMsg);
    public PageInfo<FrontUser> findAllFrontUser(int page , int size , String userName);
    public String invite(Integer userId,String teamName);
    public FrontUser getUserMsg(Integer userId);
    public Boolean checkUserInInvite(Integer userId,String teamName);
    public List<InviteUser> getUserInvite(Integer userId);
    public String agree(Integer userId,Integer teamId);
    public List<Integer> getAllTeamRole(Integer userId,Integer teamId);
    public String awardManager(Integer userId,  String  teamName);
    public String getUserName(Integer userId);
}
