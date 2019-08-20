package com.cjh.note_blog.mapper;

import com.cjh.note_blog.pojo.DO.ArticleType;
import com.cjh.note_blog.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleTypeMapper extends MyMapper<ArticleType> {


    /**
     * 查找所有关系
     * @param tId type Id
     * @param username 用户名
     * @return 关系集合
     */
    List<ArticleType> selectByAuthor(@Param(value = "tId")
                                     Integer tId,
                                     @Param(value = "username")
                                     String username);

}