package cn.bb.sourceideamanage.service.front.impl;

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

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {
    @Resource
    ProjectMapper projectMapper;

    @Resource
    TeamMapper teamMapper;

    @Autowired
    JSONObject jsonObject;

    @Override
    @Cacheable(cacheNames = "projectPage",key = "'projectPage=[page='+#page+'][size='+#size+'][projectName='+#projectName+']'")
    public PageInfo<FrontProject> findAllFrontProject(int page, int size, String projectName) {
        PageHelper.startPage(page,size);
        List<FrontProject> projects = projectMapper.findAllFrontProject(projectName, Roles.UserTeamManager.getRoleId());
        return new PageInfo<>(projects);
    }

    @Override
    @Cacheable(cacheNames = "projectMsg", key = "'projectMsg=[projectId='+#projectId+']'")
    public FrontProjectMsg getProjectMsgByProjectId(Integer projectId) {
        return projectMapper.getProjectMsgByProjectId(projectId);
    }

    /**
     * 申请加入项目 同时加入团队
     * @param userId
     * @param projectId
     * @return
     */
    @Override
    @Transactional
    public String joinProject(Integer userId, Integer projectId) {
        //加入项目同时也要加入团队
        Integer teamId = projectMapper.getTeamId(projectId);
        String checkProject = teamMapper.checkTeamMember(userId, teamId);
        //判断是否已经在项目
        if(null == checkProject){
            String checkApply = teamMapper.checkApply(userId,teamId);
            //判断是否已经进入申请列表
            if(null == checkApply){
                teamMapper.apply(userId,teamId);
                log.info("申请加入成功!! 等待团长审批!");
                jsonObject.put("msg","申请加入成功!! 等待团长审批!");
                jsonObject.put("isSuccess","1");
            }else{
                log.error("错误! 已经申请过了!");
                jsonObject.put("msg","已提交过申请,等待审批中,请勿重复申请!!");
                jsonObject.put("isSuccess","0");
            }
        }else{
            log.error("已加入该项目! 请勿重复操作");
            jsonObject.put("msg","已加入该项目! 请勿重复操作");
            jsonObject.put("isSuccess","0");

        }
        return jsonObject.toString();

    }

    @Override
    @Cacheable(cacheNames = "allProjectsByUserId" ,key = "'allProjectsByUserId=[userId='+#userId+']'")
    public List<String> getAllProjects(Integer userId) {
        return projectMapper.getAllProjects(userId);
    }

    @Override
    @Cacheable(cacheNames = "myProjects",key = "'myProjects=[page='+#page+'][size='+#size+'][projectName='+#projectName+'][userId='+#userId+']'")
    public PageInfo<FrontProject> findAllMyProject(int page, int size, String projectName, Integer userId) {
        PageHelper.startPage(page,size);
        List<FrontProject> projects = projectMapper.findAllMyProject(projectName,userId);
        return new PageInfo<>(projects);
    }

    @Override
    @Cacheable(cacheNames = "getTeamIdByProjectId" ,key = "'getTeamIdByProjectId=[projectId='+#projectId+']'")
    public Integer getTeamIdByProjectId(Integer projectId) {
        return projectMapper.getTeamIdByProjectId(projectId);
    }
}
