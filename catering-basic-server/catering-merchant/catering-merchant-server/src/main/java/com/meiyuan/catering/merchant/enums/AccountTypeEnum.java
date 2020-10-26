package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description  商户pc端登陆账号类型(1：品牌/商家，2：店铺，3：自提点，4：商家兼自提点，5：员工)
 * @Date  2020/3/10 0010 13:39
 */
@Getter
public enum AccountTypeEnum {

    /**
     * 商户 ： 1
     */
    MERCHANT(Values.MERCHANT, "merchant"),
    /**
     * 店铺 : 2
     **/
    SHOP(Values.SHOP, "shop"),
    /**
     * 自提点 ：3
     */
    PICKUP(Values.PICKUP, "pickup"),
    /**
     * 店铺兼自提点 ：4
     **/
    SHOP_PICKUP(Values.SHOP_PICKUP, "shopAndPickup"),
    /**
     * 员工 ：5
     **/
    EMPLOYEE(Values.EMPLOYEE, "shop");

    private Integer status;
    private String desc;
    AccountTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static AccountTypeEnum parse(int status) {
        for (AccountTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer MERCHANT = 1;
        private static final Integer SHOP = 2;
        private static final Integer PICKUP =3;
        private static final Integer SHOP_PICKUP = 4;
        private static final Integer EMPLOYEE = 5;
    }
}
