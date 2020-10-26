package com.meiyuan.catering.allinpay.model.result.member;

import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/25 11:34
 * @since v1.1.0
 */
@Data
public class BindBankCardResult extends AllinPayBaseResponseResult {

    /**
     * 流水号
     */
    private String tranceNum;
    /**
     * 申请时间 	YYYYMMDD
     */
    private String transDate;


}
