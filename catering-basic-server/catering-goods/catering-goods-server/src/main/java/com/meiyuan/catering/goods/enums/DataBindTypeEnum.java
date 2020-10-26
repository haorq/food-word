package com.meiyuan.catering.goods.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/3/21 16:15
 * @description 数据绑定类型
 **/
@Getter
public enum DataBindTypeEnum {
    /**
     * 商品推送
     **/
    GOODS_PUSH(Values.GOODS_PUSH, "商品推送"),
    /**
     * 菜单推送
     **/
    MENU_PUSH(Values.MENU_PUSH, "菜单推送"),
    /**
     * 菜单绑定菜品
     **/
    MENU_BIND_GOODS(Values.MENU_BIND_GOODS, "菜单绑定菜品");
    private Integer status;
    private String desc;
    DataBindTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static DataBindTypeEnum parse(int status) {
        for (DataBindTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer GOODS_PUSH = 1;
        private static final Integer MENU_PUSH = 2;
        private static final Integer MENU_BIND_GOODS = 3;
    }
}
