package com.meiyuan.catering.core.util.dada.domain.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiyuan.catering.core.util.dada.domain.BaseModel;
import lombok.Data;

/**
 * 达达妥投异常之物品返回完成
 *
 * @author lh
 */
@Data
public class OrderConfirmGoodsModel extends BaseModel {

    @JSONField(name = "order_id")
    private String orderId;
}
