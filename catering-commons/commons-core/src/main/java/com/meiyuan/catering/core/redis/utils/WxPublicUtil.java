package com.meiyuan.catering.core.redis.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.config.WxProperties;
import com.meiyuan.catering.core.constant.UserWxXinSecretConstant;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/14 15:17
 */
@Component
public class WxPublicUtil {

    private static final Logger logger = LoggerFactory.getLogger(WxPublicUtil.class);

    @Autowired
    private WxProperties properties;

    @CreateCache(name = JetcacheNames.USER_COMPANY_STATUS, area = JetcacheAreas.USER_AREA)
    private Cache<String, String> cache;

    public String getWxAccessToken() {
        String accessToken = cache.get(UserWxXinSecretConstant.ACCESS_TOKEN_KEY);
        if (!BaseUtil.isEmptyStr(accessToken)) {
            return accessToken;
        }
        try {
            // 获取公众号 access_token
            Map<String, String> map = new HashMap<>();
            map.put("grant_type", UserWxXinSecretConstant.GRANT_TYPE_ACCESS_TOKEN);
            map.put("appid", properties.getAppPublicId());
            map.put("secret", properties.getAppPublicSecret());
            String result = HttpUtil.sendGet(UserWxXinSecretConstant.ACCESS_TOKEN_URL, map);
            JSONObject json = JSON.parseObject(result);
            accessToken = json.getString("access_token");
            Long expiresIn = json.getLong("expires_in");
            cache.put(UserWxXinSecretConstant.ACCESS_TOKEN_KEY, accessToken, expiresIn, TimeUnit.SECONDS);
            return accessToken;
        } catch (Exception e) {
            logger.error("获取微信“access_token”报错：", e);
            return "";
        }
    }

}
