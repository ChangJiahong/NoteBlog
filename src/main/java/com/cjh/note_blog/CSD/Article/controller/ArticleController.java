package com.cjh.note_blog.CSD.Article.controller;

import com.cjh.note_blog.annotations.PassToken;
import com.cjh.note_blog.annotations.UserLoginToken;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.controller.BaseController;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.pojo.DO.Role;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.pojo.VO.RestResponse;
import com.cjh.note_blog.CSD.Article.service.IArticleService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
     * 获取文章列表
     * @param page 页码
     * @param size 大小
     * @return
     */
    @PassToken
    @GetMapping({"","/"})
    public RestResponse getArticles(@RequestParam(required = false, defaultValue = "1")
                                            Integer page,
                                    @RequestParam(required = false, defaultValue = "20")
                                            Integer size){

        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;

        size = size < 0 || size > WebConst.MAX_PAGESIZE ? 20 : size;

        Result<PageInfo<Article>> result = articleService.getArticles(page, size);

        if (!result.isSuccess()){
            return RestResponse.fail(result);
        }

        return RestResponse.ok(result);
    }


    /**
     * 根据文章id|别名 获取文章信息
     * @param artName
     * @param request
     * @return
     */
    @PassToken
    @GetMapping("/{artName}")
    public RestResponse getArticle(@PathVariable String artName, HttpServletRequest request){

        Result<Article> result = articleService.getArticleByArtName(artName);
        if (!result.isSuccess()){
            return RestResponse.fail(result);
        }
        // 请求成功，返回文章信息
        return RestResponse.ok(result);
    }

    /**
     * 发布文章|保存
     * @param article
     * @param request
     * @return
     */
    @PostMapping({"","/"})
    public RestResponse publish(@Valid @RequestBody Article article, Errors errors, HttpServletRequest request){

        User user  = getUser(request);

        if (errors.hasErrors()){
            LOGGER.info("文章参数错误！");
            // error: 文章参数为空
            return RestResponse.fail(StatusCode.ParameterVerificationError, errors.getFieldError().getDefaultMessage());
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
     * 删除文章
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    public RestResponse delArticleById(@PathVariable Integer id, HttpServletRequest request){

        Result result = articleService.delArticleById(id);

        if (result.isSuccess()){
            return RestResponse.ok(result);
        }

        return RestResponse.fail(result);
    }




}
