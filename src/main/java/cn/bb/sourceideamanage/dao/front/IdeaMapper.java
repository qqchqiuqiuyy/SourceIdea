package cn.bb.sourceideamanage.dao.front;

import cn.bb.sourceideamanage.dto.front.ChildComment;
import cn.bb.sourceideamanage.entity.Comment;
import cn.bb.sourceideamanage.dto.front.FrontIdea;
import cn.bb.sourceideamanage.dto.front.IdeaMsg;
import cn.bb.sourceideamanage.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author bobo
 */
@Mapper
public interface IdeaMapper {
    /**
     * 根据团队id查找团队信息
     * @param teamId 团队名
     * @param state 1删除 0未删除
     * @return
     */
    @Select(" SELECT " +
            " team_id, " +
            " team_name, " +
            " team_msg, " +
            " team_nums, " +
            " team_create_time  " +
            "FROM " +
            " team  " +
            "WHERE " +
            " team_id = #{teamId} AND is_delete = #{isDelete}")
    public Team findTeam(@Param("teamId") Integer teamId,@Param("isDelete") Integer state );


    /**
     * 根据用户id查找用户信息
     * @param userId    用户id
     * @param state    1删除 0未删除
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
            " user_id = #{userId}" +
            "   AND is_delete = #{isDelete}")
    public User findUser(@Param("userId") Integer userId ,@Param("isDelete") Integer state);

    /**
     *  根据标签id 返回标签信息
     * @param tagId 标签id
     * @return
     */
    @Select("SELECT " +
            " tag_id, " +
            " tag_name  " +
            "FROM " +
            " tag  " +
            "WHERE " +
            " tag_id = #{tagId}")
    public Tag findTag(@Param("tagId") Integer tagId);


    /**
     * 根据 想法id 返回评论想法内容
     * @param ideaId    想法id
     * @param state     1删除 0未删除
     * @return
     */
    @Select("SELECT " +
            " id, " +
            " idea_id, " +
            " user_id, " +
            " content, " +
            " comment_time  " +
            "FROM " +
            " comment_idea  " +
            "WHERE " +
            " idea_id = #{ideaId}" +
            "   AND is_delete = #{isDelete}")
    public List<CommentIdea> findComment(@Param("ideaId") Integer ideaId ,@Param("isDelete") Integer state);


    /**
     * 根据以下参数 返回想法模块的分页信息
     * @param tagName   标签名
     * @param ideaName  想法名
     * @param state 1删除 0未删除
     * @return
     */
    @Select("SELECT " +
            " i.idea_id AS ideaId, " +
            " i.idea_name AS ideaName, " +
            " i.idea_msg AS ideaMsg, " +
            " t.tag_name AS ideaTag, " +
            " i.idea_supports AS ideaSupports, " +
            " i.idea_create_time AS ideaCreateTime, " +
            " u.user_name AS ideaUserName  " +
            "FROM " +
            " tag t, " +
            " idea i, " +
            " USER u  " +
            "WHERE " +
            " t.tag_id = i.tag_id  " +
            " AND t.tag_name LIKE CONCAT( " +
            " '%',#{tagName},'%') AND i.idea_name LIKE CONCAT('%',#{ideaName},'%') " +
            " AND u.user_id = i.user_id  " +
            " AND i.is_delete = #{isDelete} AND u.is_delete = #{isDelete} " +
            "ORDER BY " +
            " ideaCreateTime DESC" )
    public List<FrontIdea> findAllFrontIdea(@Param("tagName") String tagName,@Param("ideaName") String ideaName ,@Param("isDelete") Integer state);


    /**
     * 根据想法id 返回想法相关信息
     * @param ideaId    想法id
     * @param state 1删除 0未删除
     * @return
     */
    @Select("SELECT " +
            " i.idea_msg AS ideaMsg, " +
            " i.idea_name AS ideaName, " +
            " i.idea_create_time AS ideaCreateTime, " +
            " u.user_name AS ideaUser  " +
            "FROM " +
            " idea i, " +
            " USER u  " +
            "WHERE " +
            " i.idea_id = #{ideaId} AND i.user_id = u.user_id AND i.is_delete = #{isDelete} AND u.is_delete = #{isDelete}")
    public IdeaMsg getIdeaMsg(@Param("ideaId") Integer ideaId,@Param("isDelete") Integer state);


    /**
     * 返回10条点赞最高的想法
     * @param state 1删除 0未删除
     * @return
     */
    @Select("SELECT " +
            " i.idea_name, " +
            " i.idea_supports, " +
            " i.idea_id  " +
            "FROM " +
            " idea i  " +
            "WHERE " +
            " i.is_delete = #{isDelete} " +
            "ORDER BY " +
            " i.idea_supports DESC  " +
            " LIMIT 0, ${nums}")
    public List<Idea> getIdeaSupportsRank(@Param("isDelete") Integer state,
                                          @Param("nums") Integer nums);


    /**
     * 根据用户id  返回该用户发表的所有想法
     * @param userId    用户id
     * @param state     1删除 0未删除
     * @return
     */
    @Select("SELECT " +
            " u.user_name AS ideaUserName, " +
            " i.idea_id AS ideaId, " +
            " i.idea_name AS ideaName, " +
            " i.idea_msg AS ideaMsg, " +
            " t.tag_name AS ideaTag, " +
            " i.idea_supports AS ideaSupports, " +
            " i.idea_create_time AS ideaCreateTime  " +
            "FROM " +
            " USER u, " +
            " idea i, " +
            " tag t  " +
            "WHERE " +
            " u.user_id = #{userId} AND u.user_id = i.user_id AND i.tag_id = t.tag_id\n" +
            " AND u.is_delete = #{state} AND i.is_delete = #{state} ")
    public List<FrontIdea> findAllIdea(@Param("userId") Integer userId,@Param("isDelete") Integer state);


    /**
     * 分页显示该用户自己的想法所有信息
     * @param tagName   标签名
     * @param ideaName  想法名
     * @param userId    用户id
     * @param state     1删除 0未删除
     * @return
     */
    @Select("select i.idea_id AS ideaId, i.idea_name AS ideaName, i.idea_msg AS ideaMsg,t.tag_name AS ideaTag ," +
            " i.idea_supports AS ideaSupports, i.idea_create_time AS ideaCreateTime , u.user_name AS ideaUserName" +
            " from tag t, idea i, user u" +
            " where t.tag_id = i.tag_id AND t.tag_name LIKE CONCAT('%',#{tagName},'%') AND i.idea_name LIKE CONCAT('%',#{ideaName},'%') " +
            "  AND u.user_id = i.user_id " +
            "  AND u.user_id = #{userId} " +
            "   AND i.is_delete = #{isDelete} " +
            "   AND u.is_delete = #{isDelete}" +
            "  ORDER BY ideaCreateTime DESC" )
    public List<FrontIdea> findAllMyIdea(@Param("tagName") String tagName,@Param("ideaName") String ideaName,@Param("userId") Integer userId,@Param("isDelete") Integer state);


    /**
     * 根据想法id 得到想法名
     * @param ideaId 想法id
     * @param state 1删除 0未删除
     * @return
     */
    @Select("SELECT  idea_name" +
            " FROM idea i" +
            "   WHERE i.idea_name = #{ideaId} AND i.is_delete = #{isDelete}")
    public String getIdeaName(@Param("ideaId") Integer ideaId,@Param("isDelete") Integer state);


    /**
     * 根据想法id 查找该想法的所有评论 及其子评论
     * @param ideaId 想法id
     * @param state 1删除 0未删除
     * @return
     */
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
            "           ci.id as uuid " +
            " FROM comment_idea ci" +
            " WHERE ci.idea_id = #{ideaId} AND ci.is_delete = #{isDelete}" +
            "       AND ci.uid = 0")
    @Results({
            @Result(column = "uuid",javaType = List.class, property = "childComments",
                    many = @Many(   //一对多
                            select="cn.bb.sourceideamanage.dao.front.IdeaMapper.getChildComment" //上面搜索的方法
                    ))
    })
    public List<Comment> getAllComment(@Param("ideaId") Integer ideaId,@Param("isDelete") Integer state);


    /**
     * 返回该父评论下的所有子评论
     * @param uuid  父评论
     * @return
     */
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


    /**
     * 通过用户名返回用户id
     * @param userName 用户名
     * @return
     */
    @Select("SELECT user_id from user where user_name = #{userName}")
    public Integer getUserId(@Param("userName") String userName);


    /**
     * 评论想法
     * @param content 评论内容
     * @param ideaName  评论的想法名
     * @param ideaId    想法id
     * @param userId    用户id
     * @param userName  用户名
     */
    @Insert("INSERT INTO comment_idea ( idea_id, user_id, content, user_name, idea_name ) " +
            "VALUES " +
            " (#{ideaId},#{userId},#{content},#{userName},#{ideaName})")
    public void commentIdea(@Param("content") String content,@Param("ideaName") String ideaName,
                            @Param("ideaId") Integer ideaId,@Param("userId") Integer userId,
                            @Param("userName") String userName);

    /**
     * 添加一个想法
     * @param ideaName  想法名
     * @param tagId 标签id
     * @param ideaMsg   想法信息
     * @param userId    用户id
     */
    @Insert("INSERT INTO idea (tag_id,user_id,idea_msg,idea_name) " +
            "VALUES" +
            "   (#{tagId},#{userId},#{ideaMsg},#{ideaName})")
    public void addIdea(@Param("ideaName") String ideaName, @Param("tagId") Integer tagId,
                        @Param("ideaMsg") String ideaMsg,   @Param("userId") Integer userId);

    /**
     * 增加团队想法
     * @param ideaName 想法名
     * @param tagId 标签id
     * @param ideaMsg   标签信息
     * @param userId    用户id
     * @param teamId    团队id
     */
    @Insert("INSERT INTO idea (tag_id,user_id,idea_msg,idea_name,team_id) " +
            "VALUES " +
            "   (#{tagId},#{userId},#{ideaMsg},#{ideaName},#{teamId})")
    public void addTeamIdea(@Param("ideaName") String ideaName, @Param("tagId") Integer tagId,
                        @Param("ideaMsg") String ideaMsg,   @Param("userId") Integer userId,
                               @Param("teamId") Integer teamId);

    /**
     * 找到该团队的所有想法
     * @param teamId    团队id
     * @param state     1删除 0未删除
     * @return
     */
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
            "i.team_id = #{teamId} AND i.tag_id = t.tag_id  AND i.is_delete = #{isDelete} " +
            "ORDER BY ideaCreateTime DESC")
    public List<FrontIdea> findAllTeamIdea(@Param("teamId") Integer teamId,@Param("isDelete") Integer state);


    /**
     * 删除想法
     * @param ideaId    想法id
     * @param state     1删除 0未删除
     */
    @Update("Update  idea " +
            "   SET " +
            "   is_delete = #{isDelete}" +
            "   WHERE " +
            "   idea_id = #{ideaId}")
    public void delIdea(@Param("ideaId") Integer ideaId,@Param("isDelete") Integer state);


    /**
     * 删除想法评论
     * @param ideaId 想法id
     * @param state 1删除 0未删除
     */
    @Update("Update  comment_idea " +
            "   SET is_delete = #{isDelete} " +
            "   WHERE " +
            "   idea_id = #{ideaId}")
    public void delIdeaComment(@Param("ideaId") Integer ideaId,@Param("isDelete") Integer state);


    /**
     * 持久化点赞
     * @param ideId 想法id
     * @param supports  点赞数
     * @param state 1删除 0未删除
     */
    @Update("UPDATE idea " +
            "   SET " +
            "   idea_supports = #{supports} " +
            "   WHERE " +
            "   idea_id = #{ideaId} AND is_delete = #{isDelete}")
    public void durSupports(@Param("ideaId") Integer ideId, @Param("supports") Long supports
                              ,@Param("isDelete") Integer state);


    /**
     * 在评论下面评论
     * @param uid   父评论id
     * @param ideaId    想法id
     * @param ideaName  想法名
     * @param userId    用户id
     * @param userName  用户名
     * @param parentId  父评论用户id
     * @param parentName    父评论用户名
     * @param content   内容
     */
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


    /**
     * 返回头脑风暴时间
     * @return
     */
    @Select("SELECT id,time FROM brainstorming")
    public List<BrainTime> getAllBrainTime();

    /**
     * 根据头脑风暴id返回时间
     * @param brainid   头脑风暴id
     * @return
     */
    @Select("SELECT time FROM brainstorming WHERE id = #{id}")
    public Integer getBrainTime(@Param("id") Integer brainid);


    /**
     * 通过想法名得到想法id
     * @param ideaName 想法名
     * @param state 1删除 0未删除
     * @return
     */
    @Select("SELECT  idea_name from idea " +
            "   where " +
            "   idea_name = #{ideaName} AND is_delete = #{isDelete}" +
            "   GROUP BY idea_name")
    public String getIdeaIdByIdeaName(@Param("ideaName") String ideaName,
                                       @Param("isDelete") Integer state);
}

