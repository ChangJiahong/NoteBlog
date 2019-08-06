package com.cjh.note_blog.mapper;

import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper extends MyMapper<Article> {

    /**
     * 查询
     * @param id
     * @return
     */
    public Article selectByArtName(@Param(value = "artName") String id,
                              @Param(value = "byId") Boolean byId);

    /**
     * 获取列表
     * @return
     */
    public List<Article> selectArticles();
}