package com.cjh.note_blog;

import com.cjh.note_blog.conf.SwaggerConfig;
import com.cjh.note_blog.utils.MdParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
//扫描 mybatis mapper 包路径
@MapperScan(basePackages = "com.cjh.note_blog.mapper")
@EnableTransactionManagement
@EnableConfigurationProperties(SwaggerConfig.class)
public class NoteBlogApplication {

    @Bean
    public MdParser mdParser(){
        return MdParser.getInstance();
    }

    public static void main(String[] args) {
        SpringApplication.run(NoteBlogApplication.class, args);
    }

}
