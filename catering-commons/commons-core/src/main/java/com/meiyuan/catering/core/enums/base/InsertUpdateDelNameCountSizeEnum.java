package com.meiyuan.catering.core.enums.base;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/3/11 15:01
 * @description 新增修改删除条数枚举
 **/
@Getter
public enum InsertUpdateDelNameCountSizeEnum {
    /**
     * 单个新增修改删除条数
     * 单个名字查询返回的条数
     **/
    SIZE(Values.SIZE),
    /**
     * 0
     */
    ZERO(Values.ZERO);
    private Integer status;
    InsertUpdateDelNameCountSizeEnum(Integer status){
        this.status = status;
    }

    public static InsertUpdateDelNameCountSizeEnum parse(int status) {
        for (InsertUpdateDelNameCountSizeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer SIZE = 1;
        private static final Integer ZERO = 0;
    }
}
