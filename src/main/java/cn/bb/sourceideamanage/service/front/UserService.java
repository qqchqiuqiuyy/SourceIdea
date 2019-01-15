package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.dto.front.ApplyUser;
import cn.bb.sourceideamanage.dto.front.FrontProject;
import cn.bb.sourceideamanage.entity.*;
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
}
