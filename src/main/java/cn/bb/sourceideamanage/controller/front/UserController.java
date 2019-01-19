package cn.bb.sourceideamanage.controller.front;

import cn.bb.sourceideamanage.common.config.PageSize;
import cn.bb.sourceideamanage.common.enums.Roles;
import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.front.*;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Tag;
import cn.bb.sourceideamanage.service.back.BackIdeaService;
import cn.bb.sourceideamanage.service.front.IdeaService;
import cn.bb.sourceideamanage.service.front.ProjectService;
import cn.bb.sourceideamanage.service.front.TeamService;
import cn.bb.sourceideamanage.service.front.UserService;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static cn.bb.sourceideamanage.common.config.PageSize.PAGE_SIZE;

@Controller
@RequestMapping("/UserC")
public class UserController {

    @Autowired
    IdeaService ideaService;

    @Autowired
    BackIdeaService backIdeaService;

    @Autowired
    ProjectService projectService;

    @Autowired
    TeamService teamService;

    @Autowired
    UserService userService;

    @RequestMapping("/toHome")
    public String toHome(HttpServletRequest request,Model model){
        Integer userId = (Integer)request.getSession().getAttribute("userId");
        frontUser user = userService.getUserMsg(userId);
        List<inviteUser> invites = userService.getUserInvite(userId);
        model.addAttribute("invites",invites);
        model.addAttribute("user",user);
        return "pages/front/html/home";
    }

    /**
     * 跳转到我的想法页面
     * @param model
     * @param ideaName
     * @param tagName
     * @param page
     * @param request
     * @return
     */
    @RequestMapping("/toMyIdea")
    public String toIdea(Model model, String ideaName, String tagName, Integer page,HttpServletRequest request){
        if(null == page || page < 1){
            page = 1;
        }
        if(ideaName == null){
            ideaName = "";
        }
        if(tagName == null){
            tagName = "";
        }
        Integer userId = (Integer)request.getSession().getAttribute("userId");

        PageInfo<FrontIdea> info = ideaService.findAllMyIdea(page, PAGE_SIZE, ideaName, tagName,userId);
        List<FrontIdea> ideas = info.getList();

        List<Tag> tags = backIdeaService.findAllTag();
        model.addAttribute("tagName",tagName);
        model.addAttribute("tags",tags);
        model.addAttribute("ideas",ideas);
        model.addAttribute("indexPage",page);
        model.addAttribute("totalPage",info.getPages());
        return "/pages/front/html/idea/myIdea";
    }

    /**
     * 去我的项目页面
     * @param model
     * @param projectName
     * @param page
     * @param request
     * @return
     */
    @RequestMapping("/toMyProject")
    public String toMyProject(Model model, String projectName, Integer page,HttpServletRequest request){
        if(null == page || page < 1){
            page = 1;
        }
        if(projectName == null){
            projectName = "";
        }
        Integer userId = (Integer)request.getSession().getAttribute("userId");
        PageInfo<FrontProject> info = projectService.findAllMyProject(page, PageSize.PAGE_SIZE, projectName,userId);
        List<FrontProject> projects = info.getList();
        model.addAttribute("projects",projects);
        model.addAttribute("indexPage",page);
        model.addAttribute("totalPage",info.getPages());
        return "/pages/front/html/project/myProject";
    }


    @RequestMapping("/toMyTeam")
    public String toMyTeam(Model model, String teamName, Integer page,HttpServletRequest request){
        if(null == page || page < 1){
            page = 1;
        }
        if(teamName == null){
            teamName = "";
        }

        Integer userId = (Integer)request.getSession().getAttribute("userId");
        PageInfo<FrontTeam> info = teamService.findAllMyTeam(page, PageSize.PAGE_SIZE, teamName,userId);
        List<FrontTeam> teams = info.getList();
        model.addAttribute("teams",teams);
        model.addAttribute("indexPage",page);
        model.addAttribute("totalPage",info.getPages());
        return "/pages/front/html/team/myTeam";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "/pages/front/html/user/login";
    }
    @RequestMapping("/toReg")
    public String toReg(){
        return "/pages/front/html/user/reg";
    }


    @RequestMapping("/toMyTeamMsg")
    public String toMyTeamMsg(Model model, String teamName, HttpServletRequest request){
        //获取团员列表
        List<MyTeamMember> members = teamService.findAllMyTeamMember(teamName);
        //获取当前用户名
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        //查找这个团队的Id
        Integer teamId = teamService.getTeamId(teamName);

        //审核表
        List<ApplyUser> applies = userService.getAllAppy(teamName);
        //找到所有project
        List<FrontProject> projects = userService.findProjects(teamName);
        //找到该团队所有想法
        List<FrontIdea> ideas = ideaService.findAllTeamIdea(teamName);

        model.addAttribute("manager", Roles.UserProjectManager.getRoleMsg());
        model.addAttribute("ideas",ideas);
        model.addAttribute("projects",projects);
        model.addAttribute("applies",applies);
        model.addAttribute("teamName",teamName);
        model.addAttribute("members",members);
        model.addAttribute("teamId",teamId);
        return "pages/front/html/team/myTeamMsg";
    }

    @RequestMapping("/delMember")
    @ResponseBody
    public String delMember(Integer userId,String teamName){
        String info = userService.delMember(userId, teamName);
        return info;
    }

    /**
     * 队长审批
     * @param userId
     * @param teamName
     * @return
     */
    @RequestMapping("/agreeMember")
    @ResponseBody
    public String agreeMember(Integer userId,String teamName){
        String s = userService.agreeMember(userId,teamName);
        return s;
    }
    @RequestMapping("/delProject")
    @ResponseBody
    public String delProject(Integer projectId){
        String info = userService.delProject(projectId);
        return info;
    }



    @RequestMapping("/toAddTeamIdea")
    public String toAddTeamIdea(Model model,String teamName){
        List<Tag> allTag = backIdeaService.findAllTag();
        model.addAttribute("tags",allTag);
        model.addAttribute("teamName",teamName);
        return "/pages/front/html/team/addTeamIdea";
    }
    @RequestMapping("/addTeamIdea")
    public String addTeamIdea(String ideaName, Integer tagId, String ideaMsg,String teamName ,HttpServletRequest request){
        ideaService.addTeamIdea(ideaName,tagId,ideaMsg,teamName,request);
        return "redirect:/UserC/toMyTeamMsg?teamName="+teamName;
    }


    @RequestMapping("/delIdea")
    @ResponseBody
    public String delIdea(Integer ideaId){
        String s = ideaService.delIdea(ideaId);
        return s;
    }

    @RequestMapping("/toAddProject")
    public String toAddProject(String teamName,Model model){
        model.addAttribute("teamName",teamName);
        return "/pages/front/html/team/addProject";
    }


    @RequestMapping("/addTeamProject")
    @ResponseBody
    public String addTeamProject(String teamName,String projectName,String projectMsg){
        String s = userService.addTeamProject(teamName, projectName, projectMsg);
        return s;
    }




    @RequestMapping("/toUsers")
    public String toUsesrs(Model model, String userName, Integer page,String teamName){
        if(null == page || page < 1){
            page = 1;
        }
        if(userName == null){
            userName = "";
        }
        PageInfo<frontUser> info = userService.findAllFrontUser(page, PAGE_SIZE, userName);

        List<frontUser> users = info.getList();
        model.addAttribute("users",users);
        model.addAttribute("indexPage",page);
        model.addAttribute("totalPage",info.getPages());
        model.addAttribute("teamName",teamName);
        return "pages/front/html/user/userList";
    }


   @RequestMapping("/toUserMsg")
    public String toUserMsg(Model model,HttpServletRequest request,Integer userId,String teamName){

        //查找当前用户信息
       frontUser userMsg = userService.getUserMsg(userId);
       Boolean flag = userService.checkUserInInvite(userId, teamName);
       //查找这个用户的所有teamName
        List<String> userTeams = teamService.findAllTeam(userId);
        JSONArray userteamsJson = JSONArray.fromObject(userTeams);

        model.addAttribute("flag",flag);
        model.addAttribute("userteamsJson",userteamsJson);
        model.addAttribute("teamName",teamName);
        model.addAttribute("userMsg",userMsg);

        return "pages/front/html/user/userMsg";
    }

    @RequestMapping("/invite")
    @ResponseBody
    public String invite(Integer userId,String teamName){
        String invite = userService.invite(userId, teamName);
        return invite;
    }

    /**
     * 用户同意部分
     * @param userId
     * @param teamId
     * @return
     */
    @RequestMapping("/agree")
    @ResponseBody
    public String agree(Integer userId,Integer teamId){
        String agree = userService.agree(userId, teamId);
        return agree;
    }



    @RequestMapping("/awardManager")
    @ResponseBody
    public String awardManager(Integer userId, String teamName){
        return userService.awardManager(userId,teamName);
    }
}

