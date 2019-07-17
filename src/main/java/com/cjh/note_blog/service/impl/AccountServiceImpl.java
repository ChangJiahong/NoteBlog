package com.cjh.note_blog.service.impl;

import com.cjh.note_blog.constant.Table;
import com.cjh.note_blog.exc.MoreThanOneException;
import com.cjh.note_blog.mapper.UserMapper;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.service.IAccountService;
import com.cjh.note_blog.utils.MD5;
import com.cjh.note_blog.utils.PatternKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

/**
 * ：
 *
 * @author ChangJiahong
 * @date 2019/7/16
 */

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private UserMapper userMapper ;


    /**
     * 邮箱登录
     *
     * @param emial
     * @param password
     * @return
     */
    @Override
    public Result<User> loginByEmail(String email, String password) {

        if (!PatternKit.isEmail(email)){
            return Result.fail("邮箱格式不正确");
        }


        password = MD5.encode(password);

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(Table.User.email.name(), email);
        List<User> users = userMapper.selectByExample(example);

        if (users.size() == 0){
            return Result.fail("该账户不存在");
        }else if (users.size() > 1){
            throw new MoreThanOneException();
        }else {
            User user = users.get(0);

            if (password.equals(user.getPassword())){
                // 验证成功
                return Result.ok(user);
            }else {
                return Result.fail("密码错误");
            }
        }
    }

    /**
     * 用户名登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public Result<User> loginByUsername(String username, String password) {
        return null;
    }
}
