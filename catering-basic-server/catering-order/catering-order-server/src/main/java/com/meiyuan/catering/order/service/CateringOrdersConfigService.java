package com.meiyuan.catering.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.order.dto.query.admin.OrdersConfigDTO;
import com.meiyuan.catering.order.entity.CateringOrdersConfigEntity;

import java.util.List;

/**
 * 订单配置表(CateringOrdersConfig)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:03
 */
public interface CateringOrdersConfigService extends IService<CateringOrdersConfigEntity> {
    /**
     * 功能描述: 后台——获取订单配置信息
     *
     * @return: 后台订单详情
     */
    List<OrdersConfigDTO> selectList();

    /**
     * 描述:后台——修改订单配置信息
     *
     * @param configDtoS
     * @return java.lang.String 后台订单详情
     * @author zengzhangni
     * @date 2020/6/24 10:45
     * @since v1.1.1
     */
    String updateConfig(List<OrdersConfigDTO> configDtoS);
}
