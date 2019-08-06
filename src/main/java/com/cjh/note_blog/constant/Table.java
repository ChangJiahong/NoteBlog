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

    public enum Article {
        /**
         * 文章id
         */
        id,

        /**
         * 文章标题
         */
        title,

        /**
         * 文章别名
         */
        alias,

        /**
         * 作者id
         */
        author_id,

        /**
         * 文章标签、种类
         */
        types,

        /**
         * 描述信息
         */
        info,

        /**
         * 点击数
         */
        hits,

        /**
         * 最近修改时间
         */
        modified,

        /**
         * 文章状态
         */
        status,

        /**
         * 文章内容
         */
        content,
        /**
         * 创建时间
         */
        created
    }
}
