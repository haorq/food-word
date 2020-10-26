package com.meiyuan.catering.core.util.dada.domain.message;

import lombok.Data;

/**
 * 达达消息确认消息体
 *
 * @author lh
 */
@Data
public class MessageConfirmBodyModel {
    private String orderId;
    private String dadaOrderId;
    private Integer isConfirm;
    private String cancelReason;
}
