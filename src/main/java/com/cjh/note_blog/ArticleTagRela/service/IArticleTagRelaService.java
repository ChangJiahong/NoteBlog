package com.cjh.note_blog.ArticleTagRela.service;

import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.ArticleCategory;
import com.cjh.note_blog.pojo.DO.ArticleTag;
import com.cjh.note_blog.pojo.DO.Tag;

import java.util.List;

/**
 * ：
 *
 * @author ChangJiahong
 * @date 2019/7/22
 */
public interface IArticleTagRelaService {
    /**
     * 建立文章 —— 标签关系
     * @param articleTag
     */
    public void create(ArticleTag articleTag);

    /**
     * 建立文章 —— 标签关系
     * @param aid 文章id
     * @param tid 标签id
     */
    public void create(Integer aid, Integer tid);

    /**
     * 建立文章 —— 标签关系
     * @param aid 文章id
     * @param tags 标签
     */
    public void create(Integer aid, List<Tag> tags);


    /**
     * 查询 关系
     * @param aid 文章id
     * @param tid 标签id
     */
    public Result selectOne(Integer aid, Integer tid);

    /**
     * 查询所有的文章id by articleId
     * @param aid
     * @return
     */
    public List<Tag> selectByArticleId(Integer aid);

    /**
     * 删除 关系
     * @param aid  文章id
     * @param tid  标签id
     */
    public void delete(Integer aid, Integer tid);

    /**
     * 删除 关系
     *
     * @param aid
     * @param tags
     */
    public void delete(Integer aid, List<Tag> tags);



}
