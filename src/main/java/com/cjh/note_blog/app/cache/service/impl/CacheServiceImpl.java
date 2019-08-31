package com.cjh.note_blog.app.cache.service.impl;

import com.cjh.note_blog.app.cache.dao.ICache;
import com.cjh.note_blog.app.cache.service.ICacheService;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.utils.PatternKit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/8/7
 */

@Service
public class CacheServiceImpl implements ICacheService {


    @Autowired
    private ICache cache;

    /**
     * 从缓存里获取文章访问量
     *
     * @param id 文章id
     * @return 返回该文章缓存访问量
     */
    @Override
    public int getHitsFromCache(Integer id) {
        // 键
        String key = "article" + id;
        // 领域
        String field = "hits";
        // 点击数
        Integer incrementHits = cache.hget(key, field);
        incrementHits = incrementHits == null ? 0 : incrementHits;
        return incrementHits;
    }


    /**
     * 文章访问量放入缓存
     *
     * @param id   文章id
     * @param hits 文章点击数
     */
    @Override
    public void putHitsToCache(Integer id, Integer hits) {
        // 键
        String key = "article" + id;
        // 领域
        String field = "hits";
        cache.hset(key, field, hits);
    }

    /**
     * 获取该ip在短时间之内的访问该文章的次数
     *
     * @param ip 来者ip
     * @param id 文章id
     * @return 返回该ip访问次数
     */
    @Override
    public Integer getVisitsIpCount(String ip, Integer id) {
        String ipC = ip + ":" + id;

        // 从cache获取ip地址信息
        Integer count = cache.hget(ipC, HITS_FREQUENCY);
        return count;
    }

    /**
     * 标记该ip
     * 使ip在内存中计数+1
     *
     * @param ip ip地址
     * @param id 文章id
     * @return 返回该ip访问次数
     */
    @Override
    public int markVisitsIp(String ip, Integer id) {
        String ipC = ip + ":" + id;
        Integer count = getVisitsIpCount(ip, id);
        count = count == null ? 1 : count + 1;
        cache.hset(ipC, HITS_FREQUENCY, count, WebConst.HITS_LIMIT_TIME);
        return count;
    }

    /**
     * 获取该用户在缓存里的信息
     *
     * @param emailOrUsername 用户邮箱
     * @return 用户对象
     */
    @Override
    public User getUserFromCache(String emailOrUsername) {
        String email = emailOrUsername;
        if (!PatternKit.isEmail(emailOrUsername)) {
            // 不是邮箱
            email = cache.hget(emailOrUsername, WebConst.USER_LOGIN);
        }
        // 获取用户信息
        return cache.hget(email, WebConst.USER_LOGIN);
    }

    /**
     * 获取该用户在缓存中的令牌
     *
     * @param email 用户邮箱
     * @return 用户对象
     */
    @Override
    public String getUserTokenFromCache(String email) {
        if (!PatternKit.isEmail(email)) {
            return "";
        }
        return cache.hget(email, WebConst.USER_TOKEN);
    }

    /**
     * 将用户信息放入缓存
     *
     * @param user 用户对象
     */
    @Override
    public void putUserToCache(User user) {

        String username = user.getUsername();
        String email = user.getEmail();

        // 关联username-email
        cache.hset(username, WebConst.USER_LOGIN, email, WebConst.USER_LIMIT_TIME);
        // 放入user对象到缓存
        cache.hset(email, WebConst.USER_LOGIN, user, WebConst.USER_LIMIT_TIME);
    }

    /**
     * 将用户令牌放入缓存
     *
     * @param email 用户邮箱
     * @param token 令牌
     */
    @Override
    public void putUserTokenToCache(String email, String token) {
        if (PatternKit.isEmail(email)) {
            cache.hset(email, WebConst.USER_TOKEN, token, WebConst.USER_LIMIT_TIME);
        }
    }

    @Override
    public String removeUserTokenFromCache(String email) {
        String token = getUserTokenFromCache(email);
        if (StringUtils.isNotBlank(token)) {
            cache.hdel(email, WebConst.USER_TOKEN);
        }
        return token;
    }

    /**
     * 将用户信息从缓存中移除
     *
     * @param emailOrUsername 用户邮箱
     * @return 用户信息
     */
    @Override
    public User removeUserFromCache(String emailOrUsername) {
        User user = getUserFromCache(emailOrUsername);
        if (null != user) {
            // 移除用户token
            removeUserTokenFromCache(user.getEmail());
            // 移除username-email
            cache.hdel(user.getUsername(), WebConst.USER_LOGIN);
            // 移除email-user
            cache.hdel(user.getEmail(), WebConst.USER_LOGIN);
        }
        return user;
    }

    /**
     * 获取文章内容html格式 从缓存中
     *
     * @param articleId
     * @return
     */
    @Override
    public String getArticleContentHtml(Integer articleId) {
        String key = "article" + articleId;
        return cache.hget(key, WebConst.ARTICLE);
    }

    /**
     * 向缓存中放入文章html格式内容
     *
     * @param articleId
     * @param contentHtml
     * @return
     */
    @Override
    public void putArticleContentHtml(Integer articleId, String contentHtml) {
        String key = "article" + articleId;
        cache.hset(key, WebConst.ARTICLE, contentHtml);
    }

    /**
     * 清除缓存中文章html格式内容
     *
     * @param articleId
     * @return
     */
    @Override
    public void removeArticleContentHtml(Integer articleId) {
        String key = "article" + articleId;
        cache.hdel(key, WebConst.ARTICLE);
    }
}
