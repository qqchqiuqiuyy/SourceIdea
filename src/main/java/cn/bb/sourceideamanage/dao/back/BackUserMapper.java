package cn.bb.sourceideamanage.dao.back;

import cn.bb.sourceideamanage.entity.Team;
import cn.bb.sourceideamanage.dto.back.BackUser;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface BackUserMapper {
    @Select("select  * from team where team_id IN " +
            "(select team_id from user_team where user_id = #{userId})")
    public List<Team> findAllTeamByUserId(Integer userId);

    @Select("select distinct  u.user_id AS userId, u.user_id, u.user_name As userName,u.user_msg As userMsg,u.user_create_time AS userCreateTime " +
            " from user u ,user_team ut , team t" +
            " WHERE u.user_name LIKE '%${userName}%' ")
    @Results(
            //colums是数据库列,以这个id为一组 , property是back_user对象里面的属性
            @Result(column = "user_id",javaType = List.class, property = "backTeams",
            many = @Many(   //一对多
                    select="cn.bb.sourceideamanage.dao.back.BackUserMapper.findAllTeamByUserId", //上面搜索的方法
                    fetchType = FetchType.LAZY
            ))
    )
    public List<BackUser> findAllBackUser(@Param("userName") String userName);



}
