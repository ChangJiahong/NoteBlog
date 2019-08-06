package com.cjh.note_blog.CSD.ArticleTypeRela.service.impl;

import com.cjh.note_blog.CSD.ArticleTypeRela.service.IArticleTypeRelaService;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.exc.ExecutionDatabaseExcepeion;
import com.cjh.note_blog.exc.StatusCodeException;
import com.cjh.note_blog.mapper.ArticleTypeMapper;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.ArticleType;
import com.cjh.note_blog.pojo.DO.Type;
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
public class ArticleTypeRelaServiceImpl implements IArticleTypeRelaService {

    @Autowired
    private ArticleTypeMapper articleTypeMapper;


    /**
     * 建立文章 —— 标签关系
     *
     * @param articleType
     */
    @Override
    public Result<ArticleType> create(ArticleType articleType) {

        if (articleType.getAid() == null || articleType.getTid() == null){
            return Result.fail(StatusCode.ParameterIsNull, "文章标签关系主键不能为空");
        }

        Date now = DateUtils.getNow();
        articleType.setCreated(now);
        int i = articleTypeMapper.insertSelective(articleType);
        if (i <= 0){
            return Result.fail(StatusCode.ExecutionDatabaseError,
                    "创建ArticleType失败");
        }
        return Result.ok(articleType);
    }

    /**
     * 建立文章 —— 标签关系
     *
     * @param aid 文章id
     * @param tid 标签id
     */
    @Override
    public Result<ArticleType> create(Integer aid, Integer tid) {

        ArticleType articleType = new ArticleType();

        articleType.setAid(aid);
        articleType.setTid(tid);
        return create(articleType);
    }

    /**
     * 建立文章 —— 标签关系
     *
     * @param aid  文章id
     * @param types 标签
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class, StatusCodeException.class})
    @Override
    public Result<List<ArticleType>> create(Integer aid, List<Type> types) {
        if (types.isEmpty() || aid == null){
            return Result.fail(StatusCode.ParameterIsNull);
        }

        List<ArticleType> articleTypes = new ArrayList<>();
        for (Type type : types){
            Result<ArticleType> result = create(aid, type.getId());
            if (!result.isSuccess()) {
                throw new StatusCodeException(result.getStatusCode());
            }
            articleTypes.add(result.getData());
        }
        return Result.ok(articleTypes);
    }

    /**
     * 查询 关系
     *
     * @param aid 文章id
     * @param tid 标签id
     */
    @Override
    public Result<ArticleType> selectOne(Integer aid, Integer tid) {
        if (aid == null || tid == null){
            return Result.fail(StatusCode.ParameterIsNull, "查询ArticleType参数不能为空");
        }

        ArticleType articleType = new ArticleType();

        articleType.setAid(aid);
        articleType.setTid(tid);

        ArticleType gArticleType = articleTypeMapper.selectOne(articleType);

        if (gArticleType == null){
            // error: 数据没找到
            return Result.fail(StatusCode.DataNotFound);
        }

        return Result.ok(gArticleType);
    }

    /**
     * 查询所有的文章id by articleId
     *
     * @param aid
     * @return
     */
    @Override
    public Result<List<Type>> selectByArticleId(Integer aid) {
        if (aid == null ){
            return Result.fail(StatusCode.ParameterIsNull,
                    "查询ArticleType参数不能为空");
        }

        ArticleType articleType = new ArticleType();

        articleType.setAid(aid);

        List<ArticleType> articleTypes = articleTypeMapper.select(articleType);

        if (articleTypes.isEmpty()){
            // 数据未找到
            return Result.fail(StatusCode.DataNotFound);
        }

        List<Type> types = new ArrayList<>();
        Type type = null;
        for (ArticleType at : articleTypes){
            type = new Type();
            type.setType(Type.TAG);
            type.setId(at.getTid());
            types.add(type);
        }

        return Result.ok(types);
    }


    /**
     * 删除 关系
     *
     * @param aid 文章id
     * @param tid 标签id
     */
    @Override
    public Result<ArticleType> delete(Integer aid, Integer tid) {

        if (aid == null || tid == null){
            return Result.fail(StatusCode.ParameterIsNull,
                    "删除ArticleType参数aid, tid不能为空");
        }

        ArticleType articleType = new ArticleType();

        articleType.setAid(aid);
        articleType.setTid(tid);


        int i = articleTypeMapper.delete(articleType);
        if (i <= 0){
            return Result.fail(StatusCode.ExecutionDatabaseError,
                    "执行删除ArticleType失败，aid="+aid+",tid="+tid);
        }
        return Result.ok(articleType);
    }

    /**
     * 删除 关系
     *
     * @param aid
     * @param types
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class, StatusCodeException.class})
    @Override
    public Result<List<ArticleType>> delete(Integer aid, List<Type> types) {
        if (types.isEmpty() || aid == null){
            return Result.fail(StatusCode.ParameterIsNull,
                    "删除ArticleType参数aid, types不能为空");
        }

        List<ArticleType> articleTypes = new ArrayList<>();

        for (Type type : types) {
            Result<ArticleType> result = delete(aid, type.getId());
            if (!result.isSuccess()){
                throw new StatusCodeException(result.getStatusCode());
            }
            articleTypes.add(result.getData());
        }

        return Result.ok(articleTypes);
    }
}
