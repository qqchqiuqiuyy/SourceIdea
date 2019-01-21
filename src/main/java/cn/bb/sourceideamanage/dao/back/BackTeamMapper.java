package cn.bb.sourceideamanage.dao.back;

import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.back.BackTeamMember;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Project;
import cn.bb.sourceideamanage.entity.Team;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface BackTeamMapper {


    @Select("  SELECT " +
            "p.project_id , " +
            "p.team_id, " +
            "p.project_name,    " +
            "p.project_msg, " +
            "p.project_archive, " +
            "p.project_create_time  " +
            "FROM   " +
            "project p  " +
            "WHERE  " +
            "team_id = #{teamId} ")
    public List<Project> findAllProjectByTeamId(Integer teamId);

    @Select("SELECT " +
            " r.role_msg AS teamRoleName, " +
            " ut.member_join_time AS memberJoinTime, " +
            " u.user_name AS userName  " +
            "FROM " +
            " role r, " +
            " USER u, " +
            " user_team ut  " +
            "WHERE " +
            " r.role_id = ut.role_id  " +
            " AND u.user_id = ut.user_id  " +
            " AND ut.team_id = #{tId2} " +
            "AND r.role_id IN ( SELECT max( role_id ) FROM user_team WHERE user_id = ut.user_id )")
    public List<BackTeamMember> findAllMemberByTeamId(@Param("tId2") Integer teamId);


    @Select("  SELECT " +
            "   i.idea_id AS ideaId, " +
            "   i.team_id AS teamId, " +
            "   i.tag_id AS tagId," +
            "   i.user_id AS userId," +
            "   i.idea_name AS ideaName," +
            "   i.idea_msg AS ideaMsg," +
            "   i.idea_supports AS ideaSupports," +
            "   i.idea_create_time AS ideaCreateTime" +
            "   FROM" +
            "   idea i" +
            "   WHERE" +
            "   i.team_id = #{tId3}")
    public List<Idea> findAllTeamIdea(@Param("tId3") Integer teamId);

    /**
     * 根据团队Id找队长
     * @param teamId
     * @return String
     */
    @Select("select distinct u.user_name " +
            "from role r, user_team ut ,user u" +
            " where ut.team_id = #{teamId} AND ut.role_id = '5' AND ut.user_id = u.user_id")
    public String findTeamCaptainByTeamId(Integer teamId);


    /**
     * 一对多
     * @param teamName
     * @return
     */
    @Select("select   " +
            "   t.team_name As teamName,  t.team_id As teamId,t.team_id As tId ,t.team_id As tId2 ,t.team_id As tId3 ,t.team_id As tId4 , " +
            "    ut.user_id As uId  , t.team_nums As teamNums , t.team_create_time " +
            " FROM  " +
            "       user_team ut , team t  ,role r " +
            " WHERE " +
            "       t.team_name LIKE CONCAT('%',#{teamName},'%') AND ut.team_id = t.team_id AND ut.role_id = r.role_id " +
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
    public List<BackTeam> findAllBackTeamMember(@Param("teamName") String teamName);
}
