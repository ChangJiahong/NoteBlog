package com.cjh.note_blog.constant;

/**
 * ：网站常量
 *
 * @author ChangJiahong
 * @date 2019/7/16
 */
public class WebConst {

    /**
     * 登录用户
     */
    public static final String LOGIN_USER_KEY = "login_user" ;

    /**
     * 每页最大数量
     */
    public static final Integer MAX_PAGESIZE = 40;

    /**
     * 默认每页大小
     */
    public static final Integer DEFAULT_PAGESIZE = 12;

    /**
     * 最大页数
     */
    public static final Integer MAX_PAGE = 100;

    /**
     * 点击有效时间间隔
     */
    public static final Integer HITS_LIMIT_TIME = 60*60;

    /**
     * 点击次数超过多少更新到数据库
     */
    public static final int HIT_EXCEED = 5;
}
