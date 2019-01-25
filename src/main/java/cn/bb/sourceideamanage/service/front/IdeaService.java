package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.entity.Comment;
import cn.bb.sourceideamanage.dto.front.FrontIdea;
import cn.bb.sourceideamanage.dto.front.IdeaMsg;
import cn.bb.sourceideamanage.entity.*;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 对想法的操作
 * @author bobo
 *
 */
public interface IdeaService {

    /**
     * 根据团队id查找团队信息
     * @param teamId 团队id
     * @return 团队
     */
    public Team findTeam(Integer teamId);

    /**
     * 根据用户id查找用户信息
     * @param userId    用户id
     * @return 用户信息
     */
    public User findUser(Integer userId);

    /**
     * 根据标签id 返回标签信息
     * @param tagId 标签id
     * @return Tag
     */
    public Tag findTag(Integer tagId);

    /**
     * 根据 想法id 返回评论想法内容
     * @param ideaId    想法id
     * @return CommentIdea
     */
    public List<CommentIdea> findComment(Integer ideaId);

    /**
     * 根据以下参数 返回想法模块的分页信息
     *
     * @param page 当前页
     * @param size  每页数量
     * @param ideaName  想法名
     * @param tagName   标签名
     * @return
     */
    public PageInfo<FrontIdea> findAllFrontIdea(int page, int size, String ideaName, String tagName);

    /**
     * 根据想法id 返回想法相关信息
     * @param ideaId    想法id
     * @return
     */
    public IdeaMsg getIdeaMsg(Integer ideaId);

    /**
     *返回10条点赞最高的想法
     * @return
     */
    public List<Idea> getIdeaSupports();

    /**
     * 根据想法id  用户id  定位到某个想法 点赞数+1
     * @param ideaId    想法id
     * @param userId    用户id
     * @return
     */
    public String upIdeaSupports(String ideaId, String userId);

    /**
     * 根据用户id  返回该用户发表的所有想法
     * @param userId 用户id
     * @return
     */
    public List<FrontIdea> findAllIdea(Integer userId);

    /**
     * 分页显示该用户自己的想法所有信息
     * @param page  第几页
     * @param size  一页数量
     * @param tagName   标签名
     * @param ideaName  想法名
     * @param userId    用户id
     * @return
     */
    public PageInfo<FrontIdea> findAllMyIdea(int page, int size,String tagName,String ideaName, Integer userId);

    /**
     * 根据想法id 得到想法名
     * @param ideaId 想法id
     * @return
     */
    public String getIdeaName(Integer ideaId);

    /**
     * 根据想法id 得到该想法所有评论
     * @param ideaId 想法id
     * @return
     */
    public List<Comment> getAllComment(Integer ideaId);

    /**
     * 评论该想法
     * @param content 内容
     * @param ideaName 想法名
     * @param ideaId  想法id
     * @param request
     */
    public void commentIdea(String content, String ideaName, Integer ideaId , HttpServletRequest request);

    /**
     * 添加想法
     * @param ideaName 想法名
     * @param tagId 标签id
     * @param ideaMsg 想法内容
     * @param request
     */
    public String addIdea(String ideaName,Integer tagId,String ideaMsg,HttpServletRequest request);

    /**
     * 增加团队想法
     * @param ideaName 想法名
     * @param tagId 标签id
     * @param ideaMsg   想法信息
     * @param teamName  团队名
     * @param request
     */
    public String addTeamIdea(String ideaName,Integer tagId,String ideaMsg,String teamName,HttpServletRequest request);

    /**
     * 根据团队名查找该团队所有想法
     * @param teamName 团队名
     * @return
     */
    public List<FrontIdea> findAllTeamIdea(String teamName);

    /**
     * 根据想法id删除某条想法
     * @param ideaId    想法id
     * @return
     */
    public String delIdea(Integer ideaId);

    /**
     * 将想法持久化回数据库
     * @param ideId  想法id
     * @param supports  点赞数
     */
    public void durSupports(Integer ideId,Long supports);

    /**
     * 评论 在评论区评论的用户
     * 互相评论
     * @param uid   评论父节点id, 最顶层
     * @param ideaId    想法id
     * @param ideaName  想法名
     * @param userId    用户id
     * @param userName  用户名
     * @param parentId  回复人id
     * @param parentName    回复人名字
     * @param content   内容
     * @return
     */
    public String commentIdeaUser(Integer uid,Integer ideaId,String ideaName,
                                  Integer userId,String userName, Integer parentId, String parentName,
                                  String content);

    /**
     * 获取所有的头脑风暴
     * @return
     */
    public List<BrainTime> getAllBrainTime();

    /**
     * 增加头脑风暴
     * @param userId    发出增加的用户id
     * @param brainName 头脑风暴名
     * @param timeId    团队id
     * @param brainMsg  头脑风暴信息
     * @return
     */
    public String addBrainStorming( Integer userId ,String brainName,Integer timeId,String brainMsg );

    /**
     * 得到头脑风暴剩余时间
     * @param brainid 头脑风暴id
     * @return
     */
    public Integer getBrainTime(Integer brainid);

    /**
     * 获得所有的头脑风暴
     * @return
     */
    public List<Map<String ,String >> allBrains();

    /**
     * 更新头脑风暴点赞数
     * @param brainName 头脑风暴名
     * @param userId    用户id
     * @return
     */
    public String upBrainSupports(String brainName,Integer userId);
}
