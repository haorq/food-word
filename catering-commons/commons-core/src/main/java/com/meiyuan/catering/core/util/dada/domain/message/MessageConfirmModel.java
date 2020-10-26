package com.meiyuan.catering.core.util.dada.domain.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiyuan.catering.core.util.dada.domain.BaseModel;
import lombok.Data;

/**
 * 达达骑手取消订单，消息确认
 *
 * @author lh
 */
@Data
public class MessageConfirmModel extends BaseModel {

    private String messageBody;
    private Integer messageType;

}
