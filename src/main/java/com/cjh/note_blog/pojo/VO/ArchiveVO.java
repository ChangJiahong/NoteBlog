package com.cjh.note_blog.pojo.VO;

import com.cjh.note_blog.pojo.DO.Article;

import java.util.List;

/**
 * :
 * 归档类
 * @author ChangJiahong
 * @date 2019/8/14
 */
public class ArchiveVO {

    /**
     * 归档日期
     */
    private String date;

    /**
     * 归档数量
     */
    private String count;

    /**
     * 归档内容
     */
    private List<Article> articles;

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

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
