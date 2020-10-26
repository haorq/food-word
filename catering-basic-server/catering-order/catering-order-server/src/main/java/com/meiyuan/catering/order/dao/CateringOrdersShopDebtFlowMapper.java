package com.meiyuan.catering.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.order.entity.CateringOrdersShopDebtFlowEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author GongJunZheng
 * @date 2020/10/13 11:10
 * @description 商家负债明细表数据库操作Mapper
 **/

@Mapper
public interface CateringOrdersShopDebtFlowMapper extends BaseMapper<CateringOrdersShopDebtFlowEntity> {

    CateringOrdersShopDebtFlowEntity queryRollBackDebt(@Param("orderId") Long orderId);
}
