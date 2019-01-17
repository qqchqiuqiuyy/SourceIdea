package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.front.FrontTeam;
import cn.bb.sourceideamanage.dto.front.MyTeamMember;
import cn.bb.sourceideamanage.dto.front.TeamMember;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Project;
import cn.bb.sourceideamanage.entity.User;
import cn.bb.sourceideamanage.entity.UserTeam;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeamService {
    public List<Project> findAllProject( Integer teamId);
    public List<Idea> findAllIdea( Integer ideaId);
    public List<User> findAllMembers( Integer team_id);
    public List<UserTeam> findSearchTeam( String teamName);
    public PageInfo<FrontTeam> findAllFrontTeam(int page, int size, String teamName);
    public BackTeam findAllTeamMember( String teamName);
    public String joinTeam(Integer userId,Integer teamId);
    public String checkTeamMember(Integer userId, Integer teamId);
    public List<String> findAllTeam(Integer userId);
    public PageInfo<FrontTeam> findAllMyTeam(int page, int size, String teamName,Integer userId);
    public List<MyTeamMember> findAllMyTeamMember(String teamName);
    public List<String> findTeamRoleByTeamNameAndUserId(String teamName,Integer userId);
    public Integer getTeamId(String teamName);
    public String addTeam(String teamName,String teamMsg,Integer userId);
    public String getTeamMsg(Integer teamId);
    public List<TeamMember> findAllMemberByTeamId( Integer teamId);
}
