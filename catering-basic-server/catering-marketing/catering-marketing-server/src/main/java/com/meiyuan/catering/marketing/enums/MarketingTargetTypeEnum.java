package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author GongJunZheng
 * @date 2020/08/05 15:08
 * @description 营销对象活动类型
 **/

@Getter
public enum MarketingTargetTypeEnum {

    /**
     * 用户
     */
    USER(Values.USER, "用户"),
    /**
     * 品牌
     */
    BRAND(Values.BRAND, "品牌");


    private Integer status;
    private String desc;

    MarketingTargetTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingTargetTypeEnum parse(int status) {
        for (MarketingTargetTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values{
        private static final Integer USER = 1;
        private static final Integer BRAND = 2;
    }

}
