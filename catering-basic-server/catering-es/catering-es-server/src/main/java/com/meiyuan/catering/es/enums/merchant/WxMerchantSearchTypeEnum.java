package com.meiyuan.catering.es.enums.merchant;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/5/6 15:22
 * @description 简单描述
 **/
@Getter
public enum  WxMerchantSearchTypeEnum {
    /**
     * 距离最近
     */
    CLOSEST(Values.CLOSEST, "距离最近"),
    /**
     * 好评优先
     */
    PRAISE_FIRST(Values.PRAISE_FIRST, "好评优先"),
    /**
     * 销量最高
     */
    HIGHEST_SALES(Values.HIGHEST_SALES, "销量最高");

    private Integer status;
    private String desc;
    WxMerchantSearchTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static WxMerchantSearchTypeEnum parse(int status) {
        for (WxMerchantSearchTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法的status" + status);
    }

    public static class  Values{
        private static final Integer CLOSEST = 1;
        private static final Integer PRAISE_FIRST = 2;
        private static final Integer HIGHEST_SALES = 3;
    }
}
