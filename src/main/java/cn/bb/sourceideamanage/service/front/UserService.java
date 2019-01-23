package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.dto.front.*;
import cn.bb.sourceideamanage.entity.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author bobo
 */
public interface UserService {
    /**
     * 根据用户id查找该用户所有信息
     * @param userId 用户id
     * @return
     */
    public User findUserById(Integer userId);

    /**
     * 根据用户id 查找该用户所有角色
     * @param userId 用户id
     * @return
     */
    public List<String> findUserAllRoleByUserId(Integer userId);

    /**
     * 根据用户id 返回该用户所有操作
     * @param userId 用户id
     * @return
     */
    public List<String> findUserAllPermissionByUserId(Integer userId);

    /**
     * 根据用户id 查找该用户所有团队
     * @param userId 用户id
     * @return
     */
    public List<UserTeam> findUserAllTeam(Integer userId);

    /**
     * 根据用户id 查找该用户所有想法
     * @param userId 用户id
     * @return
     */
    public List<Idea> findUserAllIdea(Integer userId);


    /**
     * 根据用户id查找该用户所有评论
     * @param userId    用户id
     * @return
     */
    public List<CommentIdea> findUserAllCommentIdea(Integer userId);


    /**
     * 根据账号  查找该用户信息
     * @param account   账号
     * @return
     */
    public User findUserByAccount( String account);

    /**
     * 根据用户id  团队名 删除该成员
     * @param userId    被删除用户id
     * @param teamName  团队名
     * @return
     */
    public String delMember(Integer userId , String teamName);

    /**
     * 根据团队名  查找申请该团队的所有用户信息
     * @param teamName  团队名
     * @return
     */
    public List<ApplyUser> getAllAppy(String teamName);

    /**
     * 根据用户id 团队名 同意该用户进入团队
     * @param userId    被同意用户id
     * @param teamName  团队名
     * @return
     */
    public String agreeMember(Integer userId,String teamName);

    /**
     * 根据团队名 查找到该团对所有项目
     * @param teamName  团队名
     * @return
     */
    public List<FrontProject> findProjects(String teamName);


    /**
     * 根据项目id  团队名 删除该团队的这个项目
     * @param projectId 被删除的项目id
     * @param teamName  团队名
     * @return
     */
    public String delProject(Integer projectId,String teamName);


    /**
     * 根据团队名 查找到该团队所有团员信息
     * @param teamName 团队名
     * @return
     */
    public List<UserTag> getUsersForTag(String teamName );


    /**
     * 对该团队增加一个项目
     * @param teamName  团队名
     * @param projectName   项目名
     * @param projectMsg    项目信息
     * @return
     */
    public String addTeamProject(String teamName,String projectName,String projectMsg);

    /**
     * 分页显示 所有用户
     * @param page  当前页
     * @param size  每页数
     * @param userName  用户名
     * @return
     */
    public PageInfo<FrontUser> findAllFrontUser(int page , int size , String userName);

    /**
     * 邀请用户进入团队
     * @param userId    用户id
     * @param teamName  团队名
     * @return
     */
    public String invite(Integer userId,String teamName);


    /**
     * 根据用户id  返回用户信息
     * @param userId    团队id
     * @return
     */
    public FrontUser getUserMsg(Integer userId);

    /**
     * 检查该用户是否在该团队邀请列表内
     * @param userId    用户id
     * @param teamName  团队名
     * @return
     */
    public Boolean checkUserInInvite(Integer userId,String teamName);

    /**
     * 根据用户id  查找该用户所有邀请请求
     * @param userId
     * @return
     */
    public List<InviteUser> getUserInvite(Integer userId);

    /**
     * 该用户同意进入该团队
     * @param userId    用户id
     * @param teamId    团队id
     * @return
     */
    public String agree(Integer userId,Integer teamId);

    /**
     * 根据用户id 团队id 返回该用户在这个团队的所有角色id
     * @param userId    用户id
     * @param teamId    团队id
     * @return
     */
    public List<Integer> getAllTeamRole(Integer userId,Integer teamId);

    /**
     * 对该用户授予项目管理员角色
     * @param userId    用户id
     * @param teamName  团队名
     * @return
     */
    public String awardManager(Integer userId,  String  teamName);

    /**
     * 根据用户id 返回该用户名
     * @param userId    用户id
     * @return
     */
    public String getUserName(Integer userId);
}
