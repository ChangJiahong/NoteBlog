package com.cjh.note_blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
//扫描 mybatis mapper 包路径
@MapperScan(basePackages = "com.cjh.note_blog.mapper")
@EnableTransactionManagement
public class NoteBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteBlogApplication.class, args);
    }

}
