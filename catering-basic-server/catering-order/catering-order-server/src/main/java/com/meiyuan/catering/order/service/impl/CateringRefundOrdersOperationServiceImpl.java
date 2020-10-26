package com.meiyuan.catering.order.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.order.audit.WxRefundAuditVO;
import com.meiyuan.catering.order.dao.CateringRefundOrdersOperationMapper;
import com.meiyuan.catering.order.entity.CateringRefundOrdersOperationEntity;
import com.meiyuan.catering.order.enums.RefundOperationEnum;
import com.meiyuan.catering.order.service.CateringRefundOrdersOperationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020-03-31
 */
@Service
public class CateringRefundOrdersOperationServiceImpl extends ServiceImpl<CateringRefundOrdersOperationMapper, CateringRefundOrdersOperationEntity> implements CateringRefundOrdersOperationService {


    @Override
    public Boolean saveOperation(Long orderId, Long refundId, RefundOperationEnum operationEnum, String remarks) {
        CateringRefundOrdersOperationEntity operationEntity = new CateringRefundOrdersOperationEntity();
        operationEntity.setOrderId(orderId);
        operationEntity.setRefundOrderId(refundId);
        operationEntity.setRemarks(remarks);
        operationEntity.setRefundOperation(operationEnum.getCode());
        return save(operationEntity);
    }

    @Override
    public List<WxRefundAuditVO> refundAuditList(Long orderId) {
        LambdaQueryWrapper<CateringRefundOrdersOperationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringRefundOrdersOperationEntity::getOrderId, orderId);
        List<CateringRefundOrdersOperationEntity> list = list(wrapper);
        return list.stream().map(operation -> {
            WxRefundAuditVO auditVO = new WxRefundAuditVO();
            auditVO.setRefundOperation(operation.getRefundOperation());
            auditVO.setAuditTime(operation.getCreateTime());
            auditVO.setRemark(operation.getRemarks());
            return auditVO;
        }).collect(Collectors.toList());

    }
}

