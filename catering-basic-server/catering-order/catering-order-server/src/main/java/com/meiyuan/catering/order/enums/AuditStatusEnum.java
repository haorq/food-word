package com.meiyuan.catering.order.enums;


import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * 退款审核状态
 *
 * @author zengzhangni
 * @date 2019/3/20
 */
@Getter
public enum AuditStatusEnum {
    /**
     * 退款审核状态（1：待审核，2：通过；3：拒绝）
     */
    AWAIT_AUDIT(1, "待审核"),
    PASS_AUDIT(2, "通过"),
    REFUSE_AUDIT(3, "拒绝");

    private final Integer status;

    private final String desc;

    AuditStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static AuditStatusEnum parse(int status) {
        for (AuditStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }
}
