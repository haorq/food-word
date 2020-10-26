package com.meiyuan.catering.core.enums.base;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName SourceEnum
 * @Description 来源枚举 1-平台；2-商家
 * @Author gz
 * @Date 2020/3/18 17:45
 * @Version 1.1
 */
@Getter
public enum SourceEnum {
    /**
     * 平台活动
     */
    PLATFORM(SourceEnum.Values.PLATFORM,"平台活动"),
    /**
     * 商家活动
     */
    MERCHANT(SourceEnum.Values.MERCHANT,"品牌活动"),

    /**
     * 店铺活动
     */
    SHOP(SourceEnum.Values.SHOP,"店铺活动");

    private Integer status;
    private String desc;
    SourceEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static SourceEnum parse(int status) {
        for (SourceEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer PLATFORM = 1;
        private static final Integer MERCHANT = 2;
        private static final Integer SHOP = 3;
    }
}
