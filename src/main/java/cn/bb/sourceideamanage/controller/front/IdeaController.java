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
     * @param ideaName 想法名
     * @param tagName   标签名
     * @param page  当前页号
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
     * 根据想法id得到想法详细
     * @param model
     * @param ideaid    想法id
     * @return
     */
    @GetMapping("/toIdeaMsg/{ideaId}")
    public String ideaMsg(@PathVariable("ideaId") Integer  ideaid,Model model){

        IdeaMsg ideaMsg = ideaService.getIdeaMsg(ideaid);
        model.addAttribute(ModelMsg.IDEA_MSG.getMsg(),ideaMsg);
        return "/pages/front/html/idea/ideaMsg";
    }

    /**
     * 根据想法id 想法名 去该想法评论页面
     * @param ideaId    想法id
     * @param ideaName  想法名
     * @param model
     * @return
     */
    @GetMapping("/toIdeaComments/{ideaId}/{ideaName}")
    public String toIdeaComments(@PathVariable("ideaId") Integer ideaId,
                                 @PathVariable("ideaName") String ideaName,Model model){
        List<Comment> comments = ideaService.getAllComment(ideaId);
        model.addAttribute(ModelMsg.IDEA_ID.getMsg(),ideaId);
        model.addAttribute(ModelMsg.IDEA_NAME.getMsg(),ideaName);
        model.addAttribute(ModelMsg.COMMENTS.getMsg(), comments);
        return "/pages/front/html/idea/ideaComments";
    }


    /**
     * 给该想法点赞
     * @param ideaId    想法id
     * @param userId    用户名
     * @return
     */
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
     * @param content   内容
     * @param ideaName  想法名
     * @param ideaId    想法id
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

    /**
     * 去添加想法页面
     * @param model
     * @return
     */
    @GetMapping("/toAddIdea")
    public String toAddIdea(Model model){
        List<Tag> tags = backIdeaService.findAllTag();
        model.addAttribute(ModelMsg.TAGS.getMsg(),tags);
        return "/pages/front/html/idea/addIdea";
    }

    /**
     * 添加想法
     * @param ideaName  想法名
     * @param tagId     标签id
     * @param ideaMsg   想法信息
     * @param request
     * @return
     */
    @PostMapping("/addIdea")
    @ResponseBody
    public String addIdea(String ideaName, Integer tagId, String ideaMsg, HttpServletRequest request){

        return ideaService.addIdea(ideaName,tagId,ideaMsg,request);
    }

    /**
     * 评论想法
     * @param request
     * @param content   内容
     * @param ideaId    想法id
     * @param ideaName  想法名
     * @param uid       父评论id
     * @param parentId  评论他人的id
     * @param parentName    评论他人的名字
     * @return
     */
    @PostMapping("/commentIdeaUser")
    @ResponseBody
    public String commentIdeaUser(HttpServletRequest request,String content,
                                  Integer ideaId,String ideaName,
                                  Integer uid,Integer parentId,String parentName){
        Integer userId = (Integer)request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
        String userName = userService.getUserName(userId);
        return ideaService.commentIdeaUser(uid,ideaId,ideaName,userId,userName,parentId,parentName,content);
    }

    /**
     * 去添加头脑风暴页面
     * @param model
     * @return
     */
    @GetMapping("/toAddBrainStorming")
    public String toAddBrainStorming(Model model){
        List<BrainTime> brainTimes = ideaService.getAllBrainTime();
        model.addAttribute("brainTimes",brainTimes);
        return  "pages/front/html/idea/addBrainStorming";
    }


    /**
     * 添加头脑风暴
     * @param request
     * @param brainName 头脑风暴名
     * @param timeId    时间
     * @param brainMsg  头脑风暴信息
     * @return
     */
    @PostMapping("/addBrainStorming")
    @ResponseBody
    public String addBrainStorming(HttpServletRequest request, String brainName,Integer timeId,String brainMsg ){
        Integer userId = (Integer)request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
        return  ideaService.addBrainStorming(userId,brainName,timeId,brainMsg);
    }

    /**
     * 去头脑风暴页面
     * @param model
     * @return
     */
    @GetMapping("/toBrainStorming")
    public String toBrainStorming(Model model){
        List<Map<String, String>> brains = ideaService.allBrains();
        model.addAttribute(ModelMsg.BRAINS.getMsg(),brains);
        return "pages/front/html/idea/brainStorming";
    }


    /**
     * 给头脑风暴点赞
     * @param request
     * @param brainName 头脑风暴名
     * @return
     */
    @PutMapping("/upBrainSupports")
    @ResponseBody
    public String upBrainSupports(HttpServletRequest request,String brainName ){
        Integer userId = (Integer) request.getSession().getAttribute(ModelMsg.USER_ID.getMsg());
        return ideaService.upBrainSupports(brainName,userId);
    }

}
