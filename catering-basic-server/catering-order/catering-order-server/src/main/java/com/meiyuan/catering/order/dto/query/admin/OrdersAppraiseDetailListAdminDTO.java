package com.meiyuan.catering.order.dto.query.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 门店评价列表信息
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ApiModel("门店评价列表信息——后台")
public class OrdersAppraiseDetailListAdminDTO {

    @ApiModelProperty("评价时间")
    private LocalDateTime appraiseTime;
    @ApiModelProperty("评价用户")
    private String userNickname;
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("商品详情")
    private List<OrdersAppraiseDetailGoodsDTO> goodsDetails;
    @ApiModelProperty("评价状态（1：好评，2：中评，3：差评）")
    private Integer status;
    @ApiModelProperty("评价内容")
    private String content;
    @ApiModelProperty("商家回复")
    private String reply;

}
