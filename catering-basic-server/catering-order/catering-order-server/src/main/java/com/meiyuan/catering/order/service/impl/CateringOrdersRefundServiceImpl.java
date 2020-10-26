package com.meiyuan.catering.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.dto.order.audit.OrdersRefundAudit;
import com.meiyuan.catering.core.dto.order.goods.OrderGoods;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.CharUtil;
import com.meiyuan.catering.core.util.FinanceUtil;
import com.meiyuan.catering.order.dao.CateringOrdersRefundMapper;
import com.meiyuan.catering.order.dto.OrderRefundDTO;
import com.meiyuan.catering.order.dto.order.OrderRefundDto;
import com.meiyuan.catering.order.dto.query.refund.RefundQueryDTO;
import com.meiyuan.catering.order.dto.query.wx.RefundOrdersListGoodsWxDTO;
import com.meiyuan.catering.order.entity.CateringOrdersRefundEntity;
import com.meiyuan.catering.order.enums.*;
import com.meiyuan.catering.order.service.CateringOrdersGoodsService;
import com.meiyuan.catering.order.service.CateringOrdersRefundAuditService;
import com.meiyuan.catering.order.service.CateringOrdersRefundService;
import com.meiyuan.catering.order.vo.RefundCountVO;
import com.meiyuan.catering.order.vo.RefundQueryListVO;
import com.meiyuan.catering.order.vo.WxRefundDetailVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单退款表(CateringOrdersRefund)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:41
 */
@Service("cateringOrdersRefundService")
public class CateringOrdersRefundServiceImpl extends ServiceImpl<CateringOrdersRefundMapper, CateringOrdersRefundEntity> implements CateringOrdersRefundService {

    @Autowired
    private CateringOrdersRefundAuditService refundAuditService;
    @Autowired
    private CateringOrdersGoodsService goodsService;

    @Override
    public IPage<RefundQueryListVO> pageList(RefundQueryDTO dto) {
        dispose(dto);
        return baseMapper.pageList(dto.getPage(), dto);
    }

    @Override
    public RefundOrder getByOrderId(Long orderId) {
        LambdaQueryWrapper<CateringOrdersRefundEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringOrdersRefundEntity::getOrderId, orderId);
        return BaseUtil.objToObj(baseMapper.selectOne(wrapper), RefundOrder.class);
    }

    @Override
    public RefundCountVO refundCount(RefundQueryDTO dto) {
        return baseMapper.refundCount(dto);
    }

    @Override
    public Map<String, Object> pageListAndRefundCount(RefundQueryDTO dto) {
        dispose(dto);
        IPage<RefundQueryListVO> list = baseMapper.pageList(dto.getPage(), dto);
        RefundCountVO refundCount = refundCount(dto);
        Map<String, Object> map = new HashMap<>(2);
        map.put("page", new PageData(list.getRecords(), list.getTotal()));
        map.put("number", refundCount);
        return map;
    }

    private void dispose(RefundQueryDTO dto) {
        //处理关键字
        String keyword = CharUtil.disposeChar(dto.getKeyword());
        if (StringUtils.isNotBlank(keyword)) {
            dto.setKeyword(keyword);
        }
        LocalDateTime createTime2 = dto.getCreateTime2();
        if (createTime2 != null) {
            dto.setCreateTime2(createTime2.plusDays(1));
        }
        LocalDateTime updateTime2 = dto.getUpdateTime2();
        if (updateTime2 != null) {
            dto.setUpdateTime2(updateTime2.plusDays(1));
        }
    }

    @Override
    public RefundOrder getRefundOrderById(Long id) {
        return BaseUtil.objToObj(baseMapper.selectById(id), RefundOrder.class);
    }

    @Override
    public Long saveRefundOrder(String refundNo, Order order) {
        CateringOrdersRefundEntity refundEntity = new CateringOrdersRefundEntity();
        refundEntity.setOrderId(order.getId());
        refundEntity.setOrderNumber(order.getOrderNumber());
        refundEntity.setTradingFlow(StringUtils.isNotBlank(order.getTradingFlow()) ? order.getTradingFlow() : FinanceUtil.systemTransactionNo());
        refundEntity.setRefundNumber(refundNo);
        refundEntity.setMemberId(order.getMemberId());
        refundEntity.setMemberName(order.getMemberName());
        refundEntity.setMemberPhone(order.getMemberPhone());
        refundEntity.setRefundType(RefundTypeEnum.USER.getStatus());
        refundEntity.setRefundAmount(order.getPaidAmount());
        save(refundEntity);
        return refundEntity.getId();
    }

    @Override
    public Boolean updateRefundStatus(Long refundId, RefundStatusEnum refundStatusEnum) {
        CateringOrdersRefundEntity refundEntity = new CateringOrdersRefundEntity();
        refundEntity.setId(refundId);
        refundEntity.setRefundStatus(refundStatusEnum.getStatus());
        return updateById(refundEntity);
    }

    @Override
    public WxRefundDetailVO refundDetailById(Long orderId) {
        WxRefundDetailVO detailVO = new WxRefundDetailVO();

        RefundOrder refundEntity = getByOrderId(orderId);
        OrdersRefundAudit audit = refundAuditService.getOneByRefundId(refundEntity.getId());
        detailVO.setRefundAuditStatus(audit.getBusinessAuditStatus());
        detailVO.setRefundWay(refundEntity.getRefundWay());
        detailVO.setRefundTotalAmount(refundEntity.getRefundAmount());
        detailVO.setRefundReason(audit.getRefundReason());
        detailVO.setRefundAmount(refundEntity.getRefundAmount());
        detailVO.setApplyTime(refundEntity.getCreateTime());
        detailVO.setRefundNumber(refundEntity.getRefundNumber());
        detailVO.setId(refundEntity.getId());

        List<OrderGoods> goodsEntities = goodsService.getByOrderId(orderId);
        List<RefundOrdersListGoodsWxDTO> dtos = goodsEntities.stream().filter(goods -> !goods.getGifts()).map(goods -> {
            RefundOrdersListGoodsWxDTO wxDTO = new RefundOrdersListGoodsWxDTO();
            wxDTO.setOrderGoodsId(goods.getGoodsId());
            wxDTO.setGoodsName(goods.getGoodsName());
            wxDTO.setQuantity(goods.getQuantity());
            wxDTO.setFee(goods.getDiscountLaterFee());
            wxDTO.setGoodsPicture(goods.getGoodsPicture());
            wxDTO.setGoodsSpecificationDesc(goods.getGoodsSpecificationDesc());
            wxDTO.setGoodsType(goods.getGoodsType());
            return wxDTO;
        }).collect(Collectors.toList());

        detailVO.setGoodsInfo(dtos);
        return detailVO;
    }

    @Override
    public OrderRefundDto queryByOrderId(Long orderId) {
        OrderRefundDto dto = baseMapper.queryByOrderId(orderId);
        if (dto == null) {
            return null;
        }
        dto.setRefundStatusDesc(RefundStatusEnum.parse(dto.getRefundStatus()));
        dto.setCargoStatusDesc(CargoStatusEnum.parse(dto.getCargoStatus()).getDesc());
        dto.setRefundTypeDesc(RefundTypeEnum.parse(dto.getRefundType()));
        dto.setRefundWayDesc(RefundWayEnum.parse(dto.getRefundWay()));
        String refundReason = dto.getRefundReason();
        List<Integer> reasonList = (List<Integer>) JSON.parse(refundReason);
        // 解析退款原因
        if (CollectionUtils.isNotEmpty(reasonList)) {
            if (CollectionUtils.isEmpty(dto.getRefundReasonList())) {
                dto.setRefundReasonList(Lists.newArrayList());
            }
            for (Integer item : reasonList) {
                dto.getRefundReasonList().add(RefundReasonEnum.parse(item));
            }
            dto.setRefundReasonDesc(Joiner.on(",").join(dto.getRefundReasonList()));
        }
        dto.setRefundEvidenceList((List<String>) JSON.parse(dto.getRefundEvidence()));
        return dto;
    }


}
