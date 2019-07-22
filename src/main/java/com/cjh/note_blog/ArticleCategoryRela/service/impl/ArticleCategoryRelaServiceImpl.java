package com.cjh.note_blog.ArticleCategoryRela.service.impl;

import com.cjh.note_blog.ArticleCategoryRela.service.IArticleCategoryRelaService;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.exc.ExecutionDatabaseExcepeion;
import com.cjh.note_blog.mapper.ArticleCategoryMapper;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.ArticleCategory;
import com.cjh.note_blog.pojo.DO.ArticleTag;
import com.cjh.note_blog.pojo.DO.Tag;
import com.cjh.note_blog.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ：
 * 文章——种类 关系管理
 * @author ChangJiahong
 * @date 2019/7/21
 */

@Service
public class ArticleCategoryRelaServiceImpl implements IArticleCategoryRelaService {

    @Autowired
    private ArticleCategoryMapper articleCategoryMapper ;

    /**
     * 建立文章——种类关系
     *
     * @param articleCategory
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class})
    @Override
    public void create(ArticleCategory articleCategory) {
        if (articleCategory == null){
            return;
        }

        if (articleCategory.getAid() == null || articleCategory.getCid() == null){
            return;
        }

        Date now = DateUtils.getNow();
        articleCategory.setCreated(now);
        articleCategoryMapper.insertSelective(articleCategory);

    }

    /**
     * 建立文章 —— 种类关系
     *
     * @param aid
     * @param cid
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class})
    @Override
    public void create(Integer aid, Integer cid) {
        if (aid == null || cid == null){
            return ;
        }

        ArticleCategory articleCategory = new ArticleCategory();

        articleCategory.setAid(aid);
        articleCategory.setCid(cid);
        create(articleCategory);
    }

    /**
     * 建立文章 —— 标签关系
     *
     * @param aid  文章id
     * @param tags 标签
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class})
    @Override
    public void create(Integer aid, List<Tag> tags) {
        if (tags.isEmpty() || aid == null){
            return;
        }

        for (Tag tag : tags){
            create(aid, tag.getId());
        }
    }

    /**
     * 查询 关系
     *
     * @param aid
     * @param cid
     */
    @Override
    public Result selectOne(Integer aid, Integer cid) {
        if (aid == null || cid == null){
            return Result.fail(StatusCode.ParameterIsNull);
        }

        ArticleCategory articleCategory = new ArticleCategory();

        articleCategory.setAid(aid);
        articleCategory.setCid(cid);

        List<ArticleCategory> articleCategoryList = articleCategoryMapper.select(articleCategory);

        if (articleCategoryList.size() != 1){
            // error: 查询出错
            return Result.fail(StatusCode.QueryError);
        }

        return Result.ok(articleCategoryList.get(0));
    }


    /**
     * 查询所有的文章categroy by articleId
     *
     * @param aid
     * @return
     */
    @Override
    public List<Tag> selectByArticleId(Integer aid) {
        ArticleCategory articleTag = new ArticleCategory();

        articleTag.setAid(aid);

        List<ArticleCategory> articleCategories = articleCategoryMapper.select(articleTag);
        List<Tag> tags = new ArrayList<>();
        Tag tag = null;
        for (ArticleCategory ac : articleCategories){
            tag = new Tag();
            tag.setType(Tag.TAG);
            tag.setId(ac.getCid());
            tags.add(tag);
        }

        return tags;
    }



    /**
     * 删除 关系
     *
     * @param aid
     * @param cid
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class})
    @Override
    public void delete(Integer aid, Integer cid) {
        if (aid == null || cid == null){
            return ;
        }

        ArticleCategory articleCategory = new ArticleCategory();

        articleCategory.setAid(aid);
        articleCategory.setCid(cid);


        articleCategoryMapper.delete(articleCategory);
    }

    /**
     * 删除 关系
     *
     * @param aid
     * @param tags
     */
    @Override
    public void delete(Integer aid, List<Tag> tags) {
        if (tags.isEmpty() || aid == null){
            return;
        }
        for (Tag tag : tags) {
            delete(aid, tag.getId());
        }
    }
}
