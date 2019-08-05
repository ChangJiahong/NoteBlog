package com.cjh.note_blog.controller;

import com.cjh.note_blog.CSD.Cache.service.ICache;
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
    public ICache cache ;

    public User getUser(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(WebConst.LOGIN_USER_KEY);
        return user;
    }

}
