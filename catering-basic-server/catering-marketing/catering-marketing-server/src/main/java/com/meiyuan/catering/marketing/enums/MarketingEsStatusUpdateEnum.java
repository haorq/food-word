package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName MarketingEsStatusUpdateEnum
 * @Description 营销ES状态更新枚举
 * @Author gz
 * @Date 2020/3/25 11:08
 * @Version 1.1
 */
@Getter
public enum MarketingEsStatusUpdateEnum {
    /**
     * 上架
     */
    UP(MarketingEsStatusUpdateEnum.Values.UP,"上架"),
    /**
     * 下架
     */
    DOWN(MarketingEsStatusUpdateEnum.Values.DOWN,"下架/冻结"),
    /**
     * 删除
     */
    DEL(MarketingEsStatusUpdateEnum.Values.DEL,"删除");
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
        private static final Integer UP = 2;
        private static final Integer DOWN = 1;
        private static final Integer DEL = 3;
    }
}
