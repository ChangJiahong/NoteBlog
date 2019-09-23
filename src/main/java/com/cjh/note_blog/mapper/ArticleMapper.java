package com.cjh.note_blog.mapper;

import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.app.article.model.ArchiveModel;
import com.cjh.note_blog.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper extends MyMapper<Article> {

    /**
     * 获取所有文章，如果作者不为空，仅查找该作者的
     *
     * @param author 作者
     * @param status 文章状态
     * @return 文章集合
     */
    List<Article> selectAllArticlesAndAuthorIfNotNull(@Param(value = "author") String author,
                                                      @Param(value = "status") String status);

    /**
     * 获取列表
     * 当分类为空时，默认查询全部
     *
     * @param author 作者 如果不为空，仅查询该作者
     * @param status 文章状态
     * @param type     分类, 可为空
     * @param typeName 分类名， 可为空
     * @return 返回根据分类查找的文章集合，如何分类为空，则默认返回全部
     */
    List<Article> selectArticlesByTypeAndAuthorIfNotNull(@Param(value = "author") String author,
                                                         @Param(value = "type") String type,
                                                         @Param(value = "typeName") String typeName,
                                                         @Param(value = "status") String status);


    /**
     * 获取type归档，如果作者不为空则获取该作者的type归档
     *
     * @param type 文章分类标签
     * @param author 作者 如果不为空，仅查询该作者
     * @param status 文章状态
     * @return 文章集合
     */
    List<ArchiveModel> selectTypeArchivesByAuthorIfNotNull(@Param(value = "type") String type,
                                                           @Param(value = "author") String author,
                                                           @Param(value = "status") String status);

    /**
     * 查询文章
     *
     * @param artName id 或 别名
     * @param byId    如果是 true 则按照id查询
     *                否则 按照别名查询
     * @param status 文章状态
     * @return 文章对象
     */
    Article selectByArtName(@Param(value = "artName") String artName,
                            @Param(value = "byId") Boolean byId,
                            @Param(value = "status") String status);

    /**
     * 根据日期查询文章
     *
     * @param date 日期 （%Y%m)
     * @param username 作者名
     * @return 文章集合
     */
    List<Article> selectArticleByDateYm(@Param(value = "date") String date,
                                        @Param(value = "username") String username);

    /**
     * 查询归档
     *
     * @param author 作者 如果不为空，仅查询该作者
     * @param status 文章状态
     * @return 归档集合
     */
    List<ArchiveModel> selectTimeArchivesByAuthorIfNotNull(@Param(value = "author") String author,
                                                           @Param(value = "status") String status);

    /**
     * 预览文章
     *
     * @param artName 文章id
     * @param author  作者
     * @param byId    如果是 true 则按照id查询
     *                否则 按照别名查询
     * @return 文章对象
     */
    Article selectByArtNameAndAuthor(@Param(value = "artName")
                                             String artName,
                                     @Param(value = "author")
                                             String author,
                                     @Param(value = "byId")
                                             Boolean byId);

    /**
     * 更新文章访问量
     *
     * @param id        文章id
     * @param increment 增量
     * @return 数据库受影响行数
     */
    int incrementHits(@Param(value = "id") Integer id,
                      @Param(value = "increment") Integer increment);


}