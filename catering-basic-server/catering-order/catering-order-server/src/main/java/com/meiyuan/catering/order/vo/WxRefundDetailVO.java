package com.meiyuan.catering.order.vo;

import com.meiyuan.catering.core.entity.IdEntity;
import com.meiyuan.catering.order.dto.query.wx.RefundOrdersListGoodsWxDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/3/24
 */
@Data
@ApiModel("wx-退款详情结果VO")
public class WxRefundDetailVO extends IdEntity {

    @ApiModelProperty("退款审核状态（1：待审核，2：通过；3：拒绝）")
    private Integer refundAuditStatus;
    @ApiModelProperty("退款方式（1：原路退回）")
    private Integer refundWay;
    @ApiModelProperty("退款总金额")
    private BigDecimal refundTotalAmount;

    @ApiModelProperty("商品信息")
    List<RefundOrdersListGoodsWxDTO> goodsInfo;

    @ApiModelProperty("退款原因")
    private List<Integer> refundReason;
    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;
    @ApiModelProperty("申请时间")
    private LocalDateTime applyTime;
    @ApiModelProperty("退款单号")
    private String refundNumber;

}
