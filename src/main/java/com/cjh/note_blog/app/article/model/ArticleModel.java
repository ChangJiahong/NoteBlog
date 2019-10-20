package com.cjh.note_blog.app.article.model;

import com.cjh.note_blog.annotations.Contains;
import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.pojo.DO.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文章视图模型
 *
 * @author ChangJiahong
 * @date 2019/8/28
 */
public class ArticleModel implements Serializable {

    public static final String PUBLISH = "publish";

    public static final String DRAFT = "draft";

    public static final String MD = "md";

    public static final String HTML = "html";

    /**
     * 文章id
     */
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
    private String author;

    /**
     * 作者头像
     */
    private String authorImgUrl;

    /**
     * 标签 逗号间隔
     */
    private String tags;

    /**
     * 种类 逗号间隔
     */
    private String categorys;

    /**
     * 描述信息
     */
    private String info;

    /**
     * 点击数
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer hits = 0;

    /**
     * 最近修改时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modified;

    /**
     * 创建时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date created;

    /**
     * 文章状态
     */
    private String status;

    /**
     * 文章内容
     */
    private String content;

    private String frontCoverImgUrl;

    private Integer likes;

    private Boolean isLiked;

    public ArticleModel() {
    }

    public ArticleModel(Article article) {
        this.alias = article.getAlias();
        this.author = article.getAuthor();
        this.created = article.getCreated();
        this.modified = article.getModified();
        this.hits = article.getHits();
        this.id = article.getId();
        this.info = article.getInfo();
        this.status = article.getStatus();
        this.title = article.getTitle();
        List<Type> types = article.getTypes();
        List<String> tags = new ArrayList<>();
        List<String> categorys = new ArrayList<>();
        types.forEach(type -> {
            if (Type.TAG.equals(type.getType())) {
                tags.add(type.getName());
            } else if (Type.CATEGORY.equals(type.getType())) {
                categorys.add(type.getName());
            }
        });

        this.tags = StringUtils.join(tags, ",");
        this.categorys = StringUtils.join(categorys, ",");

        this.content = article.getContent();
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
     *
     * @return alias - 文章别名
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置文章别名
     *
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategorys() {
        return categorys;
    }

    public void setCategorys(String categorys) {
        this.categorys = categorys;
    }

    public String getAuthorImgUrl() {
        return authorImgUrl;
    }

    public void setAuthorImgUrl(String authorImgUrl) {
        this.authorImgUrl = authorImgUrl;
    }

    public String getFrontCoverImgUrl() {
        return frontCoverImgUrl;
    }

    public void setFrontCoverImgUrl(String frontCoverImgUrl) {
        this.frontCoverImgUrl = frontCoverImgUrl;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    //    /**
//     * 获取文章标签、种类
//     * @return type - 文章分类标签
//     */
//    public List<Type> getTypes() {
//        return types;
//    }
//
//    /**
//     * 设置文章标签、种类
//     * @param types - 文章分类标签
//     */
//    public void setTypes(List<Type> types) {
//        this.types = types;
//    }

}
