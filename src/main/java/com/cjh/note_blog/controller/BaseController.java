package com.cjh.note_blog.controller;

import com.cjh.note_blog.app.cache.service.ICacheService;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.pojo.DO.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * 所有controller的父类
 *
 * @author ChangJiahong
 * @date 2019/7/16
 */
public abstract class BaseController {


    @Autowired
    public ICacheService webCacheService;

    protected User getUser(HttpServletRequest request){
        return (User) request.getAttribute(WebConst.USER_LOGIN);
    }

    protected String getUsername(HttpServletRequest request){
        User user = getUser(request);
        if (user!=null){
            return user.getUsername();
        }
        return null;
    }

    protected String getEmail(HttpServletRequest request){
        User user = getUser(request);
        if (user!=null){
            return user.getEmail();
        }
        return null;
    }
}
