package com.cjh.note_blog.Article.service.impl;

import com.cjh.note_blog.Tag.service.ITagService;
import com.cjh.note_blog.Tag.service.Impl.TagServiceImpl;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.exc.ExecutionDatabaseExcepeion;
import com.cjh.note_blog.mapper.ArticleMapper;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.Article.service.IArticleService;
import com.cjh.note_blog.pojo.DO.Tag;
import com.cjh.note_blog.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * ：
 * 文章服务 【service】
 * @author ChangJiahong
 * @date 2019/7/17
 */
@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ITagService tagService;

    /**
     * 创建文章或修改文章
     *
     * @param article
     * @return Return ok on success, or fail
     */
    @Override
    public Result publish(Article article) {
        if (article == null){
            // error: 参数为空
            return Result.fail(StatusCode.ParameterIsNull);
        }

        Result result = null;

        if (article.getId() == null){
            // 新建文章
            result = createArticle(article);
        } else {
            result = modifyArticle(article);
        }

        if (!result.isSuccess()){
            return result;
        }

        result = Result.ok();

        return result;
    }

    /**
     * 修改文章
     * 满足文章id不为空
     * @param article
     */
    @Transactional(rollbackFor = ExecutionDatabaseExcepeion.class)
    Result modifyArticle(Article article) {
        if (article.getId() == null){
            // error: 参数缺失， 文章id不为空
            return Result.fail(StatusCode.ParametersAreMissing);
        }
        Result result = null;
        result = checkParams(article);
        if (!result.isSuccess()){
            return result;
        }

        // 最近修改时间更新
        Date now = DateUtils.getNow();
        article.setModified(now);

        // 描述信息
        // TODO: 摘要文章前100字
        article.setInfo("");

        // TODO: 分类管理
        List<Tag> categorys = article.getCategorys();
        // 如果不存在则创建失败
        if (!categorys.isEmpty()){

        }

        // TODO: 标签管理
        List<Tag> tags = article.getTags();
        // 如果不存在该标签，则创建新的标签
        if (!tags.isEmpty()){

            for (Tag tag : tags) {
                Result res = tagService.select(tag);
                if (res.isSuccess()) {
                    // 存在标签
                    tag = (Tag) res.getData();
                } else if (res.getStatusCode() == StatusCode.DataNotFound) {
                    // 不存在标签
                    // 创建标签
                    tagService.create(tag);
                }

                // 关联文章


            }


        }


        // 更新文章
        int i = articleMapper.updateByPrimaryKeySelective(article);
        if (i > 0){
            // 修改成功
            return Result.ok();
        }

        // error: 执行数据库错误
        return Result.fail(StatusCode.ExecutionDatabaseError);
    }

    /**
     * 创建文章
     * @param article
     */
    @Transactional(rollbackFor = ExecutionDatabaseExcepeion.class)
    Result createArticle(Article article) {
        Result result = null;
        result = checkParams(article);
        if (!result.isSuccess()){
            return result;
        }

        Date now = DateUtils.getNow();
        // 创建时间
        article.setCreated(now);
        // 最近修改时间更新
        article.setModified(now);

        // 描述信息
        // TODO: 摘要文章前100字
        article.setInfo("");

        // TODO: 分类管理
        List<Tag> category = article.getCategorys();
        // 如果不存在则创建失败

        // TODO: 标签管理
        List<Tag> tag = article.getTags();
        // 如果不存在该标签，则创建新的标签


        int i = articleMapper.insertSelective(article);

        if (i > 0){
            // 创建成功
            return Result.ok();
        }

        // 创建文章失败
        return Result.fail(StatusCode.ExecutionDatabaseError);
    }

    /**
     * 根据文章id获取文章
     *
     * @param id
     * @return result data type: Acticle
     */
    @Override
    public Result getArticleById(Integer id) {

        if (id == null){
            // error: 参数为空
            return Result.fail(StatusCode.ParameterIsNull);
        }

        Article article = articleMapper.selectById(id);

        if (article == null){
            // error: 未找到
            return Result.fail(StatusCode.DataNotFound);
        }

        return Result.ok(article);
    }


    /**
     * 参数检查
     * @param article
     * @return if success return ok
     */
    private Result checkParams(Article article) {
        if (article.getTitle() == null){
            // error: 标题为空
            return Result.fail(StatusCode.TheTitleOfTheArticleIsEmpty);
        }

        if (article.getContent() == null){
            // error: 内容为空
            return Result.fail(StatusCode.TheArticleIsEmpty);
        }

        if (article.getStatus() == null){
            // error: 文章状态为空
            return Result.fail(StatusCode.ArticleStatusIsEmpty);
        }

        return Result.ok();
    }
}
