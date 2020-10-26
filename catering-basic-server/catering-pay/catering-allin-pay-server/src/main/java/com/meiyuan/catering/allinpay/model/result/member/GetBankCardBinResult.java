package com.meiyuan.catering.allinpay.model.result.member;

import com.meiyuan.catering.allinpay.model.bean.member.CardBinInfo;
import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/25 10:08
 * @since v1.1.0
 */
@Data
public class GetBankCardBinResult extends AllinPayBaseResponseResult {

    /**
     * 卡bin信息
     */
    private CardBinInfo cardBinInfo;
}
