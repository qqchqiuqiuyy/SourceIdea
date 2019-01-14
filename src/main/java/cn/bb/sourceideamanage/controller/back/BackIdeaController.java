package cn.bb.sourceideamanage.controller.back;

import cn.bb.sourceideamanage.common.config.PageSize;
import cn.bb.sourceideamanage.dto.back.BackIdea;
import cn.bb.sourceideamanage.entity.Tag;
import cn.bb.sourceideamanage.service.back.BackIdeaService;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/AdminC")
public class BackIdeaController {

    @Autowired
    BackIdeaService backIdeaService;

    @RequestMapping("/toIdeas")
    public String toIdea(Model model, String ideaName, String tagName, Integer page){
        if(null == page || page < 1){
            page = 1;
        }
        if(ideaName == null){
            ideaName = "";
        }
        if(tagName == null){
            tagName = "";
        }
        PageInfo<BackIdea> pageInfo = backIdeaService.findBackIdeaByPage(page, PageSize.PAGE_SIZE,ideaName,tagName);
        List<Tag> tags = backIdeaService.findAllTag();
        model.addAttribute("tags",tags);
        model.addAttribute("indexPage",page);
        model.addAttribute("totalPage",pageInfo.getPages());
        model.addAttribute("ideas",pageInfo.getList());

        return "/pages/back/back_idea";
    }

    @Autowired
    JSONObject jsonObject;

    @RequestMapping("/toDeleteIdea")
    @ResponseBody
    public String deleteIdea(Integer ideaId){
        if(ideaId == null){
            jsonObject.put("msg","0");
            return jsonObject.toString();
        }
        backIdeaService.deleteIdea(ideaId);
        jsonObject.put("msg","1");
        return jsonObject.toString();
    }
}
