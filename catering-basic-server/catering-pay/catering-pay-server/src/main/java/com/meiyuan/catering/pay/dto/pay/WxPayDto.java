package com.meiyuan.catering.pay.dto.pay;

import com.meiyuan.catering.allinpay.enums.service.order.PayMethodKeyEnums;
import com.meiyuan.catering.allinpay.model.bean.order.AllinReciever;
import com.meiyuan.catering.pay.dto.BasePayDto;
import com.meiyuan.catering.pay.enums.NotifyEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/4/2
 */
@Data
public class WxPayDto extends BasePayDto {

    private String orderNumber;
    private String openId;
    private String ip;
    private String body;
    private String attach;
    private String subAppid;
    private BigDecimal fee;
    private NotifyEnum notifyEnum;
    private PayMethodKeyEnums payMethod;
    /**
     * 分账者
     */
    private List<AllinReciever> receivers;

    public void addProfitReceivers(AllinReciever receiver) {
        if (receivers == null) {
            receivers = new ArrayList<>();
        }
        receivers.add(receiver);
    }
}
