package com.cjh.note_blog.Cache.service;

/**
 * ：
 *
 * @author ChangJiahong
 * @date 2019/7/19
 */
public interface ICache {

    /**
     * 获取缓存
     * @param key
     * @param <T>
     * @return
     */
    public <T> T get(String key);

    /**
     * 读取一个hash类型缓存
     *
     * @param key   缓存key
     * @param field 缓存field
     * @param <T>
     * @return
     */
    public <T> T hget(String key, String field);

    /**
     * 设置一个缓存 永不过期
     *
     * @param key   缓存key
     * @param value 缓存value
     */
    public void set(String key, Object value) ;

    /**
     * 设置一个缓存并带过期时间
     *
     * @param key     缓存key
     * @param value   缓存value
     * @param expired 过期时间，单位为秒
     */
    public void set(String key, Object value, long expired);

    /**
     * 设置一个hash缓存 永不过期
     *
     * @param key   缓存key
     * @param field 缓存field
     * @param value 缓存value
     */
    public void hset(String key, String field, Object value);

    /**
     * 设置一个hash缓存并带过期时间
     *
     * @param key     缓存key
     * @param field   缓存field
     * @param value   缓存value
     * @param expired 过期时间，单位为秒
     */
    public void hset(String key, String field, Object value, long expired);


    /**
     * 检查key是否存在
     * @param key
     * @return
     */
    public boolean containsKey(String key);

    /**
     * 检查hash缓存是否有该key
     * @param key
     * @param field
     * @return
     */
    public boolean hContainsKey(String key, String field);

    /**
     * 根据key删除缓存
     *
     * @param key 缓存key
     */
    public <T> T del(String key);


    /**
     * 根据key和field删除缓存
     *
     * @param key   缓存key
     * @param field 缓存field
     */
    public <T> T hdel(String key, String field);

    /**
     * 清空缓存
     */
    public void clean();

}
