package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * 小程序类目关联类型
 *
 * @author zengzhangni
 * @date 2020/6/23 11:02
 * @since v1.1.0
 */
@Getter
public enum RelevanceTypeEnum {
    /**
     * 商家
     */
    MERCHANT(1, "商家"),
    /**
     * 商品
     */
    GOODS(2, "商品"),
    /**
     * 入驻商家
     */
    ENTER_MERCHANT(3, "入驻商家");
    private Integer status;
    private String desc;

    RelevanceTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }}
