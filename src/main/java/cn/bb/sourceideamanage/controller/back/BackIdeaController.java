package cn.bb.sourceideamanage.controller.back;

import cn.bb.sourceideamanage.common.config.PageSize;
import cn.bb.sourceideamanage.common.enums.ModelMsg;
import cn.bb.sourceideamanage.dto.back.BackIdea;
import cn.bb.sourceideamanage.entity.Tag;
import cn.bb.sourceideamanage.service.back.BackIdeaService;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/adminC")
public class BackIdeaController {

    @Autowired
    BackIdeaService backIdeaService;

    @Autowired
    JSONObject jsonObject;

    /**
     *去想法页面
     * @param model
     * @param ideaName  想法名
     * @param tagName   标签名
     * @param page      当前页
     * @return
     */
    @GetMapping("/toIdeas")
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
        model.addAttribute(ModelMsg.TAGS.getMsg(),tags);
        model.addAttribute(ModelMsg.INDEX_PAGE.getMsg(),page);
        model.addAttribute(ModelMsg.TOTAL_PAGE.getMsg(),pageInfo.getPages());
        model.addAttribute(ModelMsg.IDEAS.getMsg(),pageInfo.getList());
        model.addAttribute(ModelMsg.IDEA_NAME.getMsg(),ideaName);
        model.addAttribute(ModelMsg.TAG_NAME.getMsg(),tagName);
        return "/pages/back/back_idea";
    }


    /**
     * 删除想法
     * @param ideaId
     * @return
     */
    @DeleteMapping("/toDeleteIdea/{ideaId}")
    @ResponseBody
    public String deleteIdea(@PathVariable("ideaId") Integer ideaId){
        return backIdeaService.deleteIdea(ideaId);
    }
}
