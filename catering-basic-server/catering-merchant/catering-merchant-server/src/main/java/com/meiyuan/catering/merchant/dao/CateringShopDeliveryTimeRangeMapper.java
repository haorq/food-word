package com.meiyuan.catering.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.merchant.entity.CateringShopDeliveryTimeRangeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺配送时间范围(CateringShopDeliveryTimeRange)表数据库访问层
 *
 * @author meitao
 * @since 2020-03-16 11:50:43
 */
@Mapper
public interface CateringShopDeliveryTimeRangeMapper extends BaseMapper<CateringShopDeliveryTimeRangeEntity> {

}