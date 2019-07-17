package com.cjh.note_blog.service;

import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.User;

/**
 * ：
 *
 * @author ChangJiahong
 * @date 2019/7/16
 */
public interface IAccountService {

    /**
     * 邮箱登录
     * @param emial
     * @param password
     * @return
     */
    public Result<User> loginByEmail(String email, String password);

    /**
     * 用户名登录
     * @param username
     * @param password
     * @return
     */
    public Result<User> loginByUsername(String username, String password);
}
