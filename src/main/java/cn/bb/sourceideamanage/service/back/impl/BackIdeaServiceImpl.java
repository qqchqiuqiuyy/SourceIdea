package cn.bb.sourceideamanage.service.back.impl;

import cn.bb.sourceideamanage.common.CacheConstant.CacheConstant;
import cn.bb.sourceideamanage.common.enums.IsDelete;
import cn.bb.sourceideamanage.dao.back.BackIdeaMapper;
import cn.bb.sourceideamanage.dto.back.BackIdea;
import cn.bb.sourceideamanage.entity.Tag;
import cn.bb.sourceideamanage.service.back.BackIdeaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class BackIdeaServiceImpl implements BackIdeaService {

    @Resource
    BackIdeaMapper mapper;

    @Autowired
    JSONObject jsonObject;

    @Override
    public PageInfo<BackIdea> findBackIdeaByPage(int page, int size, String ideaName, String tagName) {
        PageHelper.startPage(page,size);
        List<BackIdea> ideas = mapper.findAllBackIdea(ideaName, tagName, IsDelete.NOTDELETE.getState());
        return new PageInfo<>(ideas);
    }

    @Override
    @Cacheable( cacheNames = {CacheConstant.ALL_TAG},key = "'allTag'")
    public List<Tag> findAllTag() {
        return mapper.findAllTag();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteIdea(Integer ideaId) {
        if(ideaId == null){
            jsonObject.put("msg","删除失败!");
            jsonObject.put("success","0");
            log.error("BackIdeaService 删除错误");
        }else{
            //删除想法
            mapper.deleteIdea(ideaId);
            //删除想法评论
            mapper.deleteIdeaComment(ideaId);
            jsonObject.put("msg","删除成功!");
            jsonObject.put("success","1");
            log.info("BackIdeaService删除成功!");
        }
        return jsonObject.toString();
    }
}
