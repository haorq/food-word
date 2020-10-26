package com.meiyuan.catering.allinpay.model.param.order;

import com.meiyuan.catering.allinpay.model.bean.order.BatchPay;
import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/09/27 15:09
 * @description 通联批量托管代付（标准版）参数
 **/

@Data
@Builder
public class AllinPayBatchAgentPayParams extends AllinPayBaseServiceParams {

    /**
     * 商户批次号
     * <p>
     * 必填
     */
    private String bizBatchNo;
    /**
     * 批量代付列表
     * <p>
     * 最多支持200笔
     * <p>
     * 必填
     */
    private List<BatchPay> batchPayList;
    /**
     * 商品类型
     * <p>
     * 非必填
     */
    private Long goodsType;
    /**
     * 商户系统商品编号
     * 商家录入商品后，发起交易时可上送商品编号
     * <p>
     * 非必填
     */
    private String bizGoodsNo;
    /**
     * 业务码
     * <p>
     * 必填
     */
    private String tradeCode;

}
