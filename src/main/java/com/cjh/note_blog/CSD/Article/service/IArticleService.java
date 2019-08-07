package com.cjh.note_blog.CSD.Article.service;

import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.github.pagehelper.PageInfo;

/**
 * ：
 * 文章服务 【dao】 【接口】
 * @author ChangJiahong
 * @date 2019/7/17
 */
public interface IArticleService {

    /**
     * 创建文章或修改文章
     * @param article 文章对象
     * @return Return ok on success, or fail
     */
    Result publish(Article article);

    /**
     * 根据文章id|alias获取文章
     * @param artName id|alias
     * @return result data type: Acticle
     */
    Result<Article> getArticleByArtName(String artName);

    /**
     * 删除文章 byId
     * @param id 文章id
     * @return 统一返回对象
     */
    Result delArticleById(Integer id);

    /**
     * 获取文章列表
     * @param page 页码
     * @param size 每页大小
     * @return 统一返回对象
     */
    Result<PageInfo<Article>> getArticles(Integer page, Integer size);

    /**
     * 更新文章访问量
     * @param id 文章id
     * @param increment 访问量增量
     * @return 统一返回对象
     */
    Result updateHits(Integer id, int increment);
}
