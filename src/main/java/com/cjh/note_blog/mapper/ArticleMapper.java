package com.cjh.note_blog.mapper;

import com.cjh.note_blog.pojo.DO.Article;
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
     * 获取列表
     * @return 文章集合
     */
    List<Article> selectArticles();

    /**
     * 更新文章访问量
     * @param id 文章id
     * @param increment 增量
     * @return 数据库受影响行数
     */
    int incrementHits(@Param(value = "id") Integer id,
                             @Param(value = "increment") Integer increment);
}