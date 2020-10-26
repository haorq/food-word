package com.meiyuan.catering.allinpay.enums.service.member;

import lombok.Getter;

/**
 * @author zengzhangni
 * @date 2020/9/29 18:11
 * @since v1.1.0
 */
@Getter
public enum CardCheckEnums {

    /**
     *
     */
    CARD_CHECK_1(1L, "通联通账户实名验证(三要素)"),
    CARD_CHECK_3(3L, "通联通实名付"),
    CARD_CHECK_5(5L, "通联通账户实名验证(四要素)"),
    CARD_CHECK_6(6L, "通联通协议支付签约——支持通联通协议支付"),
    CARD_CHECK_7(7L, "收银宝快捷支付签约（有银行范围）——支持收银宝快捷支付"),
    CARD_CHECK_8(8L, "银行卡四要素验证（万鉴通，全部银行）"),
    CARD_CHECK_99(99L, "后台绑定银行卡"),
    ;

    private Long type;
    private String desc;

    CardCheckEnums(Long type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
