package com.meiyuan.catering.core.enums.base;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author GongJunZheng
 * @date 2020/08/29 08:08
 * @description 操作类型枚举
 **/

@Getter
public enum OperateTypeEnum {

    /**
     * 商品删除
     **/
    GOODS_DEL(Values.GOODS_DEL, "商品删除"),
    /**
     * 商品取消授权
     **/
    GOODS_AUTH_CANCEL(Values.GOODS_AUTH_CANCEL, "商品取消授权");
    private Integer status;
    private String desc;
    OperateTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static OperateTypeEnum parse(int status) {
        for (OperateTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer GOODS_DEL = 1;
        private static final Integer GOODS_AUTH_CANCEL = 2;
    }

}
