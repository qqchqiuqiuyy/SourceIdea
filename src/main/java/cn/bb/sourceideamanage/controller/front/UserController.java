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
import org.springframework.cache.annotation.CacheEvict;
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
        model.addAttribute(ModelMsg.IDEA_NAME.getMsg(),ideaName);
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
        model.addAttribute(ModelMsg.PROJECT_NAME.getMsg(),projectName);
        return "/pages/front/html/project/myProject";
    }


    /**
     * 去我的团队分页页面
     * @param model
     * @param teamName  团队名
     * @param page      当前页号
     * @param request
     * @return
     */
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
        model.addAttribute(ModelMsg.TEAM_NAME.getMsg(),teamName);
        return "/pages/front/html/team/myTeam";
    }

    /**
     * 去登录页面
     * @return
     */
    @GetMapping("/toLogin")
    public String toLogin(){
        return "/pages/front/html/user/login";
    }

    /**
     * 去注册页面
     * @return
     */
    @GetMapping("/toReg")
    public String toReg(){
        return "/pages/front/html/user/reg";
    }


    /**
     * 我参与的团队页面
     * @param model
     * @param teamName  团队名
     * @param request
     * @return
     */
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


    @DeleteMapping("/delProject/{projectId}/{teamName}")
    @ResponseBody
    public String delProject(@PathVariable("projectId") Integer projectId,@PathVariable("teamName") String teamName){
        String info = userService.delProject(projectId,teamName);
        return info;
    }



    @GetMapping("/toAddTeamIdea/{teamName}")
    public String toAddTeamIdea(Model model,@PathVariable("teamName") String teamName){
        List<Tag> allTag = backIdeaService.findAllTag();
        model.addAttribute(ModelMsg.TAGS.getMsg(),allTag);
        model.addAttribute(ModelMsg.TEAM_NAME.getMsg(),teamName);
        return "/pages/front/html/team/addTeamIdea";
    }


    /**
     * 添加团队想法
     * @param ideaName 想法名
     * @param tagId     想法标签
     * @param ideaMsg   想法信息
     * @param teamName  团队名
     * @param request
     * @return
     */
    @PostMapping("/addTeamIdea")
    @ResponseBody
    public String addTeamIdea(String ideaName, Integer tagId, String ideaMsg,String teamName ,HttpServletRequest request){
        return ideaService.addTeamIdea(ideaName,tagId,ideaMsg,teamName,request);
    }

    /**
     * 删除一条想法
     * @param ideaId 想法id
     * @param teamName  团队名
     * @return
     */
    @DeleteMapping("/delIdea/{ideaId}/{teamName}")
    @ResponseBody
    public String delIdea(@PathVariable("ideaId") Integer ideaId ,@PathVariable("teamName") String teamName){
        String s = ideaService.delIdea(ideaId,teamName);
        return s;
    }

    /**
     * 去添加项目页面
     * @param teamName 该团队名
     * @param model
     * @return
     */
    @GetMapping("/toAddProject/{teamName}")
    public String toAddProject(@PathVariable("teamName") String teamName,Model model){
        model.addAttribute(ModelMsg.TEAM_NAME.getMsg(),teamName);
        return "/pages/front/html/team/addProject";
    }


    /**
     * 添加团队项目
     * @param teamName 团队名
     * @param projectName   项目名
     * @param projectMsg    项目信息
     * @return
     */
    @PostMapping("/addTeamProject")
    @ResponseBody
    public String addTeamProject(String teamName,String projectName,String projectMsg){
        String s = userService.addTeamProject(teamName, projectName, projectMsg);
        return s;
    }


    /**
     * 查询用户分页页面
     * @param model
     * @param userName 用户名
     * @param page  当前页
     * @param teamName  团队名
     * @return
     */
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
        model.addAttribute(ModelMsg.USERS.getMsg(),users);
        model.addAttribute(ModelMsg.INDEX_PAGE.getMsg(),page);
        model.addAttribute(ModelMsg.TOTAL_PAGE.getMsg(),info.getPages());
        model.addAttribute(ModelMsg.TEAM_NAME.getMsg(),teamName);
        return "pages/front/html/user/userList";
    }


    /**
     * 去用户个人信息页面
     * @param model
     * @param request
     * @param userId    用户id
     * @param teamName  团队名
     * @return
     */
   @GetMapping("/toUserMsg/{userId}/{teamName}")
    public String toUserMsg(Model model,HttpServletRequest request,@PathVariable("userId") Integer userId,@PathVariable("teamName") String teamName){

        //查找当前用户信息
       FrontUser userMsg = userService.getUserMsg(userId);
       Boolean flag = userService.checkUserInInvite(userId, teamName);
       //查找这个用户的所有teamName
        List<String> userTeams = teamService.findAllTeam(userId);
        JSONArray userteamsJson = JSONArray.fromObject(userTeams);

        model.addAttribute(ModelMsg.FLAG.getMsg(),flag);
        model.addAttribute(ModelMsg.USER_TEAMS_JSON.getMsg(),userteamsJson);
        model.addAttribute(ModelMsg.TEAM_NAME.getMsg(),teamName);
        model.addAttribute(ModelMsg.USER_MSG.getMsg(),userMsg);

        return "pages/front/html/user/userMsg";
    }

    /**
     * 邀请用户进入团队
     * @param userId    用户id
     * @param teamName  邀请的团队名
     * @return
     */
    @PostMapping("/invite/{userId}/{teamName}")
    @ResponseBody
    public String invite(@PathVariable("userId") Integer userId,@PathVariable("teamName") String teamName){
        String invite = userService.invite(userId, teamName);
        return invite;
    }

    /**
     * 用户同意部分
     * @param userId    用户id
     * @param teamId    同意进入的团队id
     * @return
     */
    @PostMapping("/agree/{userId}/{teamId}")
    @ResponseBody
    public String agree(@PathVariable("userId") Integer userId,@PathVariable("teamId") Integer teamId){
        String agree = userService.agree(userId, teamId);
        return agree;
    }


    /**
     * 授予管理员权限
     * @param userId    用户id
     * @param teamName  对应的团队名
     * @return
     */
    @PutMapping("/awardManager/{userId}/{teamName}")
    @ResponseBody
    public String awardManager(@PathVariable("userId") Integer userId,@PathVariable("teamName") String teamName){
        return userService.awardManager(userId,teamName);
    }
}

