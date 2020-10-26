package com.meiyuan.catering.core.util.dada.domain;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiyuan.catering.core.util.JsonUtil;

/**
 * DATE: 18/9/3
 *
 * @author: wan
 */
public class BaseModel {

    public String toJson() {
        return JsonUtil.toJson(this);
    }
}
