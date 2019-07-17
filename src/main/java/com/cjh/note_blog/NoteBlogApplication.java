package com.cjh.note_blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication

//扫描 mybatis mapper 包路径
@MapperScan(basePackages = "com.cjh.note_blog.mapper")
public class NoteBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteBlogApplication.class, args);
    }

}
