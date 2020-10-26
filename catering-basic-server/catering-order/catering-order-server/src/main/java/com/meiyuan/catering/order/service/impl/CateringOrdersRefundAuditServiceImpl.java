package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.order.audit.OrdersRefundAudit;
import com.meiyuan.catering.core.dto.pay.wx.RefundDTO;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.order.dao.CateringOrdersRefundAuditMapper;
import com.meiyuan.catering.order.entity.CateringOrdersRefundAuditEntity;
import com.meiyuan.catering.order.enums.AuditStatusEnum;
import com.meiyuan.catering.order.service.CateringOrdersRefundAuditService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 退款订单审核表(CateringOrdersRefundAudit)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:41
 */
@Service("cateringOrdersRefundAuditService")
public class CateringOrdersRefundAuditServiceImpl extends ServiceImpl<CateringOrdersRefundAuditMapper, CateringOrdersRefundAuditEntity> implements CateringOrdersRefundAuditService {

    @Override
    public List<OrdersRefundAudit> getByRefundId(Long id) {
        LambdaQueryWrapper<CateringOrdersRefundAuditEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringOrdersRefundAuditEntity::getRefundId, id);
        return BaseUtil.objToObj(baseMapper.selectList(wrapper), OrdersRefundAudit.class);
    }

    @Override
    public OrdersRefundAudit getOneByRefundId(Long id) {
        return getByRefundId(id).get(0);
    }

    @Override
    public Boolean saveAudit(Long shopId, String shopName, String refundNo, Long refundId, RefundDTO refundDTO) {
        CateringOrdersRefundAuditEntity auditEntity = new CateringOrdersRefundAuditEntity();
        auditEntity.setRefundId(refundId);
        auditEntity.setRefundNumber(refundNo);
        auditEntity.setRefundStartTime(LocalDateTime.now());
        auditEntity.setRefundReason(refundDTO.getRefundReason());
        auditEntity.setCargoStatus(refundDTO.getCargoStatus());
        auditEntity.setRefundRemark(refundDTO.getRefundRemark());
        auditEntity.setRefundEvidence(refundDTO.getRefundEvidence());
        auditEntity.setBusinessId(shopId);
        auditEntity.setBusinessName(shopName);
        return save(auditEntity);
    }

    @Override
    public Boolean updateAudit(Long shopId, Long refundId, AuditStatusEnum auditStatusEnum, String auditExplain, String shopName) {
        LambdaUpdateWrapper<CateringOrdersRefundAuditEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CateringOrdersRefundAuditEntity::getBusinessId, shopId)
                .set(CateringOrdersRefundAuditEntity::getBusinessName, shopName)
                .set(CateringOrdersRefundAuditEntity::getBusinessAuditStatus, auditStatusEnum.getStatus())
                .set(CateringOrdersRefundAuditEntity::getBusinessAuditExplain, auditExplain)
                .set(CateringOrdersRefundAuditEntity::getBusinessAuditTime, LocalDateTime.now())
                .eq(CateringOrdersRefundAuditEntity::getRefundId, refundId);
        return update(updateWrapper);
    }
}
