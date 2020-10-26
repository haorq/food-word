package com.meiyuan.catering.order.dto.order;

import com.meiyuan.catering.order.entity.CateringOrdersRefundEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 订单退款信息 v1.4.0
 *
 * @author lh
 */
@Data
public class OrderRefundDto extends CateringOrdersRefundEntity {

    /**
     * 货物状态.1:已收到货 2:未收到货
     */
    @ApiModelProperty("货物状态.1:已收到货 2:未收到货")
    private Integer cargoStatus;
    /**
     * 货物状态.
     */
    @ApiModelProperty("货物状态")
    private String cargoStatusDesc;
    /**
     * 退款原因 1:包装损坏 2:商品质量问题 3:未按约定时间送达
     */
    @ApiModelProperty("退款原因 1:包装损坏 2:商品质量问题 3:未按约定时间送达")
    private String refundReason;

    @ApiModelProperty("退款原因（解析后的值，直接展示）")
    private String refundReasonDesc;

    @ApiModelProperty("退款说明")
    private String refundRemark;

    /**
     * 退款原因
     */
    @ApiModelProperty("退款原因")
    private List<String> refundReasonList;


    @ApiModelProperty("退款方式")
    private String refundWayDesc;

    /**
     * 退款凭证
     */
    @ApiModelProperty("退款凭证")
    private List<String> refundEvidenceList;

    /**
     * 退款类型
     */
    @ApiModelProperty("退款类型")
    private String refundTypeDesc;
    /**
     * 退款状态
     */
    @ApiModelProperty("退款状态")
    private String refundStatusDesc;

}
