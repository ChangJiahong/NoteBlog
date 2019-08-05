package com.cjh.note_blog.CSD.Tag.service;

import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Tag;

/**
 * ：
 * 标签服务
 * @author ChangJiahong
 * @date 2019/7/19
 */

public interface ITagService {

    /**
     * 查找tag
     * @param tag
     * @return
     */
    public Result select(Tag tag);

    /**
     * 创建标签
     * @param tag
     */

    public void create(Tag tag);
}
