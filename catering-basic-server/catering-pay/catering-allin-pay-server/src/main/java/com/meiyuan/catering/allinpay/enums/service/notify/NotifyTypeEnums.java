package com.meiyuan.catering.allinpay.enums.service.notify;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author zengzhangni
 * @date 2020/9/30 14:23
 * @since v1.1.0
 */
@Getter
public enum NotifyTypeEnums {

    /**
     *
     */
    ORDER_NOTIFY("订单结果通知", "allinpay.yunst.orderService.pay"),
    MEMBER_COMPANY_NOTIFY("企业信息审核结果通知", "allinpay.yunst.orderService.pay"),
    SIGN_CONTRACT_NOTIFY("会员电子协议签约", "allinpay.yunst.memberService.signContract"),
    WITHDRAW_APPLY_NOTIFY("提现申请", "allinpay.yunst.orderService.withdrawApply"),

    ;


    private String desc;
    private String type;


    NotifyTypeEnums(String desc, String type) {
        this.desc = desc;
        this.type = type;
    }

    public static NotifyTypeEnums match(String notifyType) {
        for (NotifyTypeEnums value : values()) {
            if (value.getType().equals(notifyType)) {
                return value;
            }
        }
        throw new CustomException("notifyType 不存在");

    }
}
