package com.meiyuan.catering.finance.enums;

import lombok.Getter;

/**
 * 充值活动状态
 *
 * @author zengzhangni
 * @date 2020/3/23
 */
@Getter
public enum RechargeActivityStatusEnum {

    /**
     * 进行中
     */
    UNDERWAY(0, "进行中"),
    /**
     * 未开始
     */
    NOT_START(1, "未开始"),
    /**
     * 已结束
     */
    FINISHED(2, "已结束");

    private Integer status;
    private String desc;

    RechargeActivityStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
