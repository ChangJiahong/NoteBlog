package com.cjh.note_blog.CSD.Type.service;

import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Type;

/**
 * ：
 * 标签种类服务
 * @author ChangJiahong
 * @date 2019/7/19
 */

public interface ITypeService {

    /**
     * 查找tag
     * @param type
     * @return
     */
    public Result selectOne(Type type);

    /**
     * 创建标签
     * @param type
     */

    public Result create(Type type);
}
