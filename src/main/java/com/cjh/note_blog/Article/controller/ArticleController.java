package com.cjh.note_blog.Article.controller;

import com.cjh.note_blog.annotations.UserLoginToken;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.controller.BaseController;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.pojo.VO.RestResponse;
import com.cjh.note_blog.Article.service.IArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * ：
 * 文章管理
 * @author ChangJiahong
 * @date 2019/7/17
 */

@RestController
@RequestMapping("/api/article")
@UserLoginToken
public class ArticleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private IArticleService articleService ;

    /**
     * 发布文章
     * @param article
     * @param request
     * @return
     */
    @PostMapping
    public RestResponse publish(@RequestBody Article article, HttpServletRequest request){

        User user  = getUser(request);

        if (article == null){
            LOGGER.info("文章参数错误！");
            // error: 文章参数为空
            return RestResponse.fail(StatusCode.ParameterIsNull);
        }

        // 设置作者id
        article.setAuthorId(user.getUid());

        Result result = articleService.publish(article);

        if (result.isSuccess()){
            LOGGER.info("["+user.getEmail()+"]：发布文章成功！");
            return RestResponse.ok();
        }
        LOGGER.info("["+user.getEmail()+"]：发布文章错误！错误信息："+result.getMsg());
        return RestResponse.fail(result);

    }


    /**
     * 根据文章id获取文章信息
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/{id}")
    public RestResponse getArticleById(@PathVariable Integer id, HttpServletRequest request){

        Result result = articleService.getArticleById(id);
        if (!result.isSuccess()){
            return RestResponse.fail(result);
        }
        // 请求成功，返回文章信息
        return RestResponse.ok(result);
    }


}
