package com.meiyuan.catering.merchant.goods.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * description：分类创建类型枚举
 * @author yy
 * @date 2020/7/7
 */
@Getter
public enum CategoryTypeEnum {
    /**
     * 默认标签
     */
    DEFAULT(Values.DEFAULT, "默认"),
    /**
     * 新增
     */
    NEWADD(Values.NEWADD, "新增");

    private Integer status;
    private String desc;
    CategoryTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static CategoryTypeEnum parse(int status) {
        for (CategoryTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer DEFAULT = 2;
        private static final Integer NEWADD = 1;
    }
}
