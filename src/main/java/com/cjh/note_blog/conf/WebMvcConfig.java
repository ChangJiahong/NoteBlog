package com.cjh.note_blog.conf;

import com.cjh.note_blog.handler.BaseInterceptor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author CJH
 * on 2019/3/13
 */
@ControllerAdvice
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private BaseInterceptor baseInterceptor;

    @Resource
    private WebConfig webConfig;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(baseInterceptor)
                // 排除路径
                .excludePathPatterns("/error/**", "/swagger-resources/**", "/img/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/file/**").addResourceLocations("file:"+ "D://upload/");

        registry.addResourceHandler(webConfig.userImgUrlPrefix + "/**").addResourceLocations("file:" + webConfig.fileStorageRootPath + webConfig.userImgStoragePrefix +"/");
    }
}
