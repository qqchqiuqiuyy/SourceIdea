package cn.bb.sourceideamanage.controller.front;

import cn.bb.sourceideamanage.common.enums.ModelMsg;
import cn.bb.sourceideamanage.dto.front.FrontIdea;
import cn.bb.sourceideamanage.dto.front.IdeaMsg;
import cn.bb.sourceideamanage.entity.BrainTime;
import cn.bb.sourceideamanage.entity.Comment;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Tag;
import cn.bb.sourceideamanage.service.back.BackIdeaService;
import cn.bb.sourceideamanage.service.front.IdeaService;
import cn.bb.sourceideamanage.service.front.UserService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static cn.bb.sourceideamanage.common.config.PageSize.PAGE_SIZE;

@Controller
@RequestMapping("/ideaC")
@Slf4j
public class IdeaController {

    @Autowired
    private IdeaService ideaService;
    @Autowired
    private BackIdeaService backIdeaService;


    @Autowired
    private UserService userService;

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
        if(tagName == null){
            tagName = "";
        }

        PageInfo<FrontIdea> info = ideaService.findAllFrontIdea(page, PAGE_SIZE, ideaName, tagName);
        List<FrontIdea> ideas = info.getList();

        List<Tag> tags = backIdeaService.findAllTag();
        List<Idea> ideasSupports = ideaService.getIdeaSupports();
        model.addAttribute(ModelMsg.TAG_NAME.getMsg(),tagName);
        model.addAttribute(ModelMsg.TAGS.getMsg(),tags);
        model.addAttribute(ModelMsg.IDEAS.getMsg(),ideas);
        model.addAttribute(ModelMsg.INDEX_PAGE.getMsg(),page);
        model.addAttribute(ModelMsg.TOTAL_PAGE.getMsg(),info.getPages());
        model.addAttribute(ModelMsg.IDEAS_SUPPORTS.getMsg(),ideasSupports);
        model.addAttribute(ModelMsg.IDEA_NAME.getMsg(),ideaName);
        return "/pages/front/html/idea/ideaList";
    }

    /**
     * 想法详细
     * @param model
     * @param ideaid
     * @return
     */
    @GetMapping("/toIdeaMsg/{ideaId}")
    public String ideaMsg(@PathVariable("ideaId") Integer  ideaid,Model model){

        IdeaMsg ideaMsg = ideaService.getIdeaMsg(ideaid);
        model.addAttribute(ModelMsg.IDEA_MSG.getMsg(),ideaMsg);
        return "/pages/front/html/idea/ideaMsg";
    }

    @GetMapping("/toIdeaComments/{ideaId}/{ideaName}")
    public String toIdeaComments(@PathVariable("ideaId") Integer ideaId,
                                 @PathVariable("ideaName") String ideaName,Model model){
        List<Comment> comments = ideaService.getAllComment(ideaId);
        model.addAttribute(ModelMsg.IDEA_ID.getMsg(),ideaId);
        model.addAttribute(ModelMsg.IDEA_NAME.getMsg(),ideaName);
        model.addAttribute(ModelMsg.COMMENTS.getMsg(), comments);
        return "/pages/front/html/idea/ideaComments";
    }


    @PutMapping("/upIdeaSupports/{ideaId}/{userId}")
    @ResponseBody
    public String upIdeaSupports(@PathVariable("ideaId") String ideaId,@PathVariable("userId") String userId){
        String info = ideaService.upIdeaSupports(ideaId, userId);
        return info;
    }

    /**
     * 评论想法
     * @param model
     * @param request
     * @param content
     * @param ideaName
     * @param ideaId
     * @return
     */
    @PostMapping("/commentIdea")
    public String commentIdea(Model model,HttpServletRequest request,
                              String content, String ideaName,
                              Integer ideaId ){
        ideaService.commentIdea(content,ideaName,ideaId,request);
        List<Comment> comments = ideaService.getAllComment(ideaId);
        model.addAttribute(ModelMsg.IDEA_ID.getMsg(),ideaId);
        model.addAttribute(ModelMsg.IDEA_NAME.getMsg(),ideaName);
        model.addAttribute(ModelMsg.COMMENTS.getMsg(), comments);
        return "/pages/front/html/idea/ideaComments";
    }

    @GetMapping("/toAddIdea")
    public String toAddIdea(Model model){
        List<Tag> tags = backIdeaService.findAllTag();
        model.addAttribute(ModelMsg.TAGS.getMsg(),tags);
        return "/pages/front/html/idea/addIdea";
    }

    @PostMapping("/addIdea")
    @ResponseBody
    public String addIdea(String ideaName, Integer tagId, String ideaMsg, HttpServletRequest request){

        return ideaService.addIdea(ideaName,tagId,ideaMsg,request);
    }


    @PostMapping("/commentIdeaUser")
    @ResponseBody
    public String commentIdeaUser(HttpServletRequest request,String content,
                                  Integer ideaId,String ideaName,
                                  Integer uid,Integer parentId,String parentName){
        Integer userId = (Integer)request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
        String userName = userService.getUserName(userId);
        return ideaService.commentIdeaUser(uid,ideaId,ideaName,userId,userName,parentId,parentName,content);
    }

    @GetMapping("/toAddBrainStorming")
    public String toAddBrainStorming(Model model){
        List<BrainTime> brainTimes = ideaService.getAllBrainTime();
        model.addAttribute("brainTimes",brainTimes);
        return  "pages/front/html/idea/addBrainStorming";
    }


    @PostMapping("/addBrainStorming")
    @ResponseBody
    public String addBrainStorming(HttpServletRequest request, String brainName,Integer timeId,String brainMsg ){
        Integer userId = (Integer)request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
        return  ideaService.addBrainStorming(userId,brainName,timeId,brainMsg);
    }

    @GetMapping("/toBrainStorming")
    public String toBrainStorming(Model model){
        List<Map<String, String>> brains = ideaService.allBrains();
        model.addAttribute(ModelMsg.BRAINS.getMsg(),brains);
        return "pages/front/html/idea/brainStorming";
    }


    @PutMapping("/upBrainSupports")
    @ResponseBody
    public String upBrainSupports(HttpServletRequest request,String brainName ){
        Integer userId = (Integer) request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
        return ideaService.upBrainSupports(brainName,userId);
    }

}
