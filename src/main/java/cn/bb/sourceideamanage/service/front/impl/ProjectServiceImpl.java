package cn.bb.sourceideamanage.service.front.impl;

import cn.bb.sourceideamanage.common.CacheConstant.CacheConstant;
import cn.bb.sourceideamanage.common.enums.IsDelete;
import cn.bb.sourceideamanage.common.enums.ModelMsg;
import cn.bb.sourceideamanage.common.enums.ProjectArchive;
import cn.bb.sourceideamanage.common.enums.Roles;
import cn.bb.sourceideamanage.dao.front.ProjectMapper;
import cn.bb.sourceideamanage.dao.front.TeamMapper;
import cn.bb.sourceideamanage.dto.front.FrontProject;
import cn.bb.sourceideamanage.dto.front.FrontProjectMsg;
import cn.bb.sourceideamanage.service.front.ProjectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static java.awt.SystemColor.info;

/**
 * @author bobo
 */
@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {
    @Resource
    ProjectMapper projectMapper;

    @Resource
    TeamMapper teamMapper;

    @Autowired
    JSONObject jsonObject;

    /**
     * 分页部分,和模糊查询
     * @param page  当前页
     * @param size  每页长度
     * @param projectName     项目名
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.PROJECT_PAGE},key = "'projectPage=[page='+#page+'][size='+#size+'][projectName='+#projectName+']'")
    public PageInfo<FrontProject> findAllFrontProject(int page, int size, String projectName) {
        PageHelper.startPage(page,size);
        List<FrontProject> projects = projectMapper.findAllFrontProject(projectName, Roles.UserTeamManager.getRoleId());
        return new PageInfo<>(projects);
    }


    /**
     * 据项目id查找项目信息
     * @param projectId 项目id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.PROJECT_MSG}, key = "'projectMsg=[projectId='+#projectId+']'")
    public FrontProjectMsg getProjectMsgByProjectId(Integer projectId) {
        return projectMapper.getProjectMsgByProjectId(projectId);
    }

    /**
     * 用户加入项目
     * @param userId 用户id
     * @param projectId 项目id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String joinProject(Integer userId, Integer projectId) {
        //加入项目同时也要加入团队
        Integer teamId = projectMapper.getTeamId(projectId);
        String checkProject = teamMapper.checkTeamMember(userId, teamId, IsDelete.NOTDELETE.getState());
        //判断是否已经在项目
        if(null == checkProject){
            String checkApply = teamMapper.checkApply(userId,teamId,IsDelete.NOTDELETE.getState());
            //判断是否已经进入申请列表
            if(null == checkApply){
                teamMapper.apply(userId,teamId);
                log.info("申请加入成功!! 等待团长审批!");
                jsonObject.put(ModelMsg.MSG.getMsg(),"申请加入成功!! 等待团长审批!");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
            }else{
                log.error("错误! 已经申请过了!");
                jsonObject.put(ModelMsg.MSG.getMsg(),"已提交过申请,等待审批中,请勿重复申请!!");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
            }
        }else{
            log.error("已加入该项目! 请勿重复操作");
            jsonObject.put(ModelMsg.MSG.getMsg(),"已加入该项目! 请勿重复操作");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);

        }
        return jsonObject.toString();

    }


    /**
     * 根据用户id得到该用户的所有项目
     * @param userId 用户id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.GET_ALL_PROJECTS} ,key = "'getAllProjects=[userId='+#userId+']'")
    public List<String> getAllProjects(Integer userId) {
        return projectMapper.getAllProjects(userId);
    }


    /**
     * 分页显示我的所有项目
     * @param page  第几页
     * @param size  每页显示数量
     * @param projectName   项目名
     * @param userId    用户的id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.MY_PROJECTS},key = "'myProjects=[page='+#page+'][size='+#size+'][projectName='+#projectName+'][userId='+#userId+']'")
    public PageInfo<FrontProject> findAllMyProject(int page, int size, String projectName, Integer userId) {
        PageHelper.startPage(page,size);
        List<FrontProject> projects = projectMapper.findAllMyProject(projectName,userId,IsDelete.NOTDELETE.getState());
        return new PageInfo<>(projects);
    }


    /**
     *  根据项目id查找团队id
     * @param projectId
     * @return
     */
    @Override
    @Cacheable(cacheNames = {CacheConstant.GET_TEAMID_BY_PROJECTID} ,key = "'getTeamIdByProjectId=[projectId='+#projectId+']'")
    public Integer getTeamIdByProjectId(Integer projectId) {
        return projectMapper.getTeamIdByProjectId(projectId);
    }

    /**
     *  编辑项目
     * @param projectName 项目名
     * @param projectId  项目id
     * @param projectMsg 需要修改的信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CacheConstant.PROJECT_MSG}, key = "'projectMsg=[projectId='+#projectId+']'")
    public String editProject(String projectName, Integer projectId, String projectMsg) {
        try {
            Integer integer = projectMapper.checkArchive(projectId,IsDelete.NOTDELETE.getState());
            if(integer == 1){
                log.error("已归档 无法修改!");
                jsonObject.put(ModelMsg.MSG.getMsg(),"修改项目失败! 已归档 无法修改!");
                jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
                return jsonObject.toString();
            }
            projectMapper.editProject(projectName,projectId,projectMsg);
            jsonObject.put(ModelMsg.MSG.getMsg(),"修改项目成功!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.SUCCESS);
            jsonObject.put(ModelMsg.SUCCESS_URL.getMsg(),"/projectC/toProjectMsg/"+projectId);
            log.info("修改项目成功!");
        }catch (Exception e){
            jsonObject.put(ModelMsg.MSG.getMsg(),"修改项目失败!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
            log.error("修改项目失败!");
        }

        return jsonObject.toString();
    }


    /**
     * 归档项目  归档后无法修改
     * @param projectId 项目id
     * @param projectName   项目名
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CacheConstant.MY_PROJECT}, key = "'myProject=[teamName='+#projectName+']'")
    public String archiveProject(Integer projectId,String projectName){
        try{
             projectMapper.archiveProject(projectId, ProjectArchive.FINISH.getArchive(),IsDelete.NOTDELETE.getState());
            jsonObject.put(ModelMsg.MSG.getMsg(),"归档成功!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(), CacheConstant.SUCCESS);
            log.info("归档成功!");
        }catch (Exception e){
            log.error("归档失败!!{}",e.toString());
            jsonObject.put(ModelMsg.MSG.getMsg(),"归档失败!");
            jsonObject.put(ModelMsg.SUCCESS.getMsg(),CacheConstant.FAILURE);
        }
        return jsonObject.toString();

    }


}
