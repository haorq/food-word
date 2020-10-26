package com.meiyuan.catering.order.dto.query.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiModel("门店总体评价信息——后台")
public class OrdersAppraiseAdminDTO {

    @ApiModelProperty("商户Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("门店Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long storeId;
    @ApiModelProperty("门店名称")
    private String storeName;
    @ApiModelProperty("收到评价数")
    private Long appraiseNumber;
    @ApiModelProperty("好评数量")
    private Long goodNumber;
    @ApiModelProperty("中评数量")
    private Long generalNumber;
    @ApiModelProperty("差评数量")
    private Long badNumber;

}
