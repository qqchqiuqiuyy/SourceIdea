package cn.bb.sourceideamanage.dao.front;

import cn.bb.sourceideamanage.dto.front.ChildComment;
import cn.bb.sourceideamanage.entity.Comment;
import cn.bb.sourceideamanage.dto.front.FrontIdea;
import cn.bb.sourceideamanage.dto.front.IdeaMsg;
import cn.bb.sourceideamanage.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IdeaMapper {

    @Select(" SELECT team_id,team_name,team_msg,team_nums,team_create_time from team where team_id = 30")
    public Team findTeam(@Param("teamId") Integer teamId);

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
            " user_id = #{userId}")
    public User findUser(@Param("userId") Integer userId);

    @Select("select tag_id,tag_name from tag where tag_id = #{tagId}")
    public Tag findTag(@Param("tagId") Integer tagId);

    @Select("SELECT " +
            " id, " +
            " idea_id, " +
            " user_id, " +
            " content, " +
            " comment_time  " +
            "FROM " +
            " comment_idea  " +
            "WHERE " +
            " idea_id = #{ideaId}")
    public List<CommentIdea> findComment(@Param("ideaId") Integer ideaId);

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
    public List<Idea> getIdeaSupportsRank();

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


    @Select(" SELECT    ci.id As id ," +
            "           ci.uid AS uid, " +
            "           ci.idea_id AS ideaId," +
            "           ci.user_id AS userId," +
            "           ci.user_name AS userName , " +
            "           ci.content AS content , " +
            "           ci.idea_name AS ideaName , " +
            "           ci.comment_time AS commentTime ," +
            "           ci.parent_id AS parentId ," +
            "           ci.parent_name AS parentName ," +
            "           ci.id as uuid" +
            " FROM comment_idea ci" +
            " WHERE ci.idea_id = #{ideaId}")
    @Results({
            @Result(column = "uuid",javaType = List.class, property = "childComments",
                    many = @Many(   //一对多
                            select="cn.bb.sourceideamanage.dao.front.IdeaMapper.getChildComment" //上面搜索的方法
                            //,fetchType = FetchType.LAZY
                    ))
    })
    public List<Comment> getAllComment(@Param("ideaId") Integer ideaId);

    @Select("SELECT " +
            " ci.id AS id, " +
            " ci.uid AS uid, " +
            " ci.idea_id AS ideId, " +
            " ci.idea_id AS ideaId, " +
            " ci.idea_name AS ideaName, " +
            " ci.user_id AS userId, " +
            " ci.user_name AS userName, " +
            " ci.parent_id AS parentId, " +
            " ci.parent_name AS parentName, " +
            " ci.content AS content, " +
            " ci.comment_time AS commentTime  " +
            "FROM " +
            " comment_idea ci " +
            "WHERE ci.uid = #{uuid}")
    public List<ChildComment> getChildComment(@Param("uuid") Integer uuid);

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
    public void addTeamIdea(@Param("ideaName") String ideaName, @Param("tagId") Integer tagId,
                        @Param("ideaMsg") String ideaMsg,   @Param("userId") Integer userId,
                               @Param("teamId") Integer teamId);

    @Select("SELECT " +
            "i.idea_id AS ideaId, " +
            "i.idea_name AS ideaName, " +
            "i.idea_msg AS ideaMsg, " +
            "t.tag_name AS ideaTag, " +
            "i.team_id AS teamId," +
            "i.idea_supports AS ideaSupports, " +
            "i.idea_create_time AS ideaCreateTime  " +
            "FROM " +
            "idea i, " +
            "tag t  " +
            "WHERE " +
            "i.team_id = #{teamId} AND i.tag_id = t.tag_id " +
            "ORDER BY ideaCreateTime DESC")
    public List<FrontIdea> findAllTeamIdea(@Param("teamId") Integer teamId);


    @Delete("DELETE FROM idea" +
            "   WHERE idea_id = #{ideaId}")
    public void delIdea(@Param("ideaId") Integer ideaId);

    @Delete("DELETE FROM  comment_idea  WHERE idea_id = #{ideaId}")
    public void delIdeaComment(@Param("ideaId") Integer ideaId);




    @Update("UPDATE idea SET idea_supports = #{supports} WHERE idea_id = #{ideaId}")
    public void durSupports(@Param("ideaId") Integer ideId, @Param("supports") Long supports);



    @Insert("INSERT INTO comment_idea " +
            "   (uid,idea_id,idea_name," +
            "       user_id, user_name," +
            "       parent_id,parent_name," +
            "       content) " +
            " VALUES " +
            "   (#{uid} ,#{ideaId} ,#{ideaName}," +
            "     #{userId},#{userName},#{parentId},#{parentName},#{content})")
    public void commentIdeaUser(@Param("uid") Integer uid,@Param("ideaId") Integer ideaId,
                                @Param("ideaName") String ideaName,@Param("userId") Integer userId,
                                @Param("userName") String userName,@Param("parentId") Integer parentId,
                                @Param("parentName") String parentName,@Param("content") String content);




    @Select("SELECT id,time FROM brainstorming")
    public List<BrainTime> getAllBrainTime();

    @Select("SELECT time FROM brainstorming WHERE id = #{id}")
    public Integer getBrainTime(@Param("id") Integer brainid);
}

