package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.dao.front.IdeaMapper;
import cn.bb.sourceideamanage.dao.front.TeamMapper;
import cn.bb.sourceideamanage.dto.front.FrontIdea;
import cn.bb.sourceideamanage.dto.front.IdeaMsg;
import cn.bb.sourceideamanage.dto.front.comment;
import cn.bb.sourceideamanage.entity.*;
import cn.bb.sourceideamanage.service.front.IdeaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public PageInfo<FrontIdea> findAllFrontIdea(int page, int size, String ideaName, String tagName) {
        PageHelper.startPage(page,size);
        List<FrontIdea> allFrontIdea = ideaMapper.findAllFrontIdea(tagName, ideaName);
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

    @Override
    public List<FrontIdea> findAllIdea(Integer userId) {
        return ideaMapper.findAllIdea(userId);
    }

    @Override
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
    public List<comment> getAllComment(Integer ideaId) {
        return ideaMapper.getAllComment(ideaId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void commentIdea(String content, String ideaName, Integer ideaId, HttpServletRequest request) {
        try {
            Integer userId = (Integer)request.getSession().getAttribute("userId");
            String userName = (String)request.getSession().getAttribute("userName");
            ideaMapper.commentIdea(content,ideaName,ideaId,userId,userName);
            log.info("评论成功!");
        }catch (Exception e){
            log.error("评论错误!!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    public void addProjectIdea(String ideaName, Integer tagId, String ideaMsg, String teamName, HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            Integer teamId = teamMapper.findTeamId(teamName);
            ideaMapper.addProjectIdea(ideaName,tagId,ideaMsg,userId,teamId);
            log.info("添加想法成功!!");
        }catch (Exception e){
            log.error("添加想法失败!!");
        }
    }


}
