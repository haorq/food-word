package com.meiyuan.catering.order.dto.query.merchant;

import com.meiyuan.catering.core.page.PageData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单列表信息包含订单总金额——商户端
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单列表信息包含订单总金额——商户端")
public class OrdersListAmountMerchantDTO {
    @ApiModelProperty("订单总金额")
    BigDecimal totalAmount;
    @ApiModelProperty("订单集合数据")
    PageData<OrdersListMerchantDTO> orderList;
}
