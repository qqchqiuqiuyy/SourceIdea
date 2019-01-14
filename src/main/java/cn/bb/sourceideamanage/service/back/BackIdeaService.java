package cn.bb.sourceideamanage.service.back;

import cn.bb.sourceideamanage.dto.back.BackIdea;
import cn.bb.sourceideamanage.entity.Tag;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.sound.sampled.Line;
import java.util.List;

public interface BackIdeaService {
    public PageInfo<BackIdea> findBackIdeaByPage(int page, int size, String ideaName,String tagName);
    public List<Tag> findAllTag();
    public void deleteIdea(Integer ideaId);
}
