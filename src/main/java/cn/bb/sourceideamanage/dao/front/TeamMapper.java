package cn.bb.sourceideamanage.dao.front;

import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.back.BackTeamMember;
import cn.bb.sourceideamanage.dto.front.FrontTeam;
import cn.bb.sourceideamanage.dto.front.MyTeamMember;
import cn.bb.sourceideamanage.entity.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface TeamMapper {

    @Select("select * from project where team_id = #{teamId}")
    public List<Project> findAllProject(@Param("teamId") Integer teamId);

    @Select("select * from idea where team_id = #{teamId}")
    public List<Idea> findAllIdea(@Param("teamId") Integer team_id);

    @Select("select * from user_team ut, user u where " +
            "ut.user_id = u.user_id AND ut.team_id = #{teamId}")
    public List<User> findAllMembers(@Param("teamId") Integer team_id);

    @Select("select * from user_team where team_id IN " +
            " (select team_id from team where team_name LIKE CONCAT('%',#{teamName},'%')) ")
    public List<UserTeam> findSearchTeam(@Param("teamName") String teamName);

    @Select("select distinct t.team_id AS teamId, t.team_name AS teamName,t.team_create_time AS teamCreateTime, " +
            "  u.user_name AS teamCaptain" +
            " from team t,user_team ut , role r , user u " +
            " where t.team_id = ut.team_id AND ut.role_id = '4' AND ut.user_id = u.user_id AND t.team_name LIKE CONCAT('%',#{teamName},'%') ")
    public List<FrontTeam> findAllFrontTeam(@Param("teamName") String teamName);




    @Select("select distinct * from project where team_id = #{teamId}")
    public List<Project> findAllProjectByTeamId(Integer teamId);

    @Select("select distinct r.role_msg As teamRoleName, " +
            " ut.member_join_time As memberJoinTime,u.user_name As userName" +
            " from role r, user u,user_team ut " +
            " where r.role_id = ut.role_id  AND u.user_id = ut.user_id AND ut.team_id = #{tId2} AND r.role_id != '5'")
    public List<BackTeamMember> findAllMemberByTeamId(@Param("tId2") Integer teamId);


    @Select("select distinct * from idea where team_id = #{teamId}")
    public List<Idea> findAllTeamIdea(@Param("tId3") Integer teamId);

    /**
     * 根据团队Id找队长
     * @param teamId
     * @return String
     */
    @Select("select distinct u.user_name " +
            "from role r, user_team ut ,user u" +
            " where ut.team_id = #{teamId} AND ut.role_id = '4' AND ut.user_id = u.user_id")
    public String findTeamCaptainByTeamId(Integer teamId);


    /**
     * 一对多
     * @param teamName
     * @return
     */
    @Select("select   " +
            "   t.team_name As teamName,  t.team_id As teamId,t.team_id As tId ,t.team_id As tId2 ,t.team_id As tId3 ,t.team_id As tId4 , " +
            "    ut.user_id As uId  , t.team_nums As teamNums , t.team_create_time , t.team_msg AS teamMsg" +
            " FROM  " +
            "       user_team ut , team t  ,role r " +
            " WHERE " +
            "       t.team_name = #{teamName} AND ut.team_id = t.team_id AND ut.role_id = r.role_id " +
            " GROUP BY teamId ")
    @Results({
            //colums是数据库列,以这个id为一组 , property是back_user对象里面的属性
            @Result(column = "tId", javaType = List.class, property = "teamProjects",
                    many = @Many(   //一对多
                            select = "cn.bb.sourceideamanage.dao.back.BackTeamMapper.findAllProjectByTeamId", //上面搜索的方法
                            fetchType = FetchType.LAZY
                    )),
            @Result(column = "tId2", javaType = List.class, property = "backTeamMembers",
                    many = @Many(   //一对多
                            select = "cn.bb.sourceideamanage.dao.back.BackTeamMapper.findAllMemberByTeamId", //上面搜索的方法
                            fetchType = FetchType.LAZY
                    )),
            @Result(column = "tId3", javaType = List.class, property = "teamIdeas",
                    many = @Many(   //一对多
                            select = "cn.bb.sourceideamanage.dao.back.BackTeamMapper.findAllTeamIdea", //上面搜索的方法
                            fetchType = FetchType.LAZY
                    )),
            @Result(column = "tId4", javaType = String.class, property = "teamCaptain",
                    one = @One(   //一对一
                            select = "cn.bb.sourceideamanage.dao.back.BackTeamMapper.findTeamCaptainByTeamId", //上面搜索的方法
                            fetchType = FetchType.LAZY
                    )),

    })
    public BackTeam findAllTeamMember(@Param("teamName") String teamName);

    @Insert("INSERT INTO user_team (team_id,role_id,user_id) " +
            "   VALUES" +
            "   (#{teamId} , '6',#{userId})")
    public void joinTeam(@Param("userId") Integer userId,@Param("teamId") Integer teamId);

    @Select("SELECT role_id FROM user_team ut WHERE ut.user_id = #{userId} AND ut.team_id = #{teamId}")
    public String checkTeamMember(@Param("userId") Integer userId,@Param("teamId") Integer teamId);

    @Insert("INSERT INTO apply (user_id,team_id) VALUES (#{userId},#{teamId})")
    public void apply(@Param("userId") Integer userId,@Param("teamId") Integer teamId);

    @Select("select team_id from apply where user_id = #{userId} AND team_id = #{teamId}")
    public String checkApply(@Param("userId") Integer userId,@Param("teamId") Integer teamId);

    @Select("select t.team_name" +
            "   FROM user u,user_team ut, team t" +
            "   WHERE u.user_id = #{userId} AND" +
            "         u.user_id = ut.user_id AND" +
            "           ut.team_id = t.team_id")
    public List<String> findAllTeam(@Param("userId") Integer userId);




    @Select("SELECT DISTINCT " +
            " t.team_id AS teamId, " +
            " t.team_name AS teamName, " +
            " t.team_create_time AS teamCreateTime, " +
            " u.user_name AS teamCaptain  " +
            "FROM " +
            " team t, " +
            " user_team ut, " +
            " role r, " +
            " user u  " +
            "WHERE " +
            " t.team_id = ut.team_id  " +
            " AND ut.role_id = '4'  " +
            " AND ut.role_id = r.role_id AND  t.team_name LIKE CONCAT( '%',#{teamName}, '%' ) " +
            " AND u.user_id = ut.user_id " +
            " AND ut.team_id IN ( SELECT ut.team_id FROM user_team ut WHERE ut.user_id = #{userId} )")
    public List<FrontTeam> findAllMyTeam(@Param("teamName") String teamName,@Param("userId") Integer userId);

    @Select("SELECT u.user_id AS userId,u.user_name AS userName ,r.role_msg AS teamRole,ut.member_join_time AS memberJoinTime" +
            " FROM role r,user u,user_team ut,team t " +
            " WHERE  t.team_name = #{teamName} AND t.team_id = ut.team_id AND" +
            "       ut.user_id = u.user_id AND ut.role_id = r.role_id")
    public List<MyTeamMember> findAllMyTeamMember(@Param("teamName") String teamName);

    @Select("select r.role_name " +
            "   FROM team t,user_team ut, role r" +
            "   where t.team_name = #{teamName} AND ut.user_id = #{userId} AND t.team_id = ut.team_id AND" +
            "         ut.role_id = r.role_id  ")
    public List<String> findTeamRoleByTeamNameAndUserId(@Param("teamName") String teamName,@Param("userId") Integer userId);

    @Select("select t.team_id from team t where t.team_name = #{teamName}")
    public Integer findTeamId(@Param("teamName") String teamName);
}
