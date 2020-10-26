package com.meiyuan.catering.allinpay.model.result.member;

import com.meiyuan.catering.allinpay.model.bean.member.BindCard;
import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/9/25 11:45
 * @since v1.1.0
 */
@Data
public class QueryBankCardResult extends AllinPayBaseResponseResult {

    private List<BindCard> bindCardList;

}
