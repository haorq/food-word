package com.meiyuan.catering.core.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author luohuan
 * @date 2020/4/2
 **/
@Data
@ApiModel("用户退出拼团DTO")
public class GrouponMemberQuitDTO {
    @ApiModelProperty("营销商品表主键id")
    private Long mGoodsId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("商品购买数量")
    private Integer goodsNumber;

    @ApiModelProperty("订单ID V1.3.0")
    private Long orderId;
}
