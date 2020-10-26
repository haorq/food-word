package com.meiyuan.catering.es.enums.goods;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/6/2 9:22
 * @description 简单描述
 **/
@Getter
public enum MerchantGoodsFlagEnum {
    /**
     * 推送商家商品
     **/
    PUSH_GOODS(Values.PUSH_GOODS, "推送商家商品"),
    /**
     * 没有推送给商家的商品 单纯的商品信息
     **/
    NO_PUSH_GOODS(Values.NO_PUSH_GOODS, "单纯的商品");
    private Boolean flag;
    private String desc;
    MerchantGoodsFlagEnum(boolean flag, String desc){
        this.flag = flag;
        this.desc = desc;
    }

    public static MerchantGoodsFlagEnum parse(Boolean flag) {
        for (MerchantGoodsFlagEnum type : values()) {
            if (type.flag.equals(flag)) {
                return type;
            }
        }
        throw new CustomException("非法flag");
    }

    public static class  Values{
        private static final boolean PUSH_GOODS = true;
        private static final boolean NO_PUSH_GOODS = false;
    }
}
