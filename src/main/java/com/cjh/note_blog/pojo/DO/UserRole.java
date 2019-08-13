package com.cjh.note_blog.pojo.DO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * 数据对象
 * ：用户角色表
 */
@Table(name = "user_role")
public class UserRole implements Serializable {
    /**
     * 角色id
     */
    @Id
    @Column(name = "r_id")
    private Integer rId;

    /**
     * 用户id
     */
    @Id
    @Column(name = "u_id")
    private Integer uId;

    /**
     * 创建时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date created;

    private static final long serialVersionUID = 1L;

    /**
     * 获取角色id
     *
     * @return r_id - 角色id
     */
    public Integer getrId() {
        return rId;
    }

    /**
     * 设置角色id
     *
     * @param rId 角色id
     */
    public void setrId(Integer rId) {
        this.rId = rId;
    }

    /**
     * 获取用户id
     *
     * @return u_id - 用户id
     */
    public Integer getuId() {
        return uId;
    }

    /**
     * 设置用户id
     *
     * @param uId 用户id
     */
    public void setuId(Integer uId) {
        this.uId = uId;
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
}