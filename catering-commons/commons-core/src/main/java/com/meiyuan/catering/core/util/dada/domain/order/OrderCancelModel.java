package com.meiyuan.catering.core.util.dada.domain.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiyuan.catering.core.util.dada.domain.BaseModel;
import lombok.Data;

/**
 * 订单取消
 *
 * @author lh
 */
@Data
public class OrderCancelModel extends BaseModel {
    @JSONField(name = "order_id")
    private String orderId;

    @JSONField(name = "cancel_reason_id")
    private String cancelReasonId;

    @JSONField(name = "cancel_reason")
    private String cancelReason;


    public OrderCancelModel() {
    }

    public OrderCancelModel(String orderId, String cancelReasonId, String cancelReason) {
        this.orderId = orderId;
        this.cancelReasonId = cancelReasonId;
        this.cancelReason = cancelReason;
    }
}
