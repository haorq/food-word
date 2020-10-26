package com.meiyuan.catering.order.service.impl;

import com.meiyuan.catering.order.entity.CateringOrdersDeliveryNoEntity;
import com.meiyuan.catering.order.dao.CateringOrdersDeliveryNoMapper;
import com.meiyuan.catering.order.service.CateringOrdersDeliveryNoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @auther MeiTao
 * @create ${cfg.dateTime}
 * @describe 订单与第三方配送单号关联表服务实现类
 */
@Service
public class CateringOrdersDeliveryNoServiceImpl extends ServiceImpl<CateringOrdersDeliveryNoMapper, CateringOrdersDeliveryNoEntity>
        implements CateringOrdersDeliveryNoService {

}
