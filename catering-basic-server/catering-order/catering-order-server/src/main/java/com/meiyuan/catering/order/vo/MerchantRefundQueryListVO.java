package com.meiyuan.catering.order.vo;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/3/24
 */
@Data
@ApiModel("商户退款列表结果VO")
public class MerchantRefundQueryListVO extends IdEntity {

    @ApiModelProperty("取餐方式（1：外卖配送，2：到店自取）")
    private String deliveryWay;
    @ApiModelProperty("时间段")
    private String time;
    @ApiModelProperty("退款人")
    private String userName;
    @ApiModelProperty("退款人手机")
    private String userPhone;

    @ApiModelProperty("下单时间")
    private LocalDateTime orderCreateTime;
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("订单金额")
    private String paidAmount;

    @ApiModelProperty("退款原因")
    private String refundReason;
    @ApiModelProperty("退款状态（1：待退款；2：退款成功；3退款失败）")
    private Integer refundStatus;
}
