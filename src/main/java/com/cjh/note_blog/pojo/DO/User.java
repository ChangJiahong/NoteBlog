package com.cjh.note_blog.pojo.DO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * 数据对象
 * ：用户表
 */
public class User implements Serializable {
    /**
     * 自增主键
     */
    @Id
    private Integer uid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱账号
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像地址
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 性别（1男/0女）
     */
    private Boolean sex;

    /**
     * 年龄
     */
    private Byte age;

    /**
     * 创建时间
     */
    private Date created;

    private List<String> roles;

    private static final long serialVersionUID = 1L;

    /**
     * 获取自增主键
     *
     * @return uid - 自增主键
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * 设置自增主键
     *
     * @param uid 自增主键
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 获取邮箱账号
     *
     * @return email - 邮箱账号
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱账号
     *
     * @param email 邮箱账号
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取头像地址
     *
     * @return img_url - 头像地址
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置头像地址
     *
     * @param imgUrl 头像地址
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    /**
     * 获取性别（1男/0女）
     *
     * @return sex - 性别（1男/0女）
     */
    public Boolean getSex() {
        return sex;
    }

    /**
     * 设置性别（1男/0女）
     *
     * @param sex 性别（1男/0女）
     */
    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    /**
     * 获取年龄
     *
     * @return age - 年龄
     */
    public Byte getAge() {
        return age;
    }

    /**
     * 设置年龄
     *
     * @param age 年龄
     */
    public void setAge(Byte age) {
        this.age = age;
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
     * 获取角色集合
     *
     * @return
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * 设置角色集合
     * @param roles
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", created=" + created +
                ", roles=" + roles +
                '}';
    }
}