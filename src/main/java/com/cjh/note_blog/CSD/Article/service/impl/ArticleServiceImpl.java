package com.cjh.note_blog.CSD.Article.service.impl;

import com.cjh.note_blog.CSD.Cache.service.ICacheService;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.constant.Table;
import com.cjh.note_blog.constant.WebConst;
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
 * 文章服务 【dao】
 * @author ChangJiahong
 * @date 2019/7/17
 */
@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ICacheService webCacheService;

    /**
     * 获取文章集合
     * 当分类为空时，默认查询全部
     *
     * @param type     分类标签
     * @param typeName 分类标签名
     * @param page     页码
     * @param size     大小
     * @return 统一返回对象
     */
    @Override
    public Result<PageInfo<Article>> getArticles(String type, String typeName,
                                                 int page, int size) {
//        if (StringUtils.isBlank(type) || StringUtils.isBlank(typeName)){
//            return Result.fail(StatusCode.ParameterIsNull, "分类标签不能为空");
//        }
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;

        size = size < 0 || size > WebConst.MAX_PAGESIZE ? WebConst.DEFAULT_PAGESIZE : size;

        PageHelper.startPage(page, size);

        List<Article> articleList = articleMapper.selectArticles(type, typeName);

        return conversionArticles(articleList);
    }

    /**
     * 转换文章集合
     * 将文章集合转换成页面集合
     * 统计总文章访问量 = （数据库+缓存） 文章访问量
     * @param articleList 文章集合
     * @return 页面集合
     */
    private Result<PageInfo<Article>> conversionArticles(List<Article> articleList) {
        if (articleList.isEmpty()){
            return Result.fail(StatusCode.DataNotFound);
        }

        articleList.forEach(article -> {
            // 加上cache里的访问量
            int incrementHits = webCacheService.getHitsFromCache(article.getId());
            article.setHits(article.getHits()+incrementHits);
        });

        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        return Result.ok(pageInfo);
    }

    /**
     * 根据文章id|alias获取已发布文章
     *
     * @param artName
     * @return result data type: Acticle
     */
    @Override
    public Result<Article> getArticleByArtName(String artName) {

        if (StringUtils.isBlank(artName)){
            // error: 参数为空
            return Result.fail(StatusCode.ParameterIsNull);
        }

        Article article = articleMapper.selectByArtName(artName, StringUtils.isNumeric(artName));
        if (article == null){
            // error: 未找到
            return Result.fail(StatusCode.DataNotFound);
        }

        // 获取缓存里的访问量
        int hits = webCacheService.getHitsFromCache(article.getId());

        // 加上cache里的访问量
        article.setHits(article.getHits()+hits);

        return Result.ok(article);
    }

    /**
     * 获得文章预览信息
     *
     * @param artName 文章id
     * @param author  作者
     * @return 统一返回对象
     */
    @Override
    public Result<Article> getPreviewArticleByArtNameAndAuthor(String artName, String author) {
        if (StringUtils.isBlank(artName) || StringUtils.isBlank(author)){
            return Result.fail(StatusCode.ParameterIsNull);
        }

        Article article = articleMapper.selectByArtNameAndAuthor(artName, author,
                StringUtils.isNumeric(artName));

        if (article == null) {
            // 没找到
            return Result.fail(StatusCode.DataNotFound);
        }

        // 获取缓存里的访问量
        int hits = webCacheService.getHitsFromCache(article.getId());

        // 加上cache里的访问量
        article.setHits(article.getHits()+hits);

        return Result.ok(article);
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
            Example example = new Example(Article.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(Table.Article.alias.name(), article.getAlias());
            if (article.getId() != null) {
                criteria.andNotEqualTo(Table.Article.id.name(), article.getId());
            }
            int i = articleMapper.selectCountByExample(example);
            if (i > 0){
                return Result.fail(StatusCode.DataAlreadyExists,"该别名已存在！");
            }
        }

        Date now = DateUtils.getNow();

        // 最近修改时间更新
        article.setModified(now);

        // 不修改访问量
        article.setHits(null);

        // 描述信息
        // REPAIR: 文章摘要
        if (StringUtils.isBlank(article.getInfo())) {
            if (article.getContent().length() > 100) {
                article.setInfo(article.getContent().substring(0, 100) + "...");
            } else {
                article.setInfo(article.getContent());
            }
        }

        int re = 0;

        if (article.getId() == null){
            // 创建时间
            article.setCreated(now);
            // 新建文章
            re = articleMapper.insertUseGeneratedKeys(article);
        } else {
            // 不修改创建时间
            article.setCreated(null);
            re = articleMapper.updateByPrimaryKeySelective(article);
        }

        if (re <= 0){

            // error: 执行数据库错误
            throw new ExecutionDatabaseExcepeion("publish文章失败");
        }

        return Result.ok();
    }

    /**
     * 修改文章状态
     *
     * @param id     文章id
     * @param status 文章状态
     * @return 统一返回对象
     */
    @Override
    public Result updateStatus(Integer id, String status) {
        if (id == null) {
            return Result.fail(StatusCode.ParameterIsNull);
        }
        if (!(Article.PUBLISH.equals(status) || Article.DRAFT.equals(status))) {
            return Result.fail(StatusCode.ParameterIsInvalid);
        }

        Article article = new Article();
        article.setId(id);
        article.setStatus(status);
        int i = articleMapper.updateByPrimaryKeySelective(article);
        if (i <= 0) {
            return Result.fail(StatusCode.ExecutionDatabaseError, "修改文章状态失败");
        }
        return Result.ok();
    }

    /**
     * 更新文章访问量
     *
     * @param id
     * @param increment 访问量增量
     * @return
     */
    @Override
    public Result updateHits(Integer id, int increment) {
        if (id == null){
            return Result.fail(StatusCode.ParameterIsNull);
        }

        increment = increment < 0 ? 0 : increment;

        int i = articleMapper.incrementHits(id, increment);

        if (i <= 0) {
            return Result.fail(StatusCode.ExecutionDatabaseError, "更新文章访问量失败");
        }

        return Result.ok();
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
