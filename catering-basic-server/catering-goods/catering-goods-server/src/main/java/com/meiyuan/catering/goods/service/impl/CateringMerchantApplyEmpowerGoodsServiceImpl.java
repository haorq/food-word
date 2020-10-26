package com.meiyuan.catering.goods.service.impl;

import com.meiyuan.catering.goods.service.CateringMerchantApplyEmpowerGoodsService;
import com.meiyuan.catering.goods.dao.CateringMerchantApplyEmpowerGoodsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 商户申请授权商品表(CateringMerchantApplyEmpowerGoods)表服务实现类
 *
 * @author wxf
 * @since 2020-03-09 18:15:26
 */
@Service
public class CateringMerchantApplyEmpowerGoodsServiceImpl implements CateringMerchantApplyEmpowerGoodsService {
    @Resource
    private CateringMerchantApplyEmpowerGoodsMapper cateringMerchantApplyEmpowerGoodsMapper;

    
}