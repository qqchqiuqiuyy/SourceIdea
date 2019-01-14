package cn.bb.sourceideamanage.dao.front;

import cn.bb.sourceideamanage.dto.front.FrontProject;
import cn.bb.sourceideamanage.dto.front.FrontProjectMsg;
import cn.bb.sourceideamanage.dto.front.projectMember;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface ProjectMapper {
    @Select("SELECT p.project_id AS projectId, t.team_name AS projectTeam , " +
            "   p.project_create_time AS projectCreateTime ," +
            "   u.user_name AS projectManager ," +
            "   p.project_name AS projectName" +
            " FROM project p,team t,user_team ut, user u" +
            "   WHERE p.project_name LIKE '%${projectName}%' AND" +
            "   p.team_id = t.team_id AND" +
            "   t.team_id = ut.team_id AND" +
            "   ut.role_id = '4' AND" +
            "   ut.user_id = u.user_id" +
            "   GROUP BY projectName")
    public List<FrontProject> findAllFrontProject(@Param("projectName") String projectName);

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
    public List<projectMember> getProjectMembers(@Param("teamId") Integer teamId);

    @Select("SELECT  p.project_id AS projectId,p.project_name AS projectName ," +
            "   p.project_msg AS projectMsg ,t.team_name AS teamName , " +
            "   p.project_create_time AS projectCreateTime, " +
            "   t.team_id AS teamId " +
            " FROM project p, team t"  +
            " WHERE p.project_id = #{projectId} AND p.team_id = t.team_id  " +
            "   GROUP BY projectName "
           )
    @Results({
            @Result(column = "teamId", javaType = List.class, property = "projectMembers",
                    many = @Many(   //一对多
                            select = "cn.bb.sourceideamanage.dao.front.ProjectMapper.getProjectMembers", //上面搜索的方法
                            fetchType = FetchType.LAZY
                    ))
    })
    public FrontProjectMsg getProjectMsgByProjectId(@Param("projectId") Integer projectId);


    @Select("select team_id from project where project_id = #{projectId}")
    public Integer getTeamId(@Param("projectId") Integer projectId);

    @Select("SELECT project_name " +
            " FROM user u,user_team ut, team t, project p" +
            " WHERE u.user_id = #{userId} AND u.user_id = ut.user_id AND" +
            " ut.team_id = t.team_id AND t.team_id = p.project_id")
    public List<String> getAllProjects(@Param("userId") Integer userId);
}
