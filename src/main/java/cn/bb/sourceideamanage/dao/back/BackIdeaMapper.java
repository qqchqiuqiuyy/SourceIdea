package cn.bb.sourceideamanage.dao.back;

import cn.bb.sourceideamanage.dto.back.BackIdea;
import cn.bb.sourceideamanage.entity.Tag;
import cn.bb.sourceideamanage.entity.Team;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
@Mapper
public interface BackIdeaMapper {
    @Select("select  * from team where team_id IN " +
            "(select team_id from user_team where user_id = #{user_id})")
    public List<Team> findAllTeamByUserId(@Param("user_id") Integer userId);


    @Select("select u.user_id ,u.user_name AS userName,t.tag_name, i.idea_msg, i.idea_supports,i.idea_create_time, i.idea_id" +
            " from user u, idea i,tag t  " +
            " where t.tag_name LIKE '%${tagName}%' AND idea_name LIKE '%${ideaName}%' AND i.user_id = u.user_id AND t.tag_id = i.tag_id")
    @Results(
            //colums是数据库列,以这个id为一组 , property是back_user对象里面的属性
            @Result(column = "user_id",javaType = List.class, property = "userTeams",
                    many = @Many(   //一对多
                            select="cn.bb.sourceideamanage.dao.back.BackIdeaMapper.findAllTeamByUserId", //上面搜索的方法
                            fetchType = FetchType.LAZY
                    ))
    )
    public List<BackIdea> findAllBackIdea(@Param("ideaName") String ideaName, @Param("tagName") String tagName);



    @Select("select * from tag")
    public List<Tag> findAllTag();

    //TODO 需要连同评论表一起删
    @Delete("delete from  idea where idea_id = #{ideaId}")
    public void deleteIdea(Integer ideaId);
 }
