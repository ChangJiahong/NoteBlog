package com.cjh.note_blog.app.article.service;

import com.cjh.note_blog.app.article.model.ArticleModel;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.app.article.model.ArchiveModel;
import com.github.pagehelper.PageInfo;

/**
 * ：
 * 文章服务 【dao】 【接口】
 *
 * @author ChangJiahong
 * @date 2019/7/17
 */
public interface IArticleService {
    /**
     * 获取推荐文章
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    Result<PageInfo<ArticleModel>> recommendArticles(int page, int size);

    /**
     * 获取标签归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    Result<PageInfo<ArchiveModel>> getAllTagArchives(int page, int size);

    /**
     * 获取个人标签归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    Result<PageInfo<ArchiveModel>> getPersonalTagArchives(int page, int size);

    /**
     * 获取分类归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    Result<PageInfo<ArchiveModel>> getAllCategoryArchives(int page, int size);

    /**
     * 获取个人分类归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    public Result<PageInfo<ArchiveModel>> getPersonalCategoryArchives(int page, int size);

    /**
     * 根据标签获取
     *
     * @param page 页码
     * @param size 页面大小
     * @param tag 标签名
     * @return 统一返回对象
     */
    Result<PageInfo<ArticleModel>> getAllArticlesByTag(int page, int size, String tag);

    /**
     * 根据标签获取个人
     *
     * @param page 页码
     * @param size 页面大小
     * @param tag 标签名
     * @return 统一返回对象
     */
    public Result<PageInfo<ArticleModel>> getPersonalArticlesByTag(int page, int size, String tag);

    /**
     * 根据分类获取
     *
     * @param page 页码
     * @param size 页面大小
     * @param category 分类名
     * @return 统一返回对象
     */
    Result<PageInfo<ArticleModel>> getAllArticlesByCategory(int page, int size, String category);

    /**
     * 根据分类获取个人
     *
     * @param page 页码
     * @param size 页面大小
     * @param category 分类名
     * @return 统一返回对象
     */
    public Result<PageInfo<ArticleModel>> getPersonalArticlesByCategory(int page, int size, String category);


    /**
     * 根据文章id|alias获取文章
     *
     * @param artName     id|alias
     * @param contentType 文章内容格式
     * @return 统一返回对象 result data type: Acticle
     */
    Result<ArticleModel> getArticleByArtName(String artName, String contentType);

    /**
     * 获取所有文档归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    Result<PageInfo<ArchiveModel>> getAllTimeArchives(int page, int size);

    /**
     * 获取个人归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    Result<PageInfo<ArchiveModel>> getPersonalTimeArchives(int page, int size);

    /**
     * 获取当前用户所有文档列表
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    Result<PageInfo<ArticleModel>> getArticlesByAuthor(String username, int page, int size, String status);

    /**
     * 获得文章预览信息
     *
     * @param artName     文章id
     * @param contentType 文章内容格式
     * @return 统一返回对象
     */
    Result<ArticleModel> getPreviewArticle(String artName, String contentType);

    /**
     * 创建文章或修改文章
     *
     * @param article 文章对象
     * @return Return ok on success, or fail
     */
    Result publish(Article article);


    /**
     * 删除文章 byId
     *
     * @param id 文章id
     * @return 统一返回对象
     */
    Result delArticleById(Integer id);

    /**
     * 更新文章访问量
     *
     * @param id        文章id
     * @param increment 访问量增量
     * @return 统一返回对象
     */
    Result updateHits(Integer id, int increment);


    /**
     * 修改文章状态
     *
     * @param id     文章id
     * @param status 文章状态
     * @return 统一返回对象
     */
    Result updateStatus(Integer id, String status);

    /**
     * 点赞
     *
     * @param articleId 文章id
     * @return 统一返回对象
     */
    Result likes(String articleId);
}
