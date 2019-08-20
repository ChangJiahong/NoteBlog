package com.cjh.note_blog.mapper;

import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.pojo.VO.ArchiveVO;
import com.cjh.note_blog.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper extends MyMapper<Article> {

    /**
     * 查询文章
     * @param artName id 或 别名
     * @param byId 如果是 true 则按照id查询
     *             否则 按照别名查询
     * @return 文章对象
     */
    Article selectByArtName(@Param(value = "artName") String artName,
                              @Param(value = "byId") Boolean byId);

    /**
     * 更新文章访问量
     * @param id 文章id
     * @param increment 增量
     * @return 数据库受影响行数
     */
    int incrementHits(@Param(value = "id") Integer id,
                             @Param(value = "increment") Integer increment);

    /**
     * 获取列表
     * 当分类为空时，默认查询全部
     * @param type 分类, 可为空
     * @param typeName 分类名， 可为空
     * @return 返回根据分类查找的文章集合，如何分类为空，则默认返回全部
     */
    List<Article> selectArticles(@Param(value = "type") String type,
                                 @Param(value = "typeName") String typeName);

    /**
     * 查询归档
     * @return 归档集合
     */
    List<ArchiveVO> selectArchives();

    /**
     * 根据日期查询文章
     * @param date 日期 （%Y%m)
     * @return 文章集合
     */
    List<Article> selectArticleByDateYm(@Param(value = "date") String date);

    /**
     * 查询文章
     * @param artName 文章id
     * @param author 作者
     * @param byId 如果是 true 则按照id查询
     *             否则 按照别名查询
     * @return 文章对象
     */
    Article selectByArtNameAndAuthor(@Param(value = "artName")
                                     String artName,
                                     @Param(value = "author")
                                     String author,
                                     @Param(value = "byId")
                                     Boolean byId);

    /**
     * 查询当前email用户的所有文章
     * @param author 作者名
     * @return 文章列表
     */
    List<Article> selectArticleByAuthor(@Param(value = "author") String author);
}