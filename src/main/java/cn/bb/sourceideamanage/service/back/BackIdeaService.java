package cn.bb.sourceideamanage.service.back;

import cn.bb.sourceideamanage.dto.back.BackIdea;
import cn.bb.sourceideamanage.entity.Tag;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.sound.sampled.Line;
import java.util.List;

public interface BackIdeaService {
    /**
     * 传入 第几页 page, 每页数量size
     * 想法名,标签吗 返回固定数量想法
     * @param page
     * @param size
     * @param ideaName
     * @param tagName
     * @return
     */
    public PageInfo<BackIdea> findBackIdeaByPage(int page, int size, String ideaName,String tagName);

    /**
     * 查找所有标签
     * @return
     */
    public List<Tag> findAllTag();

    /**
     * 根据id删除想法
     * @param ideaId
     * @return
     */
    public String deleteIdea(Integer ideaId);
}
