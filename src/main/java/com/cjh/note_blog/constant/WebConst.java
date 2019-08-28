package com.cjh.note_blog.constant;

/**
 * ：网站常量
 *
 * @author ChangJiahong
 * @date 2019/7/16
 */
public class WebConst {

    /**
     * 用户登录信息域
     * 标识用户信息在缓存中的位置
     */
    public static final String USER_LOGIN = "user_login";

    /**
     * 用户令牌域
     * 标识用户令牌在缓存中的位置
     */
    public static final String USER_TOKEN = "user_token";

    /**
     * 文章域
     */
    public static final String ARTICLE = "article";

    /**
     * 用户信息在缓存中的有效时间 默认7天
     */
    public static final int USER_LIMIT_TIME = 60 * 60 * 24 * 7;

    /**
     * 每页最大数量
     */
    public static final int MAX_PAGESIZE = 40;

    /**
     * 默认每页大小
     */
    public static final int DEFAULT_PAGESIZE = 12;

    /**
     * 最大页数
     */
    public static final int MAX_PAGE = 100;

    /**
     * 点击有效时间间隔
     */
    public static final int HITS_LIMIT_TIME = 60 * 60;

    /**
     * 点击次数超过多少更新到数据库
     */
    public static final int HIT_EXCEED = 5;
    /**
     * 默认文章简介的长度
     */
    public static final int DEFULT_ARTICLE_INFO_LEN = 100;
}
