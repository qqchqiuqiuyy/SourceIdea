package cn.bb.sourceideamanage.controller.front;

import cn.bb.sourceideamanage.common.config.PageSize;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/ProjectC")
public class ProjectController {

    @Autowired
    ProjectService projectService;

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
        model.addAttribute("projects",projects);
        model.addAttribute("indexPage",page);
        model.addAttribute("totalPage",info.getPages());
        return "/pages/front/html/project/projectList";
    }

    @Autowired
    TeamService teamService;
    @RequestMapping("/toProjectMsg")
    public String toProjectMsg(Model model, Integer projectId, HttpServletRequest request){
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        Integer teamId = projectService.getTeamIdByProjectId(projectId);
        List<TeamMember> members = teamService.findAllMemberByTeamId(teamId);

        FrontProjectMsg projectMsg = projectService.getProjectMsgByProjectId(projectId);
        model.addAttribute("projectMsg",projectMsg);
        model.addAttribute("members",members);

        List<String> projects = projectService.getAllProjects(userId);
        JSONArray projectsName = JSONArray.fromObject(projects);
        model.addAttribute("projectsName",projectsName);
        return "/pages/front/html/project/projectMsg";
    }

    @RequestMapping("/joinProject")
    @ResponseBody
    public String joinProject(Integer userId,Integer projectId){
        String msg = projectService.joinProject(userId, projectId);
        return msg;
    }

    @RequestMapping("/toEditProject")
    public String toEditProject(Integer projectId,Model model){
        FrontProjectMsg projectMsg = projectService.getProjectMsgByProjectId(projectId);
        model.addAttribute("project",projectMsg);
        model.addAttribute("projectId",projectId);
        return "/pages/front/html/project/editProject";
    }


    @RequestMapping("/editProject")
    @ResponseBody
    public String editProject(String projectName,Integer projectId,String projectMsg){
        return projectService.editProject(projectName,projectId,projectMsg);
    }

    @RequestMapping("/archiveProject")
    @ResponseBody
    public String archiveProject(Integer projectId){
            return projectService.archiveProject(projectId);
    }
}
