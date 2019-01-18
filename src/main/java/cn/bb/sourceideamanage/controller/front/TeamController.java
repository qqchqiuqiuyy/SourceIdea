package cn.bb.sourceideamanage.controller.front;

import cn.bb.sourceideamanage.common.config.PageSize;
import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.front.FrontTeam;
import cn.bb.sourceideamanage.dto.front.TeamMember;
import cn.bb.sourceideamanage.dto.front.TeamMsg;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Project;
import cn.bb.sourceideamanage.service.front.IdeaService;
import cn.bb.sourceideamanage.service.front.TeamService;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/TeamC")
public class TeamController {

    @Autowired
    TeamService teamService;

    @Autowired
    JSONObject jsonObject;

    @RequestMapping("/toTeam")

    public String toTeam(Model model, String teamName, Integer page){
        if(null == page || page < 1){
            page = 1;
        }
        if(teamName == null){
            teamName = "";
        }
        PageInfo<FrontTeam> info = teamService.findAllFrontTeam(page, PageSize.PAGE_SIZE, teamName);
        List<FrontTeam> teams = info.getList();
        model.addAttribute("teams",teams);
        model.addAttribute("indexPage",page);
        model.addAttribute("totalPage",info.getPages());
        return "pages/front/html/team/teamList";
    }

    @RequestMapping("/toTeamMsg")
    public String toTeamMsg(Model model, String teamName, HttpServletRequest request){
        Integer teamId = teamService.getTeamId(teamName);
        /*teamService*/
        TeamMsg team = teamService.findTeamMsg(teamName);
        //查询项目
        List<Project> projects = teamService.findAllProject(teamId);
        //查询成员
        List<TeamMember> members = teamService.findAllMemberByTeamId(teamId);
        //查询想法
        List<Idea> ideas = teamService.findAllTeamIdeas(teamId);
        //获取当前用户名
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        List<String> userTeams = teamService.findAllTeam(userId);
        JSONArray userteamsJson = JSONArray.fromObject(userTeams);
        model.addAttribute("userteamsJson",userteamsJson);
        model.addAttribute("team",team);
        model.addAttribute("members",members);
        model.addAttribute("projects",projects);
        model.addAttribute("ideas",ideas);
        return "pages/front/html/team/teamMsg";
    }

    @RequestMapping("/joinTeam")
    @ResponseBody
    public String joinTeam(Integer userId,Integer teamId){
        String info = teamService.joinTeam(userId, teamId);
        return info;
    }

    @RequestMapping("/toAddTeam")
    public String toAddTeam(){
        return "pages/front/html/team/addTeam";
    }

    @RequestMapping("/addTeam")
    @ResponseBody
    public String addTeam(String teamName,String teamMsg,HttpServletRequest request){
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String s = teamService.addTeam(teamName, teamMsg, userId);
        return s;
    }

}
