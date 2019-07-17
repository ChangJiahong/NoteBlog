package com.cjh.note_blog.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author CJH
 * on 2019/3/13
 */
//@ControllerAdvice
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private BaseInterceptor baseInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(baseInterceptor).excludePathPatterns("/admin/images/**","/admin/css/**","/admin/js/**","/admin/plugins/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+ "D://upload/");
    }
}
