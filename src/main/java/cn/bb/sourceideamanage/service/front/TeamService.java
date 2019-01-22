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

public interface TeamService {
    /**
     * 根据团队id查找该团队所有项目
     * @param teamId
     * @return
     */
    public List<Project> findAllProject( Integer teamId);

    /**
     * 根据想法id 查找所有
     * @param ideaId
     * @return
     */
    public List<Idea> findAllIdea( Integer ideaId);

    /**
     * 根据团队id查找该团队所有成员
     * @param teamId
     * @return
     */
    public List<User> findAllMembers( Integer teamId);

    /**
     * 根据团名查找
     * @param teamName
     * @return
     */
    public List<UserTeam> findSearchTeam( String teamName);

    /**
     * 分页,
     * @param page
     * @param size
     * @param teamName
     * @return
     */
    public PageInfo<FrontTeam> findAllFrontTeam(int page, int size, String teamName);

    /**
     * 根据团队名查找团队成员
     * @param teamName
     * @return
     */
    public BackTeam findAllTeamMember( String teamName);

    /**
     * 加入团队,
     * @param userId
     * @param teamId
     * @return
     */
    public String joinTeam(Integer userId,Integer teamId);

    /**
     * 根据用户id 和团队id 检查是否在该团队
     * @param userId
     * @param teamId
     * @return
     */
    public String checkTeamMember(Integer userId, Integer teamId);

    /**
     * 根据用户id查找该用户所有团队
     * @param userId
     * @return
     */
    public List<String> findAllTeam(Integer userId);

    /**
     * 分页
     * @param page
     * @param size
     * @param teamName
     * @param userId
     * @return
     */
    public PageInfo<FrontTeam> findAllMyTeam(int page, int size, String teamName,Integer userId);


    /**
     * 根据团队名查找我的团队所有团员
     * @param teamName
     * @return
     */
    public List<MyTeamMember> findAllMyTeamMember(String teamName);

    /**
     * 通过团队名和userId 查找团队角色
     * @param teamName
     * @param userId
     * @return
     */
    public List<String> findTeamRoleByTeamNameAndUserId(String teamName,Integer userId);

    /**
     * 根据团队名 查找团队Id
     * @param teamName
     * @return
     */
    public Integer getTeamId(String teamName);

    /**
     * 增加团队
     * @param teamName
     * @param teamMsg
     * @param userId
     * @return
     */
    public String addTeam(String teamName,String teamMsg,Integer userId);

    /**
     * 根据团队id 得到团队信息
     * @param teamId
     * @return
     */
    public String getTeamMsg(Integer teamId);

    /**
     * 根据团队id 查找所有的团队队员
     * @param teamId
     * @return
     */
    public List<TeamMember> findAllMemberByTeamId( Integer teamId);

    /**
     * 根据团队id 查找该团队所有想法
     * @param teamId
     * @return
     */
    public List<Idea> findAllTeamIdeas(Integer teamId);

    /**
     * 根据团队名查找团队信息
     * @param teamName
     * @return
     */
    public TeamMsg findTeamMsg(String teamName);

    /**
     * 根据userId  团队Id 判断该user是否是团队管理员
     * @param userId
     * @param teamId
     * @param roleId
     * @return
     */
    public Integer checkManager( Integer userId, Integer teamId,Integer roleId);
}
