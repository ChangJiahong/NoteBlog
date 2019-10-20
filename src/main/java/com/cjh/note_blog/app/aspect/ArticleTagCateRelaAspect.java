package com.cjh.note_blog.app.aspect;

import com.cjh.note_blog.app.article_type_rela.service.IArticleTypeRelaService;
import com.cjh.note_blog.app.cache.service.ICacheService;
import com.cjh.note_blog.app.type.service.ITypeService;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.exc.StatusCodeException;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.pojo.DO.Type;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 文章 标签、分类 关系建立 切面
 * @author ChangJiahong
 * @date 2019/8/5
 */
@Component
@Aspect
public class ArticleTagCateRelaAspect {

    @Autowired
    private ITypeService typeService ;

    @Autowired
    private IArticleTypeRelaService typeRelaService;

    @Autowired
    private ICacheService webCacheService;

    @Pointcut("execution(* com.cjh.note_blog.app.article.service.IArticleService" +
            ".publish(com.cjh.note_blog.pojo.DO.Article)) && args(article)")
    public void publish(Article article){}

    @Pointcut("execution(* com.cjh.note_blog.app.article.service.IArticleService" +
            ".update(com.cjh.note_blog.pojo.DO.Article)) && args(article)")
    public void update(Article article){}

    /**
     * 在文章提交完成后，对文章的分类标签进行关联
     * @param article
     */
    @AfterReturning(value = "publish(article) || update(article)", returning = "result")
    public void afterArticlePublish(Article article, Result result){

        if (!result.isSuccess()){
            return;
        }

        // 分类标签管理
        List<Type> types = article.getTypes();
        // 查询type，分类不存在则抛出CategoryNotExist异常，
        // tag不存在则创建
        if (!types.isEmpty()){
            // 当前所有
            List<Type> nowTypes = new ArrayList<>();
            for (Type type : types) {
                if (StringUtils.isBlank(type.getName())){
                    continue;
                }
                Result res = typeService.selectOne(type);
                if (res.isSuccess()){
                    // 存在
                    type = (Type) res.getData();
                } else if (res.getStatusCode() == StatusCode.DataNotFound) {
                    if (Type.CATEGORY.equals(type.getType())) {
                        // 不存在种类
                        // 返回不存在错误
                        throw new StatusCodeException(StatusCode.CategoryNotExist);
                    } else if (Type.TAG.equals(type.getType())) {
                        // 不存在标签
                        // 创建标签
                        Result re = typeService.create(type);
                        if (!re.isSuccess()) {
                            // 新建标签失败
                            String msg = re.getMsg();
                            throw new StatusCodeException(re.getStatusCode(),
                                    msg==null?"新建标签失败":msg);
                        }
                    }
                }
                nowTypes.add(type);
            }

            // 以前所有
            Result<List<Type>> resultType = typeRelaService.selectByArticleId(article.getId());
            if (!resultType.isSuccess()){
                if (resultType.getStatusCode() != StatusCode.DataNotFound){
                    throw new StatusCodeException(resultType.getStatusCode(), "查询type关系失败");
                }
            }
            List<Type> oldTypes = resultType.getData()!=null?resultType.getData():new ArrayList<>();

            List<Type> needDel = new ArrayList<>(oldTypes);

            // 差集求多余的
            // 以前所有的type，移除现在所有的type，就是需要删除的type
            needDel.removeAll(nowTypes);
            // 差集求缺少的
            // 现在所有的type，移除以前所有的type，就是需要插入的type
            nowTypes.removeAll(oldTypes);

            // 执行删除
            Result res = typeRelaService.delete(article.getId(), needDel);
            if (!res.isSuccess()){
                // 删除失败
                throw new StatusCodeException(res.getStatusCode(), res.getMsg());
            }
            // 执行插入
            res = typeRelaService.create(article.getId(), nowTypes);
            if (!res.isSuccess()){
                // 插入失败
                throw new StatusCodeException(res.getStatusCode(), res.getMsg());
            }
        }
        // 清除文章缓存
        webCacheService.removeArticleContentHtml(article.getId());


    }
}
