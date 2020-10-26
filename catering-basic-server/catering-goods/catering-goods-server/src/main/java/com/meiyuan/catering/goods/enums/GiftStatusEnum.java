package com.meiyuan.catering.goods.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author mt
 * @date 2020/3/10 10:59
 * @description 赠品状态 1-禁用 2-启用
 **/
@Getter
public enum GiftStatusEnum {
    /**
     * 禁用
     **/
    CLOSE(Values.CLOSE, "禁用"),
    /**
     * 启用
     **/
    OPEN(Values.OPEN, "启用");
    private Integer status;
    private String desc;
    GiftStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static GiftStatusEnum parse(int status) {
        for (GiftStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer CLOSE = 1;
        private static final Integer OPEN = 2;
    }
}
