package com.cjh.note_blog.base.controller;

import com.cjh.note_blog.app.cache.service.ICacheService;
import com.cjh.note_blog.base.Base;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.pojo.DO.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 所有controller的父类
 *
 * @author ChangJiahong
 * @date 2019/7/16
 */
public abstract class BaseController extends Base {
    @Autowired
    public HttpServletResponse response;


}
