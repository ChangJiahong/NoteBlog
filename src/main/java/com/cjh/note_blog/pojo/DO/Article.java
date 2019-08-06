package com.cjh.note_blog.pojo.DO;

import com.cjh.note_blog.annotations.Contains;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 数据对象
 * ：文章表
 */
public class Article implements Serializable {

    public static final String PUBLISH = "publish";

    public static final String DRAFT = "draft";


    /**
     * 文章id
     */
    @Id
    private Integer id;

    /**
     * 文章标题
     */
    @NotBlank(message = "文章标题不为空")
    @Size(min = 3, max = 50, message = "文章标题长度在3~50字符之间")
    private String title;

    /**
     * 文章别名
     */
    @Size(min = 1, max = 25, message = "文章别名在1~25字符之间")
    @Pattern(regexp = "^[A-Za-z]+$", message = "文章别名只能包含字母")
    private String alias;

    /**
     * 作者id
     */
    @Column(name = "author_id")
    private Integer authorId;

    /**
     * 文章标签、种类
     */
    @Valid
    private List<Type> types;

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
    @NotBlank(message = "文章状态不为空")
    @Contains(target = {Article.PUBLISH, Article.DRAFT}, message = "文章status只包含publish, draft")
    private String status;

    /**
     * 文章内容
     */
    @NotBlank(message = "文章内容不能为空")
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
     * 获取文章别名
     * @return
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置文章别名
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
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

    /**
     * 获取文章标签、种类
     * @return
     */
    public List<Type> getTypes() {
        return types;
    }

    /**
     * 设置文章标签、种类
     * @param types
     */
    public void setTypes(List<Type> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authorId=" + authorId +
                ", types=" + types +
                ", info='" + info + '\'' +
                ", hits=" + hits +
                ", modified=" + modified +
                ", created=" + created +
                ", status='" + status + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}