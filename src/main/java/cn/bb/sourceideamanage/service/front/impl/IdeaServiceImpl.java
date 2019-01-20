package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.common.enums.IdeaSupportsKey;
import cn.bb.sourceideamanage.dao.front.IdeaMapper;
import cn.bb.sourceideamanage.dao.front.TeamMapper;
import cn.bb.sourceideamanage.dto.front.FrontIdea;
import cn.bb.sourceideamanage.dto.front.IdeaMsg;
import cn.bb.sourceideamanage.entity.Comment;
import cn.bb.sourceideamanage.entity.*;
import cn.bb.sourceideamanage.service.front.IdeaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Slf4j
@CacheConfig(cacheNames = "IdeaServiceImpl")
public class IdeaServiceImpl implements IdeaService {

    @Resource
    IdeaMapper ideaMapper;

    @Autowired
    Jedis jedis;

    @Autowired
    JSONObject jsonObject;

    @Resource
    TeamMapper teamMapper;

    @Override
    public Team findTeam(Integer teamId) {
        return ideaMapper.findTeam(teamId);
    }

    @Override
    public User findUser(Integer userId) {
        return ideaMapper.findUser(userId);
    }

    @Override
    public Tag findTag(Integer tagId) {
        return ideaMapper.findTag(tagId);
    }

    @Override
    public List<commentIdea> findComment(Integer ideaId) {
        return ideaMapper.findComment(ideaId);
    }

    @Override
    @Cacheable( cacheNames = "ideaPage",key = "'findAllFrontIdea=[page='+#page+'][idea=Name='+#ideaName+'][tagName='+#tagName+']'")
    public PageInfo<FrontIdea> findAllFrontIdea(int page, int size, String ideaName, String tagName) {
        PageHelper.startPage(page,size);
        List<FrontIdea> allFrontIdea = ideaMapper.findAllFrontIdea(tagName, ideaName);
        return new PageInfo<>(allFrontIdea);
    }

    @Override
    @Cacheable(cacheNames = "ideaMsg", key = "'ideaMsg=[ideaId='+#ideaId+']'")
    public IdeaMsg getIdeaMsg(Integer ideaId) {
        return ideaMapper.getIdeaMsg(ideaId);
    }

    @Override
    @Cacheable(cacheNames = "supportsList",key = "'ideaSupports'")
    public List<Idea> getIdeaSupports() {
        return ideaMapper.getTagSupports();
    }

    /**
     * 点赞 userId存在Set里面 计算set大小即可
     * @param ideaId
     * @param userId
     * @return
     */
    @Override
    public String upIdeaSupports(String ideaId, String userId) {
        String userSetKey = IdeaSupportsKey.UserSetKey.getKey();
        if(jedis.sismember(IdeaSupportsKey.UserSetKey.getKey()+ ideaId,userId)){
            jedis.srem(userSetKey+ideaId,userId);
            jsonObject.put("msg","已取消赞");
            jsonObject.put("success","0");
        }else{
            jedis.sadd(userSetKey+ideaId,userId);
            jsonObject.put("success","1");
            jsonObject.put("msg","已点赞");
        }
        return jsonObject.toString();
    }

    @Override
    public List<FrontIdea> findAllIdea(Integer userId) {
        return ideaMapper.findAllIdea(userId);
    }

    @Override
    @Cacheable(cacheNames = "myIdeas",key = "'myIdeas=[page='+#page+'][tagName='+#tagName+'][ideaName='+#ideaName+'[userId='+#userId+']'")
    public PageInfo<FrontIdea> findAllMyIdea(int page, int size,String tagName, String ideaName, Integer userId) {
        PageHelper.startPage(page,size);
        List<FrontIdea> allFrontIdea = ideaMapper.findAllMyIdea(tagName, ideaName,userId);
        return new PageInfo<>(allFrontIdea);
    }

    @Override
    public String getIdeaName(Integer ideaId) {
        return ideaMapper.getIdeaName(ideaId);
    }

    @Override
    @Cacheable(cacheNames = "comments",key = "'comments=['+#ideaId+']'")
    public List<Comment> getAllComment(Integer ideaId) {
        return ideaMapper.getAllComment(ideaId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "comments", key = "'comments=['+#ideaId+']'")
    public void commentIdea(String content, String ideaName, Integer ideaId, HttpServletRequest request) {
        try {
            Integer userId = (Integer)request.getSession().getAttribute("userId");
            String userName = (String)request.getSession().getAttribute("userName");
            ideaMapper.commentIdea(content,ideaName,ideaId,userId,userName);
            log.info("评论成功!");
        }catch (Exception e){
            log.error("评论错误!!e={}",e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames =  "myIdeas", allEntries = true)
    public void addIdea(String ideaName, Integer tagId, String ideaMsg, HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            ideaMapper.addIdea(ideaName,tagId,ideaMsg,userId);
            log.info("添加想法成功!!");
        }catch (Exception e){
            log.error("添加想法失败!!");
        }
    }

    @Override
    @CacheEvict(cacheNames = "teamIdeas",key = "'teamIdeas=[teamName='+#teamNam+']'")
    public void addTeamIdea(String ideaName, Integer tagId, String ideaMsg, String teamName, HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            Integer teamId = teamMapper.findTeamId(teamName);
            ideaMapper.addTeamIdea(ideaName,tagId,ideaMsg,userId,teamId);
            log.info("添加想法成功!!");
        }catch (Exception e){
            log.error("添加想法失败!!");
        }
    }

    @Override
    @Cacheable(cacheNames = "teamIdeas",key = "'teamIdeas=[teamName='+#teamNam+']'")
    public List<FrontIdea> findAllTeamIdea(String teamName) {
        Integer teamId = teamMapper.findTeamId(teamName);
        List<FrontIdea> ideas = ideaMapper.findAllTeamIdea(teamId);
        return ideas;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "myIdeas",allEntries = true)
    public String delIdea(Integer ideaId) {
        try {
            ideaMapper.delIdea(ideaId);
            log.info("删除想法成功!");
            ideaMapper.delIdeaComment(ideaId);
            log.info("删除想法评论成功!");
            jsonObject.put("msg","删除想法成功!");
            jsonObject.put("success","1");
        }catch (Exception e){
            log.error("删除想法失败!");
            jsonObject.put("msg","删除想法失败!!");
            jsonObject.put("success","0");
        }

        return jsonObject.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void durSupports(Integer ideId,Long supports) {
        try {
            ideaMapper.durSupports( ideId, supports);
            log.info("点赞刷回数据库成功!");
        }catch (Exception e){
            log.error("点赞刷回数据库失败!! e={}",e);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String commentIdeaUser(Integer uid,Integer ideaId,String ideaName,
                                  Integer userId,String userName, Integer parentId, String parentName ,String content ) {
        try{
            ideaMapper.commentIdeaUser(uid,ideaId,ideaName,userId,userName,parentId,parentName,content);
            log.info("评论 某人的评论成功!");
            jsonObject.put("msg","评论成功!");
            jsonObject.put("success","1");
        }catch (Exception e){
            log.error("评论失败!e={}",e.getMessage());
            jsonObject.put("msg","评论失败!");
            jsonObject.put("success","0");
        }
        return jsonObject.toString();
    }


}
