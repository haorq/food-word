package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.order.dao.CateringOrdersOperationMapper;
import com.meiyuan.catering.order.dto.query.wx.CateringOrdersOperationDTO;
import com.meiyuan.catering.order.entity.CateringOrdersOperationEntity;
import com.meiyuan.catering.order.enums.OrderOperationEnum;
import com.meiyuan.catering.order.enums.OrderOperationTypeEnum;
import com.meiyuan.catering.order.service.CateringOrdersOperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * operation_(CateringOrdersOperation)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:41
 */
@Service("cateringOrdersOperationService")
public class CateringOrdersOperationServiceImpl extends ServiceImpl<CateringOrdersOperationMapper, CateringOrdersOperationEntity> implements CateringOrdersOperationService {
    @Resource
    private CateringOrdersOperationMapper cateringOrdersOperationMapper;


    /**
     * 订单进度
     *
     * @return 订单进度
     */
    @Override
    public List<CateringOrdersOperationDTO> progress(Long orderId) {
        return this.cateringOrdersOperationMapper.progress(orderId);
    }

    @Override
    public void asyncSaveOperation(Order order, String explain, OrderOperationEnum operationEnum, OrderOperationTypeEnum typeEnum) {
        CateringOrdersOperationEntity cateringOrdersOperationEntity = new CateringOrdersOperationEntity();
        cateringOrdersOperationEntity.setOrderId(order.getId());
        cateringOrdersOperationEntity.setOrderNumber(order.getOrderNumber());
        cateringOrdersOperationEntity.setOperationPhase(operationEnum.getCode());
        cateringOrdersOperationEntity.setOperationTime(LocalDateTime.now());
        cateringOrdersOperationEntity.setOperationType(typeEnum.value());
        cateringOrdersOperationEntity.setOperationId(order.getMemberId());
        cateringOrdersOperationEntity.setOperationName(order.getMemberName());
        cateringOrdersOperationEntity.setOperationPhone(order.getMemberPhone());
        cateringOrdersOperationEntity.setOperationExplain(explain);
        baseMapper.insert(cateringOrdersOperationEntity);
    }

    @Override
    public void saveOperation(Order order, String explain, OrderOperationEnum operationEnum, OrderOperationTypeEnum typeEnum) {
        asyncSaveOperation(order, explain, operationEnum, typeEnum);
    }
}
