package com.meiyuan.catering.allinpay.core.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zengzhangni
 * @date 2020/9/26 14:05
 * @since v1.1.0
 */
@Data
public class OpenRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String appId;
    private String method;
    private String format;
    private String charset;
    private String signType;
    private String sign;
    private String timestamp;
    private String version;
    private String notifyUrl;
    private String appAuthToken;
    private String bizContent;

}
