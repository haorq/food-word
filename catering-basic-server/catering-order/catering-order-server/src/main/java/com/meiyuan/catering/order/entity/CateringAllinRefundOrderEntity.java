package com.meiyuan.catering.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("catering_allin_refund_order")
public class CateringAllinRefundOrderEntity extends IdEntity {

    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 系统交易流水编号
     */
    private String tradingFlow;
    /**
     * 第三方交易流水编号
     */
    private String thirdTradeNumber;
    /**
     * 退款单号
     */
    private String refundNo;
    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 下单客户ID
     */
    private Long memberId;
    /**
     * 退款金额(分)
     */
    private Long amount;

    /**
     * 退款来源
     */
    private Integer source;
    /**
     * 确认退款状态(1:待确认  2.退款成功  3.退款失败)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
