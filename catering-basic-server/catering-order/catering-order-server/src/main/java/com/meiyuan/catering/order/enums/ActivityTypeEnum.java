package com.meiyuan.catering.order.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName MarketingOfTypeEnum
 * @Description 订单活动关联类型枚举
 * @Author gz
 * @Date 2020/3/16 11:04
 * @Version 1.1
 */
@Getter
public enum ActivityTypeEnum {

    /**
     * 秒杀
     **/
    SECKILL(Values.SECKILL, "秒杀"),
    /**
     * 团购
     **/
    GROUPON(Values.GROUPON, "团购"),
    /**
     * 拼单
     **/
    SHARE_BILL(Values.SHARE_BILL, "拼单"),
    /**
     * 折扣
     **/
    SPECIAL(Values.SPECIAL, "折扣")
    ;
    private Integer status;
    private String desc;

    ActivityTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static ActivityTypeEnum parse(int status) {
        for (ActivityTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values {
        private static final Integer SECKILL = 1;
        private static final Integer GROUPON = 2;
        private static final Integer SHARE_BILL = 3;
        private static final Integer SPECIAL = 4;
    }

}
