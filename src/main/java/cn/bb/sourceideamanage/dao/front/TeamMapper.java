package cn.bb.sourceideamanage.dao.front;

import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.back.BackTeamMember;
import cn.bb.sourceideamanage.dto.front.*;
import cn.bb.sourceideamanage.entity.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import javax.annotation.processing.SupportedOptions;
import java.util.List;

/**
 * @author bobo
 */
@Mapper
public interface TeamMapper {

    /**
     * 根据用户id 查找所有未删除项目
     * @param teamId    用户id
     * @param state  1删除 0未删除
     * @return
     */
    @Select("SELECT " +
            " project_id, " +
            " team_id, " +
            " project_name, " +
            " project_msg, " +
            " project_archive, " +
            " project_create_time  " +
            "FROM " +
            " project " +
            "WHERE team_id = #{teamId} " +
            "AND is_delete = #{isDelete}")
    public List<Project> findAllProject(@Param("teamId") Integer teamId,
                                        @Param("isDelete") Integer state);

    /**
     *
     * @param teamId 团队id
     * @param state 1删除 0未删除
     * @return
     */
    @Select("SELECT " +
            " idea_id, " +
            " team_id, " +
            " tag_id, " +
            " user_id, " +
            " idea_name, " +
            " idea_msg, " +
            " idea_supports, " +
            " idea_create_time  " +
            "FROM " +
            " idea  " +
            "WHERE " +
            " team_id = #{teamId}  " +
            " AND is_delete = #{isDelete}")
    public List<Idea> findAllIdea(@Param("teamId") Integer teamId,
                                  @Param("isDelete") Integer state);

    /**
     * 通过团队id 来得到该团队所有团员
     * @param teamId 团队id
     * @param state 1删除 0未删除
     * @return
     */
    @Select("SELECT " +
            " u.user_id, " +
            " u.user_name, " +
            " u.user_msg, " +
            " u.user_create_time  " +
            "FROM " +
            " user_team ut, " +
            " USER u  " +
            "WHERE " +
            " ut.user_id = u.user_id  " +
            " AND ut.team_id = #{teamId}  " +
            " AND u.is_delete = #{isDelete}  " +
            " AND ut.is_delete = #{isDelete}")
    public List<User> findAllMembers(@Param("teamId") Integer teamId,
                                     @Param("isDelete") Integer state);

    /**
     * 根据团队名查找该团队团员信息
     * @param teamName  团队名
     * @param state 1删除 0未删除
     * @return
     */
    @Select(" SELECT " +
            " id, " +
            " team_id, " +
            " role_id, " +
            " user_id, " +
            " member_join_time  " +
            "FROM " +
            " user_team  " +
            "WHERE " +
            " team_id IN ( SELECT team_id FROM team WHERE team_name LIKE CONCAT( '%', #{teamName}, '%' )  ) " +
            " AND is_delete = #{state}")
    public List<UserTeam> findSearchTeam(@Param("teamName") String teamName,
                                         @Param("isDelete") Integer state);

    /**
     * 根据下列信息 返回团队列表分页
     * @param teamName 团队名
     * @param teamManager   团队管理员
     * @param isDelete 1删除 0未删除
     * @return
     */
    @Select("SELECT   " +
            " t.team_id AS teamId, " +
            " t.team_name AS teamName, " +
            " t.team_create_time AS teamCreateTime, " +
            " u.user_name AS teamCaptain, " +
            " t.is_delete  " +
            "FROM " +
            " team t, " +
            " user_team ut, " +
            " role r, " +
            " USER u  " +
            "WHERE " +
            " t.team_id = ut.team_id  " +
            " AND ut.role_id = #{teamManager}  " +
            " AND ut.user_id = u.user_id  " +
            " AND t.team_name LIKE CONCAT( '%', #{teamName}, '%' )  " +
            " AND t.is_delete = #{isDelete}  " +
            " AND ut.is_delete = #{isDelete}  " +
            " AND u.is_delete = #{isDelete}" +
            " GROUP BY teamId")
    public List<FrontTeam> findAllFrontTeam(@Param("teamName") String teamName,@Param("teamManager") Integer teamManager,
                                            @Param("isDelete") Integer isDelete);


    /**
     *  根据团队id找到高团队所有项目
     * @param teamId 团队id
     * @return
     */
    @Select("SELECT " +
            " project_id, " +
            " team_id, " +
            " project_name, " +
            " project_msg,  " +
            " project_archive, " +
            " project_create_time  " +
            "FROM " +
            " project  " +
            "WHERE " +
            " team_id = #{teamId}  " +
            " AND is_delete = 0")
    public List<Project> findAllProjectByTeamId(@Param("teamId") Integer teamId);


    /**
     * 根据团队id 找到该团队所有团员
     * @param teamId 团队id
     * @return
     */
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
      /*      " AND ut.role_id = ( SELECT role_id FROM user_team WHERE user_id  = ut.user_id and is_delete = 0 ORDER BY role_id DESC LIMIT 1)  " +*/
            " AND ut.is_delete = 0  " +
            " AND u.is_delete = 0 " +
            " GROUP BY userName" )
    public List<TeamMember> findAllMemberByTeamId(@Param("tId2") Integer teamId);


    /**
     * 根据团队id 查找该团队所有想法
     * @param teamId    团队id
     * @param state 1表示删除 0表示未删除
     * @return
     */
    @Select("SELECT " +
            " idea_id, " +
            " team_id, " +
            " tag_id, " +
            " user_id, " +
            " idea_name, " +
            " idea_msg, " +
            " idea_supports, " +
            " idea_create_time  " +
            "FROM " +
            " idea  " +
            "WHERE " +
            " team_id = #{tId3} " +
            " AND is_delete = #{isDelete}")
    public List<Idea> findAllTeamIdea(@Param("tId3") Integer teamId,@Param("isDelete") Integer state);

    /**
     * 根据团队Id找队长
     * @param teamId
     * @return String
     */
    @Select("SELECT DISTINCT " +
            " u.user_name  " +
            "FROM " +
            " role r, " +
            " user_team ut, " +
            " USER u  " +
            "WHERE " +
            " ut.team_id = #{teamId} AND ut.role_id = #{CaptianRoleId} AND ut.user_id = u.user_id " +
            " AND u.is_delete =  #{isDelete} AND ut.is_delete = #{isDelete}")
    public String findTeamCaptainByTeamId(@Param("teamId") Integer teamId,@Param("isDelete") Integer state,
                                          @Param("CaptianRoleId") Integer roleId);


    /**
     * 一对多
     * @param teamName
     * @return
     */
    @Select("SELECT " +
            " t.team_name AS teamName, " +
            " t.team_id AS teamId, " +
            " t.team_id AS tId, " +
            " t.team_id AS tId2, " +
            " t.team_id AS tId3, " +
            " t.team_id AS tId4, " +
            " ut.user_id AS uId, " +
            " t.team_nums AS teamNums, " +
            " t.team_create_time, " +
            " t.team_msg AS teamMsg  " +
            "FROM " +
            " user_team ut, " +
            " team t, " +
            " role r " +
            "WHERE " +
            " t.team_name = #{teamName} AND ut.team_id = t.team_id AND ut.role_id = r.role_id  " +
            "   AND ut.is_delete = #{state} AND t.is_delete = #{state} " +
            "GROUP BY  " +
            " teamId ")
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
    public BackTeam findAllTeamMember(@Param("teamName") String teamName,
                                      @Param("isDelete") Integer state);

   /* @Insert("INSERT INTO user_team (team_id,role_id,user_id) " +
            "   VALUES" +
            "   (#{teamId} , '6',#{userId})")
    public void joinTeam(@Param("userId") Integer userId,@Param("teamId") Integer teamId);*/

    /**
     * 判断该用户是否是该团队成员
     * @param userId 用户id
     * @param teamId    团队id
     * @param state 1表示删除 0表示未删除
     * @return
     */
    @Select("SELECT role_id FROM user_team ut " +
            "   WHERE" +
            "    ut.user_id = #{userId} AND ut.team_id = #{teamId} AND ut.is_delete = #{isDelete}")
    public String checkTeamMember(@Param("userId") Integer userId,@Param("teamId") Integer teamId,
                                  @Param("isDelete") Integer state);


    /**
     * 申请加入团队
     * @param userId 用户id
     * @param teamId    团队id
     */
    @Insert("INSERT INTO apply (user_id,team_id) VALUES (#{userId},#{teamId})")
    public void apply(@Param("userId") Integer userId,@Param("teamId") Integer teamId);

    /**
     * 检查该用户是否已经申请过该团队
     * @param userId 用户id
     * @param teamId    团队id
     * @param state 1表示删除 0表示未删除
     * @return
     */
    @Select("select team_id from apply where user_id = #{userId} AND team_id = #{teamId} AND is_delete = #{isDelete}")
    public String checkApply(@Param("userId") Integer userId,@Param("teamId") Integer teamId,
                             @Param("isDelete") Integer state);

    /**
     * 得到该用户的所有团队
     * @param userId 用户id
     * @param state 1表示删除 0表示未删除
     * @return
     */
    @Select("select t.team_name" +
            "   FROM user u,user_team ut, team t" +
            "   WHERE u.user_id = #{userId} AND" +
            "         u.user_id = ut.user_id AND" +
            "           ut.team_id = t.team_id " +
            "          AND ut.is_delete = #{isDelete}" +
            "           AND u.is_delete = #{isDelete}" +
            "           AND t.is_delete = #{isDelete}")
    public List<String> findAllTeam(@Param("userId") Integer userId,
                                    @Param("isDelete") Integer state);


    /**
     * 查找所有的我的团队
     * @param teamName 团队名
     * @param userId    用户id
     * @param teamManager   团队管理员
     * @param state 1表示删除0表示未删除
     * @return
     */
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
            " AND ut.role_id = #{teamManager}  " +
            " AND ut.role_id = r.role_id AND  t.team_name LIKE CONCAT( '%',#{teamName}, '%' ) " +
            " AND u.user_id = ut.user_id " +
            " AND u.is_delete = #{isDelete} AND ut.is_delete = #{isDelete} And t.is_delete = #{isDelete}" +
            " AND ut.team_id IN ( SELECT ut.team_id FROM user_team ut WHERE ut.user_id = #{userId} AND ut.is_delete = #{isDelete})" +
            "   ORDER BY teamCreateTime DESC")
    public List<FrontTeam> findAllMyTeam(@Param("teamName") String teamName,@Param("userId") Integer userId,
                                        @Param("teamManager") Integer teamManager,@Param("isDelete") Integer state);

    /**
     * 得到团队的所有成员
     * @param teamName 团队名
     * @param state 1表示删除 0表示未删除
     * @return
     */
    @Select("SELECT " +
            " u.user_id AS userId, " +
            " u.user_name AS userName, " +
            " r.role_msg AS teamRole, " +
            " ut.member_join_time AS memberJoinTime, " +
            " ut.role_id " +
            "FROM " +
            " role r, " +
            " USER u, " +
            " user_team ut, " +
            " team t  " +
            "WHERE " +
            " t.team_name = #{teamName}  " +
            " AND t.team_id = ut.team_id  " +
            " AND ut.user_id = u.user_id " +
            " AND ut.role_id = r.role_id " +
            " AND u.is_delete = #{isDelete} AND ut.is_delete = #{isDelete} AND t.is_delete = #{isDelete} " +
            " group by userName" )
    public List<MyTeamMember> findAllMyTeamMember(@Param("teamName") String teamName,@Param("isDelete") Integer state);

    /**
     * 根据团队名和用户id 查找团队的角色
     * @param teamName 团队名
     * @param userId    用户id
     * @param state 1表示删除 0表示未删除
     * @return
     */
    @Select("select r.role_name " +
            "   FROM team t,user_team ut, role r" +
            "   where t.team_name = #{teamName} AND ut.user_id = #{userId} AND t.team_id = ut.team_id AND" +
            "         ut.role_id = r.role_id " +
            "   AND t.is_delete = #{isDelete} AND ut.is_delete = #{isDelete} ")
    public List<String> findTeamRoleByTeamNameAndUserId(@Param("teamName") String teamName,@Param("userId") Integer userId,
                                                        @Param("isDelete") Integer state );

    /**
     * 根据团队名 查找到该团队id]
     * @param teamName  团队名
     * @param state 1表示删除 0表示未删除
     * @return
     */
    @Select("select t.team_id from team t where t.team_name = #{teamName}")
    public Integer findTeamId(@Param("teamName") String teamName,@Param("isDelete") Integer state);

    /**
     * 根据团队id 查找到所有的团队成员id
     * @param teamId 团队id
     * @param state 1表示删除 0表示未删除
     * @return
     */
    @Select("SELECT ut.user_id from user_team ut where ut.team_id = #{teamId} AND ut.is_delete = #{isDelete}")
    public List<Integer> getTeamUserId(@Param("teamId") Integer teamId,@Param("isDelete") Integer state);

    /**
     * 发出团队邀请
     * @param teamId    团队id
     * @param userId    用户id
     */
    @Insert("INSERT INTO invite (team_id , user_id) VALUES ( #{teamId},#{userId}  )")
    public void teamInvite(@Param("teamId") Integer teamId,@Param("userId") Integer userId);


    /**
     * 根据团队名 找到团队id
     * @param teamName  团队名
     * @param state 1表示删除 0表示未删除
     * @return
     */
    @Select("SELECT t.team_id " +
            " FROM" +
            "  team t" +
            " WHERE t.team_name = #{teamName} AND t.is_delete = #{isDelete} ")
    public Integer getTeamId(@Param("teamName") String  teamName,@Param("isDelete") Integer state);


    /**
     * 新增一个团队
     * @param newTeam
     */
    @Insert("INSERT INTO team  (team_name , team_msg) VALUES (#{newTeam.teamName} ,#{newTeam.teamMsg})")
    @Options(useGeneratedKeys = true,keyProperty = "newTeam.teamId",keyColumn = "team_id")
    public void addTeam(@Param("newTeam") NewTeam newTeam);


    /**
     * 改变团队成员的角色
     * @param role  角色
     * @param teamId    团队id
     * @param userId    被改变成员id
     * @return
     */
    @Insert("INSERT INTO user_team (team_id,user_id,role_id,role_name) " +
            "   VALUES (#{teamId}, #{userId}, #{role.roleId} ,#{role.roleName})")
    public int addTeamRoles(@Param("role") Role role,@Param("teamId") Integer teamId,
                            @Param("userId") Integer userId);


    /**
     * 根据团队id 找到该团队的所有想法
     * @param teamId    团队id
     * @param state      1表示删除 0表示未删除
     * @return
     */
    @Select("SELECT i.idea_name " +
            "   FROM idea i " +
            "   WHERE i.team_id = #{teamId} AND i.is_delete = #{isDelete}")
    public List<Idea> findAllTeamIdeas(@Param("teamId") Integer teamId,@Param("isDelete") Integer state);


    /**
     * 根据团队名查找团队信息
     * @param teamName  团队名
     * @param state   1表示删除 0表示未删除
     * @return
     */
    @Select("SELECT " +
            " team_id, " +
            " team_name, " +
            " team_msg, " +
            " team_create_time, " +
            " team_nums  " +
            "FROM " +
            " team  " +
            "WHERE " +
            " team_name = #{teamName} AND is_delete = #{isDelete}")
    public TeamMsg findTeamMsg(@Param("teamName") String teamName,@Param("isDelete") Integer state);


    /**
     * 判断该用户是否是该团队的管理员
     * @param userId    用户id
     * @param teamId    团队id
     * @param roleId    角色id
     * @param state  1表示删除 0表示未删除
     * @return
     */
    @Select("SELECT role_id FROM user_team where user_id = #{userId} AND team_id = #{teamId} AND role_id = #{roleId} AND is_delete = #{isDelete}")
    public Integer checkManager(@Param("userId") Integer userId,@Param("teamId") Integer teamId,
                                @Param("roleId") Integer roleId,@Param("isDelete") Integer state);

    /**
     * 对该用户授予该团队管理员权限
     * @param userId    用户id
     * @param teamId    团队id
     * @param originRoleId  原来的角色id
     * @param roleId    新的角色id
     * @param roleName  `角色名
     */
    @Update("UPDATE user_team SET role_id = #{roleId},role_name = #{roleName} WHERE user_id = #{userId} AND team_id = #{teamId} AND role_id = #{originRoleId}")
    public void awardManager(@Param("userId") Integer userId, @Param("teamId") Integer teamId ,@Param("originRoleId") Integer originRoleId,
                             @Param("roleId") Integer roleId , @Param("roleName") String roleName);
}
