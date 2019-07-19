package com.cjh.note_blog.Article.controller;

import com.cjh.note_blog.Article.service.IArticleService;
import com.cjh.note_blog.Article.service.impl.ArticleServiceImpl;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.utils.GsonUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ：
 *
 * @author ChangJiahong
 * @date 2019/7/19
 */

@RunWith(SpringRunner.class)
@SpringBootTest()
public class Test {

    @Autowired
    IArticleService articleService;

    public void test(){
        Result result = articleService.getArticleById(1);
        System.out.println(GsonUtils.toJsonString(result.getData()));
    }
}
