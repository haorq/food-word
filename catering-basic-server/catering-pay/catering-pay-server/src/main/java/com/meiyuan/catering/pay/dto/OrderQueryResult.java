package com.meiyuan.catering.pay.dto;

import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/4/11
 */
@Data
@Builder
@ApiModel("订单查询结果")
public class OrderQueryResult {

    /**
     * 微信订单查询结果
     */
    private WxPayOrderQueryResult wxPayOrderResult;
}
