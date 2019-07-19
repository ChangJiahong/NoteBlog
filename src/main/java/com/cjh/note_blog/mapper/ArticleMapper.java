package com.cjh.note_blog.mapper;

import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface ArticleMapper extends MyMapper<Article> {

    /**
     * 查询
     * @param id
     * @return
     */
    public Article selectById(@Param(value = "id") Integer id);
}