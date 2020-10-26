package com.meiyuan.catering.es.enums.marketing;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author GongJunZheng
 * @date 2020/09/14 11:09
 * @description 简单描述
 **/

@Getter
public enum  MarketingEsSpecialFixTypeEnum {

    /**
     * 统一折扣
     */
    UNIFY_SPECIAL(Values.UNIFY_SPECIAL, "统一折扣"),
    /**
     * 折扣
     */
    SPECIAL(Values.SPECIAL, "折扣"),
    /**
     * 固定价
     */
    FIXED(Values.FIXED, "固定价");

    private Integer status;
    private String desc;

    MarketingEsSpecialFixTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingEsSpecialFixTypeEnum parse(int status) {
        for (MarketingEsSpecialFixTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values{
        private static final Integer UNIFY_SPECIAL = 1;
        private static final Integer SPECIAL = 2;
        private static final Integer FIXED = 3;
    }

}
