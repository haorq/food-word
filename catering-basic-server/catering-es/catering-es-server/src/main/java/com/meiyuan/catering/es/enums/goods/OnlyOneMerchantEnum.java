package com.meiyuan.catering.es.enums.goods;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/5/29 14:12
 * @description 简单描述
 **/
@Getter
public enum OnlyOneMerchantEnum {
    /**
     * 一个商家
     **/
    ONE(Values.ONE, "一个商家"),
    /**
     * 多个商家
     **/
    MANY(Values.MANY, "多个商家"),
    /**
     * 没有商家
     **/
    NO(Values.NO, "没有商家");
    private Integer status;
    private String desc;
    OnlyOneMerchantEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static OnlyOneMerchantEnum parse(int status) {
        for (OnlyOneMerchantEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer ONE = 1;
        private static final Integer MANY = 2;
        private static final Integer NO = 3;
    }
}
