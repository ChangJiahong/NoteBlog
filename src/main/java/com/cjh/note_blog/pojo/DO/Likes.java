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
@Table(name = "likes")
public class Likes {

    @Id
    @Column(name = "article_id")
    private Integer articleId;

    private Integer likes;

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}
