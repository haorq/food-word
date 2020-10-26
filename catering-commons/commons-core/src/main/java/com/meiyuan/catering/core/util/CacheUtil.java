package com.meiyuan.catering.core.util;

import com.meiyuan.marsh.jetcache.AdvancedCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020/6/3 15:08
 * @since v1.1.0
 */
public class CacheUtil {
    private final static String ALL = "*";


    /**
     * 描述:获取Cache 下所有的key
     *
     * @param cache
     * @return java.util.Set<java.lang.String>
     * @author zengzhangni
     * @date 2020/6/3 15:09
     * @since v1.1.0
     */
    public static Set<String> keys(AdvancedCache cache) {
        return keys(cache, ALL);
    }

    public static Set<String> keys(AdvancedCache cache, String pattern) {
        Set<String> keys = cache.keys(pattern);
        return keys.stream().map(key -> key.replace(cache.prefixKey(), "")).collect(Collectors.toSet());
    }

    /**
     * 描述:获取Cache所有数据 返回map
     *
     * @param cache
     * @return java.util.Map
     * @author zengzhangni
     * @date 2020/6/3 15:14
     * @since v1.1.0
     */
    public static Map getAll(AdvancedCache cache) {
        return getAll(cache, ALL);
    }

    public static Map getAll(AdvancedCache cache, String pattern) {
        Set<String> keys = keys(cache, pattern);
        return cache.getAll(keys);
    }

    /**
     * 描述:获取Cache所有数据 返回list
     *
     * @param cache
     * @return java.util.List
     * @author zengzhangni
     * @date 2020/6/3 15:15
     * @since v1.1.0
     */
    public static <T> T getList(AdvancedCache cache) {
        return (T) getList(cache, ALL);
    }

    public static List getList(AdvancedCache cache, String pattern) {
        return new ArrayList<>(getAll(cache, pattern).values());
    }

    /**
     * describe: 获取cache单个数据
     * @author: yy
     * @date: 2020/9/4 10:12
     * @param cache
     * @param key
     * @return: {@link T}
     * @version 1.4.0
     **/
    public static <T> T getOne(AdvancedCache cache, String key) {
        return (T) cache.get(key);
    }

    /**
     * 描述:删除Cache所有数据
     *
     * @param cache
     * @return java.util.List
     * @author zengzhangni
     * @date 2020/6/3 15:23
     * @since v1.1.0
     */
    public static void removeAll(AdvancedCache cache) {
        Set<String> keys = keys(cache);
        if (keys.size() > 0) {
            cache.removeAll(keys);
        }
    }
}
