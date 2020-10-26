package com.meiyuan.catering.order.feign;


import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.order.OrderRefundDto;
import com.meiyuan.catering.order.dto.query.refund.RefundQueryDTO;
import com.meiyuan.catering.order.enums.RefundStatusEnum;
import com.meiyuan.catering.order.service.CateringOrdersRefundService;
import com.meiyuan.catering.order.vo.WxRefundDetailVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author zengzhangni
 * @date 2020/5/20 9:39
 * @since v1.1.0
 */
@Service
public class OrderRefundClient {
    @Resource
    private CateringOrdersRefundService service;

    /**
     * 描述:保存退款订单 并返回id
     *
     * @param refundNo
     * @param order
     * @return com.meiyuan.catering.core.util.Result<java.lang.Long>
     * @author zengzhangni
     * @date 2020/5/20 10:13
     * @since v1.1.0
     */
    public Result<Long> saveRefundOrder(String refundNo, Order order) {
        return Result.succ(service.saveRefundOrder(refundNo, order));
    }


    /**
     * 描述:更新退款订单状态
     *
     * @param refundId
     * @param refundStatusEnum
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/20 10:14
     * @since v1.1.0
     */
    public Result<Boolean> updateRefundStatus(Long refundId, RefundStatusEnum refundStatusEnum) {
        return Result.succ(service.updateRefundStatus(refundId, refundStatusEnum));
    }

    /**
     * 描述:通过id查询退款订单
     *
     * @param refundId
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.dto.pay.RefundOrder>
     * @author zengzhangni
     * @date 2020/5/20 10:46
     * @since v1.1.0
     */
    public Result<RefundOrder> getRefundOrderById(Long refundId) {
        return Result.succ(service.getRefundOrderById(refundId));
    }

    /**
     * 描述: 数量统计+分页列表
     *
     * @param dto
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/20 13:52
     * @since v1.1.0
     */
    public Result<Map<String, Object>> pageListAndRefundCount(RefundQueryDTO dto) {
        return Result.succ(service.pageListAndRefundCount(dto));
    }


    /**
     * 描述:通过订单id查询退款单
     *
     * @param orderId
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.dto.pay.RefundOrder>
     * @author zengzhangni
     * @date 2020/5/20 16:04
     * @since v1.1.0
     */
    public Result<RefundOrder> getByOrderId(Long orderId) {
        return Result.succ(service.getByOrderId(orderId));
    }

    /**
     * 描述:退款详情
     *
     * @param orderId
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.order.vo.WxRefundDetailVO>
     * @author zengzhangni
     * @date 2020/5/27 17:07
     * @since v1.1.0
     */
    public Result<WxRefundDetailVO> refundDetailById(Long orderId) {
        return Result.succ(service.refundDetailById(orderId));
    }

    /**
     * 查询退款信息 lh v1.4.0
     *
     * @param orderId
     * @return
     */
    public Result<OrderRefundDto> queryByOrderId(Long orderId) {

        return Result.succ(service.queryByOrderId(orderId));

    }

}
