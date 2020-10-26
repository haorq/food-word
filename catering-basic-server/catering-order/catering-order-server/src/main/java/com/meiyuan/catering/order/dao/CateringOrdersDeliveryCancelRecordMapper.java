package com.meiyuan.catering.order.dao;

import com.meiyuan.catering.order.dto.order.OrderDeliveryStatusDto;
import com.meiyuan.catering.order.entity.CateringOrdersDeliveryCancelRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/10/12 0012 11:16
 * @Description 简单描述 :
 * @Since version-1.5.0
 */
@Mapper
public interface CateringOrdersDeliveryCancelRecordMapper extends BaseMapper<CateringOrdersDeliveryCancelRecordEntity> {


    /**
     * 商家处理骑手取消订单的操作
     *
     * @param orderId
     * @param dealRet 1：同意取消。2：拒绝取消
     */
    void dealDadaCancelOrder(@Param("orderId") Long orderId, @Param("dealRet") Integer dealRet);


    /**
     * 查询待处理取消订单
     *
     * @param shopId
     * @return
     */
    List<OrderDeliveryStatusDto> listWaitToDeal(@Param("shopId") Long shopId);

    /**
     * 查询取消订单待处理的记录
     *
     * @param orderId
     * @return
     */
    List<CateringOrdersDeliveryCancelRecordEntity> listWaitDealByOrderId(@Param("orderId") Long orderId);
}
