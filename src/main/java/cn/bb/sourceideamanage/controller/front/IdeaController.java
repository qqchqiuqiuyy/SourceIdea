package cn.bb.sourceideamanage.controller.front;

import cn.bb.sourceideamanage.common.config.PageSize;
import cn.bb.sourceideamanage.dto.front.FrontIdea;
import cn.bb.sourceideamanage.dto.front.FrontTeam;
import cn.bb.sourceideamanage.dto.front.IdeaMsg;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Tag;
import cn.bb.sourceideamanage.service.back.BackIdeaService;
import cn.bb.sourceideamanage.service.front.IdeaService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static cn.bb.sourceideamanage.common.config.PageSize.PAGE_SIZE;

@Controller
@RequestMapping("/IdeaC")
public class IdeaController {
    @Autowired
    IdeaService ideaService;
    @Autowired
    BackIdeaService backIdeaService;

    /**
     * 去idea想法列表 并且有分页
     * @param model
     * @param ideaName
     * @param tagName
     * @param page
     * @return
     */
    @RequestMapping("/toIdea")
    public String toIdea(Model model, String ideaName, String tagName,Integer page){
        if(null == page || page < 1){
            page = 1;
        }
        if(ideaName == null){
            ideaName = "";
        }

        PageInfo<FrontIdea> info = ideaService.findAllFrontIdea(page, PAGE_SIZE, ideaName, tagName);
        List<FrontIdea> ideas = info.getList();

        List<Tag> tags = backIdeaService.findAllTag();
        List<Idea> ideasSupports = ideaService.getIdeaSupports();
        model.addAttribute("tagName",tagName);
        model.addAttribute("tags",tags);
        model.addAttribute("ideas",ideas);
        model.addAttribute("indexPage",page);
        model.addAttribute("totalPage",info.getPages());
        model.addAttribute("ideasSupports",ideasSupports);
        return "/pages/front/html/idea/ideaList";
    }

    @RequestMapping("toIdeaMsg")
    public String ideaMsg(Model model,Integer ideaId){
        IdeaMsg ideaMsg = ideaService.getIdeaMsg(ideaId);
        model.addAttribute("ideaMsg",ideaMsg);
        return "/pages/front/html/idea/ideaMsg";
    }

    @RequestMapping("/toIdeaComments")
    public String toIdeaComments(Integer ideaId){
        return "/pages/front/html/idea/ideaComments";
    }
    @RequestMapping("/upIdeaSupports")
    @ResponseBody
    public String upIdeaSupports(String ideaId,String userId){
        String info = ideaService.upIdeaSupports(ideaId, userId);
        return info;
    }
}
