package com.ak47007.redis;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @Author： AK47007
 * CreateDate： 2019/11/29
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private StringRedisTemplate template;

    /**
     * set对象 默认过期时间单位为分钟
     *
     * @param key    键
     * @param object 对象
     * @param time   过期时间
     */
    @Override
    public void setObject(String key, Object object, long time) {
        template.opsForValue().set(key, Objects.requireNonNull(RedisSerialaze.objectToJson(object)), time, TimeUnit.MINUTES);
    }

    /**
     * 获取数据
     *
     * @param key    key
     * @param tClass 转换的类
     * @return
     */
    @Override
    public <T> T getValue(String key, Class<T> tClass) {
        return RedisSerialaze.jsonToPojo(template.opsForValue().get(key), tClass);
    }

    /**
     * 获取数据
     *
     * @param key    key
     * @param tClass 转换的类
     * @return
     */
    @Override
    public <T> List<T> getListValue(String key, Class<T> tClass) {
        List data = RedisSerialaze.jsonToPojo(template.opsForValue().get(key), List.class);
        if (data != null) {
            // 使用fastjson中的代码来进行转换
            List<T> list = new ArrayList<T>(data.size());
            for (Object item : data) {
                ParserConfig config = ParserConfig.getGlobalInstance();
                T classItem = TypeUtils.cast(item, tClass, config);
                list.add(classItem);
            }
            return list;
        }
        return new ArrayList<>();
    }

    /**
     * 删除数据
     *
     * @param key key
     */
    @Override
    public void delValue(String key) {
        template.delete(key);
    }


}
