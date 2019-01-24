package cn.bb.sourceideamanage.service.back.impl;

import cn.bb.sourceideamanage.common.cacheConstant.CacheConstant;
import cn.bb.sourceideamanage.common.enums.IsDelete;
import cn.bb.sourceideamanage.common.enums.ModelMsg;
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

    /**
     * 查找想法分页
     * @param page  当前页
     * @param size  每页数量
     * @param ideaName  想法名
     * @param tagName   标签名
     * @return
     */
    @Override
    public PageInfo<BackIdea> findBackIdeaByPage(int page, int size, String ideaName, String tagName) {
        PageHelper.startPage(page,size);
        List<BackIdea> ideas = mapper.findAllBackIdea(ideaName, tagName, IsDelete.NOTDELETE.getState());
        return new PageInfo<>(ideas);
    }

    /**
     * 得到所有的想法标签
     * @return
     */
    @Override
    @Cacheable( cacheNames = {CacheConstant.ALL_TAG},key = "'allTag'")
    public List<Tag> findAllTag() {
        return mapper.findAllTag();
    }


    /**
     * 删除一个想法
     * @param ideaId    想法名
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteIdea(Integer ideaId) {
        if(ideaId == null){
            jsonObject.put(ModelMsg.MSG.getMsg(),"删除失败!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
            log.error("BackIdeaService 删除错误");
        }else{
            //删除想法
            mapper.deleteIdea(ideaId,IsDelete.DELETE.getState());
            //删除想法评论
            mapper.deleteIdeaComment(ideaId,IsDelete.DELETE.getState());
            jsonObject.put(ModelMsg.MSG.getMsg(),"删除成功!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
            log.info("BackIdeaService删除成功!");
        }
        return jsonObject.toString();
    }
}
