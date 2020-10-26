package com.meiyuan.catering.merchant.service.order;

import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.CacheLockUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.feign.MarketingTicketActivityClient;
import com.meiyuan.catering.order.dto.query.refund.MerchantRefundDTO;
import com.meiyuan.catering.order.enums.AuditStatusEnum;
import com.meiyuan.catering.order.enums.OrderStatusEnum;
import com.meiyuan.catering.order.enums.RefundOperationEnum;
import com.meiyuan.catering.order.enums.RefundStatusEnum;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.order.utils.OrderUtils;
import com.meiyuan.catering.pay.enums.PayEnum;
import com.meiyuan.catering.pay.util.PayLock;
import com.meiyuan.catering.pay.util.PaySupport;
import com.meiyuan.catering.pay.util.RefundSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * 订单退款服务
 *
 * @author zengzhangni
 * @date 2020-03-24
 */
@Service
@Slf4j
public class MerchantOrderRefundService {

    @Resource
    private RefundSupport support;
    @Resource
    private PaySupport paySupport;
    @Resource
    private PayLock lock;
    @Autowired
    private MarketingTicketActivityClient activityClient;
    @Resource
    private OrderClient orderClient;

    @Transactional(rollbackFor = Exception.class)
    public Result refuseRefund(MerchantRefundDTO dto) {

        log.info("拒绝退款开始");
        Long refundId = dto.getId();

        Boolean run = lock.refundLock(CacheLockUtil.orderRefundLock(refundId), () -> {
            log.info("拒绝退款执行");
            RefundOrder refundOrder = support.getRefundOrderById(refundId);

            //验证退款单
            verifyRefundStatus(refundOrder);

            Long orderId = refundOrder.getOrderId();
            Order order = support.getOrderById(orderId);

            //更新退款状态 为退款失败
            support.updateRefundStatus(refundId, RefundStatusEnum.FAIL_REFUND);

            //修改退款审核
            support.updateById(order.getStoreId(), refundId, AuditStatusEnum.REFUSE_AUDIT, dto.getRefuseReason());

            //添加退款进度(审核拒绝)
            support.saveRefundOperation(orderId, refundId, RefundOperationEnum.REFUSE, dto.getRefuseReason());

            //添加退款进度(订单退款关闭)
            support.saveRefundOperation(orderId, refundId, RefundOperationEnum.REFUNDED_CLOSE, "");

            //执行分账,不验证售后状态
            paySupport.asyncDisposeSplitBill(orderId.toString(), false);

        });
        log.info("拒绝退款结束");
        return Result.logicResult(run, "正在处理");
    }

    private void verifyRefundStatus(RefundOrder refundOrder) {
        if (refundOrder == null) {
            throw new CustomException("订单不存在");
        }
        if (refundOrder.getRefundStatus().equals(RefundStatusEnum.FAIL_REFUND.getStatus())) {
            throw new CustomException("订单已经拒绝退款");
        }
        if (refundOrder.getRefundStatus().equals(RefundStatusEnum.SUCCESS_REFUND.getStatus())) {
            throw new CustomException("订单已经同意退款");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public Result confirmRefund(MerchantRefundDTO dto) {
        Long id = dto.getId();

        //加锁处理(非堵塞锁)
        Boolean run = lock.refundLock(CacheLockUtil.orderRefundLock(id), () -> {

            RefundOrder refundOrder = support.getRefundOrderById(id);
            //验证退款单
            verifyRefundStatus(refundOrder);

            Long orderId = refundOrder.getOrderId();
            Order order = support.getOrderById(orderId);

            refundOrder.setStoreId(order.getStoreId());

            //退款金额
            BigDecimal refundAmount = refundOrder.getRefundAmount();
            //已支付金额
            BigDecimal discountLaterFee = order.getPaidAmount();
            // 金额验证
            if (!Objects.equals(refundAmount, discountLaterFee)) {
                throw new CustomException("退款金额和订单支付金额不一致");
            }

            if (!Objects.equals(refundOrder.getRefundStatus(), RefundStatusEnum.SUCCESS_REFUND.getStatus())) {
                Integer payWay = order.getPayWay();
                //退款
                support.refundImpl(PayEnum.parse(payWay), refundOrder);

                Long refundId = refundOrder.getId();
                Long merchantId = order.getMerchantId();

                Long shopId = order.getStoreId();


                //更新退款状态 为退款成功
                support.updateRefundStatus(refundId, RefundStatusEnum.SUCCESS_REFUND);
                //修改退款审核
                support.updateById(shopId, refundId, AuditStatusEnum.PASS_AUDIT, dto.getRefuseReason());
                //添加退款进度(审核通过)
                support.saveRefundOperation(orderId, refundId, RefundOperationEnum.PASS);
                //添加退款进度(订单已退款)
                support.saveRefundOperation(orderId, refundId, RefundOperationEnum.REFUNDED);


                // V1.3.0 订单退款成功，删除优惠券数据记录
                activityClient.cancelTicketDataRecord(orderId);
            } else {
                log.debug("订单已经退款成功");
            }
        });

        return Result.logicResult(run, "正在处理");

    }

    @Transactional(rollbackFor = Exception.class)
    public Result cancelOrder(MerchantRefundDTO dto) {
        Long orderId = dto.getId();

//        OrdersDetailMerchantDTO ordersDetailMerchantDTO = orderClient.orderDetailQueryMerchant(orderId).getData();
//        List<OrderDeliveryStatusDto> deliveryStatusList = ordersDetailMerchantDTO.getOrderDeliveryStatusList();
//        if (!CollectionUtils.isEmpty(deliveryStatusList)) {
//            // 取达达最新的配送状态
//            OrderDeliveryStatusDto orderDeliveryStatusDto = deliveryStatusList.get(0);
//            if (!OrderDeliveryStatusEnum.wait_accept.getCode().equals(orderDeliveryStatusDto.getOrderStatus())) {
//                throw new CustomException("只有待骑手接单状态才能取消订单~");
//            }
//        }


        //加锁处理(非堵塞锁)
        Boolean run = lock.refundLock(CacheLockUtil.orderCancelLock(orderId), () -> {
            Order order = support.getOrderById(orderId);
            Integer status = order.getStatus();

            if (!OrderUtils.CANCEL_STATUS.contains(status)) {
                throw new CustomException("待自提/待配送的订单才能取消");
            }
            if (!Objects.equals(order.getStatus(), OrderStatusEnum.CANCELED.getValue())) {
                //更新订单状态为取消，商家取消原因写死""
                support.updateStatusToCanceled(orderId, "商家已取消订单");
                //退款
                support.cancelImpl(PayEnum.parse(order.getPayWay()), order);

                //添加订单进度(已取消/已退款)
                support.saveOrderOperation(order, dto.getRefuseReason());

                //短信通知
                support.notifySms(order);
            } else {
                log.debug("订单已取消成功");
            }
        });
        if (run) {
            // 处理订单秒杀、赠品，商品库存
            support.goodsInventoryHandle(orderId);
        }
        return Result.logicResult(run, "正在处理");
    }
}
