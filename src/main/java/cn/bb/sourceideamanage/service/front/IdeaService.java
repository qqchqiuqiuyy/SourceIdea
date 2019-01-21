package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.entity.Comment;
import cn.bb.sourceideamanage.dto.front.FrontIdea;
import cn.bb.sourceideamanage.dto.front.IdeaMsg;
import cn.bb.sourceideamanage.entity.*;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Select;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IdeaService {

    public Team findTeam(Integer teamId);

    public User findUser(Integer userId);

    public Tag findTag(Integer tagId);

    public List<commentIdea> findComment(Integer ideaId);

    public PageInfo<FrontIdea> findAllFrontIdea(int page, int size, String ideaName, String tagName);

    public IdeaMsg getIdeaMsg(Integer ideaId);

    public List<Idea> getIdeaSupports();

    public String upIdeaSupports(String ideaId, String userId);

    public List<FrontIdea> findAllIdea(Integer userId);

    public PageInfo<FrontIdea> findAllMyIdea(int page, int size,String tagName,String ideaName, Integer userId);


    public String getIdeaName(Integer ideaId);

    public List<Comment> getAllComment(Integer ideaId);

    public void commentIdea(String content, String ideaName, Integer ideaId , HttpServletRequest request);

    public void addIdea(String ideaName,Integer tagId,String ideaMsg,HttpServletRequest request);
    public void addTeamIdea(String ideaName,Integer tagId,String ideaMsg,String teamName,HttpServletRequest request);

    public List<FrontIdea> findAllTeamIdea(String teamName);

    public String delIdea(Integer ideaId);

    public void durSupports(Integer ideId,Long supports);

    public String commentIdeaUser(Integer uid,Integer ideaId,String ideaName,
                                  Integer userId,String userName, Integer parentId, String parentName,
                                  String content);


    public List<BrainTime> getAllBrainTime();

    public String addBrainStorming( Integer userId ,String brainName,Integer timeId,String brainMsg );

    public Integer getBrainTime(Integer brainid);

    public List<Map<String ,String >> allBrains();

    public String upBrainSupports(String brainName,Integer userId);
}
