package com.cjh.note_blog.CSD.Cache.service.impl;

import com.cjh.note_blog.CSD.Cache.dao.ICache;
import com.cjh.note_blog.CSD.Cache.service.ICacheService;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.utils.IPKit;
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
        String key = "article"+id;
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
     * @param id 文章id
     * @param hits 文章点击数
     */
    @Override
    public void putHitsToCache(Integer id, Integer hits) {
        // 键
        String key = "article"+id;
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
        Integer count = cache.hget(HITS_FREQUENCY, ipC);
        return count;
    }

    /**
     * 标记该ip
     * 使ip在内存中计数+1
     * @param ip ip地址
     * @param id 文章id
     * @return 返回该ip访问次数
     */
    @Override
    public int markVisitsIp(String ip, Integer id) {
        String ipC = ip + ":" + id;
        Integer count = getVisitsIpCount(ip, id);
        count = count == null ? 1 : count + 1;
        cache.hset(HITS_FREQUENCY, ipC, count, WebConst.HITS_LIMIT_TIME);
        return count;
    }

    /**
     * 获取该用户在缓存里的信息
     *
     * @param email 用户邮箱
     * @return 用户对象
     */
    @Override
    public User getUserFromCache(String email) {

        return cache.get(email);
    }

    /**
     * 将用户信息放入缓存
     *
     * @param email 用户邮箱
     * @param user 用户对象
     */
    @Override
    public void putUserToCache(String email, User user) {
        cache.set(email, user);
    }

    /**
     * 将用户信息从缓存中移除
     *
     * @param email 用户邮箱
     * @return 用户信息
     */
    @Override
    public User removeUserFromCache(String email) {
        User user = getUserFromCache(email);
        if (null != user) {
            cache.del(email);
        }
        return user;
    }
}
