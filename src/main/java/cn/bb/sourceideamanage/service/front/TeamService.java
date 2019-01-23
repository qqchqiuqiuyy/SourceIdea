package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.front.FrontTeam;
import cn.bb.sourceideamanage.dto.front.MyTeamMember;
import cn.bb.sourceideamanage.dto.front.TeamMember;
import cn.bb.sourceideamanage.dto.front.TeamMsg;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Project;
import cn.bb.sourceideamanage.entity.User;
import cn.bb.sourceideamanage.entity.UserTeam;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author bobo
 */
public interface TeamService {
    /**
     * 根据团队id查找该团队所有项目
     * @param teamId   团队id
     * @return
     */
    public List<Project> findAllProject( Integer teamId);

    /**
     * 根据想法id 查找所有
     * @param ideaId    想法id
     * @return
     */
    public List<Idea> findAllIdea( Integer ideaId);

    /**
     * 根据团队id查找该团队所有成员
     * @param teamId    团队id
     * @return
     */
    public List<User> findAllMembers( Integer teamId);

    /**
     * 根据团名查找
     * @param teamName  团队名
     * @return
     */
    public List<UserTeam> findSearchTeam( String teamName);

    /**
     * 根据以下参数返回 团队模块前台分页
     * @param page  当前页
     * @param size  每页长度
     * @param teamName  团队名
     * @return
     */
    public PageInfo<FrontTeam> findAllFrontTeam(int page, int size, String teamName);

    /**
     * 根据团队名查找团队成员
     * @param teamName 团队名
     * @return
     */
    public BackTeam findAllTeamMember( String teamName);

    /**
     * 加入团队,
     * @param userId    用户id
     * @param teamId    需要加入的团队
     * @return
     */
    public String joinTeam(Integer userId,Integer teamId);

    /**
     * 根据用户id 和团队id 检查是否在该团队
     * @param userId    用户id
     * @param teamId    团队id
     * @return
     */
    public String checkTeamMember(Integer userId, Integer teamId);

    /**
     * 根据用户id查找该用户所有团队
     * @param userId    用户id
     * @return
     */
    public List<String> findAllTeam(Integer userId);

    /**
     * 通过以下参数 返回我的团队分页信息
     * @param page  当前页
     * @param size  每页长度
     * @param teamName  团队名
     * @param userId    当前用户id
     * @return
     */
    public PageInfo<FrontTeam> findAllMyTeam(int page, int size, String teamName,Integer userId);


    /**
     * 根据团队名查找我的团队所有团员
     * @param teamName  团队名
     * @return
     */
    public List<MyTeamMember> findAllMyTeamMember(String teamName);

    /**
     * 通过团队名和userId 查找团队角色
     * @param teamName  团队名
     * @param userId    用户id
     * @return
     */
    public List<String> findTeamRoleByTeamNameAndUserId(String teamName,Integer userId);

    /**
     * 根据团队名 查找团队Id
     * @param teamName  团队名
     * @return
     */
    public Integer getTeamId(String teamName);

    /**
     * 新增一个团队
     * @param teamName  团队名
     * @param teamMsg   团队信息
     * @param userId    当前用户id
     * @return
     */
    public String addTeam(String teamName,String teamMsg,Integer userId);


    /**
     * 根据团队id 查找所有的团队队员
     * @param teamId    团队id
     * @return
     */
    public List<TeamMember> findAllMemberByTeamId( Integer teamId);

    /**
     * 根据团队id 查找该团队所有想法
     * @param teamId 团队id
     * @return
     */
    public List<Idea> findAllTeamIdeas(Integer teamId);

    /**
     * 根据团队名查找团队信息
     * @param teamName 团队名
     * @return
     */
    public TeamMsg findTeamMsg(String teamName);

    /**
     * 根据userId  团队Id 判断该user是否是团队管理员
     * @param userId    用户id
     * @param teamId    团队id
     * @param roleId    角色id
     * @return
     */
    public Integer checkManager( Integer userId, Integer teamId,Integer roleId);
}
