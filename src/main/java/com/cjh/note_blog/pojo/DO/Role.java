package com.cjh.note_blog.pojo.DO;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * 数据对象
 * 角色表(超级管理员(superAdmin)、管理员(admin)、用户(user))
 */
public class Role implements Serializable {

    /**
     * 超级管理员角色
     */
    public static final String SUPER_ADMIN = "superAdmin";

    /**
     * 普通管理员角色
     */
    public static final String ADMIN = "admin";

    /**
     * 普通用户角色
     */
    public static final String USER = "user";


    /**
     * 自增主键
     */
    @Id
    private Integer rid;

    /**
     * 角色名
     */
    private String rname;

    /**
     * 创建时间
     */
    private Date created;

    private static final long serialVersionUID = 1L;

    /**
     * 获取自增主键
     *
     * @return rid - 自增主键
     */
    public Integer getRid() {
        return rid;
    }

    /**
     * 设置自增主键
     *
     * @param rid 自增主键
     */
    public void setRid(Integer rid) {
        this.rid = rid;
    }

    /**
     * 获取角色名
     *
     * @return rname - 角色名
     */
    public String getRname() {
        return rname;
    }

    /**
     * 设置角色名
     *
     * @param rname 角色名
     */
    public void setRname(String rname) {
        this.rname = rname == null ? null : rname.trim();
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