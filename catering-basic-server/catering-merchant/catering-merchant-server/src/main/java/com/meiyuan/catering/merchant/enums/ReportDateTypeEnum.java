package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 标签类型：1：默认标签 2：系统添加标签
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum ReportDateTypeEnum {
    /**
     * 自然日
     */
    NATURAL_DAY(Values.NATURAL_DAY, "自然日"),
    /**
     * 营业日
     */
    BUSINESS_DAY(Values.BUSINESS_DAY, "营业日"),
    /**
     * 本日、本周、本月
     */
    TODAY(Values.TODAY, "本日"),
    THIS_WEEK(Values.THIS_WEEK, "本周"),
    THIS_MONTH(Values.THIS_MONTH, "本月");


    private Integer status;
    private String desc;

    ReportDateTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    };



    public static ReportDateTypeEnum parse(int status) {
        for (ReportDateTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }


    public static class  Values{
        private static final Integer NATURAL_DAY = 1;
        private static final Integer BUSINESS_DAY = 2;
        private static final Integer TODAY = 1;
        private static final Integer THIS_WEEK = 2;
        private static final Integer THIS_MONTH = 3;
    }
}
