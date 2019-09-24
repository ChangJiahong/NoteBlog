package com.cjh.note_blog.base;

import com.cjh.note_blog.app.cache.service.ICacheService;
import com.cjh.note_blog.conf.WebConfig;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.pojo.DO.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/9/23
 */
public abstract class Base {

    @Autowired
    public WebConfig webConfig;

    @Autowired
    public ICacheService webCacheService;

    @Autowired
    public HttpServletRequest request;

    protected User getUser(){
        return (User) request.getAttribute(WebConst.USER_LOGIN);
    }

    protected String getUsername(){
        User user = getUser();
        if (user!=null){
            return user.getUsername();
        }
        return null;
    }

    protected String getEmail(){
        User user = getUser();
        if (user!=null){
            return user.getEmail();
        }
        return null;
    }
}
