package com.cjh.note_blog.app.article.service.impl;

import com.cjh.note_blog.app.article.model.ArticleModel;
import com.cjh.note_blog.app.article_type_rela.service.IArticleTypeRelaService;
import com.cjh.note_blog.base.service.BaseService;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.constant.Table;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.exc.ExecutionDatabaseExcepeion;
import com.cjh.note_blog.exc.StatusCodeException;
import com.cjh.note_blog.mapper.ArticleMapper;
import com.cjh.note_blog.mapper.LikesMapper;
import com.cjh.note_blog.mapper.UserLikesArticleMapper;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.app.article.service.IArticleService;
import com.cjh.note_blog.app.article.model.ArchiveModel;
import com.cjh.note_blog.pojo.DO.Likes;
import com.cjh.note_blog.pojo.DO.Type;
import com.cjh.note_blog.pojo.DO.UserLikesArticle;
import com.cjh.note_blog.utils.DateUtils;
import com.cjh.note_blog.utils.MD5;
import com.cjh.note_blog.utils.MdParser;
import com.cjh.note_blog.utils.PojoUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * ：
 * 文章服务 【dao】
 *
 * @author ChangJiahong
 * @date 2019/7/17
 */
@Service
public class ArticleServiceImpl extends BaseService implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private IArticleTypeRelaService relaService;

    @Autowired
    private MdParser mdParser;

    @Autowired
    private LikesMapper likesMapper;

    @Autowired
    private UserLikesArticleMapper userLikesArticleMapper;

    /**
     * 获取推荐文章
     *
     * @param page 页码
     * @param size 大小
     * @return 统一返回对象
     */
    @Override
    public Result<PageInfo<ArticleModel>> recommendArticles(int page, int size) {
        // REPAIR: 推荐方案, 最新

        return getAllArticlesAndAuthorIfNotNull(null, Article.PUBLISH, page, size);
    }

    /**
     * 获取标签归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    @Override
    public Result<PageInfo<ArchiveModel>> getAllTagArchives(int page, int size) {
        return getTypeArchivesByAuthorIfNotNull(Type.TAG, null, page, size);
    }

    /**
     * 获取个人标签归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    @Override
    public Result<PageInfo<ArchiveModel>> getPersonalTagArchives(int page, int size) {
        String author = getUsername();
        return getTypeArchivesByAuthorIfNotNull(Type.TAG, author, page, size);
    }

    /**
     * 获取分类归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    @Override
    public Result<PageInfo<ArchiveModel>> getAllCategoryArchives(int page, int size) {
        return getTypeArchivesByAuthorIfNotNull(Type.CATEGORY, null, page, size);
    }

    /**
     * 获取个人分类归档
     *
     * @param page 页码
     * @param size 页面大小
     * @return 统一返回对象
     */
    @Override
    public Result<PageInfo<ArchiveModel>> getPersonalCategoryArchives(int page, int size) {
        String author = getUsername();
        return getTypeArchivesByAuthorIfNotNull(Type.CATEGORY, author, page, size);
    }

    /**
     * 根据标签获取
     *
     * @param page 页码
     * @param size 页面大小
     * @param tag
     * @return 统一返回对象
     */
    @Override
    public Result<PageInfo<ArticleModel>> getAllArticlesByTag(int page, int size, String tag) {
        return getArticlesByTypeAndAuthorIfNotNull(null, tag, Type.TAG, page, size);
    }

    /**
     * 根据标签获取个人
     *
     * @param page 页码
     * @param size 页面大小
     * @param tag
     * @return 统一返回对象
     */
    @Override
    public Result<PageInfo<ArticleModel>> getPersonalArticlesByTag(int page, int size, String tag) {
        String author = getUsername();
        return getArticlesByTypeAndAuthorIfNotNull(author, tag, Type.TAG, page, size);
    }

    /**
     * 根据分类获取
     *
     * @param page     页码
     * @param size     页面大小
     * @param category
     * @return 统一返回对象
     */
    @Override
    public Result<PageInfo<ArticleModel>> getAllArticlesByCategory(int page, int size, String category) {
        return getArticlesByTypeAndAuthorIfNotNull(null, category, Type.CATEGORY, page, size);
    }

    /**
     * 根据分类获取个人
     *
     * @param page     页码
     * @param size     页面大小
     * @param category
     * @return 统一返回对象
     */
    @Override
    public Result<PageInfo<ArticleModel>> getPersonalArticlesByCategory(int page, int size, String category) {
        String author = getUsername();
        return getArticlesByTypeAndAuthorIfNotNull(author, category, Type.CATEGORY, page, size);
    }

    /**
     * 获取type 文档
     *
     * @param page 页码
     * @param size 页面大小
     * @param name
     * @param type
     * @return 统一返回对象
     */
    private Result<PageInfo<ArticleModel>> getArticlesByTypeAndAuthorIfNotNull(String author, String name, String type, int page, int size) {
        pagination(page, size);
        List<Article> articles = getArticlesByTypeAndAuthorIfNotNull(author, name, type);
        if (articles == null || articles.isEmpty()) {
            return Result.fail(StatusCode.DataNotFound);
        }
        PageInfo<ArticleModel> articleModels = getArticleModelPageInfo(articles);
        return Result.ok(articleModels);
    }

    /**
     * 获取种类归档
     *
     * @param page 页码
     * @param size 页面大小
     * @param type
     * @return 统一返回对象
     */
    private Result<PageInfo<ArchiveModel>> getTypeArchivesByAuthorIfNotNull(String type, String author, int page, int size) {
        pagination(page, size);
        List<ArchiveModel> typeArchives = articleMapper.selectTypeArchivesByAuthorIfNotNull(type, author, Article.PUBLISH);
        if (typeArchives == null || typeArchives.isEmpty()) {
            return Result.fail(StatusCode.DataNotFound);
        }

        typeArchives.forEach(typeArchive -> {
            List<Article> articles = getArticlesByTypeAndAuthorIfNotNull(author, typeArchive.getName(), type);
            // 转换文章访问量,点赞状态
            List<ArticleModel> archiveModelList = conversionArticles(articles);
            typeArchive.setArticles(archiveModelList);
        });

        PageInfo<ArchiveModel> pageInfo = new PageInfo<>(typeArchives);
        return Result.ok(pageInfo);
    }

    private List<Article> getArticlesByTypeAndAuthorIfNotNull(String author, String name, String type) {
        return articleMapper.selectArticlesByTypeAndAuthorIfNotNull(author, type, name, Article.PUBLISH);
    }

    /**
     * 根据文章id|alias获取已发布文章
     *
     * @param artName
     * @return 统一返回对象 result data type: Acticle
     */
    @Override
    public Result<ArticleModel> getArticleByArtName(String artName, String contentType) {
        if (StringUtils.isBlank(artName)) {
            // error: 参数为空
            return Result.fail(StatusCode.ParameterIsNull);
        }
        Article article = articleMapper.selectByArtName(artName, StringUtils.isNumeric(artName), Article.PUBLISH);
        if (article == null) {
            // error: 未找到
            return Result.fail(StatusCode.DataNotFound);
        }

        ArticleModel articleModel = conversionArticle(article, contentType);

        return Result.ok(articleModel);
    }

    /**
     * 获取所有文档归档
     *
     * @param page 页码
     * @param size 大小
     * @return 统一返回对象 统一返回对象
     */
    @Override
    public Result<PageInfo<ArchiveModel>> getAllTimeArchives(int page, int size) {
        return getTimeArchivesByAuthorIfNotNull(page, size, null);
    }

    /**
     * 获取个人文档归档
     *
     * @param page 页码
     * @param size 大小
     * @return 统一返回对象
     */
    @Override
    public Result<PageInfo<ArchiveModel>> getPersonalTimeArchives(int page, int size) {
        String username = getUsername();
        return getTimeArchivesByAuthorIfNotNull(page, size, username);
    }

    /**
     * 获取时间归档
     *
     * @param page   页码
     * @param size   页面大小
     * @param author
     * @return 统一返回对象
     */
    private Result<PageInfo<ArchiveModel>> getTimeArchivesByAuthorIfNotNull(int page, int size, String author) {
        pagination(page, size);
        List<ArchiveModel> archiveModels = articleMapper.selectTimeArchivesByAuthorIfNotNull(author, Article.PUBLISH);
        if (null == archiveModels || archiveModels.isEmpty()) {
            // error: 没有找到
            return Result.fail(StatusCode.DataNotFound);
        }
        archiveModels.forEach(archiveModel -> {
            List<Article> articles = articleMapper.selectArticleByDateYm(archiveModel.getName(), author);
            // 转换文章访问量,点赞状态
            List<ArticleModel> archiveModelList = conversionArticles(articles);
            archiveModel.setArticles(archiveModelList);
        });
        PageInfo<ArchiveModel> pageInfo = new PageInfo<>(archiveModels);
        return Result.ok(pageInfo);
    }

    /**
     * 获取当前用户所有文档列表
     *
     * @param page 页码
     * @param size 大小
     * @return 统一返回对象 统一返回对象
     */
    @Override
    public Result<PageInfo<ArticleModel>> getArticlesByAuthor(String username, int page, int size, String status) {
        if (StringUtils.isBlank(username)) {
            return Result.fail(StatusCode.ParameterIsNull);
        }
        return getAllArticlesAndAuthorIfNotNull(username, status, page, size);
    }

    private Result<PageInfo<ArticleModel>> getAllArticlesAndAuthorIfNotNull(String author, String status, int page, int size) {
        pagination(page, size);
        List<Article> articleList = articleMapper.selectAllArticlesAndAuthorIfNotNull(author, status);
        if (null == articleList || articleList.isEmpty()) {
            return Result.fail(StatusCode.DataNotFound);
        }
        PageInfo<ArticleModel> pageInfo = getArticleModelPageInfo(articleList);
        return Result.ok(pageInfo);
    }


    /**
     * 获得文章预览信息
     *
     * @param artName     文章id
     * @param contentType 文章内容格式
     * @return 统一返回对象 统一返回对象
     */
    @Override
    public Result<ArticleModel> getPreviewArticle(String artName, String contentType) {
        String author = getUsername();
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
     * @param article 文章对象
     * @return Return ok on success, or fail
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class, StatusCodeException.class})
    @Override
    public Result publish(Article article) {

        if (StringUtils.isBlank(article.getTitle())) {
            return Result.fail(StatusCode.ParameterIsNull, "文章标题不能为空");
        }
        if (StringUtils.isBlank(article.getContent())) {
            return Result.fail(StatusCode.ParameterIsNull, "文章内容不能为空");
        }
        if (StringUtils.isBlank(article.getStatus())) {
            return Result.fail(StatusCode.ParameterIsNull, "文章状态不能为空");
        }

        Result result = checkeArticleParams(article);
        if (!result.isSuccess()) {
            return result;
        }

        fillArticleInfo(article);
        // 添加文章封面
        String frontCoverImgUrl = article.getFrontCoverImgUrl();
        if (StringUtils.isNotBlank(frontCoverImgUrl)) {
            frontCoverImgUrl = frontCoverImgUrl.substring(webConfig.resAddress.length());
        } else {
            // 自动生成默认封面
            int n = new Random().nextInt(19) + 1;
            frontCoverImgUrl = "/img/" + n + ".jpg";
        }
        article.setFrontCoverImgUrl(frontCoverImgUrl);

        Date now = DateUtils.getNow();
        // 最近修改时间更新
        article.setModified(now);
        // 创建时间
        article.setCreated(now);
        // 新建文章
        int re = articleMapper.insertUseGeneratedKeys(article);

        if (re <= 0) {
            // error: 执行数据库错误
            throw new ExecutionDatabaseExcepeion("发布文章失败");
        }

        return Result.ok();
    }

    /**
     * 更新文章
     *
     * @param article 文章
     * @return 统一返回对象
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class, StatusCodeException.class})
    @Override
    public Result update(Article article) {
        if (article.getId() == null) {
            return Result.fail("文章id不能为空");
        }
        Result result = checkeArticleParams(article);
        if (!result.isSuccess()) {
            return result;
        }
        String frontCoverImgUrl = article.getFrontCoverImgUrl();
        if (StringUtils.isNotBlank(frontCoverImgUrl)) {
            frontCoverImgUrl = frontCoverImgUrl.substring(webConfig.resAddress.length());
            article.setFrontCoverImgUrl(frontCoverImgUrl);
        }
        Date now = DateUtils.getNow();
        // 最近修改时间更新
        article.setModified(now);

        // 填充文章简介
        if (StringUtils.isNotBlank(article.getContent())){
            fillArticleInfo(article);
        }

        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(Table.Article.id.name(), article.getId());
        criteria.andEqualTo(Table.Article.author.name(), article.getAuthor());
        // 文章作者和修改作者相同
        int re = articleMapper.updateByExampleSelective(article, example);
        if (re <= 0) {
            // error: 执行数据库错误
            throw new ExecutionDatabaseExcepeion("修改文章失败");
        }
        return Result.ok();
    }

    private Result checkeArticleParams(Article article) {
        if (article == null) {
            // error: 参数为空
            return Result.fail(StatusCode.ParameterIsNull);
        }
        if (StringUtils.isNotBlank(article.getTitle()) && (article.getTitle().length() > 50 || article.getTitle().length() < 3)) {
            return Result.fail(StatusCode.ParameterIsInvalid, "文章标题长度在3~50之间");
        }
        if (StringUtils.isNotBlank(article.getAlias()) && (article.getAlias().length() > 25 || article.getAlias().length() < 1)) {
            return Result.fail(StatusCode.ParameterIsInvalid, "文章别名在1~25字符之间");
        }
        if (StringUtils.isNumeric(article.getAlias())) {
            return Result.fail(StatusCode.ParameterIsInvalid, "文章别名不能为纯数字");
        }
        if (StringUtils.isNotBlank(article.getStatus()) && !Article.DRAFT.equals(article.getStatus()) && !Article.PUBLISH.equals(article.getStatus())) {
            return Result.fail(StatusCode.ParameterIsNull, "文章状态只能为（draft，publish）");
        }
        if (StringUtils.isNotBlank(article.getFrontCoverImgUrl()) && !article.getFrontCoverImgUrl().startsWith(webConfig.resAddress)) {
            return Result.fail(StatusCode.UnsafeLink, "封面图片来自不安全的链接");
        }

        if (StringUtils.isNotBlank(article.getAlias())) {
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
        return Result.ok();
    }

    /**
     * 填充文章简介
     *
     * @param article
     */
    private void fillArticleInfo(Article article) {
        // 描述信息
        // REPAIR: 文章摘要
        if (StringUtils.isBlank(article.getInfo())) {
            if (article.getContent().length() > WebConst.DEFULT_ARTICLE_INFO_LEN) {
                article.setInfo(article.getContent().substring(0, 100) + "...");
            } else {
                article.setInfo(article.getContent());
            }
        }
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

        String author = getUsername();

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
     * @return 统一返回对象
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
     * 点赞
     *
     * @param articleId 文章id
     * @return 统一返回对象
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class})
    @Override
    public Result likes(String articleId) {
        if (!StringUtils.isNumeric(articleId)) {
            return Result.fail(StatusCode.ParameterIsInvalid, "文章id必须为纯数字");
        }
        String username = getUsername();
        Integer aid = Integer.valueOf(articleId);
        boolean liked = userLikesArticleMapper.selectLikedAboutArticle(aid, username) != null;
        int i, j;
        if (liked) {
            // 取消赞
            i = likesMapper.unLike(aid);

            UserLikesArticle userLikesArticle = new UserLikesArticle(username, aid);
            j = userLikesArticleMapper.delete(userLikesArticle);

        } else {
            Likes likes = likesMapper.selectByPrimaryKey(aid);
            if (likes==null){
                likes = new Likes();
                likes.setArticleId(aid);
                likes.setLikes(0);
                likesMapper.insert(likes);
            }
            // 点赞
            i = likesMapper.like(aid);

            UserLikesArticle userLikesArticle = new UserLikesArticle(username, aid);
            j = userLikesArticleMapper.insert(userLikesArticle);

        }
        if (i != 1 || j != 1) {
            throw new ExecutionDatabaseExcepeion();
        }
        return Result.ok();
    }

    /**
     * 删除文章 byId
     *
     * @param id 文章id
     * @return 统一返回对象
     */
    @Transactional(rollbackFor = ExecutionDatabaseExcepeion.class)
    @Override
    public Result delArticleById(Integer id) {
        if (id == null) {
            return Result.fail(StatusCode.ParameterIsNull);
        }
        String author = getUsername();

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

    /**
     * 分页
     *
     * @param articleList 文章集合
     * @return 分页对象
     */
    private PageInfo<ArticleModel> getArticleModelPageInfo(List<Article> articleList) {
        PageInfo<Article> articlePageInfo = new PageInfo<>(articleList);

        // 转换文章访问量
        List<ArticleModel> articleModels = conversionArticles(articleList);

        PageInfo<ArticleModel> pageInfo = copyPage(articlePageInfo);
        pageInfo.setList(articleModels);
        return pageInfo;
    }

    /**
     * 转换文章集合
     * 将文章集合转换成页面集合
     * 统计总文章访问量 = （数据库+缓存） 文章访问量
     * 点赞状态
     *
     * @param articleList 文章集合
     * @return 文章model集合
     */
    private List<ArticleModel> conversionArticles(List<Article> articleList) {
        List<ArticleModel> articleModels = new ArrayList<>();
        articleList.forEach(article -> {
            // 加上cache里的访问量
            loadHits(article);
            ArticleModel articleModel = new ArticleModel(article);
            loadAuthorImgUrl(article, articleModel);
            articleModel.setFrontCoverImgUrl(webConfig.resAddress + article.getFrontCoverImgUrl());
            loadLiked(article, articleModel);
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
     * @param article     文章
     * @param contentType 文章内容格式
     * @return 文章model
     */
    private ArticleModel conversionArticle(Article article, String contentType) {
        if (ArticleModel.HTML.equals(contentType)) {
            loadHtmlContent(article);
        }
        // 获取缓存里的访问量
        loadHits(article);
        ArticleModel articleModel = new ArticleModel(article);
        loadAuthorImgUrl(article, articleModel);
        loadLiked(article, articleModel);
        return articleModel;
    }

    /**
     * 设置作者头像链接
     *
     * @param article      文章
     * @param articleModel 文章model
     */
    private void loadAuthorImgUrl(Article article, ArticleModel articleModel) {
        String imgUrl = MD5.Base64Encode(article.getAuthor());
        articleModel.setAuthorImgUrl(webConfig.resAddress + webConfig.userImgUrlPrefix + "/" + imgUrl);
    }

    /**
     * 设置点击数
     *
     * @param article 文章
     */
    private void loadHits(Article article) {
        int incrementHits = webCacheService.getHitsFromCache(article.getId());
        article.setHits(article.getHits() + incrementHits);
    }


    /**
     * 设置文章内容
     *
     * @param article 文章
     */
    private void loadHtmlContent(Article article) {
        String contentHtml = webCacheService.getArticleContentHtml(article.getId());
        if (StringUtils.isBlank(contentHtml)) {
            contentHtml = mdParser.md2html(article.getContent());
            // 放入缓存
            webCacheService.putArticleContentHtml(article.getId(), contentHtml);
        }
        article.setContent(contentHtml);
    }

    /**
     * 设置点赞状态
     *
     * @param article      文章
     * @param articleModel 文章model
     */
    private void loadLiked(Article article, ArticleModel articleModel) {
        String username = getUsername();
        if (StringUtils.isNotBlank(username)) {
            Integer likes = likesMapper.selectLikesByAid(article.getId());
            articleModel.setLikes(likes);
            Boolean liked = userLikesArticleMapper.selectLikedAboutArticle(article.getId(), username) != null;
            articleModel.setLiked(liked);
        }
    }

    /**
     * 页面copy
     *
     * @param articlePageInfo 文章分页对象
     * @return 文章model分页对象
     */
    private PageInfo<ArticleModel> copyPage(PageInfo<Article> articlePageInfo) {
        PageInfo<ArticleModel> articleModelPageInfo = new PageInfo<>();
        PojoUtils.copyProperties(articlePageInfo, articleModelPageInfo);
        articleModelPageInfo.setHasNextPage(articlePageInfo.isHasNextPage());
        articleModelPageInfo.setHasPreviousPage(articlePageInfo.isHasPreviousPage());
        articleModelPageInfo.setIsFirstPage(articlePageInfo.isIsFirstPage());
        articleModelPageInfo.setIsLastPage(articlePageInfo.isIsLastPage());
//        articleModelPageInfo.setEndRow(articlePageInfo.getEndRow());
//        articleModelPageInfo.setNavigateFirstPage(articlePageInfo.getNavigateFirstPage());
//        articleModelPageInfo.setNavigateLastPage(articlePageInfo.getNavigateLastPage());
//        articleModelPageInfo.setNavigatepageNums(articlePageInfo.getNavigatepageNums());
//        articleModelPageInfo.setNavigatePages(articlePageInfo.getNavigatePages());
//        articleModelPageInfo.setNextPage(articlePageInfo.getNextPage());
//        articleModelPageInfo.setPageNum(articlePageInfo.getPageNum());
//        articleModelPageInfo.setPages(articlePageInfo.getPages());
//        articleModelPageInfo.setPageSize(articlePageInfo.getPageSize());
//        articleModelPageInfo.setPrePage(articlePageInfo.getPrePage());
//        articleModelPageInfo.setSize(articlePageInfo.getSize());
//        articleModelPageInfo.setStartRow(articlePageInfo.getStartRow());
//        articleModelPageInfo.setTotal(articlePageInfo.getTotal());
        return articleModelPageInfo;
    }

}
