package com.example.demo.redis;

import java.util.List;
import java.util.Set;

public interface RedisDAO {

    /**
     * 存储数据，Json存储
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value);


    /**
     * 设置缓存，Json存储
     *
     * @param key
     * @param value
     * @param timeout 失效时间，单位分钟，null或0表示不设置。
     */
    public void put(String key, Object value, Long timeout);

    /**
     * 根据类型获取
     *
     * @param key
     * @param valueClasses
     * @return
     */
    public <T> T get(String key, Class<T> valueClasses);

    /**
     * 存储数据，序列化存储
     *
     * @param key
     * @param value
     */
    public void putSerializable(String key, Object value);


    /**
     * 设置缓存，序列化存储
     *
     * @param key
     * @param value
     * @param timeout 失效时间，单位分钟，null或0表示不设置。
     */
    public void putSerializable(String key, Object value, Long timeout);


    /**
     * 根据类型获取,只能获取序列化存储的
     *
     * @param key
     * @param valueClasses
     * @return
     */
    public <T> T getSerializable(String key, Class<T> valueClasses);

    /**
     * 删除数据
     *
     * @param key
     */
    public void delete(String key);

    /**
     * 按前缀失效
     *
     * @param prefix
     */
    public void deleteByPrefix(String prefix);

    /**
     * 按列表失效
     *
     * @param keys
     */
    public void delete(List<String> keys);

    /**
     * 获取key列表
     *
     * @param prefix
     */
    public Set<String> getKeyByPrefix(String prefix);

    /**
     * 推送消息
     *
     * @param channel 通道(topic)
     * @param message 推送消息内容
     */
    public void convertAndSend(String channel, Object message);

}
