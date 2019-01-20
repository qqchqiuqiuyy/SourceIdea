package cn.bb.sourceideamanage.dao.front;

import cn.bb.sourceideamanage.dto.front.FrontProject;
import cn.bb.sourceideamanage.dto.front.FrontProjectMsg;
import cn.bb.sourceideamanage.dto.front.ProjectMember;
import cn.bb.sourceideamanage.dto.front.UserTag;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProjectMapper {
    @Select("SELECT p.project_id AS projectId, t.team_name AS projectTeam , " +
            "   p.project_create_time AS projectCreateTime ," +
            "   u.user_name AS projectManager ," +
            "   p.project_name AS projectName" +
            " FROM project p,team t,user_team ut, user u" +
            "   WHERE p.project_name LIKE CONCAT('%',#{projectName},'%') AND" +
            "   p.team_id = t.team_id AND" +
            "   t.team_id = ut.team_id AND" +
            "   ut.role_id = #{teamManager} AND" +
            "   ut.user_id = u.user_id" +
            "   GROUP BY projectName DESC")
    public List<FrontProject> findAllFrontProject(@Param("projectName") String projectName,
                                                  @Param("teamManager") Integer teamManager);

    /**
     * 一对多查找projectMsg
     * @param userId
     * @param roleId
     * @return
     */
    @Select("SELECT " +
            "u.user_name AS userName, " +
            "r.role_msg AS projectRole  " +
            "FROM " +
            "user u, " +
            "role r, " +
            "user_role ur, " +
            "user_team ut " +
            "WHERE " +
            "ut.team_id = #{teamId} AND " +
            "ut.role_id = r.role_id AND " +
            "ut.user_id = u.user_id  " +
            "GROUP BY user_name")
    public List<ProjectMember> getProjectMembers(@Param("teamId") Integer teamId);

    @Select("SELECT  p.project_id AS projectId,p.project_name AS projectName ," +
            "   p.project_msg AS projectMsg ,t.team_name AS teamName , " +
            "   p.project_create_time AS projectCreateTime, " +
            "   t.team_id AS teamId " +
            " FROM project p, team t"  +
            " WHERE p.project_id = #{projectId} AND p.team_id = t.team_id  " +
            "   GROUP BY projectName "
           )
    public FrontProjectMsg getProjectMsgByProjectId(@Param("projectId") Integer projectId);


    @Select("select team_id from project where project_id = #{projectId}")
    public Integer getTeamId(@Param("projectId") Integer projectId);

    @Select("SELECT p.project_name " +
            "FROM project p " +
            "where p.team_id IN (select ut.team_id from user_team ut where ut.user_id = #{userId})")
    public List<String> getAllProjects(@Param("userId") Integer userId);


    @Select("SELECT p.project_id AS projectId, t.team_name AS projectTeam , " +
            "              p.project_create_time AS projectCreateTime ," +
            "            u.user_name AS projectManager  ," +
            "              p.project_name AS projectName " +
            "           FROM project p,team t,user_team ut, user u, role r" +
            "             WHERE p.project_name LIKE CONCAT('%',#{projectName},'%') AND " +
            "               ut.user_id = #{userId} AND " +
            "               ut.team_id = t.team_id AND " +
            "               t.team_id = p.team_id AND " +
            "                  ut.role_id = r.role_id AND" +
            "                   u.user_id = ut.user_id " +
            "           GROUP BY projectName")
    public List<FrontProject> findAllMyProject(@Param("projectName") String projectName,@Param("userId") Integer userId);

    @Select("SELECT  u.user_id ,u.user_name" +
            "   from user_team ut,user u" +
            "   where ut.team_id = #{teamId} AND ut.user_id = u.user_id ")
    public List<UserTag> getUsersForTag(@Param("teamId") Integer teamId);

    @Insert("INSERT INTO project ( team_id, project_msg, project_archive, project_name ) " +
            " VALUES " +
            " ( #{teamId}, #{projectMsg},#{archive} ,#{projectName})")
    public void addTeamProject(@Param("teamId") Integer teamId, @Param("projectName") String projectName,
                               @Param("projectMsg") String projectMsg,@Param("archive") Integer archive);

    @Select("SELECT id FROM user_team WHERE team_id = #{teamId} AND user_id = #{userId}")
    public Integer checkProjectManager(@Param("teamId") Integer teamId,@Param("userId") Integer userId);

    @Select("INSERT INTO user_team (team_id,role_id,user_id) VALUES (#{teamId},'3',#{userId})")
    public void addTeamProjectManager(@Param("teamId") Integer teamId,@Param("userId") Integer userId);

    @Select("select project_id from project where project_name = #{projectName}")
    public Integer checkProjectName(@Param("projectName") String projectName);


    @Select("SELECT team_id FROM project p WHERE p.project_id = #{projectId}")
    public Integer getTeamIdByProjectId(@Param("projectId") Integer projectId);


    @Update("UPDATE project SET project_name = #{projectName},project_msg = #{projectMsg}" +
            "   WHERE project_id = #{projectId}")
    public void editProject(@Param("projectName") String projectName,
                            @Param("projectId") Integer projectId,
                            @Param("projectMsg") String projectMsg);

    @Update("UPDATE project SET project_archive = 1 WHERE project_id = #{projectId}")
    public void archiveProject(@Param("projectId") Integer projectId);

    @Select("SELECT project_archive from project where project_id= #{projectId}")
    public Integer checkArchive(@Param("projectId") Integer projectId);
}
