package cn.bb.sourceideamanage.controller.front;

import cn.bb.sourceideamanage.common.config.PageSize;
import cn.bb.sourceideamanage.common.enums.ModelMsg;
import cn.bb.sourceideamanage.dto.front.FrontTeam;
import cn.bb.sourceideamanage.dto.front.TeamMember;
import cn.bb.sourceideamanage.dto.front.TeamMsg;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Project;
import cn.bb.sourceideamanage.service.front.TeamService;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/teamC")
public class TeamController {

    @Autowired
    TeamService teamService;

    @Autowired
    JSONObject jsonObject;

    /**
     * 团队分页
     * @param model
     * @param teamName  团队名
     * @param page      当前页
     * @return
     */
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
        model.addAttribute(ModelMsg.TEAMS.getMsg(),teams);
        model.addAttribute(ModelMsg.INDEX_PAGE.getMsg(),page);
        model.addAttribute(ModelMsg.TOTAL_PAGE.getMsg(),info.getPages());
        model.addAttribute(ModelMsg.TEAM_NAME.getMsg(),teamName);
        return "pages/front/html/team/teamList";
    }


    /**
     * 去团队信息页面
     * @param model
     * @param teamName 团队名
     * @param request
     * @return
     */
    @GetMapping("/toTeamMsg/{teamName}")
    public String toTeamMsg(Model model, @PathVariable("teamName") String teamName, HttpServletRequest request){
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
        Integer userId = (Integer) request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());

        List<String> userTeams = teamService.findAllTeam(userId);
        JSONArray userteamsJson = JSONArray.fromObject(userTeams);

        model.addAttribute(ModelMsg.USER_TEAMS_JSON.getMsg(),userteamsJson);
        model.addAttribute(ModelMsg.TEAM.getMsg(),team);
        model.addAttribute(ModelMsg.MEMBERS.getMsg(),members);
        model.addAttribute(ModelMsg.PROJECTS.getMsg(),projects);
        model.addAttribute(ModelMsg.IDEAS.getMsg(),ideas);
        return "pages/front/html/team/teamMsg";
    }

    /**
     * 加入团队
     * @param userId 加入用户的id
     * @param teamId  团队id
     * @return
     */
    @PostMapping("/joinTeam/{userId}/{teamId}")
    @ResponseBody
    public String joinTeam(@PathVariable("userId") Integer userId,
                           @PathVariable("teamId") Integer teamId){
        String info = teamService.joinTeam(userId, teamId);
        return info;
    }

    /**
     * 去团队添加页面
     * @return
     */
    @GetMapping("/toAddTeam")
    public String toAddTeam(){
        return "pages/front/html/team/addTeam";
    }

    /**
     * 增加团队
     * @param teamName 团队名
     * @param teamMsg   团队信息
     * @param request
     * @return
     */
    @PostMapping("/addTeam")
    @ResponseBody
    public String addTeam(String teamName,String teamMsg,HttpServletRequest request){
        Integer userId = (Integer) request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
        String s = teamService.addTeam(teamName, teamMsg, userId);
        return s;
    }

}
