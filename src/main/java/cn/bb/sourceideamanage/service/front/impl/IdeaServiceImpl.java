package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.common.enums.BrainKey;
import cn.bb.sourceideamanage.common.enums.IdeaSupportsKey;
import cn.bb.sourceideamanage.dao.front.IdeaMapper;
import cn.bb.sourceideamanage.dao.front.TeamMapper;
import cn.bb.sourceideamanage.dao.front.UserMapper;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class IdeaServiceImpl implements IdeaService {

    @Resource
    IdeaMapper ideaMapper;

    @Autowired
    Jedis jedis;

    @Autowired
    JSONObject jsonObject;

    @Resource
    TeamMapper teamMapper;

    @Resource
    UserMapper userMapper;

    @Override
    public Team findTeam(Integer teamId) {
        return ideaMapper.findTeam(teamId);
    }

    @Override
    public User findUser(Integer userId) {
        return ideaMapper.findUser(userId);
    }

    @Override
    @Cacheable(cacheNames = "findTag" ,key = "'tagId='+#tagId")
    public Tag findTag(Integer tagId) {
        return ideaMapper.findTag(tagId);
    }

    @Override
    public List<CommentIdea> findComment(Integer ideaId) {
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
        return ideaMapper.getIdeaSupportsRank();
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

    /**
     * 评论想法
     */
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

    /**
     * 删除想法
     * @param ideaId
     * @return
     */
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

    /**
     *
     * @param ideId
     * @param supports
     */
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

    /**
     * 评论想法部分
     * @param uid
     * @param ideaId
     * @param ideaName
     * @param userId
     * @param userName
     * @param parentId
     * @param parentName
     * @param content
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "comments" ,key = "'comments=['+#ideaId+']'")
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


    @Override
    @Cacheable(cacheNames = "allBrainTime",key = "'findAllTime'")
    public List<BrainTime> getAllBrainTime() {
        return ideaMapper.getAllBrainTime();
    }



    @Override
    public String addBrainStorming(Integer userId ,String brainName, Integer timeId, String brainMsg) {
        try{
            /*String key = BrainKey.BRAIN_KEY.getKey() +userId.toString();*/
            String key = BrainKey.BRAIN_KEY.getKey() +brainName;
            if(jedis.exists(key)){
                log.error("增加头脑风暴失败!!!");
                jsonObject.put("msg","增加头脑风暴失败!!!名称已重复");
                jsonObject.put("success","0");
                throw new Exception("增加头脑风暴失败");
            }
            String userName = userMapper.getUserName(userId);
            //转换成秒
            Integer brainTime = this.getBrainTime(timeId) * 60;
            Map<String,String> map = new HashMap<>(16);
            map.put("brainName",brainName);
            map.put("brainMsg",brainMsg);
            map.put("supports","0");
            map.put("userId",userId.toString());
            map.put("userName",userName);
            jedis.hmset(key,map);
            jedis.expire(key,brainTime);
            log.info("增加头脑风暴成功!!!");
            jsonObject.put("msg","增加头脑风暴成功!!!");
            jsonObject.put("success","1");
            jsonObject.put("successUrl","/UserC/toMyIdea");
        }catch (Exception e){
            log.error("增加头脑风暴失败!! e={}",e.getMessage());
            jsonObject.put("msg","增加头脑风暴失败!!");
            jsonObject.put("success","0");
        }

        return jsonObject.toString();
    }

    @Override
    @Cacheable(cacheNames = "brainTime" ,key = "'brainId=['+#brainId +']'")
    public Integer getBrainTime(Integer brainid) {
        return ideaMapper.getBrainTime(brainid);
    }


    /**
     * 头脑风暴页面 找出所有的头脑风暴
     * @return
     */
    @Override
    public List<Map<String, String>> allBrains() {
        String cursor = "0";
        ScanResult<String> keys = null;
        List<String> allMapKeys = null;
        List<String> userIds = new ArrayList<>() ;
        List<Map<String,String>> brains = new ArrayList<>();
        ScanParams scanParams = new ScanParams();
        String mach;
        try{
            do {
                mach = BrainKey.BRAIN_KEY.getKey()+"*";
                keys = jedis.scan(cursor, scanParams.match(mach));
                //得到所有的keyName
                allMapKeys = keys.getResult();
                //取出: 后面的userId;
                if(null != allMapKeys && allMapKeys.size() > 0 ){
                    //通过key得到所有的map
                    for(String key : allMapKeys){
                        Map<String, String> map = jedis.hgetAll(key);
                        Long ttl = jedis.ttl(key) / 60;
                        map.put("time",ttl.toString());
                        brains.add(map);
                    }
                }
                cursor = keys.getStringCursor();
            }while (Integer.parseInt(cursor) != 0);

            log.info("去头脑风暴页面成功!!!");
        }catch (Exception e){
            log.error("去头脑风暴页面失败!!e={}",e);
        }
        return brains;
    }

    /**
     * 点赞
     * @param brainName
     * @return
     */
    @Override
    public String upBrainSupports(String brainName,Integer uuserId) {
            String userKey = BrainKey.BRAIN_SUPPORT_USER.getKey() +brainName;
            String supportsKey = BrainKey.BRAIN_KEY.getKey()+brainName;
            String userId = uuserId.toString();
            Boolean exist = jedis.sismember(userKey, userId);
            //判断集合Set之中是否有相同userId  如果有相同的userId 就删除 同时 点赞-1
            if(exist){
                //删除某个用户点赞, 之后点赞数-1
                jedis.srem(userKey,userId);
                jedis.hincrBy(supportsKey,"supports",-1);
                jsonObject.put("msg","取消赞成功!");
                jsonObject.put("msg","取消赞成功!!");
                jsonObject.put("success","0");
            }else{
                //用户点赞
                jedis.sadd(userKey,userId);
                jedis.hincrBy(supportsKey,"supports",1);
                log.info("点赞成功!!");
                jsonObject.put("msg","点赞成功!!");
                jsonObject.put("success","1");
            }


        return jsonObject.toString();
    }


}
