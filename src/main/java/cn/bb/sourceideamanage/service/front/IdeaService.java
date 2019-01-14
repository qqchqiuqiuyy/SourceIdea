package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.dto.front.FrontIdea;
import cn.bb.sourceideamanage.dto.front.IdeaMsg;
import cn.bb.sourceideamanage.entity.*;
import com.github.pagehelper.PageInfo;
import sun.java2d.loops.FontInfo;

import java.util.List;

public interface IdeaService {

    public Team findTeam(Integer teamId);

    public User findUser(Integer userId);

    public Tag findTag(Integer tagId);

    public List<commentIdea> findComment(Integer ideaId);

    public PageInfo<FrontIdea> findAllFrontIdea(int page, int size, String ideaName, String tagName);

    public IdeaMsg getIdeaMsg(Integer ideaId);

    public List<Idea> getIdeaSupports();

    public String upIdeaSupports(String ideaId, String userId);
}
