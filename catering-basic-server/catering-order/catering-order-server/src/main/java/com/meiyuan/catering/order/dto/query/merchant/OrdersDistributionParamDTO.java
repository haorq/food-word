package com.meiyuan.catering.order.dto.query.merchant;

import com.meiyuan.catering.order.dto.MerchantBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单分布情况请求参数——商户
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单分布情况请求参数——商户")
public class OrdersDistributionParamDTO extends MerchantBaseDTO {
    @ApiModelProperty("月份：yyyy-mm")
    private String estimateTime;
}
