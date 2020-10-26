package com.meiyuan.catering.allinpay.model.bean.member;

import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/25 10:06
 * @since v1.1.0
 */
@Data
public class CardBinInfo {

    /**
     * 卡bin
     */
    private String cardBin;
    /**
     * 卡种
     */
    private Long cardType;
    /**
     * 发卡行代码
     */
    private String bankCode;
    /**
     * 发卡行名称
     */
    private String bankName;
    /**
     * 卡名
     */
    private String cardName;
    /**
     * 卡片长度
     */
    private Long cardLenth;
    /**
     * 状态（1：有效；0：无效）
     */
    private Long cardState;
    /**
     * 卡种名称
     */
    private String cardTypeLabel;
}
