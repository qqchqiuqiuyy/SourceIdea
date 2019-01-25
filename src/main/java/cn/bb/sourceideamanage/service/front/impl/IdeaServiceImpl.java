package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.common.cacheConstant.CacheConstant;
import cn.bb.sourceideamanage.common.enums.*;
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

/**
 * @author bobo
 */
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

    /**
     *根据团队id查找团队信息
     * @param teamId 团队id
     * @return 团队
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_TEAM}, key = "'findTeam=[teamId='+#teamId +']'")
    public Team findTeam(Integer teamId) {
        return ideaMapper.findTeam(teamId,IsDelete.NOTDELETE.getState());
    }

    /**
     *根据用户id查找用户信息
     * @param userId    用户id
     * @return  用户信息
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_USER}, key = "'findUser=[userId='+#userId +']'")
    public User findUser(Integer userId) {
        return ideaMapper.findUser(userId,IsDelete.NOTDELETE.getState());
    }

    /**
     *根据标签id 返回标签信息
     * @param tagId 标签id
     * @return  标签
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_TAG} ,key = "'findTag=[tagId='+#tagId +']'")
    public Tag findTag(Integer tagId) {
        return ideaMapper.findTag(tagId);
    }

    /**
     *根据 想法id 返回评论想法内容
     * @param ideaId    想法id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_COMMENT} ,key = "'findComment=[ideaId=' + #ideaId + ']'")
    public List<CommentIdea> findComment(Integer ideaId) {
        return ideaMapper.findComment(ideaId,IsDelete.NOTDELETE.getState());
    }

    /**
     *根据以下参数 返回想法模块的分页信息
     * @param page 当前页
     * @param size  每页数量
     * @param ideaName  想法名
     * @param tagName   标签名
     * @return
     */
    @Override
    @Cacheable( cacheNames = {CacheConstant.IDEA_PAGE},key = "'findAllFrontIdea=[page='+#page+'][idea=Name='+#ideaName+'][tagName='+#tagName+']'")
    public PageInfo<FrontIdea> findAllFrontIdea(int page, int size, String ideaName, String tagName) {
        PageHelper.startPage(page,size);
        List<FrontIdea> allFrontIdea = ideaMapper.findAllFrontIdea(tagName, ideaName,IsDelete.NOTDELETE.getState());
        return new PageInfo<>(allFrontIdea);
    }

    /**
     *根据想法id 返回想法相关信息
     * @param ideaId    想法id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.GET_IDEA_MSG}, key = "'ideaMsg=[ideaId='+#ideaId+']'")
    public IdeaMsg getIdeaMsg(Integer ideaId) {
        return ideaMapper.getIdeaMsg(ideaId,IsDelete.NOTDELETE.getState());
    }

    /**
     *返回10条点赞最高的想法
     * @return 返回想法排名
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.GET_IDEA_SUPPORTS},key = "'ideaSupports'")
    public List<Idea> getIdeaSupports() {
        return ideaMapper.getIdeaSupportsRank(IsDelete.NOTDELETE.getState(), IdeaSupportRank.NUMS);
    }

    /**
     * 点赞 userId存在Set里面 计算set大小即可
     * @param ideaId
     * @param userId
     * @return
     */
    @Override
    public String upIdeaSupports(String ideaId, String userId) {
        if(null == ideaId || "".equals(ideaId) || null == userId || "".equals(userId) ){
            jsonObject.put(ModelMsg.MSG.getMsg(),"无法点赞出现错误!");
            return jsonObject.toString();
        }
        //得到存有用户id 的Set集合的key,通过拼接 想法Id来区分不同的想法 ideaSupportUser:id
        String userSetKey = IdeaSupportsKey.UserSetKey.getKey() + ideaId;
        //判断当前用户是否已经在Set集合里面
        if(jedis.sismember(userSetKey,userId)){
            //将Set集合当前用户id删除
            jedis.srem(userSetKey,userId);
            jsonObject.put(ModelMsg.MSG.getMsg(),"已取消赞");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }else{
            //添加当前用户id到Set集合
            jedis.sadd(userSetKey,userId);
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
            jsonObject.put(ModelMsg.MSG.getMsg(),"已点赞");
        }
        return jsonObject.toString();
    }

    /**
     *根据用户id  返回该用户发表的所有想法
     * @param userId 用户id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.FIND_ALL_IDEA} ,key = "'findAllIdea=[userId='+#userId +']'")
    public List<FrontIdea> findAllIdea(Integer userId) {
        return ideaMapper.findAllIdea(userId,IsDelete.NOTDELETE.getState());
    }

    /**
     *分页显示该用户自己的想法所有信息
     * @param page  第几页
     * @param size  一页数量
     * @param tagName   标签名
     * @param ideaName  想法名
     * @param userId    用户id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.MY_IDEAS},key = "'myIdeas=[page='+#page+'][tagName='+#tagName+'][ideaName='+#ideaName+'[userId='+#userId+']'")
    public PageInfo<FrontIdea> findAllMyIdea(int page, int size,String tagName, String ideaName, Integer userId) {
        PageHelper.startPage(page,size);
        List<FrontIdea> allFrontIdea = ideaMapper.findAllMyIdea(tagName, ideaName,userId,IsDelete.NOTDELETE.getState());
        return new PageInfo<>(allFrontIdea);
    }

    /**
     *根据想法id 得到想法名
     * @param ideaId 想法id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.GET_IDEA_NAME} ,key = "'getIdeaName=[ideaId='+#ideaId +']'")
    public String getIdeaName(Integer ideaId) {
        return ideaMapper.getIdeaName(ideaId,IsDelete.NOTDELETE.getState());
    }

    /**
     *根据想法id 得到该想法所有评论
     * @param ideaId 想法id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.COMMENTS},key = "'comments=['+#ideaId+']'")
    public List<Comment> getAllComment(Integer ideaId) {
        return ideaMapper.getAllComment(ideaId,IsDelete.NOTDELETE.getState());
    }

    /**
     *评论该想法
     * @param content 内容
     * @param ideaName 想法名
     * @param ideaId  想法id
     * @param request
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
                jsonObject.put(ModelMsg.SUCCESS_URL.getMsg(),"/userC/toMyIdea");
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

    /**
     *增加团队想法
     * @param ideaName 想法名
     * @param tagId 标签id
     * @param ideaMsg   想法信息
     * @param teamName  团队名
     * @param request
     * @return
     */
    @Override
    @CacheEvict(cacheNames = {CacheConstant.TEAM_IDEAS},key = "'teamIdeas=[teamName='+#teamName+']'")
    public String addTeamIdea(String ideaName, Integer tagId, String ideaMsg, String teamName, HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
            Integer teamId = teamMapper.findTeamId(teamName, IsDelete.NOTDELETE.getState());
            String exist = ideaMapper.getIdeaIdByIdeaName(ideaName, IsDelete.NOTDELETE.getState());
            if(null != exist){
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
                jsonObject.put(ModelMsg.MSG.getMsg(),"重复想法名 请换一个");
                log.info("添加重复想法名!!");
                return jsonObject.toString();
            }
            ideaMapper.addTeamIdea(ideaName,tagId,ideaMsg,userId,teamId);
            log.info("添加想法成功!!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
            jsonObject.put(ModelMsg.MSG.getMsg(),"添加想法成功!");
            jsonObject.put(ModelMsg.SUCCESS_URL.getMsg(),"/userC/toMyTeamMsg/"+teamName);

        }catch (Exception e){
            log.error("添加想法失败!!e={}",e.getMessage());
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
            jsonObject.put(ModelMsg.MSG.getMsg(),"添加想法失败!");
        }
        return jsonObject.toString();
    }

    /**
     * 根据团队名查找该团队所有想法
     * @param teamName 团队名
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.TEAM_IDEAS},key = "'teamIdeas=[teamName='+#teamName+']'")
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
    @CacheEvict(cacheNames = {CacheConstant.TEAM_IDEAS,CacheConstant.MY_IDEAS},allEntries = true)
    public String delIdea(Integer ideaId) {
        try { //todo 删除点赞缓存
            String userSetKey = IdeaSupportsKey.UserSetKey.getKey() + ideaId;
            jedis.del(userSetKey);
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
     *将想法持久化回数据库
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
     * 评论 在评论区评论的用户
     *  互相评论
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
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CacheConstant.COMMENTS} ,key = "'comments=['+#ideaId+']'")
    public String commentIdeaUser(Integer uid,Integer ideaId,String ideaName,
                                  Integer userId,String userName, Integer parentId, String parentName ,String content ) {
        try{
            if(null == uid){
                uid = 0;
            }
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


    /**
     * 获取所有的头脑风暴
     * @return
     */
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
            //得到key ,key结构: brainStorming:branName
            String key = BrainKey.BRAIN_KEY.getKey() +brainName;
            //判断是否已存在
            if(jedis.exists(key)){
                log.error("增加头脑风暴失败!!!");
                jsonObject.put(ModelMsg.MSG.getMsg(),"增加头脑风暴失败!!!名称已重复");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
                throw new Exception("增加头脑风暴失败");
            }

            String userName = userMapper.getUserName(userId,IsDelete.NOTDELETE.getState());
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


    /**
     *  得到头脑风暴剩余时间
     * @param brainId
     * @return
     */
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
                //得到一系列的key      brainStorming:*
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
     * 更新头脑风暴点赞数
     * @param brainName
     * @return
     */
    @Override
    public String upBrainSupports(String brainName,Integer uuserId) {
        if(null == brainName || "".equals(brainName) || null == uuserId || "".equals(uuserId)){
            jsonObject.put(ModelMsg.MSG.getMsg(),"无法点赞出现错误!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
            return jsonObject.toString();
        }
            //得到该头脑风暴的key      brainSupportsUsers:brainName
            String userKey = BrainKey.BRAIN_SUPPORT_USER.getKey() +brainName;
            //得到点赞数                 brainStorming:brainName
            String supportsKey = BrainKey.BRAIN_KEY.getKey()+brainName;
            String userId = uuserId.toString();
            //判断当前用户是否已经点过赞
            Boolean exist = jedis.sismember(userKey, userId);
            //判断集合Set之中是否有相同userId  如果有相同的userId 就删除 同时 点赞-1
            if(exist){
                //从set集合删除某个用户id, 之后点赞数-1
                jedis.srem(userKey,userId);
                jedis.hincrBy(supportsKey,"supports",-1);
                jsonObject.put(ModelMsg.MSG.getMsg(),"取消赞成功!");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
            }else{
                //用户点赞 将该用户id添加到set集合
                jedis.sadd(userKey,userId);
                jedis.hincrBy(supportsKey,"supports",1);
                jsonObject.put(ModelMsg.MSG.getMsg(),"点赞成功!!");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
            }
            //对保存用户id的key进行设置生存时间,先从点赞key获取ttl赋予
            Long ttl = jedis.ttl(supportsKey);
            jedis.expire(userKey,ttl.intValue());

        return jsonObject.toString();
    }


}
