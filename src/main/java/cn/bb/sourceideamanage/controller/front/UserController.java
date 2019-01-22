package cn.bb.sourceideamanage.controller.front;

import cn.bb.sourceideamanage.common.config.PageSize;
import cn.bb.sourceideamanage.common.enums.ModelMsg;
import cn.bb.sourceideamanage.common.enums.Roles;
import cn.bb.sourceideamanage.dto.front.*;
import cn.bb.sourceideamanage.entity.Tag;
import cn.bb.sourceideamanage.service.back.BackIdeaService;
import cn.bb.sourceideamanage.service.front.IdeaService;
import cn.bb.sourceideamanage.service.front.ProjectService;
import cn.bb.sourceideamanage.service.front.TeamService;
import cn.bb.sourceideamanage.service.front.UserService;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static cn.bb.sourceideamanage.common.config.PageSize.PAGE_SIZE;

@Controller
@RequestMapping("/userC")
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

    @GetMapping("/toHome")
    public String toHome(HttpServletRequest request,Model model){
        Integer userId = (Integer)request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
        FrontUser user = userService.getUserMsg(userId);
        List<InviteUser> invites = userService.getUserInvite(userId);
        model.addAttribute(ModelMsg.INVITES.getMsg(),invites);
        model.addAttribute(ModelMsg.USER.getMsg(),user);
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
        Integer userId = (Integer)request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());

        PageInfo<FrontIdea> info = ideaService.findAllMyIdea(page, PAGE_SIZE, ideaName, tagName,userId);
        List<FrontIdea> ideas = info.getList();

        List<Tag> tags = backIdeaService.findAllTag();
        model.addAttribute(ModelMsg.TAG_NAME.getMsg(),tagName);
        model.addAttribute(ModelMsg.TAGS.getMsg(),tags);
        model.addAttribute(ModelMsg.IDEAS.getMsg(),ideas);
        model.addAttribute(ModelMsg.INDEX_PAGE.getMsg(),page);
        model.addAttribute(ModelMsg.TOTAL_PAGE.getMsg(),info.getPages());
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
        Integer userId = (Integer)request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
        PageInfo<FrontProject> info = projectService.findAllMyProject(page, PageSize.PAGE_SIZE, projectName,userId);
        List<FrontProject> projects = info.getList();
        model.addAttribute(ModelMsg.PROJECTS.getMsg(),projects);
        model.addAttribute(ModelMsg.INDEX_PAGE.getMsg(),page);
        model.addAttribute(ModelMsg.TOTAL_PAGE.getMsg(),info.getPages());
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

        Integer userId = (Integer)request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
        PageInfo<FrontTeam> info = teamService.findAllMyTeam(page, PageSize.PAGE_SIZE, teamName,userId);
        List<FrontTeam> teams = info.getList();
        model.addAttribute(ModelMsg.TEAMS.getMsg(),teams);
        model.addAttribute(ModelMsg.INDEX_PAGE.getMsg(),page);
        model.addAttribute(ModelMsg.TOTAL_PAGE.getMsg(),info.getPages());
        return "/pages/front/html/team/myTeam";
    }

    @GetMapping("/toLogin")
    public String toLogin(){
        return "/pages/front/html/user/login";
    }
    @GetMapping("/toReg")
    public String toReg(){
        return "/pages/front/html/user/reg";
    }


    @GetMapping("/toMyTeamMsg/{teamName}")
    public String toMyTeamMsg(Model model, @PathVariable("teamName") String teamName, HttpServletRequest request){
        //获取团员列表
        List<MyTeamMember> members = teamService.findAllMyTeamMember(teamName);
        //获取当前用户名
        Integer userId = (Integer) request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());

        //查找这个团队的Id
        Integer teamId = teamService.getTeamId(teamName);

        //审核表
        List<ApplyUser> applies = userService.getAllAppy(teamName);
        //找到所有project
        List<FrontProject> projects = userService.findProjects(teamName);
        //找到该团队所有想法
        List<FrontIdea> ideas = ideaService.findAllTeamIdea(teamName);

        model.addAttribute(ModelMsg.USER_ID.getMsg(),userId);
        model.addAttribute(ModelMsg.MANAGER.getMsg(), Roles.UserProjectManager.getRoleMsg());
        model.addAttribute(ModelMsg.IDEAS.getMsg(),ideas);
        model.addAttribute(ModelMsg.PROJECTS.getMsg(),projects);
        model.addAttribute(ModelMsg.APPLIES.getMsg(),applies);
        model.addAttribute(ModelMsg.TEAM_NAME.getMsg(),teamName);
        model.addAttribute(ModelMsg.MEMBERS.getMsg(),members);
        model.addAttribute(ModelMsg.TEAM_ID.getMsg(),teamId);
        return "pages/front/html/team/myTeamMsg";
    }

    @DeleteMapping("/delMember/{userId}/{teamName}")
    @ResponseBody
    public String delMember(@PathVariable("userId") Integer userId,
                            @PathVariable("teamName") String teamName){
        String info = userService.delMember(userId, teamName);
        return info;
    }

    /**
     * 队长审批
     * @param userId
     * @param teamName
     * @return
     */
    @PostMapping("/agreeMember/{userId}/{teamName}")
    @ResponseBody
    public String agreeMember(@PathVariable("userId") Integer userId,
                              @PathVariable("teamName") String teamName){
        String s = userService.agreeMember(userId,teamName);
        return s;
    }
    @DeleteMapping("/delProject/{projectId}")
    @ResponseBody
    public String delProject(@PathVariable("projectId") Integer projectId){
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


    @DeleteMapping("/delIdea/{ideaId}")
    @ResponseBody
    public String delIdea(@PathVariable("ideaId") Integer ideaId){
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
        PageInfo<FrontUser> info = userService.findAllFrontUser(page, PAGE_SIZE, userName);

        List<FrontUser> users = info.getList();
        model.addAttribute("users",users);
        model.addAttribute("indexPage",page);
        model.addAttribute("totalPage",info.getPages());
        model.addAttribute("teamName",teamName);
        return "pages/front/html/user/userList";
    }


   @RequestMapping("/toUserMsg")
    public String toUserMsg(Model model,HttpServletRequest request,Integer userId,String teamName){

        //查找当前用户信息
       FrontUser userMsg = userService.getUserMsg(userId);
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



    @PutMapping("/awardManager/{userId}/{teamName}")
    @ResponseBody
    public String awardManager(@PathVariable("userId") Integer userId,@PathVariable("teamName") String teamName){
        return userService.awardManager(userId,teamName);
    }
}

