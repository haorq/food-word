package com.meiyuan.catering.merchant.dto.shop.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/9/3 0003 16:51
 * @Description 简单描述 :
 * @Since version-1.3.0
 */
@Data
public class OrderNoticeInfoDTO{

        @ApiModelProperty(value = "店铺id")
        private Long shopId;

        @ApiModelProperty(value = "自提点id")
        private Long pickupId;

        @ApiModelProperty(value = "订单号")
        private Long orderId;

}







