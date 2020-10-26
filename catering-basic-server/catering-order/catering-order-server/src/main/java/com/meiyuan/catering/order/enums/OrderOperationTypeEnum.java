package com.meiyuan.catering.order.enums;

import lombok.Getter;

/**
 * 订单操作类型枚举定义
 *
 * @author wangbing
 * @version v1.0.0
 * @date 2019/10/28 15:04
 *
 */
@Getter
public enum OrderOperationTypeEnum {

    /** 用户*/
    MEMBER(Values.MEMBER),
    /** 商家*/
    MERCHANT(Values.MERCHANT),
    /** 系统*/
    SYSTEM(Values.SYSTEM);

    private final Integer value;

    OrderOperationTypeEnum(Integer value) {
        this.value = value;
    }
    public static class Values{
        public static final int MEMBER = 1;
        public static final int MERCHANT = 2;
        public static final int SYSTEM = 3;
    }

    public static Integer getValue(OrderOperationTypeEnum operationTypeEnum) {
        return operationTypeEnum.value;
    }

    public static OrderOperationTypeEnum getByValue(Integer status){
        for (OrderOperationTypeEnum operationTypeEnum : values()) {
            if (operationTypeEnum.value.equals(status)) {
                return operationTypeEnum;
            }
        }
        return null;
    }

    public int value() {
        return this.value;
    }
}
