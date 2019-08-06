package com.cjh.note_blog.CSD.Article.service.impl;

import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.constant.Table;
import com.cjh.note_blog.exc.ExecutionDatabaseExcepeion;
import com.cjh.note_blog.exc.StatusCodeException;
import com.cjh.note_blog.mapper.ArticleMapper;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.CSD.Article.service.IArticleService;
import com.cjh.note_blog.utils.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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


    /**
     * 获取文章列表
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result<PageInfo<Article>> getArticles(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = new Example(Article.class);
        // 根据创建时间降序排列
        example.orderBy(Table.Article.created.name()).desc();
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo(Table.Article.status.name(), Article.PUBLISH);

        List<Article> articles = articleMapper.selectArticles();

        if (articles.isEmpty()){
            // error: 没有找到
            return Result.fail(StatusCode.DataNotFound);
        }

        PageInfo<Article> pageInfo = new PageInfo<>(articles);

        return Result.ok(pageInfo);
    }

    /**
     * 创建文章或修改文章
     *
     * @param article
     * @return Return ok on success, or fail
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class, StatusCodeException.class})
    @Override
    public Result publish(Article article) {
        if (article == null){
            // error: 参数为空
            return Result.fail(StatusCode.ParameterIsNull);
        }

        if (!StringUtils.isBlank(article.getAlias())){
            // 检查别名是否重复
            Result result = getArticleByArtName(article.getAlias());
            if (result.isSuccess()){
                return Result.fail(StatusCode.DataAlreadyExists,"该别名已存在！");
            }
        }

        Date now = DateUtils.getNow();
        // 创建时间
        article.setCreated(now);
        // 最近修改时间更新
        article.setModified(now);

        // 描述信息
        // TODO: 摘要文章前100字
        article.setInfo("");

        int re = 0;

        if (article.getId() == null){
            // 新建文章
            re = articleMapper.insertUseGeneratedKeys(article);
        } else {
            re = articleMapper.updateByPrimaryKeySelective(article);

        }

        if (re <= 0){

            // error: 执行数据库错误
            throw new ExecutionDatabaseExcepeion("publish文章失败");
        }

        return Result.ok();
    }

    /**
     * 根据文章id|alias获取已发布文章
     *
     * @param artName
     * @return result data type: Acticle
     */
    @Override
    public Result<Article> getArticleByArtName(String artName) {

        if (artName == null){
            // error: 参数为空
            return Result.fail(StatusCode.ParameterIsNull);
        }
        Article article = new Article();
        if (StringUtils.isNumeric(artName)) {
            Integer id = Integer.valueOf(artName);
            article.setId(id);
        }else {
            article.setAlias(artName);
        }

        article.setStatus(Article.PUBLISH);

//        article = articleMapper.selectOne(article);

        article = articleMapper.selectByArtName(artName, StringUtils.isNumeric(artName));
        if (article == null){
            // error: 未找到
            return Result.fail(StatusCode.DataNotFound);
        }

        return Result.ok(article);
    }


    /**
     * 删除文章 byId
     *
     * @param id
     * @return
     */
    @Override
    public Result delArticleById(Integer id) {
        if (id == null){
            return Result.fail(StatusCode.ParameterIsNull);
        }

        int i = articleMapper.deleteByPrimaryKey(id);
        if (i <= 0 ){
            return Result.fail(StatusCode.ExecutionDatabaseError, "删除文章失败");
        }

        return Result.ok();
    }
}
