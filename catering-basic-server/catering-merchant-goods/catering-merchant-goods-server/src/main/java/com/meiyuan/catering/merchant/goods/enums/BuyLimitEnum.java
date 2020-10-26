package com.meiyuan.catering.merchant.goods.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/3/20 20:40
 * @description 限购枚举
 **/
@Getter
public enum BuyLimitEnum {
    /**
     * -1表示 不限购
     **/
    NO_BUY_LIMIT(Values.NO_BUY_LIMIT, "-1表示 不限购");
    private Long status;
    private String desc;
    BuyLimitEnum(Long status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static BuyLimitEnum parse(Long status) {
        for (BuyLimitEnum type : values()) {
            if (type.status.equals(status)) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Long NO_BUY_LIMIT = -1L;
    }
}
