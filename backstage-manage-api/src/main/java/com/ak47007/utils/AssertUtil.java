package com.ak47007.utils;

import com.ak47007.exception.AssertException;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * @author AK47007
 * @date 2020/7/31 19:20
 * describe： 断言工具类
 */
public class AssertUtil {

    /**
     * 断言不为空
     *
     * @param obj     对象
     * @param message 为空时提示的消息
     */
    public static void notNull(Object obj, String message) {
        try {
            Assert.notNull(obj, message);
        } catch (IllegalArgumentException e) {
            throw new AssertException(e.getMessage());
        }
    }

    /**
     * 断言不为空
     *
     * @param collection 集合
     * @param message    为空时提示的消息
     */
    public static void notEmpty(Collection<?> collection, String message) {
        try {
            Assert.notEmpty(collection, message);
        } catch (IllegalArgumentException e) {
            throw new AssertException(e.getMessage());
        }
    }
}
