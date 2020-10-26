package com.meiyuan.catering.core.util.dada.service.order;

import com.meiyuan.catering.core.util.dada.config.UrlConstant;
import com.meiyuan.catering.core.util.dada.service.BaseService;

/**
 * 妥投异常之物品返回完成
 *
 * @author lh
 */
public class OrderConfirmGoodsService extends BaseService {
    public OrderConfirmGoodsService(String params) {
        super(UrlConstant.ORDER_CONFIRM_GOODS, params);
    }
}
