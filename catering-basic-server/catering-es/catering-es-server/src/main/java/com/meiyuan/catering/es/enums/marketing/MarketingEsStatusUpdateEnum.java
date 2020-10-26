package com.meiyuan.catering.es.enums.marketing;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/3/30 14:12
 * @description 简单描述
 **/
@Getter
public enum MarketingEsStatusUpdateEnum {
    /**
     * 下架
     **/
    LOWER_SHELF(Values.LOWER_SHELF, "下架"),
    /**
     * 上架
     **/
    UPPER_SHELF(Values.UPPER_SHELF, "上架"),
    /**
     * 删除
     **/
    DEL(Values.DEL, "删除");
    private Integer status;
    private String desc;
    MarketingEsStatusUpdateEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingEsStatusUpdateEnum parse(int status) {
        for (MarketingEsStatusUpdateEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer LOWER_SHELF = 1;
        private static final Integer UPPER_SHELF = 2;
        private static final Integer DEL = 3;
    }
}
