package com.cjh.note_blog.pojo.DO;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/9/3
 */
@Table(name = "user_likes_article")
public class UserLikesArticle {

    @Id
    private String username;

    @Id
    @Column(name = "article_id")
    private Integer articleId;

    public UserLikesArticle(){}

    public UserLikesArticle(String username, Integer articleId){
        this.username = username;
        this.articleId = articleId;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
}
