package com.cjh.note_blog.CSD.Aspect;

import com.cjh.note_blog.CSD.Article.service.IArticleService;
import com.cjh.note_blog.CSD.Cache.service.ICacheService;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Article;
import com.cjh.note_blog.pojo.VO.RestResponse;
import com.cjh.note_blog.utils.IPKit;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * :
 * 文章点击数计算 切面
 * @author ChangJiahong
 * @date 2019/8/7
 */
@Component
@Aspect
public class ArticleHitsRecordAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleHitsRecordAspect.class);

    @Autowired
    private ICacheService webCacheService;

    @Autowired
    private IArticleService articleService;

    @Pointcut("execution(* com.cjh.note_blog.CSD.Article.controller.ArticleController" +
            ".getArticle(java.lang.String,javax.servlet.http.HttpServletRequest)) && args(artName, request)")
    public void performance(String artName, HttpServletRequest request){}

    @AfterReturning(value = "performance(artName, request)", returning = "restResponse")
    public void afterGetArticle(String artName, HttpServletRequest request, RestResponse<Article> restResponse) {

        if (!restResponse.isOK()){
            return;
        }

        Article article = restResponse.getData();

        if (checkHitsFrequency(request, article.getId())) {
            // 最近访问过，不算点击率
            return;
        }

        // 点击数增量
        int incrementHits = webCacheService.getHitsFromCache(article.getId());

        // 访问量+1
        incrementHits++;

        if (incrementHits >= WebConst.HIT_EXCEED) {
            // 大于等于临界值， 保存
            Result result = articleService.updateHits(article.getId(), incrementHits);
            if (result.isSuccess()) {
                // 成功， 缓存清零
                incrementHits = 0;
            }
        }
        // 成功 存入0
        // 失败 存在之前的缓存中
        webCacheService.putHitsToCache(article.getId(), incrementHits);

    }

    /**
     * 检查同一个ip地址是否在1小时内访问同一文章
     *
     * @param request
     * @param cid
     * @return true表示最近访问过
     */
    private boolean checkHitsFrequency(HttpServletRequest request, Integer cid) {
        // 从cache获取ip地址信息
        String ip = IPKit.getIpAddrByRequest(request);
        Integer count = webCacheService.getVisitsIpCount(ip, cid);

        // 如果 获取的文章点击频率不为空 且 >0
        if (null != count && count > 0) {
            // 表示最近访问过 标记一次
            int i = webCacheService.markVisitsIp(ip, cid);
            LOGGER.info("ip:{} 访问频繁，访问次数：{}", ip, i);
            return true;
        }
        // 之前未访问过，此次标记
        webCacheService.markVisitsIp(ip, cid);
        return false;
    }
}
