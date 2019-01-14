package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.dao.front.IdeaMapper;
import cn.bb.sourceideamanage.dto.front.FrontIdea;
import cn.bb.sourceideamanage.dto.front.IdeaMsg;
import cn.bb.sourceideamanage.entity.*;
import cn.bb.sourceideamanage.service.front.IdeaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IdeaServiceImpl implements IdeaService {

    @Resource
    IdeaMapper ideaMapper;

    @Autowired
    Jedis jedis;

    @Autowired
    JSONObject jsonObject;

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
    public PageInfo<FrontIdea> findAllFrontIdea(int page, int size, String ideaName, String ideaTag) {
        PageHelper.startPage(page,size);
        List<FrontIdea> allFrontIdea = ideaMapper.findAllFrontIdea(ideaTag, ideaName);
        return new PageInfo<>(allFrontIdea);
    }

    @Override
    public IdeaMsg getIdeaMsg(Integer ideaId) {
        return ideaMapper.getIdeaMsg(ideaId);
    }

    @Override
    public List<Idea> getIdeaSupports() {
        return ideaMapper.getTagSupports();
    }

    @Override
    public String upIdeaSupports(String ideaId, String userId) {
        //TODO 刷新回数据库
        //首先判断redis的Set中有没有这个userId
        if(jedis.sismember("ideaSupportUser:"+ ideaId,userId)){
            jsonObject.put("msg","已点过赞了 亲!");
            jsonObject.put("isSuccess","0");
            return jsonObject.toString();
        }else{
            jedis.sadd("ideaSupportUser:"+ideaId,userId);
            jedis.incr("ideaSupportNums:"+ideaId);
            jsonObject.put("isSuccess","1");
            return jsonObject.toString();
        }
    }
}
