package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 店铺门店结算信息审核步骤
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum ShopAuditStepsEnum {
    /**
     * 1：实名认证
     */
    NAME_AUTH(Values.NAME_AUTH, "实名认证"),
    /**
     * 2：电子签约
     */
    CONTRACT(Values.CONTRACT, "电子签约"),
    /**
     * 3：绑定银行卡
     */
    BIND_BANK(Values.BIND_BANK, "绑定银行卡"),
    /**
     * 4：已完成
     */
    FINISH(Values.FINISH, "已完成");

    private Integer status;
    private String desc;
    ShopAuditStepsEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ShopAuditStepsEnum parse(int status) {
        for (ShopAuditStepsEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer NAME_AUTH = 1;
        private static final Integer CONTRACT = 2;
        private static final Integer BIND_BANK = 3;
        private static final Integer FINISH = 4;
    }
}
