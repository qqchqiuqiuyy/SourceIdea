package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.common.CacheConstant.CacheConstant;
import cn.bb.sourceideamanage.common.enums.BrainKey;
import cn.bb.sourceideamanage.common.enums.IdeaSupportsKey;
import cn.bb.sourceideamanage.common.enums.IsDelete;
import cn.bb.sourceideamanage.common.enums.ModelMsg;
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
    @Cacheable(cacheNames = {CacheConstant.FIND_TEAM}, key = "'findTeam=[teamId='+#teamId +']'")
    public Team findTeam(Integer teamId) {
        return ideaMapper.findTeam(teamId,IsDelete.NOTDELETE.getState());
    }

    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_USER}, key = "'findUser=[userId='+#userId +']'")
    public User findUser(Integer userId) {
        return ideaMapper.findUser(userId,IsDelete.NOTDELETE.getState());
    }

    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_TAG} ,key = "'findTag=[tagId='+#tagId +']'")
    public Tag findTag(Integer tagId) {
        return ideaMapper.findTag(tagId);
    }

    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_COMMENT} ,key = "'findComment=[ideaId=' + #ideaId + ']'")
    public List<CommentIdea> findComment(Integer ideaId) {
        return ideaMapper.findComment(ideaId,IsDelete.NOTDELETE.getState());
    }

    @Override
    @Cacheable( cacheNames = {CacheConstant.IDEA_PAGE},key = "'findAllFrontIdea=[page='+#page+'][idea=Name='+#ideaName+'][tagName='+#tagName+']'")
    public PageInfo<FrontIdea> findAllFrontIdea(int page, int size, String ideaName, String tagName) {
        PageHelper.startPage(page,size);
        List<FrontIdea> allFrontIdea = ideaMapper.findAllFrontIdea(tagName, ideaName,IsDelete.NOTDELETE.getState());
        return new PageInfo<>(allFrontIdea);
    }

    @Override
    @Cacheable(cacheNames = {CacheConstant.GET_IDEA_MSG}, key = "'ideaMsg=[ideaId='+#ideaId+']'")
    public IdeaMsg getIdeaMsg(Integer ideaId) {
        return ideaMapper.getIdeaMsg(ideaId,IsDelete.NOTDELETE.getState());
    }

    @Override
    @Cacheable(cacheNames = {CacheConstant.GET_IDEA_SUPPORTS},key = "'ideaSupports'")
    public List<Idea> getIdeaSupports() {
        return ideaMapper.getIdeaSupportsRank(IsDelete.NOTDELETE.getState());
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
            jsonObject.put(ModelMsg.MSG.getMsg(),"已取消赞");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
        }else{
            jedis.sadd(userSetKey+ideaId,userId);
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
            jsonObject.put(ModelMsg.MSG.getMsg(),"已点赞");
        }
        return jsonObject.toString();
    }

    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_ALL_IDEA} ,key = "'findAllIdea=[userId='+#userId +']'")
    public List<FrontIdea> findAllIdea(Integer userId) {
        return ideaMapper.findAllIdea(userId,IsDelete.NOTDELETE.getState());
    }

    @Override
    @Cacheable(cacheNames = {CacheConstant.MY_IDEAS},key = "'myIdeas=[page='+#page+'][tagName='+#tagName+'][ideaName='+#ideaName+'[userId='+#userId+']'")
    public PageInfo<FrontIdea> findAllMyIdea(int page, int size,String tagName, String ideaName, Integer userId) {
        PageHelper.startPage(page,size);
        List<FrontIdea> allFrontIdea = ideaMapper.findAllMyIdea(tagName, ideaName,userId,IsDelete.NOTDELETE.getState());
        return new PageInfo<>(allFrontIdea);
    }

    @Override
    @Cacheable(cacheNames = {CacheConstant.GET_IDEA_NAME} ,key = "'getIdeaName=[ideaId='+#ideaId +']'")
    public String getIdeaName(Integer ideaId) {
        return ideaMapper.getIdeaName(ideaId,IsDelete.NOTDELETE.getState());
    }

    @Override
    @Cacheable(cacheNames = {CacheConstant.COMMENTS},key = "'comments=['+#ideaId+']'")
    public List<Comment> getAllComment(Integer ideaId) {
        return ideaMapper.getAllComment(ideaId,IsDelete.NOTDELETE.getState());
    }

    /**
     * 评论想法 然后清空想法缓存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CacheConstant.COMMENTS}, key = "'comments=['+#ideaId+']'")
    public void commentIdea(String content, String ideaName, Integer ideaId, HttpServletRequest request) {
        try {
            Integer userId = (Integer)request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
            String userName = (String)request.getSession().getAttribute(ModelMsg.USER_NAME.getMsg());
            ideaMapper.commentIdea(content,ideaName,ideaId,userId,userName);
            log.info("评论成功!");
        }catch (Exception e){
            log.error("评论错误!!e={}",e.getMessage());
        }
    }

    /**
     * 增加想法
     * @param ideaName
     * @param tagId
     * @param ideaMsg
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames =  {CacheConstant.MY_IDEAS}, allEntries = true)
    public String addIdea(String ideaName, Integer tagId, String ideaMsg, HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
            String exist = ideaMapper.getIdeaIdByIdeaName(ideaName, IsDelete.NOTDELETE.getState());
            if(null == exist){
                ideaMapper.addIdea(ideaName,tagId,ideaMsg,userId);
                log.info("添加想法成功!!");
                jsonObject.put(ModelMsg.MSG.getMsg(),"添加想法成功!");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
                //TODO 页面不跳转
                jsonObject.put(ModelMsg.SUCCESS_URL.getMsg(),"redirect :/userC/toMyIdea");
            }else{
                  jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
                  jsonObject.put(ModelMsg.MSG.getMsg(),"重复想法名 请换一个");
                log.info("添加重复想法名!!");
            }

        }catch (Exception e){
            log.error("添加想法失败!!ideaServiceImpl e={}",e.getMessage());
            jsonObject.put(ModelMsg.MSG.getMsg(),"添加想法失败!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }
        return jsonObject.toString();
    }

    @Override
    @CacheEvict(cacheNames = {CacheConstant.TEAM_IDEAS},key = "'teamIdeas=[teamName='+#teamNam+']'")
    public void addTeamIdea(String ideaName, Integer tagId, String ideaMsg, String teamName, HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
            Integer teamId = teamMapper.findTeamId(teamName, IsDelete.NOTDELETE.getState());
            ideaMapper.addTeamIdea(ideaName,tagId,ideaMsg,userId,teamId);
            log.info("添加想法成功!!");
        }catch (Exception e){
            log.error("添加想法失败!!");
        }
    }

    @Override
    @Cacheable(cacheNames = {CacheConstant.TEAM_IDEAS},key = "'teamIdeas=[teamName='+#teamNam+']'")
    public List<FrontIdea> findAllTeamIdea(String teamName) {
        Integer teamId = teamMapper.findTeamId(teamName,IsDelete.NOTDELETE.getState());
        List<FrontIdea> ideas = ideaMapper.findAllTeamIdea(teamId,IsDelete.NOTDELETE.getState());
        return ideas;
    }

    /**
     * 删除想法 将is_delete 设置为1
     * @param ideaId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CacheConstant.MY_IDEAS},allEntries = true)
    public String delIdea(Integer ideaId) {
        try {
            ideaMapper.delIdea(ideaId,IsDelete.DELETE.getState());
            log.info("删除想法成功!");
            ideaMapper.delIdeaComment(ideaId,IsDelete.DELETE.getState());
            log.info("删除想法评论成功!");
            jsonObject.put(ModelMsg.MSG.getMsg(),"删除想法成功!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
        }catch (Exception e){
            log.error("删除想法失败!");
            jsonObject.put(ModelMsg.MSG.getMsg(),"删除想法失败!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
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
            ideaMapper.durSupports( ideId, supports,IsDelete.NOTDELETE.getState());
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
    @CacheEvict(cacheNames = {CacheConstant.COMMENTS} ,key = "'comments=['+#ideaId+']'")
    public String commentIdeaUser(Integer uid,Integer ideaId,String ideaName,
                                  Integer userId,String userName, Integer parentId, String parentName ,String content ) {
        try{
            ideaMapper.commentIdeaUser(uid,ideaId,ideaName,userId,userName,parentId,parentName,content);
            log.info("评论 某人的评论成功!");
            jsonObject.put(ModelMsg.MSG.getMsg(),"评论成功!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
        }catch (Exception e){
            log.error("评论失败!e={}",e.getMessage());
            jsonObject.put(ModelMsg.MSG.getMsg(),"评论失败!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }
        return jsonObject.toString();
    }


    @Override
    @Cacheable(cacheNames = {CacheConstant.ALL_BRAIN_TIME},key = "'findAllTime'")
    public List<BrainTime> getAllBrainTime() {
        return ideaMapper.getAllBrainTime();
    }


    /**
     * 添加头脑风暴
     * @param userId    发出增加的用户id
     * @param brainName 头脑风暴名
     * @param timeId    团队id
     * @param brainMsg  头脑风暴信息
     * @return
     */
    @Override
    public String addBrainStorming(Integer userId ,String brainName, Integer timeId, String brainMsg) {
        try{
            String key = BrainKey.BRAIN_KEY.getKey() +brainName;
            if(jedis.exists(key)){
                log.error("增加头脑风暴失败!!!");
                jsonObject.put(ModelMsg.MSG.getMsg(),"增加头脑风暴失败!!!名称已重复");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
                throw new Exception("增加头脑风暴失败");
            }
            String userName = userMapper.getUserName(userId);
            //转换成秒
            Integer brainTime = this.getBrainTime(timeId) * 60;
            Map<String,String> map = new HashMap<>(16);
            map.put(CacheConstant.BRAIN_NAME,brainName);
            map.put(CacheConstant.BRAIN_MSG,brainMsg);
            map.put(CacheConstant.SUPPORTS,CacheConstant.FAILURE);
            map.put(CacheConstant.USER_ID,userId.toString());
            map.put(CacheConstant.USER_NAME,userName);
            jedis.hmset(key,map);
            jedis.expire(key,brainTime);

            log.info("增加头脑风暴成功!!!");
            jsonObject.put(ModelMsg.MSG.getMsg(),"增加头脑风暴成功!!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
            jsonObject.put(ModelMsg.SUCCESS_URL.getMsg(),"/userC/toMyIdea");
        }catch (Exception e){
            log.error("增加头脑风暴失败!! e={}",e.getMessage());
            jsonObject.put(ModelMsg.MSG.getMsg(),"增加头脑风暴失败!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }

        return jsonObject.toString();
    }


    @Override
    @Cacheable(cacheNames = {CacheConstant.BRAIN_TIME} ,key = "'brainId=['+#brainId +']'")
    public Integer getBrainTime(Integer brainId) {
        return ideaMapper.getBrainTime(brainId);
    }


    /**
     * 头脑风暴页面 找出所有的头脑风暴 直接走redis
     * @return
     */
    @Override
    public List<Map<String, String>> allBrains() {
        //游标
        String cursor = CacheConstant.CURSOR;
        ScanResult<String> keys = null;
        List<String> allMapKeys = null;
        List<String> userIds = new ArrayList<>() ;
        List<Map<String,String>> brains = new ArrayList<>();
        ScanParams scanParams = new ScanParams();
        String mach;
        try{
            do {
                //得到一系列的key
                mach = BrainKey.BRAIN_KEY.getKey()+"*";
                keys = jedis.scan(cursor, scanParams.match(mach));
                //得到所有的keyName
                allMapKeys = keys.getResult();
                //取出: 后面的userId;
                if(null != allMapKeys && allMapKeys.size() > 0 ){
                    //通过key得到所有的map
                    for(String key : allMapKeys){
                        Map<String, String> map = jedis.hgetAll(key);
                        //设置成分钟
                        Long ttl = jedis.ttl(key) / 60;
                        map.put("time",ttl.toString());
                        brains.add(map);
                    }
                }
                //得到返回的游标
                cursor = keys.getStringCursor();
            }while (Integer.parseInt(cursor) != 0);

            log.info("去头脑风暴页面成功!!!");
        }catch (Exception e){
            log.error("去头脑风暴页面失败!!e={}",e);
        }
        return brains;
    }

    /**
     * 给 头脑风暴点赞
     * @param brainName
     * @return
     */
    @Override
    public String upBrainSupports(String brainName,Integer uuserId) {
            //得到该头脑风暴的key
            String userKey = BrainKey.BRAIN_SUPPORT_USER.getKey() +brainName;
            //得到点赞数
            String supportsKey = BrainKey.BRAIN_KEY.getKey()+brainName;
            String userId = uuserId.toString();
            Boolean exist = jedis.sismember(userKey, userId);
            //判断集合Set之中是否有相同userId  如果有相同的userId 就删除 同时 点赞-1
            if(exist){
                //删除某个用户点赞, 之后点赞数-1
                jedis.srem(userKey,userId);
                jedis.hincrBy(supportsKey,"supports",-1);
                jsonObject.put(ModelMsg.MSG.getMsg(),"取消赞成功!");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
            }else{
                //用户点赞
                jedis.sadd(userKey,userId);
                jedis.hincrBy(supportsKey,"supports",1);
                log.info("点赞成功!!");
                jsonObject.put(ModelMsg.MSG.getMsg(),"点赞成功!!");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
            }
            //对保存用户id的key进行设置生存时间,先从点赞key获取ttl赋予
        Long ttl = jedis.ttl(supportsKey);
        jedis.expire(userKey,ttl.intValue());

        return jsonObject.toString();
    }


}
