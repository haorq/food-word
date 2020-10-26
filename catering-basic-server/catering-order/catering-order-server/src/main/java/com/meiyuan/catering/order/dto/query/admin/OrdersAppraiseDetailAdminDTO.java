package com.meiyuan.catering.order.dto.query.admin;

import com.meiyuan.catering.core.page.PageData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单评价详情信息
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ApiModel("订单评价详情信息——后台")
public class OrdersAppraiseDetailAdminDTO {
    @ApiModelProperty("门店总体评价信息")
    OrdersAppraiseAdminDTO ordersAppraise;

    @ApiModelProperty("门店评价列表信息")
    PageData<OrdersAppraiseDetailListAdminDTO> ordersAppraiseList;
}
