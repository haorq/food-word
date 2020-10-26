package com.meiyuan.catering.order.dto.order;

import com.meiyuan.catering.order.entity.CateringOrderDeliveryStatusHistoryEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lh
 */
@Data
public class OrderDeliveryStatusDto extends CateringOrderDeliveryStatusHistoryEntity {
    @ApiModelProperty(value = "配送状态中文描述")
    private String orderStatusDesc;
}
