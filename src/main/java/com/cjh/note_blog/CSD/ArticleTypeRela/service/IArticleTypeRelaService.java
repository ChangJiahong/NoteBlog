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
     * @param articleType 关系对象
     * @return 统一返回对象
     */
    public Result<ArticleType> create(ArticleType articleType);

    /**
     * 建立文章 —— 标签关系
     * @param aid 文章id
     * @param tid 标签id
     * @return 统一返回对象
     */
    public Result<ArticleType> create(Integer aid, Integer tid);

    /**
     * 建立文章 —— 标签关系
     * @param aid 文章id
     * @param types 标签
     * @return 统一返回对象
     */
    public Result<List<ArticleType>> create(Integer aid, List<Type> types);


    /**
     * 查询 关系
     * @param aid 文章id
     * @param tid 标签id
     * @return 统一返回对象
     */
    public Result<ArticleType> selectOne(Integer aid, Integer tid);

    /**
     * 查询所有的 by articleId
     * @param aid 文章id
     * @return 统一返回对象
     */
    public Result<List<Type>> selectByArticleId(Integer aid);

    /**
     * 删除 关系
     * @param aid  文章id
     * @param tid  标签id
     * @return 统一返回对象
     */
    public Result<ArticleType> delete(Integer aid, Integer tid);

    /**
     * 删除 关系
     *
     * @param aid
     * @param types
     * @return 统一返回对象
     */
    public Result<List<ArticleType>> delete(Integer aid, List<Type> types);


    /**
     * 获取关系，通过type和email
     * @param tId type Id
     * @param username 用户名
     * @return 统一返回对象
     */
    Result<List<ArticleType>> selectByAuthor(Integer tId, String username);

    /**
     * 删除关系
     * @param id 文章id
     * @return 统一返回对象
     */
    Result<ArticleType> deleteByArticleId(Integer id);
}
