package com.cjh.note_blog.app.article.model;

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
     * 归档名
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    /**
     * 归档数量
     */
    private String count;

    /**
     * 归档内容
     */
    private List<ArticleModel> articles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
