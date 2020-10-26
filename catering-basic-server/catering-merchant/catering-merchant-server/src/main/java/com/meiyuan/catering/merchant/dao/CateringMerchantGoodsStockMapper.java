package com.meiyuan.catering.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.merchant.entity.CateringMerchantGoodsStockEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商户商品库存表(CateringMerchantGoodsStock)表数据库访问层
 *
 * @author meitao
 * @since 2020-03-16 11:46:31
 */
@Mapper
public interface CateringMerchantGoodsStockMapper extends BaseMapper<CateringMerchantGoodsStockEntity> {

}