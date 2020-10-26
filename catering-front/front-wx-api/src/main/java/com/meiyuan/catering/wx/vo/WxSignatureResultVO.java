/*
 * Copyright (c) 2019.
 * hnf Co.Ltd. 2002-
 * All rights resolved
 */
package com.meiyuan.catering.wx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信签名结果
 *
 * @author wangbing
 * @version v1.0.0
 * @date 2019/6/3 13:51
 */
@Data
@Builder
public class WxSignatureResultVO implements Serializable {

    @ApiModelProperty("appid")
    private String appId;
    @ApiModelProperty("时间戳")
    private String timestamp;
    @ApiModelProperty("随机字符串")
    private String nonceStr;
    @ApiModelProperty("签名")
    private String signature;
    @ApiModelProperty("签名URL")
    private String url;


}
