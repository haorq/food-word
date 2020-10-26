package com.meiyuan.catering.core.util.dada.domain.order;


import com.alibaba.fastjson.annotation.JSONField;
import com.meiyuan.catering.core.util.dada.domain.BaseModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 重发订单
 * DATE: 18/9/2
 *
 * @author: wan
 */
@Data
public class OrderReAddModel extends BaseModel {

    @JSONField(name = "shop_no")
    private String shopNo;

    @JSONField(name = "origin_id")
    private String originId;

    @JSONField(name = "city_code")
    private String cityCode;

    @JSONField(name = "cargo_price")
    private BigDecimal cargoPrice;

    @JSONField(name = "is_prepay")
    private Integer isPrepay;

    @JSONField(name = "receiver_name")
    private String receiverName;

    @JSONField(name = "receiver_address")
    private String receiverAddress;

    @JSONField(name = "receiver_lat")
    private BigDecimal receiverLat;

    @JSONField(name = "receiver_lng")
    private BigDecimal receiverLng;

    private String callback;

    @JSONField(name = "receiver_phone")
    private String receiverPhone;

}
