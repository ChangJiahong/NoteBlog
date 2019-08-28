package com.cjh.note_blog.app.type.service;

import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Type;

import java.util.List;

/**
 * ：
 * 标签种类服务
 * @author ChangJiahong
 * @date 2019/7/19
 */

public interface ITypeService {

    /**
     * 查找tag
     * @param type 标签对象
     * @return 统一返回对象
     */
    Result<Type> selectOne(Type type);

    /**
     * 创建标签
     * @param type 标签对象
     * @return 统一返回对象
     */
    Result<Type> create(Type type);

    /**
     * 获取种类标签
     * @param val category or tag
     * @return 统一返回对象
     */
    Result<List<Type>> getTypes(String val);
}
