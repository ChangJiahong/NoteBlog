package com.cjh.note_blog.CSD.Article.service;

import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.github.pagehelper.PageInfo;

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
     * 根据文章id|alias获取文章
     * @param artName
     * @return result data type: Acticle
     */
    public Result<Article> getArticleByArtName(String artName);

    /**
     * 删除文章 byId
     * @param id
     * @return
     */
    public Result delArticleById(Integer id);

    /**
     * 获取文章列表
     * @param page
     * @param size
     * @return
     */
    public Result<PageInfo<Article>> getArticles(Integer page, Integer size);
}
