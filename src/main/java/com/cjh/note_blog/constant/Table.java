package com.cjh.note_blog.constant;

/**
 * ：数据库表
 * 字段名
 * @author ChangJiahong
 * @date 2019/7/16
 */
public class Table {
    public enum User{
        /**
         * 主键id
         */
        uid,
        /**
         * 用户名
         */
        username,
        /**
         * 邮箱账号
         */
        email,
        /**
         * 密码
         */
        password,
        /**
         * 头像地址
         */
        img_url,
        /**
         * 性别
         */
        sex,
        /**
         * 年龄
         */
        age,
        /**
         * 创建时间
         */
        created
    }

    public enum Tag {
        /**
         * 主键
         */
        id,
        /**
         * 名字
         */
        name,
        /**
         * 类型[tag, category]
         */
        type
    }
}
