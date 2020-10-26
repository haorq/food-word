package com.meiyuan.catering.es.enums.merchant;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/6/8 14:12
 * @description 简单描述
 **/
@Getter
public enum  MerchantHaveGoodsEnum {
    /**
     * 有
     */
    HAVE(Values.HAVE, "有"),
    /**
     * 没有
     */
    NO_HAVE(Values.NO_HAVE, "没有");

    private Boolean flag;
    private String desc;
    MerchantHaveGoodsEnum(Boolean flag, String desc){
        this.flag = flag;
        this.desc = desc;
    }

    public static MerchantHaveGoodsEnum parse(Boolean flag) {
        for (MerchantHaveGoodsEnum type : values()) {
            if (type.flag.equals(flag)) {
                return type;
            }
        }
        throw new CustomException("非法的status");
    }

    public static class  Values{
        private static final Boolean HAVE = true;
        private static final Boolean NO_HAVE = false;
    }
}
