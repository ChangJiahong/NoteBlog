package com.cjh.note_blog.annotations;

import com.cjh.note_blog.pojo.DO.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 允许请求头
 * @author ChangJiahong
 * @date 2019/8/28
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowHeaders {
    /**
     * 允许的请求头集合
     */
    String[] value();
}
