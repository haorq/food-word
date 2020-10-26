package com.meiyuan.catering.merchant.service.impl;

import javax.annotation.Resource;

import com.meiyuan.catering.merchant.dao.CateringMerchantGoodsStockMapper;
import com.meiyuan.catering.merchant.service.CateringMerchantGoodsStockService;
import org.springframework.stereotype.Service;

/**
 * 商户商品库存表(CateringMerchantGoodsStock)表服务实现类
 *
 * @author meitao
 * @since 2020-03-16 11:46:31
 */
@Service("cateringMerchantGoodsStockService")
public class CateringMerchantGoodsStockServiceImpl implements CateringMerchantGoodsStockService {
    @Resource
    private CateringMerchantGoodsStockMapper cateringMerchantGoodsStockMapper;
}