package com.meiyuan.catering.admin.enums.base;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/5/26 14:34
 * @description 简单描述
 **/
@Getter
public enum RelevanceTypeEnum {
    /**
     * 商家
     **/
    MERCHANT(Values.MERCHANT, "商家"),
    /**
     * 商品
     **/
    GOODS(Values.GOODS,"商品"),
    /**
     * 入驻商家
     **/
    SETTLE_IN_MERCHANT(Values.SETTLE_IN_MERCHANT,"入驻商家");
    private Integer status;
    private String desc;
    RelevanceTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static RelevanceTypeEnum parse(int status) {
        for (RelevanceTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer MERCHANT = 1;
        private static final Integer GOODS = 2;
        private static final Integer SETTLE_IN_MERCHANT = 3;
    }
}
