package com.meiyuan.catering.order.dao;

import com.meiyuan.catering.order.entity.CateringOrdersDeliveryNoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author MeiTao
 * @create ${cfg.dateTime}
 * @describe 订单与第三方配送单号关联表mapper类
 */
@Mapper
public interface CateringOrdersDeliveryNoMapper extends BaseMapper<CateringOrdersDeliveryNoEntity> {

    /**
     * 更新订单达达配送违约金
     *
     * @param orderId
     * @param deductFee
     */
    void updateDeductFee(@Param("orderId") Long orderId, @Param("deductFee") BigDecimal deductFee);

    /**
     * 查询订单发单记录
     *
     * @param orderId
     * @return
     */
    List<CateringOrdersDeliveryNoEntity> listByOrderId(@Param("orderId") Long orderId);

}
