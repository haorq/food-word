package com.meiyuan.catering.order.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.order.dto.order.OrderRefundDto;
import com.meiyuan.catering.order.dto.query.refund.RefundQueryDTO;
import com.meiyuan.catering.order.entity.CateringOrdersRefundEntity;
import com.meiyuan.catering.order.enums.RefundStatusEnum;
import com.meiyuan.catering.order.vo.RefundCountVO;
import com.meiyuan.catering.order.vo.RefundQueryListVO;
import com.meiyuan.catering.order.vo.WxRefundDetailVO;

import java.util.Map;

/**
 * 订单退款表(CateringOrdersRefund)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:03
 */
public interface CateringOrdersRefundService extends IService<CateringOrdersRefundEntity> {

    /**
     * 分页列表
     *
     * @param dto
     * @return
     */
    IPage<RefundQueryListVO> pageList(RefundQueryDTO dto);

    /**
     * 描述:通过订单号查询退款单信息
     *
     * @param orderId
     * @return com.meiyuan.catering.order.entity.CateringOrdersRefundEntity
     * @author zengzhangni
     * @date 2020/4/3 14:29
     */
    RefundOrder getByOrderId(Long orderId);

    /**
     * 描述:退款统计
     *
     * @param dto
     * @return java.util.Map<java.lang.String, java.lang.Long>
     * @author zengzhangni
     * @date 2020/4/26 16:59
     */
    RefundCountVO refundCount(RefundQueryDTO dto);

    /**
     * 描述:退款列表和退款统计
     *
     * @param dto
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author zengzhangni
     * @date 2020/4/26 17:22
     */
    Map<String, Object> pageListAndRefundCount(RefundQueryDTO dto);

    /**
     * 描述:通过id查询退款订单
     *
     * @param id
     * @return com.meiyuan.catering.core.dto.pay.RefundOrder
     * @author zengzhangni
     * @date 2020/5/19 17:44
     * @since v1.1.0
     */
    RefundOrder getRefundOrderById(Long id);

    /**
     * 描述:保存退款订单 并返回id
     *
     * @param refundNo
     * @param order
     * @return java.lang.Long
     * @author zengzhangni
     * @date 2020/5/20 10:14
     * @since v1.1.0
     */
    Long saveRefundOrder(String refundNo, Order order);

    /**
     * 描述:更新退款订单状态
     *
     * @param refundId
     * @param refundStatusEnum
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/20 10:14
     * @since v1.1.0
     */
    Boolean updateRefundStatus(Long refundId, RefundStatusEnum refundStatusEnum);

    /**
     * 描述:退款详情
     *
     * @param orderId
     * @return com.meiyuan.catering.order.vo.WxRefundDetailVO
     * @author zengzhangni
     * @date 2020/5/27 17:08
     * @since v1.1.0
     */
    WxRefundDetailVO refundDetailById(Long orderId);


    /**
     * 查询退款信息 lh v1.4.0
     *
     * @param orderId
     * @return
     */
    OrderRefundDto queryByOrderId(Long orderId);
}
