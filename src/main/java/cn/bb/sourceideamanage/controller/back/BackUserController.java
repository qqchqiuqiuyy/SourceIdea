package cn.bb.sourceideamanage.controller.back;

import cn.bb.sourceideamanage.common.config.PageSize;
import cn.bb.sourceideamanage.dto.back.BackUser;
import cn.bb.sourceideamanage.service.back.BackUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/AdminC")
public class BackUserController {

    @Autowired
    BackUserService backUserService;

    /**
     * 终端关于用户部分
     * @param model
     * @param userName
     * @param page
     * @return
     */
    @RequestMapping("/toUsers")
    public String toUser(Model model, String userName,Integer page)
    {

        if(null == page || page < 1){
            page = 1;
        }
        if(userName == null){
            userName = "";
        }
        PageInfo<BackUser> pageInfo = backUserService.findUserByPage(page, PageSize.PAGE_SIZE,userName);
        model.addAttribute("indexPage",page);
        model.addAttribute("totalPage",pageInfo.getPages());
        model.addAttribute("users",pageInfo.getList());
        return "/pages/back/back_user";
    }




}
