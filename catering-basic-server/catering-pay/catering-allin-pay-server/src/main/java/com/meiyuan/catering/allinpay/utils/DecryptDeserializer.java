package com.meiyuan.catering.allinpay.utils;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.meiyuan.catering.core.util.SpringContextUtils;

import java.lang.reflect.Type;

/**
 * 统一处理敏感信息解密
 *
 * @author zengzhangni
 * @date 2020/9/26 17:02
 * @since v1.5.0
 */
public class DecryptDeserializer implements ObjectDeserializer {


    @Override
    public String deserialze(DefaultJSONParser parser, Type type, Object o) {
        Object value = parser.parseObject(type);

        AllinPayOpenClient client = SpringContextUtils.getBean(AllinPayOpenClient.class);
        return client.decrypt(value.toString());
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
