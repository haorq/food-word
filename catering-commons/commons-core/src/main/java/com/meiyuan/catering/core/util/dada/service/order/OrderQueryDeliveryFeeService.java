package com.meiyuan.catering.core.util.dada.service.order;


import com.meiyuan.catering.core.util.dada.config.UrlConstant;
import com.meiyuan.catering.core.util.dada.service.BaseService;

/**
 * DATE: 18/9/3
 *
 * @author: wan
 */
public class OrderQueryDeliveryFeeService extends BaseService {

    public OrderQueryDeliveryFeeService(String params){
        super(UrlConstant.ORDER_QUERY_DELIVERY_FEE_URL, params);
    }
}
