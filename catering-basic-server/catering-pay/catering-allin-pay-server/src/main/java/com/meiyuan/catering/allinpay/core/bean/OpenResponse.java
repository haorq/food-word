package com.meiyuan.catering.allinpay.core.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zengzhangni
 * @date 2020/9/26 14:06
 * @since v1.1.0
 */
@Data
public class OpenResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String msg;
    private String subCode;
    private String subMsg;
    private String data;
    private String sign;

}
