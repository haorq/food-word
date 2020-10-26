package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 商户审核状态枚举
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum AuditStatusEnum {
    /**
     * 1:待上传，2：未审核，3：已通过，4：未通过
     */
    WAIT_UPLOAD(Values.WAIT_UPLOAD, "待上传"),
    /**
     * 2：未审核
     */
    WAIT_AUDIT(Values.WAIT_AUDIT, "未审核"),
    /**
     * 3：已通过
     */
    PASS(Values.PASS, "已通过"),
    /**
     * 4：未通过
     */
    NOT_PASS(Values.NOT_PASS, "未通过");

    private Integer status;
    private String desc;
    AuditStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static AuditStatusEnum parse(int status) {
        for (AuditStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer WAIT_UPLOAD = 1;
        private static final Integer WAIT_AUDIT = 2;
        private static final Integer PASS = 3;
        private static final Integer NOT_PASS = 4;
    }
}
