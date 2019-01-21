package cn.bb.sourceideamanage.service.back.impl;

import cn.bb.sourceideamanage.dao.back.BackIdeaMapper;
import cn.bb.sourceideamanage.dto.back.BackIdea;
import cn.bb.sourceideamanage.entity.Tag;
import cn.bb.sourceideamanage.service.back.BackIdeaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BackIdeaServiceImpl implements BackIdeaService {

    @Resource
    BackIdeaMapper mapper;

    @Override
    @Cacheable(cacheNames = "backIdeasPage",key = "'backIdeasPage=[page='+#page+'][size='+#size+'][ideaName=['+#ideaName+'][tagName'+#tagName+']'")
    public PageInfo<BackIdea> findBackIdeaByPage(int page, int size, String ideaName, String tagName) {
        PageHelper.startPage(page,size);
        List<BackIdea> ideas = mapper.findAllBackIdea(ideaName, tagName);
        return new PageInfo<>(ideas);
    }

    @Override
    @Cacheable( cacheNames = "allTag",key = "'findAllTag'")
    public List<Tag> findAllTag() {
        return mapper.findAllTag();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteIdea(Integer ideaId) {
        mapper.deleteIdea(ideaId);
    }
}
