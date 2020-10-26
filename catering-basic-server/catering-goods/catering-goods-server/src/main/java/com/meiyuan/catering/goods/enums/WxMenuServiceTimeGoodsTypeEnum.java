package com.meiyuan.catering.goods.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/4/8 14:31
 * @description 简单描述
 **/
@Getter
public enum  WxMenuServiceTimeGoodsTypeEnum {
    /**
     * 商品售卖模式
     */
    GOOD(Values.GOOD, "商品"),
    /**
     * 菜单售卖模式
     */
    GOOD_MENU(Values.GOOD_MENU, "菜单"),
    /**
     * 团购
     */
    GROUP_BUY(Values.GROUP_BUY, "团购");

    private Integer status;
    private String desc;
    WxMenuServiceTimeGoodsTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static WxMenuServiceTimeGoodsTypeEnum parse(int status) {
        for (WxMenuServiceTimeGoodsTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values{
        private static final Integer GOOD = 2;
        private static final Integer GOOD_MENU = 1;
        private static final Integer GROUP_BUY = 3;
    }
}
