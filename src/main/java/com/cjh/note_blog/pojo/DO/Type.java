package com.cjh.note_blog.pojo.DO;

import com.cjh.note_blog.annotations.Contains;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 数据对象
 * ：分类标签 表
 */
public class Type implements Serializable {

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
    @NotBlank(message = "分类标签名不能为空")
    @Size(min = 1, max = 10, message = "分类标签名称长度在1~10字符之间")
    private String name;

    /**
     * 类型
     */
    @NotBlank(message = "分类标签type不能为空")
    @Contains(target = {Type.TAG, Type.CATEGORY})
    private String type;

    /**
     * 创建时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date created;

    private static final long serialVersionUID = 1L;

    public Type(){}

    public Type(String name, String type){
        this.name = name;
        this.type = type;
    }

    /**
     * 获取自增主键
     *
     * @return id - 自增主键
     */
    @JsonIgnore
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

    /**
     * 获取创建时间
     * @return 创建时间
     */
    @JsonIgnore
    public Date getCreated() {
        return created;
    }

    /**
     * 设置创建时间
     * @param created 创建时间
     */
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
        Type type = (Type) o;
        return id.equals(type.id) &&
                this.type.equals(type.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return "type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", created=" + created +
                '}';
    }
}