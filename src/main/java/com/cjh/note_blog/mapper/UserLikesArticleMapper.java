package com.cjh.note_blog.mapper;

import com.cjh.note_blog.pojo.DO.UserLikesArticle;
import com.cjh.note_blog.utils.MyMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/9/3
 */
public interface UserLikesArticleMapper extends MyMapper<UserLikesArticle> {
    /**
     *
     * @param articleId
     * @param username
     * @return
     */
    UserLikesArticle selectLikedAboutArticle(@Param(value = "articleId") Integer articleId,
                                             @Param(value = "username") String username);
}
