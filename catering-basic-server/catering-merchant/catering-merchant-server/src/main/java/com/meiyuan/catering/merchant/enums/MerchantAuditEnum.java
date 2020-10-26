package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 商户审核状态枚举
 * @Date  2020/3/10 11:27
 */
@Getter
public enum MerchantAuditEnum {
    /**
     * 审核通过
     */
    MERCHANT_AUDIT_PASS(Values.MERCHANT_AUDIT_PASS, "审核通过"),
    /**
     * 审核失败
     */
    MERCHANT_AUDIT_FAIL(Values.MERCHANT_AUDIT_FAIL, "审核失败");
    private Integer status;
    private String desc;
    MerchantAuditEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MerchantAuditEnum parse(int status) {
        for (MerchantAuditEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer MERCHANT_AUDIT_FAIL = 1;
        private static final Integer MERCHANT_AUDIT_PASS = 2;
    }
}
