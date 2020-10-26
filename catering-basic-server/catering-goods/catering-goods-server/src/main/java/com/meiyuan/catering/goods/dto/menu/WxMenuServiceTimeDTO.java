package com.meiyuan.catering.goods.dto.menu;

import com.meiyuan.catering.core.dto.base.TimeRangeDTO;
import com.meiyuan.catering.merchant.vo.merchant.MerchantInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/31 18:08
 * @description 简单描述
 **/
@Data
@ApiModel("微信菜单送达时间数据模型")
public class WxMenuServiceTimeDTO {
    @ApiModelProperty("送达时间日期")
    private List<String> serviceTime;
    @ApiModelProperty("商户信息")
    private MerchantInfoVo merchantInfo;
    @ApiModelProperty("配送价格")
    private BigDecimal deliveryPrice;
    @ApiModelProperty("满单免配送金额")
    private BigDecimal freeDeliveryPrice;
    @ApiModelProperty("订单起送金额")
    private BigDecimal leastDeliveryPrice;
    @ApiModelProperty("配送时间范围")
    private List<TimeRangeDTO> deliveryTimeRanges;
    @ApiModelProperty("自提时间范围")
    private List<TimeRangeDTO> pickupTimeRanges;
    @ApiModelProperty("立即送达时间【1.5.0】")
    private List<String> nowDeliveryTime;
}
