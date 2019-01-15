package cn.bb.sourceideamanage.dao.front;

import cn.bb.sourceideamanage.dto.front.ApplyUser;
import cn.bb.sourceideamanage.dto.front.FrontProject;
import cn.bb.sourceideamanage.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user where user_id = #{userId}")
    public User findUserById(@Param("userId") Integer userId);

    @Select("select role_name from role where role_id IN" +
            "(select role_id from  user_role ur where user_id = #{userId}" +
            "   UNION " +
            "  select ut.role_id from user_team ut where ut.user_id = #{userId} )"
    )
    public List<String> findUserAllRoleByUserId(@Param("userId") Integer userId);


    @Select("select permission_name from permission where role_id IN" +
            "(select role_id from  user_role where user_id = #{userId} )")
    public List<String> findUserAllPermissionByUserId(@Param("userId") Integer userId);

    @Select("select * from user_team where team_id IN" +
            "(select team_id from user_team where user_id = #{userId})")
    public List<UserTeam> findUserAllTeam(@Param("userId") Integer userId);


    @Select("select * from idea where user_id = #{userId}")
    public List<Idea> findUserAllIdea(@Param("userId") Integer userId);

    @Select("select * from commentIdea where user_id = #{userId}")
    public List<commentIdea> findUserAllCommentIdea(@Param("userId") Integer userId);


    @Select("select * from user where user_account=#{account}")
    public User findUserByAccount(@Param("account") String account);


    @Delete(" DELETE " +
            "FROM " +
            " user_team " +
            " WHERE" +
            " user_id = #{userId} " +
            " AND team_id IN ( SELECT t.team_id FROM team t WHERE t.team_name = #{teamName} )")
    public void delMember(@Param("userId") Integer userId ,@Param("teamName") String teamName);


    @Select("SELECT a.team_id AS teamId, a.user_id AS userId ,a.apply_time AS applyTime ,u.user_name AS userName" +
            "   FROM apply a, team t,user u" +
            "   WHERE a.team_id = t.team_id AND u.user_id = a.user_id AND t.team_name = #{teamName}")
    public List<ApplyUser> getAllAppy(@Param("teamName") String teamName);


    @Select("SELECT t.team_id " +
            " FROM" +
            "  team t" +
            " WHERE t.team_name = #{teamName}")
    public Integer getTeamId(@Param("teamName") String  teamName);

    @Delete("DELETE FROM apply  WHERE user_id = #{userId} AND team_id = #{teamId}")
    public void delApply(@Param("userId") Integer userId,@Param("teamId") Integer teamId);

    @Insert("INSERT INTO user_team (team_id,role_id,user_id) " +
            "   VALUES (#{teamId}, '6' , #{userId})")
    public void joinTeam(@Param("userId") Integer userId,@Param("teamId") Integer teamId);

    @Select("SELECT p.project_id AS projectId," +
            "           p.project_name AS projectName," +
            "           p.project_create_time AS projectCreateTime " +
            "   FROM team t,project p" +
            "   WHERE t.team_name = #{teamName} AND" +
            "   t.team_id = p.team_id")
    public List<FrontProject> findProjects(@Param("teamName") String teamName);

    @Delete("DELETE FROM project WHERE project_id = #{projectId}")
    public void delProject(@Param("projectId") Integer projectId);
}

