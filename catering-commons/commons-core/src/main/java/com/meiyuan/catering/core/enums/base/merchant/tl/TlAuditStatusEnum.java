package com.meiyuan.catering.core.enums.base.merchant.tl;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 店铺通联审核状态：店铺与通联绑定状态:1-实名认证、2-电子签约、3-绑定银行卡、4-完成、5、手机号绑定完成
 * @Date  2020/3/10 0010 13:39
 */
@Getter
public enum TlAuditStatusEnum {
    /**
     * 1-实名认证
     **/
    REAL_NAME(Values.REAL_NAME, "1-实名认证"),
    /**
     * 2-电子签约
     */
    SIGN(Values.SIGN, "2-电子签约"),
    /**
     * 3-绑定银行卡
     **/
    BIND_BANK(Values.BIND_BANK, "3-绑定银行卡"),
    /**
     * 4-完成
     */
    FINISH(Values.FINISH, "4-完成"),
    /**
     * 5、手机号绑定完成
     */
    BIND_PHONE(Values.BIND_PHONE, "5、手机号绑定完成");

    private Integer status;
    private String desc;
    TlAuditStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static TlAuditStatusEnum parse(int status) {
        for (TlAuditStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer REAL_NAME = 1;
        private static final Integer SIGN = 2;
        private static final Integer BIND_BANK = 3;
        private static final Integer FINISH = 4;
        private static final Integer BIND_PHONE = 5;
    }
}
