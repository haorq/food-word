package com.meiyuan.catering.core.util.yly;

import lombok.Data;

/**
 * @author lh
 */
@Data
public class YlyService {
    /**client_id*/
    private String appId;
    /**client_secret*/
    private String appSecret;
    /**Access Token*/
    private String appToken;
    /**Access Token*/
    private String imgUrl;
}
