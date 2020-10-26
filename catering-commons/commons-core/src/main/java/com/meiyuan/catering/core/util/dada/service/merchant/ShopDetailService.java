package com.meiyuan.catering.core.util.dada.service.merchant;


import com.meiyuan.catering.core.util.dada.config.UrlConstant;
import com.meiyuan.catering.core.util.dada.service.BaseService;

/**
 * DATE: 18/9/4
 *
 * @author: wan
 */
public class ShopDetailService extends BaseService {

    public ShopDetailService(String params){
        super(UrlConstant.SHOP_DETAIL_URL, params);
    }
}
