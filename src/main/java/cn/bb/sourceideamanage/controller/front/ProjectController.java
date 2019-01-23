package cn.bb.sourceideamanage.controller.front;

import cn.bb.sourceideamanage.common.config.PageSize;
import cn.bb.sourceideamanage.common.enums.ModelMsg;
import cn.bb.sourceideamanage.dto.front.FrontProject;
import cn.bb.sourceideamanage.dto.front.FrontProjectMsg;
import cn.bb.sourceideamanage.dto.front.FrontTeam;
import cn.bb.sourceideamanage.dto.front.TeamMember;
import cn.bb.sourceideamanage.service.front.ProjectService;
import cn.bb.sourceideamanage.service.front.TeamService;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/projectC")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    TeamService teamService;

    /**
     * 去项目页面 分页处理
     * @param model
     * @param projectName   项目名
     * @param page  当前页
     * @return
     */
    @RequestMapping("/toProject")
    public String toProject(Model model, String projectName, Integer page){
        if(null == page || page < 1){
            page = 1;
        }
        if(projectName == null){
            projectName = "";
        }
        PageInfo<FrontProject> info = projectService.findAllFrontProject(page, PageSize.PAGE_SIZE, projectName);
        List<FrontProject> projects = info.getList();
        model.addAttribute(ModelMsg.PROJECTS.getMsg(),projects);
        model.addAttribute(ModelMsg.INDEX_PAGE.getMsg(),page);
        model.addAttribute(ModelMsg.TOTAL_PAGE.getMsg(),info.getPages());
        return "/pages/front/html/project/projectList";
    }


    /**
     * 去项目信息页面
     * @param model
     * @param projectId 项目id
     * @param request
     * @return
     */
    @GetMapping("/toProjectMsg/{projectId}")
    public String toProjectMsg(Model model,@PathVariable("projectId") Integer projectId, HttpServletRequest request){
        Integer userId = (Integer) request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
        Integer teamId = projectService.getTeamIdByProjectId(projectId);
        List<TeamMember> members = teamService.findAllMemberByTeamId(teamId);

        FrontProjectMsg projectMsg = projectService.getProjectMsgByProjectId(projectId);
        model.addAttribute(ModelMsg.PROJECT_MSG.getMsg(),projectMsg);
        model.addAttribute(ModelMsg.MEMBERS.getMsg(),members);

        List<String> projects = projectService.getAllProjects(userId);
        JSONArray projectsName = JSONArray.fromObject(projects);
        model.addAttribute(ModelMsg.PROJECTS_NAME.getMsg(),projectsName);
        return "/pages/front/html/project/projectMsg";
    }


    /**
     * 加入项目
     * @param userId 用户名
     * @param projectId 项目id
     * @return
     */
    @PostMapping("/joinProject/{userId}/{projectId}")
    @ResponseBody
    public String joinProject(@PathVariable("userId") Integer userId,
                              @PathVariable("projectId") Integer projectId){
        String msg = projectService.joinProject(userId, projectId);
        return msg;
    }


    /**
     * 区修改项目页面
     * @param projectId 项目id
     * @param model
     * @return
     */
    @GetMapping("/toEditProject/{projectId}")
    public String toEditProject(@PathVariable("projectId") Integer projectId,Model model){
        FrontProjectMsg projectMsg = projectService.getProjectMsgByProjectId(projectId);
        model.addAttribute("project",projectMsg);
        model.addAttribute("projectId",projectId);
        return "/pages/front/html/project/editProject";
    }


    /**
     * 修改项目
     * @param projectName 项目名
     * @param projectId 项目id
     * @param projectMsg    项目信息
     * @return
     */
    @PostMapping("/editProject")
    @ResponseBody
    public String editProject( String projectName,
                              Integer projectId,
                               String projectMsg){
        return projectService.editProject(projectName,projectId,projectMsg);
    }

    /**
     * 项目归档
     * @param projectId 项目id
     * @param teamName  团队名
     * @return
     */
    @PutMapping("/archiveProject/{projectId}/{teamName}")
    @ResponseBody
    public String archiveProject(@PathVariable("projectId") Integer projectId,@PathVariable("teamName") String teamName){
            return projectService.archiveProject(projectId,teamName);
    }
}
