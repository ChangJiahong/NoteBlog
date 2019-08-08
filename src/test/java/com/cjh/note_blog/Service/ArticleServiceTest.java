package com.cjh.note_blog.Service;

import com.cjh.note_blog.CSD.Article.service.IArticleService;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.utils.GsonUtils;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/8/8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    private IArticleService articleService;

    @Test
    public void getArticles(){
        Result<PageInfo<Article>> articles =
                articleService.getArticles("tag", "切面标签",
                        1, 20);

        System.out.println(GsonUtils.toJsonString(articles));
    }

}
