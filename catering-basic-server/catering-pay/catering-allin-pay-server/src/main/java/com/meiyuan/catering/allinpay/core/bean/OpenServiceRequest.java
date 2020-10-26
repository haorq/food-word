package com.meiyuan.catering.allinpay.core.bean;

import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/26 14:06
 * @since v1.1.0
 */
@Data
public class OpenServiceRequest extends OpenRequest {
    private static final long serialVersionUID = 3020405199034536117L;
    private String clientAppId;
    private String jumpUrl;

    public OpenServiceRequest() {
    }

}
