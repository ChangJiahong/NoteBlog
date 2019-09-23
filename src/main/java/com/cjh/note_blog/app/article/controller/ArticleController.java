package com.cjh.note_blog.app.article.controller;

import com.cjh.note_blog.annotations.AllowHeaders;
import com.cjh.note_blog.annotations.Contains;
import com.cjh.note_blog.annotations.PassToken;
import com.cjh.note_blog.annotations.UserLoginToken;
import com.cjh.note_blog.app.article.model.ArticleModel;
import com.cjh.note_blog.constant.StatusCode;
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
import com.cjh.note_blog.base.controller.BaseController;

import javax.validation.Valid;
import java.util.Objects;

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

    /* ################################# */
    /* 一般接口 */

    /**
     * 获取推荐文章
     *
     * @param page 页码
     * @param size 页面大小
     * @return
     */
    @ApiOperation(value = "获取推荐文章", notes = "获取推荐文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页面大小", dataType = "string", paramType = "query"),
    })
    @PassToken
    @GetMapping({"", "/"})
    public RestResponse recommendArticles(@RequestParam(required = false, defaultValue = "1")
                                                  Integer page,
                                          @RequestParam(required = false, defaultValue = "12")
                                                  Integer size) {
        Result<PageInfo<ArticleModel>> listResult = articleService.recommendArticles(page, size);
        if (!listResult.isSuccess()) {
            return RestResponse.fail(listResult);
        }
        return RestResponse.ok(listResult);
    }

    /**
     * 获取标签归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取标签归档", notes = "获取标签归档")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页面大小", dataType = "string", paramType = "query"),
    })
    @PassToken
    @GetMapping("/tag")
    public RestResponse getAllTagArchives(@RequestParam(required = false, defaultValue = "1")
                                                  Integer page,
                                          @RequestParam(required = false, defaultValue = "12")
                                                  Integer size) {

        Result<PageInfo<ArchiveModel>> listResult = articleService.getAllTagArchives(page, size);
        if (!listResult.isSuccess()) {
            return RestResponse.fail(listResult);
        }
        return RestResponse.ok(listResult);
    }


    /**
     * 根据文章标签获取
     *
     * @param page 页码
     * @param size 页面大小
     * @param tag 标签名
     * @return 统一返回对象
     */
    @ApiOperation(value = "根据标签获取文章", notes = "根据标签获取文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页面大小", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "tag", value = "标签名", dataType = "string", paramType = "path")
    })
    @PassToken
    @GetMapping("/tag/{tag}")
    public RestResponse getAllArticlesByTag(@RequestParam(required = false, defaultValue = "1")
                                                    Integer page,
                                            @RequestParam(required = false, defaultValue = "12")
                                                    Integer size,
                                            @PathVariable(value = "tag") String tag) {

        Result<PageInfo<ArticleModel>> listResult = articleService.getAllArticlesByTag(page, size, tag);
        if (!listResult.isSuccess()) {
            return RestResponse.fail(listResult);
        }
        return RestResponse.ok(listResult);
    }

    /**
     * 获取分类归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取分类归档", notes = "获取分类归档")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页面大小", dataType = "string", paramType = "query")
    })
    @PassToken
    @GetMapping("/category")
    public RestResponse getAllCategoryArchives(@RequestParam(required = false, defaultValue = "1")
                                                       Integer page,
                                               @RequestParam(required = false, defaultValue = "12")
                                                       Integer size) {

        Result<PageInfo<ArchiveModel>> listResult = articleService.getAllCategoryArchives(page, size);
        if (!listResult.isSuccess()) {
            return RestResponse.fail(listResult);
        }
        return RestResponse.ok(listResult);
    }

    /**
     * 根据文章分类获取
     *
     * @param page 页码
     * @param size 页面大小
     * @param category 分类名
     * @return 统一返回对象
     */
    @ApiOperation(value = "根据分类获取文章", notes = "根据分类获取文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页面大小", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "category", value = "分类名", dataType = "string", paramType = "path")
    })
    @PassToken
    @GetMapping("/category/{category}")
    public RestResponse getAllArticlesByCategory(@RequestParam(required = false, defaultValue = "1")
                                                         Integer page,
                                                 @RequestParam(required = false, defaultValue = "12")
                                                         Integer size,
                                                 @PathVariable(value = "category") String category) {

        Result<PageInfo<ArticleModel>> listResult = articleService.getAllArticlesByCategory(page, size, category);
        if (!listResult.isSuccess()) {
            return RestResponse.fail(listResult);
        }
        return RestResponse.ok(listResult);
    }

    /**
     * 【公共方法】【免验证】
     * 根据文章id|别名 获取文章信息
     *
     * @param contentType 文章内容格式
     * @param artName 文章id或者别名
     * @return 统一返回对象
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
     * @param size 页面大小
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取时间归档", notes = "获取时间归档信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页大小", defaultValue = "12", dataType = "int", paramType = "query")
    })
    @PassToken
    @GetMapping("/archives")
    public RestResponse getArchives(@RequestParam(required = false, defaultValue = "1")
                                            Integer page,
                                    @RequestParam(required = false, defaultValue = "12")
                                            Integer size) {
        Result<PageInfo<ArchiveModel>> result = articleService.getAllTimeArchives(page, size);
        if (!result.isSuccess()) {
            return RestResponse.fail(result);
        }
        return RestResponse.ok(result);
    }

    /**
     *
     * @param username
     * @param page 页码
     * @param size 页面大小
     * @param username 用户名
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取该用户文章列表", notes = "获取该用户文章列表,仅已发布")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页大小", defaultValue = "12", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "用户名", defaultValue = "", dataType = "string", paramType = "query")
    })
    @PassToken
    @GetMapping("/list")
    public RestResponse getArticleList(@RequestParam(value = "username") String username,
                                       @RequestParam(required = false, defaultValue = "1")
                                               Integer page,
                                       @RequestParam(required = false, defaultValue = "12")
                                               Integer size) {
        Result<PageInfo<ArticleModel>> result =
                articleService.getArticlesByAuthor(username, page, size, ArticleModel.PUBLISH);
        if (!result.isSuccess()) {
            return RestResponse.fail(result);
        }
        return RestResponse.ok(result);
    }

    /* ###################################################### */
    /* 需要登陆 */

    /**
     * 【私有方法】
     * 【验证】
     * 获取当前用户的所有文章归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取个人时间归档", notes = "获取个人时间归档信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页大小", defaultValue = "12", dataType = "int", paramType = "query")
    })
    @GetMapping("/u/archives")
    public RestResponse getUArchives(@RequestParam(required = false, defaultValue = "1")
                                                  Integer page,
                                          @RequestParam(required = false, defaultValue = "12")
                                                  Integer size) {

        Result<PageInfo<ArchiveModel>> result = articleService.getPersonalTimeArchives(page, size);
        if (!result.isSuccess()) {
            return RestResponse.fail(result);
        }
        return RestResponse.ok(result);
    }

    /**
     * 获取登录用户标签归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取登录用户标签归档", notes = "获取登录用户标签归档")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页面大小", dataType = "string", paramType = "query")
    })
    @PassToken
    @GetMapping("/u/tag")
    public RestResponse getUTagArchives(@RequestParam(required = false, defaultValue = "1")
                                                  Integer page,
                                          @RequestParam(required = false, defaultValue = "12")
                                                  Integer size) {

        Result<PageInfo<ArchiveModel>> listResult = articleService.getPersonalTagArchives(page, size);
        if (!listResult.isSuccess()) {
            return RestResponse.fail(listResult);
        }
        return RestResponse.ok(listResult);
    }

    /**
     * 根据标签获取登录用户文章
     *
     * @param page 页码
     * @param size 页面大小
     * @param tag  标签名
     * @return 统一返回对象
     */
    @ApiOperation(value = "根据标签获取登录用户文章", notes = "根据标签获取登录用户文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页面大小", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "tag", value = "标签名", dataType = "string", paramType = "path")
    })
    @PassToken
    @GetMapping("/u/tag/{tag}")
    public RestResponse getUArticlesByTag(@RequestParam(required = false, defaultValue = "1")
                                                    Integer page,
                                            @RequestParam(required = false, defaultValue = "12")
                                                    Integer size,
                                            @PathVariable(value = "tag") String tag) {

        Result<PageInfo<ArticleModel>> listResult = articleService.getPersonalArticlesByTag(page, size, tag);
        if (!listResult.isSuccess()) {
            return RestResponse.fail(listResult);
        }
        return RestResponse.ok(listResult);
    }

    /**
     * 获取登录用户分类归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取登录用户分类归档", notes = "获取登录用户分类归档")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页面大小", dataType = "string", paramType = "query")
    })
    @PassToken
    @GetMapping("/u/category")
    public RestResponse getUCategoryArchives(@RequestParam(required = false, defaultValue = "1")
                                                       Integer page,
                                               @RequestParam(required = false, defaultValue = "12")
                                                       Integer size) {

        Result<PageInfo<ArchiveModel>> listResult = articleService.getPersonalCategoryArchives(page, size);
        if (!listResult.isSuccess()) {
            return RestResponse.fail(listResult);
        }
        return RestResponse.ok(listResult);
    }

    /**
     * 根据分类获取登录用户文章
     *
     * @param page 页码
     * @param size 页面大小
     * @param category 分类名
     * @return 统一返回对象
     */
    @ApiOperation(value = "根据分类获取登录用户文章", notes = "根据分类获取登录用户文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页面大小", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "category", value = "分类名", dataType = "string", paramType = "path")
    })
    @PassToken
    @GetMapping("/u/category/{category}")
    public RestResponse getUArticlesByCategory(@RequestParam(required = false, defaultValue = "1")
                                                         Integer page,
                                                 @RequestParam(required = false, defaultValue = "12")
                                                         Integer size,
                                                 @PathVariable(value = "category") String category) {

        Result<PageInfo<ArticleModel>> listResult = articleService.getPersonalArticlesByCategory(page, size, category);
        if (!listResult.isSuccess()) {
            return RestResponse.fail(listResult);
        }
        return RestResponse.ok(listResult);
    }

    /**
     * 【私有方法】【验证】
     * 【个人用户权限】
     * 获取该用户所有文章列表
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取登录用户文章列表", notes = "获取该用户文章列表,登录用户返回全部文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页大小", defaultValue = "12", dataType = "int", paramType = "query")
    })
    @GetMapping("/u/list")
    public RestResponse getUArticleList(@RequestParam(required = false, defaultValue = "1")
                                                Integer page,
                                        @RequestParam(required = false, defaultValue = "12")
                                                Integer size) {

        Result<PageInfo<ArticleModel>> result = articleService.getArticlesByAuthor(getUsername(), page, size, null);
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
     * @param contentType 文章内容格式
     * @param artName 文章id|别名
     * @return 统一返回对象
     */
    @ApiOperation(value = "预览个人文章", notes = "获取浏览当前用户的文章详细内容，包括未发布的，需登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "article-type", value = "文章内容格式md html", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "artName", value = "文章id或者别名", dataType = "string", paramType = "path"),
    })
    @GetMapping("/u/pre/{artName}")
    public RestResponse preview(@PathVariable String artName,
                                @RequestHeader(value = "article-type", defaultValue = "md") String contentType) {

        Result<ArticleModel> result = articleService.getPreviewArticle(artName, contentType);
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
     * @return 统一返回对象
     */
    @ApiOperation(value = "发布/保存文章", notes = "发布或保存文章，如果有id则保存，没有则新建")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "articleModel", value = "文章实体", dataType = "articleModel", paramType = "body")
    })
    @PostMapping("/u")
    public RestResponse publish(@Valid @RequestBody ArticleModel articleModel,
                                Errors errors) {

        User user = getUser();


        if (errors.hasErrors()) {
            LOGGER.info("文章参数错误！");
            // error: 文章参数为空
            return RestResponse.fail(StatusCode.ParameterVerificationError, Objects.requireNonNull(errors.getFieldError()).getDefaultMessage());
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
            @ApiImplicitParam(name = "status", value = "状态(publish、draft)", dataType = "string", paramType = "query")
    })
    @PostMapping("/u/status/{id}")
    public RestResponse articleEnable(@PathVariable(value = "id") Integer id,
                                      @Contains(target = {Article.PUBLISH, Article.DRAFT},
                                              message = "请求参数不正确")
                                      @RequestParam(value = "status") String status) {

        Result result = articleService.updateStatus(id, status);

        if (!result.isSuccess()) {
            return RestResponse.fail(result);
        }

        return RestResponse.ok();
    }

    /**
     * 点赞
     *
     * @param articleId 文章id
     * @return 统一返回对象
     */
    @ApiOperation(value = "点赞", notes = "点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章id", dataType = "string", paramType = "path")
    })
    @PostMapping("/u/like/{articleId}")
    public RestResponse like(@PathVariable(value = "articleId") String articleId) {

        Result result = articleService.likes(articleId);

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
     * @return 统一返回对象
     */
    @ApiOperation(value = "删除文章", notes = "删除文章通过文章id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "文章id", dataType = "int", paramType = "path")
    })
    @DeleteMapping("/u/{id}")
    public RestResponse delArticleById(@PathVariable Integer id) {

        Result result = articleService.delArticleById(id);

        if (result.isSuccess()) {
            return RestResponse.ok(result);
        }

        return RestResponse.fail(result);
    }


}
