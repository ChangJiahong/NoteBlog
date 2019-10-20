package com.cjh.note_blog.pojo.DO;

import com.cjh.note_blog.app.article.model.ArticleModel;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * 数据对象
 * ：文章表
 */
public class Article implements Serializable {

    public static final String PUBLISH = "publish";

    public static final String DRAFT = "draft";

    public static final String MD = "md";

    public static final String HTML = "html";

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
     * 文章别名
     */
    private String alias;

    /**
     * 作者 username
     */
    @Column(name = "author")
    private String author;

    /**
     * 文章标签、种类
     */
    private List<Type> types;

    /**
     * 描述信息
     */
    private String info;

    /**
     * 点击数
     */
    private Integer hits = 0;

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

    @Column(name = "front_cover_img_url")
    private String frontCoverImgUrl;

    private static final long serialVersionUID = 1L;

    public Article(){}

    public Article(ArticleModel articleModel){
        this.alias = articleModel.getAlias();
        this.author = articleModel.getAuthor();
        this.id = articleModel.getId();
        this.info = articleModel.getInfo();
        this.status = articleModel.getStatus();
        this.title = articleModel.getTitle();

        List<Type> types = new ArrayList<>();
        if (StringUtils.isNotBlank(articleModel.getTags())) {
            String[] tags = articleModel.getTags().split(",");
            for (String tag : tags) {
                types.add(new Type(tag, Type.TAG));
            }
        }
        if (StringUtils.isNotBlank(articleModel.getCategorys())) {
            String[] categorys = articleModel.getCategorys().split(",");
            for (String category : categorys) {
                types.add(new Type(category, Type.CATEGORY));
            }
        }

        this.types = types;
        this.content = articleModel.getContent();
        this.frontCoverImgUrl = articleModel.getFrontCoverImgUrl();
    }

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
     * @return alias - 文章别名
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置文章别名
     * @param alias 文章别名
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 获取作者
     *
     * @return author - 作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置作者
     *
     * @param author 作者
     */
    public void setAuthor(String author) {
        this.author = author;
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
     * @param hits 访问量
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取文章标签、种类
     * @return type - 文章分类标签
     */
    public List<Type> getTypes() {
        return types;
    }

    /**
     * 设置文章标签、种类
     * @param types - 文章分类标签
     */
    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public String getFrontCoverImgUrl() {
        return frontCoverImgUrl;
    }

    public void setFrontCoverImgUrl(String frontCoverImgUrl) {
        this.frontCoverImgUrl = frontCoverImgUrl;
    }

    @Override
    public String toString() {
        return "article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author=" + author +
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