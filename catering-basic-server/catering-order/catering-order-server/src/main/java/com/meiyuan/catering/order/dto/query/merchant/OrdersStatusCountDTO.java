package com.meiyuan.catering.order.dto.query.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单查询状态数量统计——商户
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单查询状态数量统计——商户")
public class OrdersStatusCountDTO {
    @ApiModelProperty("未完成")
    private Integer unfinished;
    @ApiModelProperty("已完成")
    private Integer finished;
    @ApiModelProperty("已取消")
    private Integer cancel;
    @ApiModelProperty("已失效")
    private Integer failure;
    @ApiModelProperty("待退款")
    private Integer unRefund;
    @ApiModelProperty("已退款")
    private Integer refund;
}
