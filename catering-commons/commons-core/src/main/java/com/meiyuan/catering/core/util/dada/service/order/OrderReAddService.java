package com.meiyuan.catering.core.util.dada.service.order;


import com.meiyuan.catering.core.util.dada.config.UrlConstant;
import com.meiyuan.catering.core.util.dada.service.BaseService;

/**
 * 重发订单
 * DATE: 18/9/3
 *
 * @author: wan
 */
public class OrderReAddService extends BaseService {

    public OrderReAddService(String params){
        super(UrlConstant.ORDER_RE_ADD_URL, params);
    }


}
