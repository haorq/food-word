package com.meiyuan.catering.allinpay.core.bean;

import com.alibaba.fastjson.JSON;

import java.util.LinkedHashMap;

/**
 * @author zengzhangni
 * @date 2020/9/26 14:02
 * @since v1.1.0
 */
public class BizParameter extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public BizParameter() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
