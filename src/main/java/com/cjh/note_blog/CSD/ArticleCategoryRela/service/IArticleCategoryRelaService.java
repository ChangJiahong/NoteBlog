package com.cjh.note_blog.CSD.ArticleCategoryRela.service;

import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.ArticleCategory;
import com.cjh.note_blog.pojo.DO.Tag;

import java.util.List;

/**
 * ：
 *
 * @author ChangJiahong
 * @date 2019/7/21
 */
public interface IArticleCategoryRelaService {

    /**
     * 建立文章 —— 种类关系
     * @param articleCategory
     */
    public void create(ArticleCategory articleCategory);

    /**
     * 建立文章 —— 种类关系
     * @param aid
     * @param cid
     */
    public void create(Integer aid, Integer cid);

    /**
     * 建立文章 —— 标签关系
     * @param aid 文章id
     * @param tags 标签
     */
    public void create(Integer aid, List<Tag> tags);


    /**
     * 查询 关系
     * @param aid
     * @param cid
     */
    public Result selectOne(Integer aid, Integer cid);


    /**
     * 查询所有的文章categroy by articleId
     * @param aid
     * @return
     */
    List<Tag> selectByArticleId(Integer aid);



    /**
     * 删除 关系
     * @param aid
     * @param cid
     */
    public void delete(Integer aid, Integer cid);

    /**
     * 删除 关系
     *
     * @param aid
     * @param tags
     */
    public void delete(Integer aid, List<Tag> tags);


}
