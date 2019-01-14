package cn.bb.sourceideamanage.dao.front;

import cn.bb.sourceideamanage.dto.front.FrontIdea;
import cn.bb.sourceideamanage.dto.front.IdeaMsg;
import cn.bb.sourceideamanage.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IdeaMapper {

    @Select("select * from team where team_id = #{teamId}")
    public Team findTeam(@Param("teamId") Integer teamId);

    @Select("select * from user where user_id = #{userId}")
    public User findUser(@Param("userId") Integer userId);

    @Select("select * from tag where tag_id = #{tagId}")
    public Tag findTag(@Param("tagId") Integer tagId);

    @Select("select * from commentIdea where idea_id = #{ideaId}")
    public List<commentIdea> findComment(@Param("ideaId") Integer ideaId);

    @Select("select i.idea_id AS ideaId, i.idea_name AS ideaName, i.idea_msg AS ideaMsg,t.tag_name AS ideaTag ," +
            " i.idea_supports AS ideaSupports, i.idea_create_time AS ideaCreateTime , u.user_name AS ideaUserName" +
            " from tag t, idea i, user u" +
            " where t.tag_id = i.tag_id AND t.tag_name LIKE '%${tagName}%' AND i.idea_name LIKE '%${ideaName}%'" +
            "  AND u.user_id = i.user_id " +
            "  ORDER BY ideaCreateTime DESC" )
    public List<FrontIdea> findAllFrontIdea(@Param("tagName") String tagName,@Param("ideaName") String ideaName);

    @Select("select i.idea_msg AS ideaMsg,i.idea_name AS ideaName,i.idea_create_time AS ideaCreateTime, " +
            " u.user_name AS ideaUser" +
            " from  idea i, user u" +
            " where i.idea_id = #{ideaId} AND i.user_id = u.user_id")
    public IdeaMsg getIdeaMsg(Integer ideaId);

    @Select("select i.idea_name ,i.idea_supports, i.idea_id" +
            " from idea i " +
            " ORDER BY i.idea_supports DESC" +
            " limit 0 ,10")
    public List<Idea> getTagSupports();
}
