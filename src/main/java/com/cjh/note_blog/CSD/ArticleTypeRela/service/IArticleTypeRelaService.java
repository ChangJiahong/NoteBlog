package com.cjh.note_blog.CSD.ArticleTypeRela.service;

import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.ArticleType;
import com.cjh.note_blog.pojo.DO.Type;

import java.util.List;

/**
 * ：
 *
 * @author ChangJiahong
 * @date 2019/7/22
 */
public interface IArticleTypeRelaService {
    /**
     * 建立文章 —— 标签关系
     * @param articleType
     */
    public Result<ArticleType> create(ArticleType articleType);

    /**
     * 建立文章 —— 标签关系
     * @param aid 文章id
     * @param tid 标签id
     */
    public Result<ArticleType> create(Integer aid, Integer tid);

    /**
     * 建立文章 —— 标签关系
     * @param aid 文章id
     * @param types 标签
     * @return
     */
    public Result<List<ArticleType>> create(Integer aid, List<Type> types);


    /**
     * 查询 关系
     * @param aid 文章id
     * @param tid 标签id
     */
    public Result<ArticleType> selectOne(Integer aid, Integer tid);

    /**
     * 查询所有的文章id by articleId
     * @param aid
     * @return
     */
    public Result<List<Type>> selectByArticleId(Integer aid);

    /**
     * 删除 关系
     * @param aid  文章id
     * @param tid  标签id
     */
    public Result<ArticleType> delete(Integer aid, Integer tid);

    /**
     * 删除 关系
     *
     * @param aid
     * @param types
     */
    public Result<List<ArticleType>> delete(Integer aid, List<Type> types);



}
