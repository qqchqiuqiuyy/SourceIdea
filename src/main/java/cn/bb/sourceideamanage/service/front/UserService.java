package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.dto.front.*;
import cn.bb.sourceideamanage.entity.*;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {
    public User findUserById(Integer userId);
    public List<String> findUserAllRoleByUserId(Integer userId);
    public List<String> findUserAllPermissionByUserId(Integer userId);
    public List<UserTeam> findUserAllTeam(Integer userId);
    public List<Idea> findUserAllIdea(Integer userId);
    public List<commentIdea> findUserAllCommentIdea(Integer userId);
    public User findUserByAccount( String account);
    public String delMember(Integer userId , String teamName);
    public List<ApplyUser> getAllAppy(String teamName);
    public String agreeMember(Integer userId,String teamName);
    public List<FrontProject> findProjects(String teamName);
    public String delProject(Integer projectId);
    public List<UserTag> getUsersForTag(String teamName );
    public String addTeamProject(String teamName,String projectName,String projectMsg);
    public PageInfo<frontUser> findAllFrontUser(int page , int size , String userName);
    public String invite(Integer userId,String teamName);
    public frontUser getUserMsg(Integer userId);
    public Boolean checkUserInInvite(Integer userId,String teamName);
    public List<inviteUser> getUserInvite(Integer userId);
    public String agree(Integer userId,Integer teamId);
    public List<Integer> getAllTeamRole(Integer userId,Integer teamId);
    public String awardManager(Integer userId,  String  teamName);
}
