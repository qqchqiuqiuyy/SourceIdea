package cn.bb.sourceideamanage.controller.back;

import cn.bb.sourceideamanage.common.config.PageSize;
import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.service.back.BackTeamService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/AdminC")
public class BackTeamController {

    @Autowired
    BackTeamService backTeamService;

    @RequestMapping("/toTeams")
    public String toTeam(Model model, String teamName, Integer page){

        if(null == page || page < 1){
            page = 1;
        }
        if(teamName == null){
            teamName = "";
        }
        PageInfo<BackTeam> pageInfo = backTeamService.findTeamByPage(page, PageSize.PAGE_SIZE, teamName);
        List<BackTeam> backTeams = pageInfo.getList();
        model.addAttribute("indexPage",page);
        model.addAttribute("totalPage",pageInfo.getPages());
        model.addAttribute("backTeams",backTeams);
        return "/pages/back/back_team";
    }
}
