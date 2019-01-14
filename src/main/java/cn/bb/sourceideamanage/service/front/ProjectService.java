package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.dto.front.FrontProject;
import cn.bb.sourceideamanage.dto.front.FrontProjectMsg;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectService {
    public PageInfo<FrontProject> findAllFrontProject(int page, int size, String projectName);
    public FrontProjectMsg getProjectMsgByProjectId( Integer projectId);
    public String joinProject(Integer userId,Integer projectId);
    public List<String> getAllProjects( Integer userId);
}
