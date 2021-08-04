package com.ak47007.redis;

import java.util.List;

/**
 * @Author： AK47007
 * CreateDate： 2019/11/29
 */
public interface RedisService {


    /**
     * set对象
     *
     * @param key    key
     * @param object value
     * @param time   过期时间,单位分钟
     */
    void setObject(String key, Object object, long time);

    /**
     * 获取数据
     *
     * @param key    key
     * @param tClass 转换的类
     * @return
     */
    <T> T getValue(String key, Class<T> tClass);


    /**
     * 获取数据
     *
     * @param key    key
     * @param tClass 转换的类
     * @return
     */
    <T> List<T> getListValue(String key, Class<T> tClass);


    /**
     * 删除数据
     *
     * @param key key
     */
    void delValue(String key);
}
