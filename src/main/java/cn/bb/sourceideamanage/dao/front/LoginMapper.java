package cn.bb.sourceideamanage.dao.front;

import cn.bb.sourceideamanage.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * @author  bobo
 */
@Mapper
public interface LoginMapper {
    /**
     * 根据账号返回密码
     * @param account
     * @return
     */
    @Select("select password from Managers where account = #{account}")
    public String getPassword(@Param("account") String account);

    /**
     * 添加用户
     * @param user
     */
    @Insert("INSERT INTO user  " +
            "   (user_name,user_account,user_password,user_msg) " +
            "VALUES " +
            "   (#{userName} , #{userAccount} , #{userPassword},#{userMsg})")
    @Options(useGeneratedKeys = true,keyProperty = "userId",keyColumn = "user_id")
    public void addUser(User user);


    /**
     * 添加角色
     * @param userId     用户id
     * @param roleId    角色id
     */
    @Insert("INSERT INTO user_role (user_id,role_id) values " +
            "(#{userId} , #{roleId})")
    public void addRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    /*@Insert("insert into user_team (user_id,team_id,role_id) values (#{id},1000,1)")
    public void addUserTeam(Integer id);*/
}
