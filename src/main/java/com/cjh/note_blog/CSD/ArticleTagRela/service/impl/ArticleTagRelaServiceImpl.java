package com.cjh.note_blog.CSD.ArticleTagRela.service.impl;

import com.cjh.note_blog.CSD.ArticleTagRela.service.IArticleTagRelaService;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.exc.ExecutionDatabaseExcepeion;
import com.cjh.note_blog.mapper.ArticleTagMapper;
import com.cjh.note_blog.pojo.BO.Result;
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
 * 标签关系管理
 * @author ChangJiahong
 * @date 2019/7/22
 */
@Service
public class ArticleTagRelaServiceImpl implements IArticleTagRelaService {

    @Autowired
    private ArticleTagMapper articleTagMapper ;


    /**
     * 建立文章 —— 标签关系
     *
     * @param articleTag
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class})
    @Override
    public void create(ArticleTag articleTag) {
        if (articleTagMapper == null){
            return;
        }

        if (articleTag.getAid() == null || articleTag.getTid() == null){
            return;
        }

        Date now = DateUtils.getNow();
        articleTag.setCreated(now);
        articleTagMapper.insertSelective(articleTag);
    }

    /**
     * 建立文章 —— 标签关系
     *
     * @param aid 文章id
     * @param tid 标签id
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class})
    @Override
    public void create(Integer aid, Integer tid) {
        if (aid == null || tid == null){
            return ;
        }

        ArticleTag articleTag = new ArticleTag();

        articleTag.setAid(aid);
        articleTag.setTid(tid);
        create(articleTag);
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
     * @param aid 文章id
     * @param tid 标签id
     */
    @Override
    public Result selectOne(Integer aid, Integer tid) {
        if (aid == null || tid == null){
            return Result.fail(StatusCode.ParameterIsNull);
        }

        ArticleTag articleTag = new ArticleTag();

        articleTag.setAid(aid);
        articleTag.setTid(tid);

        List<ArticleTag> articleTags = articleTagMapper.select(articleTag);

        if (articleTags.size() != 1){
            // error: 查询出错
            return Result.fail(StatusCode.QueryError);
        }

        return Result.ok(articleTags.get(0));
    }

    /**
     * 查询所有的文章id by articleId
     *
     * @param aid
     * @return
     */
    @Override
    public List<Tag> selectByArticleId(Integer aid) {

        ArticleTag articleTag = new ArticleTag();

        articleTag.setAid(aid);

        List<ArticleTag> articleTags = articleTagMapper.select(articleTag);
        List<Tag> tags = new ArrayList<>();
        Tag tag = null;
        for (ArticleTag at : articleTags){
            tag = new Tag();
            tag.setType(Tag.TAG);
            tag.setId(at.getTid());
            tags.add(tag);
        }

        return tags;
    }


    /**
     * 删除 关系
     *
     * @param aid 文章id
     * @param tid 标签id
     */
    @Override
    public void delete(Integer aid, Integer tid) {
        if (aid == null || tid == null){
            return ;
        }

        ArticleTag articleTag = new ArticleTag();

        articleTag.setAid(aid);
        articleTag.setTid(tid);


        articleTagMapper.delete(articleTag);
    }

    /**
     * 删除 关系
     *
     * @param aid
     * @param tags
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class})
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
