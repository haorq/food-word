package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author GongJunZheng
 * @date 2020/08/08 18:08
 * @description 营销商品上下架枚举
 **/

@Getter
public enum  MarketingGoodsStatusEnum {
    /**
     * 下架
     **/
    LOWER_SHELF(Values.LOWER_SHELF, "下架"),
    /**
     * 上架
     **/
    UPPER_SHELF(Values.UPPER_SHELF, "上架");
    private Integer status;
    private String desc;
    MarketingGoodsStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingGoodsStatusEnum parse(int status) {
        for (MarketingGoodsStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer LOWER_SHELF = 1;
        private static final Integer UPPER_SHELF = 2;
    }

}
