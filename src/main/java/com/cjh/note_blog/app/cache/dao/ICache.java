package com.cjh.note_blog.app.cache.dao;

/**
 * ：
 *
 * @author ChangJiahong
 * @date 2019/7/19
 */
public interface ICache {

    /**
     * 获取缓存
     * @param key 缓存key
     * @param <T> 泛型，缓存value的类型
     * @return 缓存value
     */
    <T> T get(String key);

    /**
     * 读取一个hash类型缓存
     *
     * @param key   缓存key
     * @param field 缓存field
     * @param <T> 泛型，缓存value的类型
     * @return 缓存value
     */
    <T> T hget(String key, String field);

    /**
     * 设置一个缓存 永不过期
     *
     * @param key   缓存key
     * @param value 缓存value
     */
    void set(String key, Object value) ;

    /**
     * 设置一个缓存并带过期时间
     *
     * @param key     缓存key
     * @param value   缓存value
     * @param expired 过期时间，单位为秒
     */
    void set(String key, Object value, long expired);

    /**
     * 设置一个hash缓存 永不过期
     *
     * @param key   缓存key
     * @param field 缓存field
     * @param value 缓存value
     */
    void hset(String key, String field, Object value);

    /**
     * 设置一个hash缓存并带过期时间
     *
     * @param key     缓存key
     * @param field   缓存field
     * @param value   缓存value
     * @param expired 过期时间，单位为秒
     */
    void hset(String key, String field, Object value, long expired);


    /**
     * 检查key是否存在
     * @param key 缓存key
     * @return 如果存在返回true，否则返回false
     */
    boolean containsKey(String key);

    /**
     * 检查hash缓存是否有该key
     * @param key 缓存key
     * @param field 缓存域
     * @return 如果存在返回true，否则返回false
     */
    boolean hContainsKey(String key, String field);

    /**
     * 根据key删除缓存
     *
     * @param key 缓存key
     */
    <T> T del(String key);


    /**
     * 根据key和field删除缓存
     *
     * @param key   缓存key
     * @param field 缓存field
     */
    <T> T hdel(String key, String field);

    /**
     * 清空缓存
     */
    void clean();

}
