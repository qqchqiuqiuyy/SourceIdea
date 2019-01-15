package cn.bb.sourceideamanage.dao.front;

import cn.bb.sourceideamanage.dto.front.FrontIdea;
import cn.bb.sourceideamanage.dto.front.IdeaMsg;
import cn.bb.sourceideamanage.dto.front.comment;
import cn.bb.sourceideamanage.entity.*;
import org.apache.ibatis.annotations.Insert;
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

    @Select("select * from comment_idea where idea_id = #{ideaId}")
    public List<commentIdea> findComment(@Param("ideaId") Integer ideaId);

    @Select("select i.idea_id AS ideaId, i.idea_name AS ideaName, i.idea_msg AS ideaMsg,t.tag_name AS ideaTag ," +
            " i.idea_supports AS ideaSupports, i.idea_create_time AS ideaCreateTime , u.user_name AS ideaUserName" +
            " from tag t, idea i, user u" +
            " where t.tag_id = i.tag_id AND t.tag_name LIKE CONCAT('%',#{tagName},'%') AND i.idea_name LIKE CONCAT('%',#{ideaName},'%') " +
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

    @Select("select u.user_name AS ideaUserName ,i.idea_id AS ideaId, i.idea_name AS ideaName , " +
            "   i.idea_msg AS ideaMsg , t.tag_name AS ideaTag, i.idea_supports AS ideaSupports" +
            "   ,i.idea_create_time AS ideaCreateTime " +
            " FROM user u, idea i ,tag t" +
            " WHERE u.user_id = #{userId} AND u.user_id = i.user_id AND i.tag_id = t.tag_id")
    public List<FrontIdea> findAllIdea(@Param("userId") Integer userId);

    @Select("select i.idea_id AS ideaId, i.idea_name AS ideaName, i.idea_msg AS ideaMsg,t.tag_name AS ideaTag ," +
            " i.idea_supports AS ideaSupports, i.idea_create_time AS ideaCreateTime , u.user_name AS ideaUserName" +
            " from tag t, idea i, user u" +
            " where t.tag_id = i.tag_id AND t.tag_name LIKE CONCAT('%',#{tagName},'%') AND i.idea_name LIKE CONCAT('%',#{ideaName},'%') " +
            "  AND u.user_id = i.user_id " +
            "  AND u.user_id = #{userId}" +
            "  ORDER BY ideaCreateTime DESC" )
    public List<FrontIdea> findAllMyIdea(@Param("tagName") String tagName,@Param("ideaName") String ideaName,@Param("userId") Integer userId);


    @Select("SELECT  idea_name" +
            " FROM idea i" +
            "   WHERE i.idea_name = #{ideaId}")
    public String getIdeaName(@Param("ideaId") Integer ideaId);


    @Select(" SELECT ci.idea_id AS ideaId,ci.user_id AS userId,ci.user_name AS userName , " +
            "   ci.content AS content , ci.idea_name AS ideaName , ci.comment_time AS commentTime " +
            " FROM comment_idea ci" +
            " WHERE ci.idea_id = #{ideaId}")
    public List<comment> getAllComment(@Param("ideaId") Integer ideaId);

    @Select("SELECT user_id from user where user_name = #{userName}")
    public Integer getUserId(@Param("userName") String userName);

    @Insert("INSERT INTO comment_idea (idea_id,user_id,content,user_name,idea_name) VALUES" +
            "   (#{ideaId},#{userId},#{content},#{userName},#{ideaName})")
    public void commentIdea(@Param("content") String content,@Param("ideaName") String ideaName,
                            @Param("ideaId") Integer ideaId,@Param("userId") Integer userId,
                            @Param("userName") String userName);

    @Insert("INSERT INTO idea (tag_id,user_id,idea_msg,idea_name) VALUES" +
            "   (#{tagId},#{userId},#{ideaMsg},#{ideaName})")
    public void addIdea(@Param("ideaName") String ideaName, @Param("tagId") Integer tagId,
                        @Param("ideaMsg") String ideaMsg,   @Param("userId") Integer userId);

    @Insert("INSERT INTO idea (tag_id,user_id,idea_msg,idea_name,team_id) VALUES" +
            "   (#{tagId},#{userId},#{ideaMsg},#{ideaName},#{teamId})")
    public void addProjectIdea(@Param("ideaName") String ideaName, @Param("tagId") Integer tagId,
                        @Param("ideaMsg") String ideaMsg,   @Param("userId") Integer userId,
                               @Param("teamId") Integer teamId);
}
