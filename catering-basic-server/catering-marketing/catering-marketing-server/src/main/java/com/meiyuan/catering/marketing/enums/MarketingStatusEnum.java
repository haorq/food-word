package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author GongJunZheng
 * @date 2020/08/05 15:08
 * @description 营销活动状态
 **/

@Getter
public enum MarketingStatusEnum {

    /**
     * 未开始
     */
    NO_BEGIN(Values.NO_BEGIN, "未开始"),
    /**
     * 进行中
     */
    ING(Values.ING, "进行中"),
    /**
     * 已结束
     */
    END(Values.END, "已结束(正常结束)"),
    /**
     * 已冻结
     */
    FREEZE(Values.FREEZE, "已冻结(冻结结束)");

    private Integer status;
    private String desc;

    MarketingStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingStatusEnum parse(int status) {
        for (MarketingStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values{
        private static final Integer NO_BEGIN = 1;
        private static final Integer ING = 2;
        private static final Integer END = 3;
        private static final Integer FREEZE = 4;
    }

}
