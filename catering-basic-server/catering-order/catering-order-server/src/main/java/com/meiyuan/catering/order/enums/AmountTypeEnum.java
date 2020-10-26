package com.meiyuan.catering.order.enums;

import lombok.Getter;

/**
 * @author zengzhangni
 * @date 2020/10/14 17:50
 * @since v1.5.0
 */
@Getter
public enum AmountTypeEnum {
    /**
     * 描述:款项类型（1：应付；2：已付）
     */
    COPE_WITH(1, "应付(负债)"),
    PAID(2, "已付(还款)"),
    ;

    private Integer type;
    private String desc;

    AmountTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
