package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.entity.*;

import java.util.List;

public interface UserService {
    public User findUserById(Integer userId);
    public List<String> findUserAllRoleByUserId(Integer userId);
    public List<String> findUserAllPermissionByUserId(Integer userId);
    public List<UserTeam> findUserAllTeam(Integer userId);
    public List<Idea> findUserAllIdea(Integer userId);
    public List<commentIdea> findUserAllCommentIdea(Integer userId);
    public User findUserByAccount( String account);
}
