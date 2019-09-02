package com.cjh.note_blog.app.article.model;

import com.cjh.note_blog.pojo.DO.Article;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * :
 * 归档类
 * @author ChangJiahong
 * @date 2019/8/14
 */
public class ArchiveModel {

    /**
     * 归档日期
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String date;

    /**
     * 归档数量
     */
    private String count;

    /**
     * 归档内容
     */
    private List<ArticleModel> articles;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<ArticleModel> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleModel> articles) {
        this.articles = articles;
    }
}
