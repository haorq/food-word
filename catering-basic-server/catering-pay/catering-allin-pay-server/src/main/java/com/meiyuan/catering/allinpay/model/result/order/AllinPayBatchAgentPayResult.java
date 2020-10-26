package com.meiyuan.catering.allinpay.model.result.order;

import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/27 15:09
 * @description 通联批量托管代付（标准版）返回值
 **/

@Data
public class AllinPayBatchAgentPayResult {

    /**
     * 商户批次号
     *
     * 必返
     */
    private String bizBatchNo;

}
