package com.meiyuan.catering.core.util.dada.service.order;


import com.meiyuan.catering.core.util.dada.config.UrlConstant;
import com.meiyuan.catering.core.util.dada.service.BaseService;

/**
 * DATE: 18/9/3
 *
 * @author: wan
 */
public class OrderAddAfterQueryService extends BaseService {

    public OrderAddAfterQueryService(String params){
        super(UrlConstant.ORDER_ADD_AFTER_QUERY_URL, params);
    }
}
