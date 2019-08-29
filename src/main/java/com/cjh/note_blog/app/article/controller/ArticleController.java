package com.cjh.note_blog.app.article.controller;

import com.cjh.note_blog.annotations.AllowHeaders;
import com.cjh.note_blog.annotations.Contains;
import com.cjh.note_blog.annotations.PassToken;
import com.cjh.note_blog.annotations.UserLoginToken;
import com.cjh.note_blog.app.article.model.ArticleModel;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.controller.BaseController;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.app.article.model.ArchiveModel;
import com.cjh.note_blog.pojo.VO.RestResponse;
import com.cjh.note_blog.app.article.service.IArticleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * ：
 * 文章管理
 *
 * @author ChangJiahong
 * @date 2019/7/17
 */
@Api(tags = "文章管理接口")
@RestController
@RequestMapping("/api/article")
@Validated
@UserLoginToken
public class ArticleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private IArticleService articleService;

    /**
     * 【公共方法】【免验证】
     * 获取文章集合
     * 当分类为空时，默认查询全部
     *
     * @param typeName - 分类标签
     * @param type     - tag or category ; 是分类还是标签
     * @param page     页码
     * @param size     单页大小
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取文章集合", notes = "获取文章集合, 可根据分类标签获取(tag or category.)。\n当分类为空时，默认查询全部")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "分类, 只接受'tag' or 'category'", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "typeName", value = "分类标签名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页大小", defaultValue = "12", dataType = "int", paramType = "query")
    })
    @PassToken
    @GetMapping({"", "/"})
    public RestResponse getArticles(@RequestParam(required = false)
                                            String type,
                                    @RequestParam(required = false)
                                            String typeName,
                                    @RequestParam(required = false, defaultValue = "1")
                                            Integer page,
                                    @RequestParam(required = false, defaultValue = "12")
                                            Integer size) {


        Result<PageInfo<ArticleModel>> listResult = articleService.getArticles(type, typeName, page, size);

        if (!listResult.isSuccess()) {
            return RestResponse.fail(listResult);
        }

        return RestResponse.ok(listResult);
    }

    /**
     * 【公共方法】【免验证】
     * 根据文章id|别名 获取文章信息
     *
     * @param artName 文章id或者别名
     */
    @ApiOperation(value = "获取单个已发布文章", notes = "获取单个已发布文章详细内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "article-type", value = "文章内容格式md html", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "artName", value = "文章id或者别名", dataType = "string", paramType = "path"),
    })
    @AllowHeaders({"article-type"})
    @PassToken
    @GetMapping("/{artName}")
    public RestResponse getArticle(@PathVariable String artName,
                                   // 点击率
                                   HttpServletRequest request,
                                   @RequestHeader(value = "article-type", defaultValue = "md") String contentType) {


        Result<ArticleModel> result = articleService.getArticleByArtName(artName, contentType);
        if (!result.isSuccess()) {
            return RestResponse.fail(result);
        }
        // 请求成功，返回文章信息
        return RestResponse.ok(result);
    }

    /**
     * 【公共方法】【免验证】
     * 获取文章归档
     *
     * @param page 页码
     * @param size 大小
     * @return
     */
    @ApiOperation(value = "获取归档", notes = "获取归档信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页大小", defaultValue = "12", dataType = "int", paramType = "query")
    })
    @PassToken
    @GetMapping("/archives")
    public RestResponse getArchive(@RequestParam(required = false, defaultValue = "1")
                                           Integer page,
                                   @RequestParam(required = false, defaultValue = "12")
                                           Integer size) {
        Result<PageInfo<ArchiveModel>> result = articleService.getArchives(page, size);
        if (!result.isSuccess()) {
            return RestResponse.fail(result);
        }
        return RestResponse.ok(result);
    }

    /**
     * 【私有方法】【验证】
     * 【个人用户权限】
     * 获取该用户所有文章列表
     *
     * @param page    页码
     * @param size    大小
     * @param request 请求对象
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取该用户所有文章列表", notes = "获取该用户所有文章列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页大小", defaultValue = "12", dataType = "int", paramType = "query")
    })
    @GetMapping("/list")
    public RestResponse getArticleList(@RequestParam(required = false, defaultValue = "1")
                                               Integer page,
                                       @RequestParam(required = false, defaultValue = "12")
                                               Integer size,
                                       HttpServletRequest request) {
        User user = getUser(request);
        Result<PageInfo<ArticleModel>> result = articleService.getArticleList(user.getUsername(), page, size);
        if (!result.isSuccess()) {
            return RestResponse.fail(result);
        }
        return RestResponse.ok(result);
    }

    /**
     * 【私有方法】【验证】
     * 【个人用户权限】
     * 查询个人文章 只能浏览当前用户的文章，包括未发布的
     *
     * @param artName 文章id|别名
     * @param request 请求对象
     * @return 统一返回对象
     */
    @ApiOperation(value = "查询个人文章", notes = "获取浏览当前用户的文章详细内容，包括未发布的，需登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "article-type", value = "文章内容格式md html", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "artName", value = "文章id或者别名", dataType = "string", paramType = "path"),
    })
    @GetMapping("/pre/{artName}")
    public RestResponse preview(@PathVariable String artName, HttpServletRequest request,
                                @RequestHeader(value = "article-type", defaultValue = "md") String contentType) {

        User user = getUser(request);
        String author = user.getUsername();
        Result<ArticleModel> result = articleService.getPreviewArticleByArtNameAndAuthor(artName, author, contentType);
        if (!result.isSuccess()) {
            return RestResponse.fail(result);
        }
        return RestResponse.ok(result);
    }

    /**
     * 【私有方法】【验证】
     * 【个人用户权限】
     * 发布文章|保存
     *
     * @param articleModel 文章对象
     * @param request 请求对象
     * @return 统一返回对象
     */
    @ApiOperation(value = "发布/保存文章", notes = "发布或保存文章，如果有id则保存，没有则新建")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "articleModel", value = "文章实体", dataType = "articleModel", paramType = "body")
    })
    @PostMapping({"", "/"})
    public RestResponse publish(@Valid @RequestBody ArticleModel articleModel,
                                Errors errors,
                                HttpServletRequest request) {

        User user = getUser(request);


        if (errors.hasErrors()) {
            LOGGER.info("文章参数错误！");
            // error: 文章参数为空
            return RestResponse.fail(StatusCode.ParameterVerificationError, errors.getFieldError().getDefaultMessage());
        }

        // 设置作者id 作者username
        articleModel.setAuthor(user.getUsername());

        Article article = new Article(articleModel);

        Result result = articleService.publish(article);

        if (result.isSuccess()) {
            LOGGER.info("[" + user.getEmail() + "]：发布文章成功！");
            return RestResponse.ok();
        }
        LOGGER.info("[" + user.getEmail() + "]：发布文章错误！错误信息：" + result.getMsg());
        return RestResponse.fail(result);

    }


    /**
     * 【私有方法】【验证】
     * 【个人用户权限】
     * 文章状态管理
     *
     * @param id     文章id
     * @param status 文章状态（publis|draft）
     * @return 统一返回对象
     */
    @ApiOperation(value = "文章状态管理", notes = "更改文章状态（发布or草稿）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章id", dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "status", value = "状态(publish、draft)", dataType = "string", paramType = "path")
    })
    @PostMapping("/status/{id}/{status}")
    public RestResponse articleEnable(@PathVariable(value = "id") Integer id,
                                      @Contains(target = {Article.PUBLISH, Article.DRAFT},
                                              message = "请求参数不正确")
                                      @PathVariable(value = "status") String status,
                                      HttpServletRequest request) {

        User user = getUser(request);
        String author = user.getUsername();
        Result result = articleService.updateStatus(id, status, author);

        if (!result.isSuccess()) {
            return RestResponse.fail(result);
        }

        return RestResponse.ok();
    }

    /**
     * 【私有方法】【验证】
     * 【个人用户权限】
     * 删除文章
     *
     * @param id 文章id
     * @return
     */
    @ApiOperation(value = "删除文章", notes = "删除文章通过文章id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "文章id", dataType = "int", paramType = "path")
    })
    @DeleteMapping("/{id}")
    public RestResponse delArticleById(@PathVariable Integer id, HttpServletRequest request) {

        User user = getUser(request);
        String author = user.getUsername();

        Result result = articleService.delArticleById(id, author);

        if (result.isSuccess()) {
            return RestResponse.ok(result);
        }

        return RestResponse.fail(result);
    }


}