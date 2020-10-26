package com.meiyuan.catering.order.enums;

import lombok.Getter;

/**
 * 分账规则枚举
 *
 * @author zengzhangni
 * @date 2020/10/14 10:25
 * @since v1.5.0
 */
@Getter
public enum SplitTypeEnum {

    /**
     * 1:分账 2:内扣(平台分账)
     */
    SPLIT(1, "分账"),
    INNER_BUCKLE(2, "内扣(平台分账)");

    private final Integer type;

    private final String desc;

    SplitTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
