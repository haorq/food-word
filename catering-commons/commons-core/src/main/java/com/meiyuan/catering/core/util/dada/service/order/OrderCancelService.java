package com.meiyuan.catering.core.util.dada.service.order;

import com.meiyuan.catering.core.util.dada.config.UrlConstant;
import com.meiyuan.catering.core.util.dada.service.BaseService;

/**
 * 取消达达订单
 *
 * @author lh
 */
public class OrderCancelService extends BaseService {
    public OrderCancelService(String params) {
        super(UrlConstant.ORDER_CANCEL_URL, params);
    }
}
