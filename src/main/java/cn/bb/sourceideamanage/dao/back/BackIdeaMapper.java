package cn.bb.sourceideamanage.dao.back;

import cn.bb.sourceideamanage.dto.back.BackIdea;
import cn.bb.sourceideamanage.entity.Tag;
import cn.bb.sourceideamanage.entity.Team;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * @ahuthor b
 */
@Mapper
public interface BackIdeaMapper {
    /**
     * 根据userId找到所有的团队
     * @param userId
     * @return
     */
    @Select("select  team_id AS teamId," +
            "        team_name AS teamName" +
            "        team_msg AS teamMsg" +
            "        team_nums AS teamNums" +
            "        team_create_time AS teamCreateTime" +
            "from team where team_id IN " +
            "(select team_id from user_team where user_id = #{user_id})")
    public List<Team> findAllTeamByUserId(@Param("user_id") Integer userId);


    /**
     * 根据想法名和想法标签  查找所有的想法
     * @param ideaName
     * @param tagName
     * @return
     */
    @Select("select u.user_id ,u.user_name AS userName,t.tag_name AS tagName, i.idea_msg AS ideaMsg," +
            "   i.idea_supports AS ideaSupports ,i.idea_create_time AS ideaCreateTime, i.idea_id AS ideaId" +
            " from user u, idea i,tag t  " +
            " where t.tag_name LIKE CONCAT('%',#{tagName},'%') AND idea_name LIKE CONCAT('%',#{ideaName},'%') AND i.user_id = u.user_id AND t.tag_id = i.tag_id")
    @Results(
            //colums是数据库列,以这个id为一组 , property是back_user对象里面的属性
            @Result(column = "user_id",javaType = List.class, property = "userTeams",
                    many = @Many(   //一对多
                            select="cn.bb.sourceideamanage.dao.back.BackIdeaMapper.findAllTeamByUserId", //上面搜索的方法
                            fetchType = FetchType.LAZY
                    ))
    )
    public List<BackIdea> findAllBackIdea(@Param("ideaName") String ideaName, @Param("tagName") String tagName);


    /**
     * 查找所有的标签
     * @return
     */
    @Select("select * from tag")
    public List<Tag> findAllTag();

    /**
     * 删除评论
     * @param ideaId
     */
    @Delete("delete from  idea where idea_id = #{ideaId}")
    public void deleteIdea(Integer ideaId);

    @Delete("DELETE from comment_idea WHERE idea_id = #{ideaId}")
    public void deleteIdeaComment(@Param("ideaId") Integer ideaId);
 }
