package com.meiyuan.catering.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.order.entity.CateringOrderDeliveryStatusHistoryEntity;

/**
 * @author lh
 */
public interface OrderDeliveryStatusService
        extends IService<CateringOrderDeliveryStatusHistoryEntity> {


    /**
     * 达达订单预发布（分两步1：查询运费，2：发单），查询运费 author lh 20200930
     *
     * @param orderId
     * @return
     */
    public Object dadaQueryDeliveryFee(Long orderId);

    /**
     * 达达订单预发布（分两步1：查询运费，2：发单），查询运费 author lh 20200930
     *
     * @param deliveryNo 达达订单编号，第一步查询运费后获取
     * @return
     */
    public Object addAfterQuery(String deliveryNo);

    /**
     * 直接发单到达达
     *
     * @param orderId 业务系统订单ID
     * @return
     */
    public Object addOrder(Long orderId);

}
