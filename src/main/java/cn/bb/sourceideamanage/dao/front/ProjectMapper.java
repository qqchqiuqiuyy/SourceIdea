package cn.bb.sourceideamanage.dao.front;

import cn.bb.sourceideamanage.dto.front.FrontProject;
import cn.bb.sourceideamanage.dto.front.FrontProjectMsg;
import cn.bb.sourceideamanage.dto.front.ProjectMember;
import cn.bb.sourceideamanage.dto.front.UserTag;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * @author  bobo
 */
@Mapper
public interface ProjectMapper {
    /**
     * 根据项目名和团队管理员id 查找项目在项目模块的分页展示
     * @param projectName 项目名
     * @param teamManager   团队管理员
     * @return
     */
    @Select("SELECT p.project_id AS projectId, t.team_name AS projectTeam , " +
            "   p.project_create_time AS projectCreateTime ," +
            "   u.user_name AS projectManager ," +
            "   p.project_name AS projectName" +
            " FROM project p,team t,user_team ut, user u" +
            "   WHERE p.project_name LIKE CONCAT('%',#{projectName},'%') AND" +
            "   p.team_id = t.team_id AND" +
            "   t.team_id = ut.team_id AND" +
            "   ut.role_id = #{teamManager} AND" +
            "   ut.user_id = u.user_id AND p.is_delete = #{state} " +
            "   AND t.is_delete = #{state} AND ut.is_delete = #{state}" +
            "   AND u.is_delete = #{state}" +
            "   GROUP BY projectName DESC")
    public List<FrontProject> findAllFrontProject(@Param("projectName") String projectName,
                                                  @Param("teamManager") Integer teamManager,
                                                  @Param("state") Integer state);

    /**
     * 根据团队id 查找所有项目成员
     * @param teamId 团队id
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


    /**
     * 根据项目id 查找项目信息
     * @param projectId 项目id
     * @return
     */
    @Select("SELECT  p.project_id AS projectId," +
            "       p.project_name AS projectName ," +
            "       p.project_msg AS projectMsg ," +
            "       t.team_name AS teamName , " +
            "       p.project_create_time AS projectCreateTime, " +
            "        t.team_id AS teamId " +
            " FROM " +
            "       project p, team t "  +
            " WHERE " +
            "   p.project_id = #{projectId} AND p.team_id = t.team_id  " +
            "   GROUP BY projectName "
           )
    public FrontProjectMsg getProjectMsgByProjectId(@Param("projectId") Integer projectId);


    /**根据项目id 查找该项目的团队id
     *
     * @param projectId 项目id
     * @return
     */
    @Select("select " +
            "   team_id " +
            "   from project " +
            "   where project_id = #{projectId}")
    public Integer getTeamId(@Param("projectId") Integer projectId);


    /**
     * 根据用户id 返回该用户的所有项目
     * @param userId 用户id
     * @return
     */
    @Select("SELECT p.project_name " +
            "FROM project p " +
            "where p.team_id IN " +
            "   (select ut.team_id " +
            "       from user_team ut " +
            "       where " +
            "           ut.user_id = #{userId} AND ut.is_delete = #{state})" +
            "   AND p.is_delete = #{state}")
    public List<String> getAllProjects(@Param("userId") Integer userId,@Param("state") Integer state);


    /**
     * 根据下面参数 返回该用户的所有项目
     * @param projectName   项目名
     * @param userId    用户id
     * @param state 1删除 0未删除
     * @return
     */
    @Select("SELECT p.project_id AS projectId, t.team_name AS projectTeam , " +
            "   p.project_create_time AS projectCreateTime ," +
            "   u.user_name AS projectManager ," +
            "   p.project_name AS projectName" +
            " FROM project p,team t,user_team ut, user u" +
            "   WHERE p.project_name LIKE CONCAT('%',#{projectName},'%') AND" +
            "   p.team_id = t.team_id AND" +
            "   t.team_id = ut.team_id AND" +
            "   ut.role_id = #{teamManager} AND" +
            "   ut.user_id = u.user_id AND p.is_delete = #{state} " +
            "   AND t.is_delete = #{state} AND ut.is_delete = #{state}" +
            "   AND u.is_delete = #{state} AND t.team_id IN (select team_id from user_team where user_id = #{userId} and is_delete = #{state} ) " +
            "   GROUP BY projectName DESC")
    public List<FrontProject> findAllMyProject(@Param("projectName") String projectName,
                                               @Param("teamManager") Integer roleId,
                                               @Param("userId") Integer userId,
                                               @Param("state") Integer state);

    /**
     * 根据团队id 用户信息
     * @param teamId 团队id
     * @return
     */
    @Select("SELECT  u.user_id ,u.user_name" +
            "   FROM " +
            "       user_team ut,user u" +
            "   WHERE " +
            "           ut.team_id = #{teamId} AND ut.user_id = u.user_id ")
    public List<UserTag> getUsersForTag(@Param("teamId") Integer teamId);


    /**
     * 添加一个团队项目
     * @param teamId    团队id
     * @param projectName   项目名
     * @param projectMsg    项目信息
     * @param archive   是否已归档
     */
    @Insert("INSERT INTO project " +
            "   ( team_id, project_msg, project_archive, project_name ) " +
            " VALUES " +
            " ( #{teamId}, #{projectMsg},#{archive} ,#{projectName})")
    public void addTeamProject(@Param("teamId") Integer teamId, @Param("projectName") String projectName,
                               @Param("projectMsg") String projectMsg,@Param("archive") Integer archive);

    /**
     * 根据用户id 判断是否是该团队id 的项目管理员
     * @param teamId    团队id
     * @param userId  用户id
     * @return
     */
    @Select("SELECT id " +
            "   FROM user_team " +
            "   WHERE " +
            "   team_id = #{teamId} AND user_id = #{userId}")
    public Integer checkProjectManager(@Param("teamId") Integer teamId,@Param("userId") Integer userId);

/*
    @Select("INSERT INTO user_team (team_id,role_id,user_id) VALUES (#{teamId},'3',#{userId})")
    public void addTeamProjectManager(@Param("teamId") Integer teamId,@Param("userId") Integer userId);*/

    /**
     * 检查是否有该项目
     * @param projectName   项目名
     * @return
     */
    @Select("select project_id from project where project_name = #{projectName}")
    public Integer checkProjectName(@Param("projectName") String projectName);


    /**
     * 通过项目id 得到团队id
     * @param projectId 项目id
     * @return
     */
    @Select("SELECT team_id FROM project p WHERE p.project_id = #{projectId}")
    public Integer getTeamIdByProjectId(@Param("projectId") Integer projectId);


    /**
     * 更新项目信息
     * @param projectName 项目名
     * @param projectId 项目id
     * @param projectMsg    项目信息
     */
    @Update("UPDATE project SET project_name = #{projectName},project_msg = #{projectMsg}" +
            "   WHERE project_id = #{projectId}")
    public void editProject(@Param("projectName") String projectName,
                            @Param("projectId") Integer projectId,
                            @Param("projectMsg") String projectMsg);

    /**
     * 将项目归档
     * @param projectId 项目名
     * @param archive   归档1
     * @param state 1删除 0未删除
     */
    @Update("UPDATE project SET project_archive = #{archive} WHERE project_id = #{projectId} AND is_delete = #{isSuccess}")
    public void archiveProject(@Param("projectId") Integer projectId,
                               @Param("archive") Integer archive,
                               @Param("isSuccess") Integer state);

    /**
     * 检查是否已归档
     * @param projectId 项目id
     * @param state 1删除 0未删除
     * @return
     */
    @Select("SELECT project_archive from project where project_id= #{projectId} AND is_delete = #{isSuccess}")
    public Integer checkArchive(@Param("projectId") Integer projectId,@Param("isSuccess") Integer state);
}
