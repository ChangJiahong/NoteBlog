package com.cjh.note_blog.app.account.service;

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
     * 用户名|邮箱登录
     * @param email
     * @param password
     * @return
     */
    public Result<User> loginByEmailOrUsername(String email, String password);

}
