package com.cjh.note_blog.annotations;

import com.cjh.note_blog.pojo.DO.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ：
 * 需要登录验证 注解
 * 方法级优先级比类级的高，
 * 方法级没有说明按类级定义
 * 类级没有说明，默认为 Not pass
 * 和{@link com.cjh.note_blog.annotations.PassToken}相反
 * @author ChangJiahong
 * @date 2019/7/18
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLoginToken {
    /**
     * 是否需要验证
     * @return
     */
//    boolean required() default true;

    /**
     * 该类或方法至少需要的访问权限
     * 默认为最低权限
     * @return
     */
    String value() default Role.USER;
}
