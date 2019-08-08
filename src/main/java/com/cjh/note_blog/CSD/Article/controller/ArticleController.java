package com.cjh.note_blog.CSD.Article.controller;

import com.cjh.note_blog.annotations.PassToken;
import com.cjh.note_blog.annotations.UserLoginToken;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.controller.BaseController;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.pojo.VO.RestResponse;
import com.cjh.note_blog.CSD.Article.service.IArticleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * ：
 * 文章管理
 * @author ChangJiahong
 * @date 2019/7/17
 */
@Api(tags = "文章管理接口")
@RestController
@RequestMapping("/api/article")
@UserLoginToken
public class ArticleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private IArticleService articleService ;

    /**
     * 获取文章集合
     * 当分类为空时，默认查询全部
     *
     * @param typeName - 分类标签
     * @param type - tag or category ; 是分类还是标签
     * @param page 页码
     * @param size 单页大小
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取文章集合", notes = "获取文章集合, 可根据分类标签获取(tag or category.)。\n当分类为空时，默认查询全部")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "分类, 只接受'tag' or 'category'", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "typeName", value = "分类标签名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页大小", defaultValue = "20", dataType = "int", paramType = "query")
    })
    @PassToken
    @GetMapping({"", "/"})
    public RestResponse getArticles(@RequestParam(required = false)
                                          String type,
                                          @RequestParam(required = false)
                                          String typeName,
                                          @RequestParam(required = false, defaultValue = "1")
                                          Integer page,
                                          @RequestParam(required = false, defaultValue = "20")
                                          Integer size) {

        Result<PageInfo<Article>> listResult = articleService.getArticles(type, typeName, page, size);

        if (!listResult.isSuccess()){
            return RestResponse.fail(listResult);
        }

        return RestResponse.ok(listResult);
    }

    /**
     * 根据文章id|别名 获取文章信息
     * @param artName
     * @param request
     * @return
     */
    @ApiOperation(value="获取单个文章", notes="获取单个文章详细内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "artName", value = "文章id或者别名", dataType = "string", paramType = "path"),
     })
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
    @ApiOperation(value="发布/保存文章", notes="发布或保存文章，如果有id则保存，没有则新建")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "article", value = "文章实体", dataType = "Article", paramType = "body")
    })
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
    @ApiOperation(value="删除文章", notes="删除文章通过文章id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "文章id", dataType = "int", paramType = "path")
    })
    @DeleteMapping("/{id}")
    public RestResponse delArticleById(@PathVariable Integer id, HttpServletRequest request){

        Result result = articleService.delArticleById(id);

        if (result.isSuccess()){
            return RestResponse.ok(result);
        }

        return RestResponse.fail(result);
    }




}
