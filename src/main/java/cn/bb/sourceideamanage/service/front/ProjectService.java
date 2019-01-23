package cn.bb.sourceideamanage.service.front;

import cn.bb.sourceideamanage.common.config.PageSize;
import cn.bb.sourceideamanage.dto.front.FrontProject;
import cn.bb.sourceideamanage.dto.front.FrontProjectMsg;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author  bobo
 */
public interface ProjectService {
    /**
     * 分页部分,和模糊查询
     * @param page  当前页
     * @param size  每页长度
     * @param projectName     项目名
     * @return
     */
    public PageInfo<FrontProject> findAllFrontProject(int page, int size, String projectName);

    /**
     * 根据项目id查找项目信息
     * @param projectId 项目id
     * @return
     */
    public FrontProjectMsg getProjectMsgByProjectId( Integer projectId);

    /**
     * 用户加入项目
     * @param userId 用户id
     * @param projectId 项目id
     * @return
     */
    public String joinProject(Integer userId,Integer projectId);

    /**
     * 根据用户id得到该用户的所有项目
     * @param userId 用户id
     * @return
     */
    public List<String> getAllProjects( Integer userId);

    /**
     * 分页显示我的所有项目
     * @param page  第几页
     * @param size  每页显示数量
     * @param projectName   项目名
     * @param userId    用户的id
     * @return
     */
    public PageInfo<FrontProject> findAllMyProject(int page,int size, String projectName, Integer userId);


    /**
     * 根据项目id查找团队id
     * @param projectId
     * @return
     */
    public Integer getTeamIdByProjectId( Integer projectId);

    /**
     * 编辑项目
     * @param projectName 项目名
     * @param projectId  项目id
     * @param projectMsg 需要修改的信息
     * @return
     */
    public String editProject(String projectName,Integer projectId,String projectMsg);

    /**
     * 归档项目  归档后无法修改
     * @param projectId 项目id
     * @param projectName   项目名
     * @return
     */
    public String archiveProject(Integer projectId,String projectName);
}
