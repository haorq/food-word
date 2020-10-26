package com.meiyuan.catering.allinpay.utils;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.meiyuan.catering.core.util.SpringContextUtils;

import java.lang.reflect.Type;

/**
 * 统一处理敏感信息加密
 *
 * @author zengzhangni
 * @date 2020/9/25 16:49
 * @since v1.1.0
 */
public class EncryptSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer jsonSerializer, Object value, Object fieldName, Type type, int i) {
        AllinPayOpenClient client = SpringContextUtils.getBean(AllinPayOpenClient.class);
        jsonSerializer.write(client.encrypt(value.toString()));
    }
}
