package com.meiyuan.catering.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.order.entity.CateringOrdersShopDebtEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/10/13 11:10
 * @description 商家负债表数据库操作Mapper
 **/

@Mapper
public interface CateringOrdersShopDebtMapper extends BaseMapper<CateringOrdersShopDebtEntity> {

    /**
     * 描述:门店负债金额累加
     *
     * @param shopId
     * @param debtAmount
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/13 14:49
     * @since v1.5.0
     */
    Boolean accumulate(@Param("shopId") Long shopId, @Param("debtAmount") BigDecimal debtAmount);


    /**
     * 描述:减去负债金额
     *
     * @param shopId
     * @param debtAmount
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/13 15:14
     * @since v1.5.0
     */
    Boolean subtractDebtAmount(Long shopId, BigDecimal debtAmount);
}
