package com.meiyuan.catering.order.dao;

import com.meiyuan.catering.order.dto.order.OrderDeliveryStatusDto;
import com.meiyuan.catering.order.entity.CateringOrderDeliveryStatusHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author MeiTao
 * @create ${cfg.dateTime}
 * @describe mapper类
 */
@Mapper
public interface CateringOrderDeliveryStatusHistoryMapper
        extends BaseMapper<CateringOrderDeliveryStatusHistoryEntity> {


    /**
     * 查询订单配送状态记录（达达）
     *
     * @param orderIds
     * @return
     */
    List<CateringOrderDeliveryStatusHistoryEntity> listByOrderIds(@Param("list") List<Long> orderIds);

}
