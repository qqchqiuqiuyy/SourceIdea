package cn.bb.sourceideamanage.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/indexC")
public class IndexController {

    /**
     * 去首页
     * @return
     */
    @GetMapping("/toIndex")
    public String toIndex(){
        return "/pages/front/html/index";
    }
}
