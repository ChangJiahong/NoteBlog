package com.cjh.note_blog.mapper;

import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends MyMapper<User> {
    /**
     * 查询用户 by Email
     * @param email
     * @return
     */
    public User selectUserByEmial(@Param(value = "email") String email);

    /**
     * 查询用户 by username
     * @param username
     * @return
     */
    public User selectUserByUsername(@Param(value = "username") String username);



}