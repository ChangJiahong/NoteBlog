package com.cjh.note_blog.pojo.DO;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

/**
 * 数据对象
 * ：标签 表
 */
public class Tag implements Serializable {

    /**
     * 标签
     */
    public static final String TAG = "tag";

    /**
     * 分类
     */
    public static final String CATEGORY = "category";

    /**
     * 自增主键
     */
    @Id
    private Integer id;

    /**
     * 名字
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 创建时间
     */
    private Date created;

    private static final long serialVersionUID = 1L;

    /**
     * 获取自增主键
     *
     * @return id - 自增主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置自增主键
     *
     * @param id 自增主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取名字
     *
     * @return name - 名字
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名字
     *
     * @param name 名字
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取类型
     *
     * @return type - 类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return id.equals(tag.id) &&
                type.equals(tag.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}