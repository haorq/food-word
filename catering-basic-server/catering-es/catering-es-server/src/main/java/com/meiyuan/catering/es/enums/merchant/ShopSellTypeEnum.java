package com.meiyuan.catering.es.enums.merchant;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 售卖模式枚举 ：1-商品售卖模式 2-菜单售卖模式
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum ShopSellTypeEnum {
    /**
     * 商品售卖模式
     */
    GOOD(Values.GOOD, "商品售卖模式"),
    /**
     * 菜单售卖模式
     */
    GOOD_MENU(Values.GOOD_MENU, "菜单售卖模式");

    private Integer status;
    private String desc;
    ShopSellTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ShopSellTypeEnum parse(int status) {
        for (ShopSellTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer GOOD = 2;
        private static final Integer GOOD_MENU = 1;
    }
}
