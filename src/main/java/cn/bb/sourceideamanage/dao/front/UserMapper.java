package cn.bb.sourceideamanage.dao.front;

import cn.bb.sourceideamanage.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user where user_id = #{userId}")
    public User findUserById(@Param("userId") Integer userId);

    @Select("select role_name from role where role_id IN" +
            "(select role_id from  user_role where user_id = #{userId})"
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


}
