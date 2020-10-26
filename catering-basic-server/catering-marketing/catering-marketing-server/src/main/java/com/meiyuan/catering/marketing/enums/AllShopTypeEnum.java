package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 赠品活动是否面向所有店铺枚举
 * @Date  2020/3/20 0020 22:12
 */
@Getter
public enum AllShopTypeEnum {
    /**
     * 面向所有商家
     **/
    FACE_ALL(AllShopTypeEnum.Values.FACE_ALL, Boolean.TRUE,"面向所有商家"),
    /**
     * 未面向所有商家
     **/
    NOT_FACE_ALL(AllShopTypeEnum.Values.NOT_FACE_ALL, Boolean.FALSE,"未面向所有商家");
    private Integer status;
    private Boolean flag;
    private String desc;
    AllShopTypeEnum(Integer status, Boolean flag, String desc){
        this.status = status;
        this.flag = flag;
        this.desc = desc;
    }

    public static AllShopTypeEnum parse(int status) {
        for (AllShopTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer NOT_FACE_ALL = 0;
        private static final Integer FACE_ALL = 1;
    }
}
