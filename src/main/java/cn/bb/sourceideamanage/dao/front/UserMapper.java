package cn.bb.sourceideamanage.dao.front;

import cn.bb.sourceideamanage.dto.front.ApplyUser;
import cn.bb.sourceideamanage.dto.front.FrontProject;
import cn.bb.sourceideamanage.dto.front.FrontUser;
import cn.bb.sourceideamanage.dto.front.InviteUser;
import cn.bb.sourceideamanage.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author bobo
 */
@Mapper
public interface UserMapper {

    /**
     * 查找用户
     * @param userId 用户id
     * @param state 1表示删除 0表示未删除
     * @return
     */
    @Select("SELECT " +
            " user_id, " +
            " user_name, " +
            " user_msg, " +
            " user_account, " +
            " user_password, " +
            " user_create_time  " +
            "FROM " +
            "USER  " +
            "WHERE " +
            " user_id = #{userId} " +
            " AND is_delete = #{state}")
    public User findUserById(@Param("userId") Integer userId,
                             @Param("state") Integer state);

    /**
     * 查找该用户所有角色
     * @param userId
     * @return
     */
    @Select("  SELECT " +
            " role_name  " +
            "FROM " +
            " role r, " +
            " user_role ur  " +
            "WHERE " +
            " ur.role_id = r.role_id " +
            " AND ur.user_id = #{userId} UNION " +
            "SELECT " +
            " role_name  " +
            "FROM " +
            " user_team ut  " +
            "WHERE " +
            " ut.user_id = #{userId} "
    )
    public List<String> findUserAllRoleByUserId(@Param("userId") Integer userId);


    /**
     *查找该用户所有操作
     * @param userId
     * @return
     */
    @Select("select permission_name from permission where role_id IN" +
            "(select role_id from  user_role where user_id = #{userId} )")
    public List<String> findUserAllPermissionByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户id查找团队用户信息
     * @param userId
     * @param state
     * @return
     */
    @Select("select ut.id, " +
            " ut.team_id, " +
            " ut.role_id, " +
            " ut.user_id, " +
            " ut.member_join_time  from user_team ut where team_id IN " +
            "            (select team_id from user_team where user_id = #{userId}) " +
            "       AND ut.is_delete = #{state}")
    public List<UserTeam> findUserAllTeam(@Param("userId") Integer userId,
                                          @Param("state") Integer state);

    /**
     * 根据用户id 查找想法
     * @param userId
     * @param state
     * @return
     */
    @Select(" SELECT " +
            " idea_id,team_id,tag_id,user_id,idea_name,idea_msg,idea_supports,idea_create_time " +
            "FROM " +
            " idea  " +
            "WHERE " +
            " user_id = #{userId} AND is_delete = #{state}")
    public List<Idea> findUserAllIdea(@Param("userId") Integer userId,@Param("state") Integer state);

    /**
     * 根据用户id 查找想法评论
     * @param userId
     * @param state
     * @return
     */
    @Select("SELECT " +
            " id, " +
            " uid, " +
            " idea_id, " +
            " idea_name, " +
            " user_id, " +
            " user_name, " +
            " parent_id, " +
            " parent_name, " +
            " content, " +
            " comment_time, " +
            " is_delete  " +
            "FROM " +
            " comment_idea  " +
            "WHERE " +
            " user_id = #{userId} " +
            " AND is_delete = #{state}")
    public List<CommentIdea> findUserAllCommentIdea(@Param("userId") Integer userId,@Param("state") Integer state);


    /**
     * 根据账号找到该用户信息
     * @param account 账号
     * @param state
     * @return
     */
    @Select(" SELECT " +
            " user_id, " +
            " user_name, " +
            " user_msg, " +
            " user_create_time, " +
            " user_account, " +
            " user_password  " +
            "FROM " +
            "USER  " +
            "WHERE " +
            " user_account = #{account} " +
            " AND is_delete = #{state}")
    public User findUserByAccount(@Param("account") String account,@Param("state") Integer state);


    /**
     * 删除成员 将is_delete字段改为1
     * @param userId 用户id
     * @param teamName  团队名
     * @param state 1删除 0未删除
     */
    @Update(" UPDATE " +
            " user_team SET is_delete = #{isSuccess} " +
            " WHERE" +
            " user_id = #{userId} " +
            " AND team_id IN ( SELECT t.team_id FROM team t WHERE t.team_name = #{teamName} )")
    public void delMember(@Param("userId") Integer userId ,@Param("teamName") String teamName,
                          @Param("isSuccess") Integer state);

    /**
     * 减少该团队成员数量
     * @param teamId
     * @param state
     */
    @Update("UPDATE team set team_nums = team_nums - 1 WHERE team_id = #{teamId} AND is_delete = #{isSuccess}")
    public void decrMemberNums(@Param("teamId") Integer teamId, @Param("isSuccess") Integer state);

    /**
     * 根据团队名 找到申请该团队人员列表
     * @param teamName
     * @param state
     * @return
     */
    @Select("SELECT " +
            " a.team_id AS teamId, " +
            " a.user_id AS userId, " +
            " a.apply_time AS applyTime, " +
            " u.user_name AS userName  " +
            "FROM " +
            " apply a, " +
            " team t, " +
            " USER u  " +
            "WHERE " +
            " a.team_id = t.team_id  " +
            " AND u.user_id = a.user_id  " +
            " AND t.team_name = #{teamName} AND a.is_delete = #{isDelete} " +
            " AND t.is_delete = #{isDelete} AND u.is_delete = #{isDelete} ")
    public List<ApplyUser> getAllAppy(@Param("teamName") String teamName, @Param("isDelete") Integer state);


    /**
     * 根据团队名找到团队id
     * @param teamName
     * @param state
     * @return
     */
    @Select("SELECT t.team_id " +
            " FROM" +
            "  team t" +
            " WHERE t.team_name = #{teamName} AND t.is_delete = #{isDelete} ")
    public Integer getTeamId(@Param("teamName") String  teamName, @Param("isDelete") Integer state);

    /**
     * 删除一条申请
     * @param userId 用户id
     * @param teamId    团队id
     * @param state 1删除 0保留
     */
    @Update("Update  apply SET is_delete = #{isDelete} WHERE user_id = #{userId} AND team_id = #{teamId}")
    public void delApply(@Param("userId") Integer userId,@Param("teamId") Integer teamId,
                         @Param("isDelete") Integer state);

    /**
     * 进入团队
     * @param userId 用户id
     * @param teamId    团队id
     * @param roleId    角色id
     * @param roleName  角色名
     */
    @Insert("INSERT INTO user_team (team_id,role_id,user_id,role_name) " +
            "   VALUES (#{teamId}, #{roleId} , #{userId},#{roleName})")
    public void joinTeam(@Param("userId") Integer userId,@Param("teamId") Integer teamId,
                         @Param("roleId") Integer roleId,@Param("roleName") String roleName);

    /**
     * 根据团队名 找到所有的项目
     * @param teamName
     * @param state
     * @return
     */
    @Select("SELECT p.project_id AS projectId," +
            "           p.project_name AS projectName," +
            "           p.project_create_time AS projectCreateTime ," +
            "           p.project_archive AS projectArchive" +
            "   FROM team t,project p" +
            "   WHERE t.team_name = #{teamName} AND" +
            "   t.team_id = p.team_id AND t.is_delete = #{isDelete} AND p.is_delete = #{isDelete}" +
            "   ORDER BY projectCreateTime DESC ")
    public List<FrontProject> findProjects(@Param("teamName") String teamName,@Param("isDelete") Integer state);

    /**
     * 删除一个项目 将字段is_Success改为1
     * @param projectId 项目id
     * @param state 1删除  0为未删除
     */
    @Update("UPDATE  project SET is_delete = #{isDelete} WHERE project_id = #{projectId}")
    public void delProject(@Param("projectId") Integer projectId,@Param("isDelete") Integer state);


    /**
     * 将团队人数+1
     * @param teamId 团队id
     */
    @Update("UPDATE team SET team_nums = team_nums + 1 " +
            "   WHERE team_id = #{teamId}")
    public void addMemberNums(@Param("teamId") Integer teamId);

    /**
     * 根据用户名查找用户
     * @param userName  用户名
     * @param state 1删除 0未删除
     * @return
     */
    @Select("SELECT u.user_id AS userId, u.user_name AS userName ,u.user_msg AS userMsg , u.user_create_time AS userCreateTime  " +
            " FROM  user u" +
            "   WHERE u.user_name LIKE CONCAT( '%',#{userName}, '%' ) AND u.is_delete = #{state}")
    public List<FrontUser> findAllFrontUser(@Param("userName") String userName, @Param("state") Integer state);

    /**
     * 根据用户id 找到用户信息
     * @param userId
     * @return
     */
    @Select("SELECT u.user_id AS userId ,u.user_name AS userName,u.user_msg AS userMsg, " +
            "   u.user_create_time AS userCreateTime" +
            "   FROM user u" +
            "   WHERE u.user_id = #{userId} AND u.is_delete = #{state} " )
    public FrontUser getUserMsg(@Param("userId") Integer userId ,@Param("state") Integer state);


    /**
     * 判断该用户是否已经在该团队邀请表里
     * @param userId   用户id
     * @param teamId    团队id
     * @return
     */
    @Select("select user_id from invite where user_id = #{userId} AND team_id = #{teamId} AND is_delete = #{state}")
    public String checkUserInInvite(@Param("userId") Integer userId, @Param("teamId") Integer teamId,@Param("state") Integer state);


    /**
     * 根据用户id 查找所有邀请该用户的团队
     * @param userId    用户id
     * @param state 1删除 0未删除
     * @return
     */
    @Select("SELECT it.user_id,it.team_id ,t.team_name,it.invite_time" +
            "   FROM invite it ,team t" +
            "   WHERE it.user_id = #{userId} AND" +
            "         it.team_id = t.team_id  AND" +
            "          it.is_delete = #{state} AND t.is_delete = #{state}")
    public List<InviteUser> getUserInvite(@Param("userId") Integer userId,@Param("state") Integer state);

    /**
     * 删除一条邀请信息 将is_delete设置为1
     * @param userId
     * @param teamId
     * @param state
     */
    @Update("Update  invite set is_delete = #{isDelete} WHERE user_id = #{userId} AND team_id = #{teamId}")
    public void delInvite(@Param("userId") Integer userId,@Param("teamId") Integer teamId,
                          @Param("isDelete") Integer state);

    /**
     * 根据用户id 团队id 得到这个用户对于该团队的所有角色
     * @param userId    用户id
     * @param teamId    团队id
     * @param state     1删除 0未删除
     * @return
     */
    @Select("SELECT " +
            " ut.role_id  " +
            "FROM " +
            " user_team ut  " +
            "WHERE " +
            " ut.user_id = #{userId}  " +
            " AND ut.team_id = #{teamId} AND ut.is_delete = #{state}")
    public List<Integer> getAllTeamRole(@Param("userId") Integer userId,@Param("teamId") Integer teamId,
                                        @Param("state") Integer state);


    /**
     * 根据用户id 得到用户名
     * @param userId
     * @param state
     * @return
     */
    @Select("select user_name from user where user_id = #{userId} AND is_delete = #{state}")
    public String getUserName(@Param("userId") Integer userId, @Param("state") Integer state);
}

