package com.meiyuan.catering.core.util.dada.domain.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiyuan.catering.core.util.dada.domain.BaseModel;

public class OrderAfterQueryMode extends BaseModel {
    @JSONField(name = "deliveryNo")
    private String deliveryNo;

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }
}
