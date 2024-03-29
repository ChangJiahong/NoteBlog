package com.cjh.note_blog.app.account.service.impl;

import com.cjh.note_blog.app.account.model.UserModel;
import com.cjh.note_blog.app.cache.service.ICacheService;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.mapper.UserMapper;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.app.account.service.IAccountService;
import com.cjh.note_blog.utils.MD5;
import com.cjh.note_blog.utils.PatternKit;
import com.cjh.note_blog.utils.PojoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ：
 * 账户服务
 *
 * @author ChangJiahong
 * @date 2019/7/16
 */

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ICacheService webCacheService;


    /**
     * 用户名|邮箱登录
     *
     * @param name
     * @param password
     * @return
     */
    @Override
    public Result<User> loginByEmailOrUsername(String name, String password) {

        if (StringUtils.isBlank(name) || StringUtils.isBlank(password)) {
            // 参数为空
            return Result.fail(StatusCode.ParameterIsNull);
        }

        password = MD5.MD5Encode(password);

        User user = null;

        if (PatternKit.isEmail(name)) {
            // 邮箱登录
            user = userMapper.selectUserByEmial(name);
        } else {
            // 用户名登录
            user = userMapper.selectUserByUsername(name);
        }

        if (user == null) {
            // 账号不存在
            return Result.fail(StatusCode.UsersDonTExist);
        } else {
            assert password != null;
            if (password.equals(user.getPassword())) {
                // 验证成功
                return Result.ok(user);
            } else {
                // 密码错误
                return Result.fail(StatusCode.PasswordMistake);
            }
        }
    }

    /**
     * 获取用户信息
     *
     * @param username
     * @return
     */
    @Override
    public Result<UserModel> getUserByName(String username) {
        if (StringUtils.isBlank(username)) {
            return Result.fail(StatusCode.ParameterIsNull);
        }

        User user = userMapper.selectUserByUsername(username);
        if (user == null) {
            return Result.fail(StatusCode.DataNotFound);
        }
        user.setPassword(null);
        UserModel userModel = PojoUtils.copyToModelExclude(user, UserModel.class
                , new String[]{"created", "roles", "uid"});
        return Result.ok(userModel);
    }
}
