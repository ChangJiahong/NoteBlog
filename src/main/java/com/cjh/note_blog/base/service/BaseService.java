package com.cjh.note_blog.base.service;

import com.cjh.note_blog.app.cache.service.ICacheService;
import com.cjh.note_blog.base.Base;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.pojo.DO.User;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/9/23
 */
public abstract class BaseService extends Base {

    /**
     * 分页
     *
     * @param page 页码
     * @param size 大小
     */
    public void pagination(int page, int size) {
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
        size = size < 0 || size > WebConst.MAX_PAGESIZE ? WebConst.DEFAULT_PAGESIZE : size;
        PageHelper.startPage(page, size);
    }

}
