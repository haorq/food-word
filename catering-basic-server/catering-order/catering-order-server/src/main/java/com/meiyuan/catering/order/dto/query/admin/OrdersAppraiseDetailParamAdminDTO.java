package com.meiyuan.catering.order.dto.query.admin;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 订单评价表(CateringOrdersAppraise)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ApiModel("订单评价详情请求参数——后台")
public class OrdersAppraiseDetailParamAdminDTO extends BasePageDTO {

    @ApiModelProperty(value = "商户Id",required = true)
    @NotNull(message = "商户Id不能为空")
    private Long merchantId;
    @ApiModelProperty(value = "门店Id",required = true)
    @NotNull(message = "门店Id不能为空")
    private Long shopId;

}
