package com.meiyuan.catering.core.util;

import com.alibaba.fastjson.JSON;

/**
 * @author lh
 */
public class JsonUtil {
    public static String toJson(Object object) {
        return object == null ? "" : JSON.toJSONString(object);
    }

    public static <T> T fromJson(String json, Class<T> tClass){
        return JSON.parseObject(json, tClass);
    }
}
