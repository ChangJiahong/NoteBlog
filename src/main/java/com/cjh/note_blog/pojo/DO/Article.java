package com.cjh.note_blog.pojo.DO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * 数据对象
 * ：文章表
 */
public class Article implements Serializable {
    /**
     * 文章id
     */
    @Id
    private Integer id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 作者id
     */
    @Column(name = "author_id")
    private Integer authorId;

    /**
     * 文章种类
     */
    private List<Tag> categorys;

    /**
     * 文章标签
     */
    private List<Tag> tags;

    /**
     * 描述信息
     */
    private String info;

    /**
     * 点击数
     */
    private Integer hits;

    /**
     * 最近修改时间
     */
    private Date modified;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 文章状态
     */
    private String status;

    /**
     * 文章内容
     */
    private String content;

    private static final long serialVersionUID = 1L;

    /**
     * 获取文章id
     *
     * @return id - 文章id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置文章id
     *
     * @param id 文章id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取文章标题
     *
     * @return title - 文章标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置文章标题
     *
     * @param title 文章标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取作者id
     *
     * @return author_id - 作者id
     */
    public Integer getAuthorId() {
        return authorId;
    }

    /**
     * 设置作者id
     *
     * @param authorId 作者id
     */
    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    /**
     * 获取文章种类
     *
     * @return category - 文章种类
     */
    public List<Tag> getCategorys() {
        return categorys;
    }

    /**
     * 设置文章种类
     *
     * @param categorys 文章种类
     */
    public void setCategorys(List<Tag> categorys) {
        this.categorys = categorys ;
    }

    /**
     * 获取文章标签
     *
     * @return tags - 文章标签
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * 设置文章标签
     *
     * @param tags 文章标签
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * 获取描述信息
     *
     * @return info - 描述信息
     */
    public String getInfo() {
        return info;
    }

    /**
     * 设置描述信息
     *
     * @param info 描述信息
     */
    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    /**
     * 获取点击次数
     *
     * @return hits
     */
    public Integer getHits() {
        return hits;
    }

    /**
     * 设置点击次数
     *
     * @param hits
     */
    public void setHits(Integer hits) {
        this.hits = hits;
    }

    /**
     * 获取最近修改时间
     *
     * @return modified - 最近修改时间
     */
    public Date getModified() {
        return modified;
    }

    /**
     * 设置最近修改时间
     *
     * @param modified 最近修改时间
     */
    public void setModified(Date modified) {
        this.modified = modified;
    }

    /**
     * 获取创建时间
     *
     * @return created - 创建时间
     */
    public Date getCreated() {
        return created;
    }

    /**
     * 设置创建时间
     *
     * @param created 创建时间
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * 获取文章状态
     *
     * @return status - 文章状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置文章状态
     *
     * @param status 文章状态
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 获取文章内容
     *
     * @return content - 文章内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置文章内容
     *
     * @param content 文章内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}