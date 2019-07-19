package com.cjh.note_blog.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ：
 * 跳过验证注解
 * 方法级优先级比类级的高，
 * 方法级没有说明按类级定义
 * 类级没有说明，默认为 Not pass
 * 和{@link com.cjh.note_blog.annotations.UserLoginToken}相反
 * @author ChangJiahong
 * @date 2019/7/18
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassToken {

    boolean required() default true;

}
