package com.meiyuan.catering.es.enums.marketing;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName MarketingUpDownStatusEnum
 * @Description 促销活动上下架状态枚举
 * @Author gz
 * @Date 2020/3/16 11:15
 * @Version 1.1
 */
@Getter
public enum MarketingUpDownStatusEnum {
    /**
     * 上架
     */
    UP(Values.UP,"上架"),
    /**
     * 下架
     */
    DOWN(Values.DOWN,"下架");

    private Integer status;
    private String desc;
    MarketingUpDownStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingUpDownStatusEnum parse(int status) {
        for (MarketingUpDownStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer DOWN = 1;
        private static final Integer UP = 2;
    }
}
