package com.cjh.note_blog.app.cache.service;

import com.cjh.note_blog.pojo.DO.User;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/8/7
 */
public interface ICacheService {

    String HITS_FREQUENCY = "hits:frequency";


    /**
     * 从缓存里获取文章访问量
     *
     * @param id 文章id
     * @return 返回该文章缓存访问量
     */
    int getHitsFromCache(Integer id);

    /**
     * 文章访问量放入缓存
     *
     * @param id   文章id
     * @param hits 文章点击数
     */
    void putHitsToCache(Integer id, Integer hits);

    /**
     * 获取该ip在短时间之内的访问该文章的次数
     *
     * @param ip 来者ip
     * @param id 文章id
     * @return 返回该ip访问次数
     */
    Integer getVisitsIpCount(String ip, Integer id);

    /**
     * 标记该ip
     *
     * @param ip 来者ip
     * @param id 文章id
     * @return 返回该ip访问次数
     */
    int markVisitsIp(String ip, Integer id);

    /**
     * 获取该用户在缓存里的信息
     *
     * @param emailOrUsername 用户邮箱或用户名
     * @return 用户对象
     */
    User getUserFromCache(String emailOrUsername);

    /**
     * 获取该用户在缓存中的令牌
     *
     * @param email 用户邮箱
     * @return 用户对象
     */
    String getUserTokenFromCache(String email);

    /**
     * 将用户信息放入缓存
     *
     * @param user 用户对象
     */
    void putUserToCache(User user);

    /**
     * 将用户令牌放入缓存
     *
     * @param email 用户邮箱
     * @param token 令牌
     */
    void putUserTokenToCache(String email, String token);

    String removeUserTokenFromCache(String email);

    /**
     * 将用户信息从缓存中移除
     *
     * @param emailOrUsername 用户邮箱
     * @return 用户信息
     */
    User removeUserFromCache(String emailOrUsername);

    /**
     * 获取文章内容html格式 从缓存中
     *
     * @param articleId
     * @return
     */
    String getArticleContentHtml(Integer articleId);

    /**
     * 向缓存中放入文章html格式内容
     * @param articleId
     * @param contentHtml
     * @return
     */
    void putArticleContentHtml(Integer articleId, String contentHtml);

    /**
     * 清除缓存中文章html格式内容
     * @param articleId
     * @return
     */
    void removeArticleContentHtml(Integer articleId);

}
