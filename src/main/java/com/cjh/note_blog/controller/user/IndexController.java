package com.cjh.note_blog.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ：index界面
 * @author ChangJiahong
 * @date 2019/7/16
 */

@RestController
public class IndexController {

    /**
     * test demo
     * @return
     */
    @GetMapping("/")
    public String index(){
        return "HelloWolrd";
    }




}
