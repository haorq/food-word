package com.meiyuan.catering.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.order.dto.query.wx.CateringOrdersOperationDTO;
import com.meiyuan.catering.order.entity.CateringOrdersOperationEntity;
import com.meiyuan.catering.order.enums.OrderOperationEnum;
import com.meiyuan.catering.order.enums.OrderOperationTypeEnum;

import java.util.List;

/**
 * operation_(CateringOrdersOperation)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:03
 */
public interface CateringOrdersOperationService extends IService<CateringOrdersOperationEntity> {

    /**
     * 描述:订单进度
     *
     * @param orderId
     * @return java.util.List<com.meiyuan.catering.order.dto.query.wx.CateringOrdersOperationDTO>
     * @author zengzhangni
     * @date 2020/6/24 10:50
     * @since v1.1.1
     */
    List<CateringOrdersOperationDTO> progress(Long orderId);


    /**
     * 描述:异步添加退款进度
     *
     * @param order
     * @param explain
     * @param operationEnum
     * @param typeEnum
     * @return void
     * @author zengzhangni
     * @date 2020/4/10 15:49
     */
    void asyncSaveOperation(Order order, String explain, OrderOperationEnum operationEnum, OrderOperationTypeEnum typeEnum);

    /**
     * 描述:同步添加
     *
     * @param order
     * @param explain
     * @param operationEnum
     * @param typeEnum
     * @return void
     * @author zengzhangni
     * @date 2020/4/10 16:17
     */
    void saveOperation(Order order, String explain, OrderOperationEnum operationEnum, OrderOperationTypeEnum typeEnum);

}
