package com.cjh.note_blog.CSD.Cache.dao.impl;

import com.cjh.note_blog.CSD.Cache.dao.ICache;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ：
 * 网站缓存组件
 * @author ChangJiahong
 * @date 2019/7/19
 */

@Repository
public class WebCache implements ICache {


    /**
     * 默认最大缓存数 1024
     */
    private static final int DEFAULT_CACHES = 1024;

    /**
     *  缓存cache 线程安全
     */
    private Map<String,CacheObject> cacheObjectMap = new ConcurrentHashMap<>(DEFAULT_CACHES);


    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    @Override
    public <T> T get(String key) {
        CacheObject cacheObject = cacheObjectMap.get(key);
        if (null != cacheObject) {
            long cur = System.currentTimeMillis() / 1000;
            //未过期直接返回
            if (cacheObject.getExpired() <= 0 || cacheObject.getExpired() > cur) {
                Object result = cacheObject.getValue();
                return (T) result;
            }
            //已过期直接删除
            if (cur > cacheObject.getExpired()) {
                cacheObjectMap.remove(key);
            }
        }
        return null;
    }

    /**
     * 读取一个hash类型缓存
     *
     * @param key   缓存key
     * @param field 缓存field
     * @return
     */
    @Override
    public <T> T hget(String key, String field) {
        key = key + ":" + field;
        return this.get(key);
    }

    /**
     * 设置一个缓存 永不过期
     *
     * @param key   缓存key
     * @param value 缓存value
     */
    @Override
    public void set(String key, Object value) {
        this.set(key, value, -1);
    }

    /**
     * 设置一个缓存并带过期时间
     *
     * @param key     缓存key
     * @param value   缓存value
     * @param expired 过期时间，单位为秒
     */
    @Override
    public void set(String key, Object value, long expired) {
        // 计算定时
        expired = expired > 0 ? System.currentTimeMillis() / 1000 + expired : expired;
        //cachePool大于800时，强制清空缓存池，这个操作有些粗暴会导致误删问题，后期考虑用redis替代MapCache优化
        if (cacheObjectMap.size() > 800) {
            cacheObjectMap.clear();
        }
        CacheObject cacheObject = new CacheObject(key, value, expired);
        cacheObjectMap.put(key, cacheObject);
    }

    /**
     * 设置一个hash缓存 永不过期
     *
     * @param key   缓存key
     * @param field 缓存field
     * @param value 缓存value
     */
    @Override
    public void hset(String key, String field, Object value) {
        this.hset(key, field, value, -1);
    }

    /**
     * 设置一个hash缓存并带过期时间
     *
     * @param key     缓存key
     * @param field   缓存field
     * @param value   缓存value
     * @param expired 过期时间，单位为秒
     */
    @Override
    public void hset(String key, String field, Object value, long expired) {
        key = key + ":" + field;
        expired = expired > 0 ? System.currentTimeMillis() / 1000 + expired : expired;
        CacheObject cacheObject = new CacheObject(key, value, expired);
        cacheObjectMap.put(key, cacheObject);
    }

    /**
     * 检查key是否存在
     *
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(String key) {
        return cacheObjectMap.containsKey(key);
    }

    /**
     * 检查hash缓存是否有该key
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public boolean hContainsKey(String key, String field) {
        key = key + ":" + field;
        return cacheObjectMap.containsKey(key);
    }

    /**
     * 根据key删除缓存
     *
     * @param key 缓存key
     */
    @Override
    public <T> T del(String key) {
        if (this.containsKey(key)) {
            return (T) cacheObjectMap.remove(key);
        }
        return null;
    }

    /**
     * 根据key和field删除缓存
     *
     * @param key   缓存key
     * @param field 缓存field
     */
    @Override
    public <T> T hdel(String key, String field) {
        key = key + ":" + field;
        return this.del(key);
    }

    /**
     * 清空缓存
     */
    @Override
    public void clean() {
        cacheObjectMap.clear();
    }

    private class CacheObject {

        private String key;

        private Object value;
        /**
         * 有效时间 单位 s
         */
        private long expired;

        public CacheObject(String key, Object value, long expired) {
            this.key = key;
            this.value = value;
            this.expired = expired;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public long getExpired() {
            return expired;
        }

    }
}
