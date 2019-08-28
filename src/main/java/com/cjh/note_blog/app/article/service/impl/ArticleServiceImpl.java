package com.cjh.note_blog.app.article.service.impl;

import com.cjh.note_blog.app.article.model.ArticleModel;
import com.cjh.note_blog.app.article_type_rela.service.IArticleTypeRelaService;
import com.cjh.note_blog.app.cache.service.ICacheService;
import com.cjh.note_blog.conf.WebConfig;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.constant.Table;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.exc.ExecutionDatabaseExcepeion;
import com.cjh.note_blog.exc.StatusCodeException;
import com.cjh.note_blog.mapper.ArticleMapper;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.app.article.service.IArticleService;
import com.cjh.note_blog.app.article.model.ArchiveModel;
import com.cjh.note_blog.utils.DateUtils;
import com.cjh.note_blog.utils.MD5;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ：
 * 文章服务 【dao】
 *
 * @author ChangJiahong
 * @date 2019/7/17
 */
@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ICacheService webCacheService;

    @Autowired
    private IArticleTypeRelaService relaService;

    @Autowired
    private WebConfig webConfig;

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
    public Result<PageInfo<ArticleModel>> getArticles(String type, String typeName,
                                                      int page, int size) {

        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;

        size = size < 0 || size > WebConst.MAX_PAGESIZE ? WebConst.DEFAULT_PAGESIZE : size;

        PageHelper.startPage(page, size);

        List<Article> articleList = articleMapper.selectArticles(type, typeName);
        if (null == articleList || articleList.isEmpty()) {
            return Result.fail(StatusCode.DataNotFound);
        }
        // 转换文章访问量
        List<ArticleModel> articleModels = conversionArticles(articleList);


        PageInfo<ArticleModel> pageInfo = new PageInfo<>(articleModels);
        return Result.ok(pageInfo);
    }

    /**
     * 获取所有文档归档
     *
     * @param page 页码
     * @param size 大小
     * @return 统一返回对象
     */
    @Override
    public Result<PageInfo<ArchiveModel>> getArchives(int page, int size) {
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;

        size = size < 0 || size > WebConst.MAX_PAGESIZE ? WebConst.DEFAULT_PAGESIZE : size;

        PageHelper.startPage(page, size);
        List<ArchiveModel> archiveModels = articleMapper.selectArchives();
        if (null == archiveModels || archiveModels.isEmpty()) {
            // error: 没有找到
            return Result.fail(StatusCode.DataNotFound);
        }
        archiveModels.forEach(archiveModel -> {
            List<Article> articles = articleMapper.selectArticleByDateYm(archiveModel.getDate());
            // 转换文章访问量
            conversionArticles(articles);
            archiveModel.setArticles(articles);
        });
        PageInfo<ArchiveModel> pageInfo = new PageInfo<>(archiveModels);
        return Result.ok(pageInfo);
    }


    /**
     * 获取当前用户所有文档列表
     *
     * @param author 作者名
     * @param page   页码
     * @param size   大小
     * @return 统一返回对象
     */
    @Override
    public Result<PageInfo<ArticleModel>> getArticleList(String author, int page, int size) {
        if (StringUtils.isBlank(author)) {
            return Result.fail(StatusCode.ParameterIsNull);
        }
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;

        size = size < 0 || size > WebConst.MAX_PAGESIZE ? WebConst.DEFAULT_PAGESIZE : size;

        PageHelper.startPage(page, size);

        List<Article> articles = articleMapper.selectArticleByAuthor(author);

        // 转换文章访问量
        List<ArticleModel> articleModels = conversionArticles(articles);
        PageInfo<ArticleModel> pageInfo = new PageInfo<>(articleModels);
        return Result.ok(pageInfo);

    }

    /**
     * 转换文章集合
     * 将文章集合转换成页面集合
     * 统计总文章访问量 = （数据库+缓存） 文章访问量
     *
     * @param articleList 文章集合
     */
    private List<ArticleModel> conversionArticles(List<Article> articleList) {
        List<ArticleModel> articleModels = new ArrayList<>();
        articleList.forEach(article -> {
            // 加上cache里的访问量
            int incrementHits = webCacheService.getHitsFromCache(article.getId());
            article.setHits(article.getHits() + incrementHits);
            ArticleModel articleModel = new ArticleModel(article);
            String imgUrl = MD5.Base64Encode(article.getAuthor());
            articleModel.setAuthorImgUrl(webConfig.root+"/u/"+imgUrl);
            articleModels.add(articleModel);
        });
        return articleModels;
    }

    /**
     * 转换文章
     * 将文章集合转换成页面
     * 统计总文章访问量 = （数据库+缓存） 文章访问量
     * 文章内容格式md to html
     *
     * @param article 文章
     */
    private ArticleModel conversionArticle(Article article, String contentType){

        // 获取缓存里的访问量
        int hits = webCacheService.getHitsFromCache(article.getId());

        // 加上cache里的访问量
        article.setHits(article.getHits() + hits);

        if (ArticleModel.HTML.equals(contentType)) {
            String contentHtml = webCacheService.getArticleContentHtml(article.getId());
            if (StringUtils.isBlank(contentHtml)) {
                // TODO: md转换成html格式
                contentHtml = "暂不支持";
                // 放入缓存
                webCacheService.putArticleContentHtml(article.getId(), contentHtml);
            }
            article.setContent(contentHtml);

        }

        String imgUrl = MD5.Base64Encode(article.getAuthor());
        ArticleModel articleModel = new ArticleModel(article);
        articleModel.setAuthorImgUrl(webConfig.root+"/u/"+imgUrl);

        return articleModel;
    }

    /**
     * 根据文章id|alias获取已发布文章
     *
     * @param artName
     * @return result data type: Acticle
     */
    @Override
    public Result<ArticleModel> getArticleByArtName(String artName, String contentType) {

        if (StringUtils.isBlank(artName)) {
            // error: 参数为空
            return Result.fail(StatusCode.ParameterIsNull);
        }

        Article article = articleMapper.selectByArtName(artName, StringUtils.isNumeric(artName));
        if (article == null) {
            // error: 未找到
            return Result.fail(StatusCode.DataNotFound);
        }

        ArticleModel articleModel = conversionArticle(article, contentType);

        return Result.ok(articleModel);
    }

    /**
     * 获得文章预览信息
     *
     * @param artName 文章id
     * @param author  作者
     * @return 统一返回对象
     */
    @Override
    public Result<ArticleModel> getPreviewArticleByArtNameAndAuthor(String artName, String author, String contentType) {
        if (StringUtils.isBlank(artName) || StringUtils.isBlank(author)) {
            return Result.fail(StatusCode.ParameterIsNull);
        }

        Article article = articleMapper.selectByArtNameAndAuthor(artName, author,
                StringUtils.isNumeric(artName));

        if (article == null) {
            // 没找到
            return Result.fail(StatusCode.DataNotFound);
        }


        ArticleModel articleModel = conversionArticle(article, contentType);

        return Result.ok(articleModel);
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

        if (article == null) {
            // error: 参数为空
            return Result.fail(StatusCode.ParameterIsNull);
        }

        if (!StringUtils.isBlank(article.getAlias())) {
            // 检查别名是否重复
            Example example = new Example(Article.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(Table.Article.alias.name(), article.getAlias());
            if (article.getId() != null) {
                criteria.andNotEqualTo(Table.Article.id.name(), article.getId());
            }
            int i = articleMapper.selectCountByExample(example);
            if (i > 0) {
                return Result.fail(StatusCode.DataAlreadyExists, "该别名已存在！");
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
            if (article.getContent().length() > WebConst.DEFULT_ARTICLE_INFO_LEN) {
                article.setInfo(article.getContent().substring(0, 100) + "...");
            } else {
                article.setInfo(article.getContent());
            }
        }

        int re = 0;

        if (article.getId() == null) {
            // 创建时间
            article.setCreated(now);
            // 新建文章
            re = articleMapper.insertUseGeneratedKeys(article);
        } else {
            // 不修改创建时间
            article.setCreated(null);
            Example example = new Example(Article.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(Table.Article.id.name(), article.getId());
            criteria.andEqualTo(Table.Article.author.name(), article.getAuthor());
            // 文章作者和修改作者相同
            re = articleMapper.updateByExampleSelective(article, example);
        }

        if (re <= 0) {
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
     * @param author 作者
     * @return 统一返回对象
     */
    @Override
    public Result updateStatus(Integer id, String status, String author) {
        if (id == null) {
            return Result.fail(StatusCode.ParameterIsNull);
        }
        if (!(Article.PUBLISH.equals(status) || Article.DRAFT.equals(status))) {
            return Result.fail(StatusCode.ParameterIsInvalid);
        }

        Article article = new Article();
        article.setId(id);
        article.setStatus(status);

        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(Table.Article.id.name(), id);
        criteria.andEqualTo(Table.Article.author.name(), author);
        // 文章作者相同
        int i = articleMapper.updateByExampleSelective(article, example);
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
     */
    @Override
    public Result updateHits(Integer id, int increment) {
        if (id == null) {
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
     * @param author 作者
     */
    @Transactional(rollbackFor = ExecutionDatabaseExcepeion.class)
    @Override
    public Result delArticleById(Integer id, String author) {
        if (id == null) {
            return Result.fail(StatusCode.ParameterIsNull);
        }

        Article article = new Article();
        article.setId(id);
        article.setAuthor(author);
        // 删除文章
        int i = articleMapper.delete(article);
        if (i <= 0) {
            throw new ExecutionDatabaseExcepeion("删除文章失败");
        }
        // 删除文章关系
        Result result = relaService.deleteByArticleId(id);
        if (!result.isSuccess()) {
            throw new ExecutionDatabaseExcepeion(result.getMsg());
        }
        return Result.ok();
    }
}
