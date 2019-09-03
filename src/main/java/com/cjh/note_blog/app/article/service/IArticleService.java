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
     * 获取文章集合
     * 当分类为空时，默认查询全部
     *
     * @param type     分类标签
     * @param typeName 分类标签名
     * @param page     页码
     * @param size     大小
     * @return
     */
    Result<PageInfo<ArticleModel>> getArticles(String type, String typeName,
                                               int page, int size);

    /**
     * 创建文章或修改文章
     *
     * @param article 文章对象
     * @return Return ok on success, or fail
     */
    Result publish(Article article);

    /**
     * 根据文章id|alias获取文章
     *
     * @param artName     id|alias
     * @param contentType 文章内容格式
     * @return result data type: Acticle
     */
    Result<ArticleModel> getArticleByArtName(String artName, String contentType);

    /**
     * 删除文章 byId
     *
     * @param id     文章id
     * @param author 作者
     * @return 统一返回对象
     */
    Result delArticleById(Integer id, String author);

    /**
     * 更新文章访问量
     *
     * @param id        文章id
     * @param increment 访问量增量
     * @return 统一返回对象
     */
    Result updateHits(Integer id, int increment);

    /**
     * 获得文章预览信息
     *
     * @param artName     文章id
     * @param author      作者
     * @param contentType 文章内容格式
     * @return 统一返回对象
     */
    Result<ArticleModel> getPreviewArticleByArtNameAndAuthor(String artName, String author, String contentType);

    /**
     * 修改文章状态
     *
     * @param id     文章id
     * @param status 文章状态
     * @param author 作者
     * @return 统一返回对象
     */
    Result updateStatus(Integer id, String status, String author);

    /**
     * 获取所有文档归档
     *
     * @param page 页码
     * @param size 大小
     * @return 统一返回对象
     */
    Result<PageInfo<ArchiveModel>> getArchives(int page, int size);

    Result<PageInfo<ArchiveModel>> getArchives(int page, int size, String username);

    /**
     * 获取当前用户所有文档列表
     *
     * @param author 作者名
     * @param page   页码
     * @param size   大小
     * @return 统一返回对象
     */
    Result<PageInfo<ArticleModel>> getArticleList(String author, int page, int size);
}
