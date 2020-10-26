package com.meiyuan.catering.order.dto.query.admin;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单评价表(CateringOrdersAppraise)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ApiModel("订单评价列表请求参数——后台")
public class OrdersAppraiseParamAdminDTO extends BasePageDTO {

    @ApiModelProperty("门店名称")
    private String storeName;

}
