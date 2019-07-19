package com.cjh.note_blog.Article.service;

import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;

/**
 * ：
 * 文章服务 【service】 【接口】
 * @author ChangJiahong
 * @date 2019/7/17
 */
public interface IArticleService {

    /**
     * 创建文章或修改文章
     * @param article
     * @return Return ok on success, or fail
     */
    public Result publish(Article article);

    /**
     * 根据文章id获取文章
     * @param id
     * @return result data type: Acticle
     */
    public Result getArticleById(Integer id);
}
