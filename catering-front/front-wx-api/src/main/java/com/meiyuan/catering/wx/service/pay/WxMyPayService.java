package com.meiyuan.catering.wx.service.pay;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.meiyuan.catering.allinpay.model.bean.notify.NotifyResult;
import com.meiyuan.catering.allinpay.model.bean.notify.OrderNotifyResult;
import com.meiyuan.catering.allinpay.model.result.order.AllinPayOrderDetailResult;
import com.meiyuan.catering.core.constant.AccountRecordConstant;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.account.BalanceAccountInfo;
import com.meiyuan.catering.core.dto.pay.wx.RefundDTO;
import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.*;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.order.enums.OrderStatusEnum;
import com.meiyuan.catering.order.enums.TransactionFlowEnum;
import com.meiyuan.catering.pay.dto.PaySuccessResult;
import com.meiyuan.catering.pay.dto.wx.PayDTO;
import com.meiyuan.catering.pay.enums.PayEnum;
import com.meiyuan.catering.pay.enums.WxOperationTypeEnum;
import com.meiyuan.catering.pay.util.PayLock;
import com.meiyuan.catering.pay.util.PaySupport;
import com.meiyuan.catering.pay.util.PayUtil;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 积分
 *
 * @author zengzhangni
 * @date 2020-03-25
 */
@Service
@Slf4j
public class WxMyPayService {

    @Autowired
    private WxPayService wxPayService;
    @Resource
    private PayLock lock;
    @Autowired
    private PaySupport support;
    @Autowired
    private PayOrderGoodsSupport payOrderGoodsSupport;
    @Resource
    private ShopClient shopClient;


    /**
     * 微信订单支付 (预支付配合支付回调完成订单支付)
     * <p>
     *
     * @param dto    用户信息
     * @param payDTO 支付参数
     * @return 预支付会话标识, 前端调起支付
     */
    public Result wxPay(UserTokenDTO dto, PayDTO payDTO) {
        Long orderId = payDTO.getOrderId();
        Long userId = dto.getUserId();
        log.debug("{}:微信订单支付:{}", userId, orderId);

        List<Object> list = new ArrayList<>();

        lock.payLock(CacheLockUtil.wxPayLock(orderId), () -> {

            Order order = support.orderVerify(orderId);
            ShopInfoDTO shop = support.getShopIsNullThrowEx(order.getStoreId());
            String tradingFlow = FinanceUtil.wxPayNo(shop.getShopCode());
            Object wxPay = support.wxPay(order, tradingFlow, dto.getOpenId());
            //更新订单表的系统流水
            support.updateOrderTradingFlow(order.getId(), tradingFlow);
            //完成
            list.add(wxPay);
        });

        return list.size() > 0 ? Result.succ(list.get(0)) : Result.fail("正在处理");
    }

    /**
     * 余额支付(支付成功同步修改订单状态)
     * <p>
     * 1.验证支付密码
     * 2.查询订单,判断订单是否能够支付
     * 3.判断订单下单用户 和支付用户是否一致
     * 4.同步修改订单状态
     *
     * @param dto
     * @param payDTO 支付参数
     * @return
     */

    public Result balancePay(UserTokenDTO dto, PayDTO payDTO) {
        Result<List<PaySuccessResult>> result = balancePayImpl(dto, payDTO);
        List<PaySuccessResult> list = result.getData();

        //数据处理完成 发送通知
        paySuccessAfter(list);

        return Result.logicResult(list.size() > 0, "正在处理");
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<List<PaySuccessResult>> balancePayImpl(UserTokenDTO dto, PayDTO payDTO) {
        Long orderId = payDTO.getOrderId();

        List<PaySuccessResult> list = new ArrayList<>();

        //加锁处理
        lock.payLock(CacheLockUtil.balancePayLock(orderId), () -> {

            Long userId = dto.getUserId();
            log.debug("{}:余额支付开始.", userId);
            //查询用户账户信息
            BalanceAccountInfo accountInfo = support.userAccountInfo(userId);

            //解密密码
            String oldDesEnc = support.desEncrypt(payDTO.getPassword());

            //支付密码验证
            if (!Md5Util.verifyPayPassword(oldDesEnc, accountInfo.getPassword())) {
                throw new CustomException("支付密码错误");
            }

            Order dbOrder = support.orderVerify(orderId);

            Long storeId = dbOrder.getStoreId();

            ShopInfoDTO shop = support.getShopIsNullThrowEx(storeId);
            MerchantInfoDTO merchant = support.getMerchantIsNullThrowEx(shop.getMerchantId());

            log.debug("shop:{},merchant:{}", shop, merchant);

            // 支付金额
            BigDecimal laterFee = dbOrder.getDiscountLaterFee();
            //数据库余额
            BigDecimal dbBalance = accountInfo.getBalance();
            //支付后余额
            BigDecimal balance = dbBalance.subtract(laterFee);
            // 判断余额是否大于支付金额
            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                throw new CustomException("余额不足");
            }

            //是否支付成功
            Boolean paySuccess = support.isPaySuccess(dbOrder);

            if (!paySuccess) {

                log.debug("订单号:{},订单开始处理", dbOrder.getOrderNumber());

                //余额支付
                support.balancePay(userId, laterFee);

                //添加用户余额账户明细
                support.saveBalanceAccountRecord(userId, AccountRecordConstant.Title.ORDER_PAY, laterFee, AccountRecordConstant.Type.EXPEND, AccountRecordConstant.FundType.ORDER_PAY, FinanceUtil.balanceOrderTransactionNo());

                //是否为首单
                Boolean firstOrder = support.firstOrder(userId);

                //支付成功更新订单
                support.updateOrderByPaySuccess(dbOrder, LocalDateTime.now(), laterFee, PayWayEnum.BALANCE);

                //添加订单交易流水
                support.saveOrdersTransactionFlow(dbOrder, "", FinanceUtil.balanceOrderTransactionNo(), PayWayEnum.BALANCE, TransactionFlowEnum.PAY);


                //保存分账信息
                support.saveOrderSplitBill(dbOrder);

                //下单成功短信通知
                support.smsNotify(dbOrder);

                //保存返回信息
                list.add(new PaySuccessResult(orderId, userId, firstOrder));

            } else {
                log.debug("订单已经处理成功!");
            }
        });

        return Result.succ(list);
    }


    public Result zeroYuanPay(UserTokenDTO dto, PayDTO payDTO) {
        Result<List<PaySuccessResult>> result = zeroYuanPayImpl(dto, payDTO);
        List<PaySuccessResult> list = result.getData();

        //数据处理完成 发送通知
        paySuccessAfter(list);

        return Result.logicResult(list.size() > 0, "正在处理");
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<List<PaySuccessResult>> zeroYuanPayImpl(UserTokenDTO dto, PayDTO payDTO) {
        Long orderId = payDTO.getOrderId();

        List<PaySuccessResult> list = new ArrayList<>();

        //加锁处理
        lock.payLock(CacheLockUtil.balancePayLock(orderId), () -> {

            Long userId = dto.getUserId();
            log.debug("{}:0元支付开始.", userId);
            //查询用户账户信息
            Order dbOrder = support.orderVerify(orderId);
            // 支付金额
            BigDecimal laterFee = dbOrder.getDiscountLaterFee();

            if (!BaseUtil.priceEquals(laterFee, BigDecimal.ZERO)) {
                throw new CustomException("支付金额不为0");
            }

            Long storeId = dbOrder.getStoreId();

            ShopInfoDTO shop = support.getShopIsNullThrowEx(storeId);
            MerchantInfoDTO merchant = support.getMerchantIsNullThrowEx(shop.getMerchantId());
            log.debug("shop:{},merchant:{}", shop, merchant);

            //是否支付成功
            Boolean paySuccess = support.isPaySuccess(dbOrder);

            if (!paySuccess) {

                log.debug("订单号:{},订单开始处理", dbOrder.getOrderNumber());

                //是否为首单
                Boolean firstOrder = support.firstOrder(userId);

                //支付成功更新订单
                support.updateOrderByPaySuccess(dbOrder, LocalDateTime.now(), laterFee, PayWayEnum.ZERO_PAY);

                //添加订单交易流水
                support.saveOrdersTransactionFlow(dbOrder, "", FinanceUtil.balanceOrderTransactionNo(), PayWayEnum.ZERO_PAY, TransactionFlowEnum.PAY);

                //保存分账信息
                support.saveOrderSplitBill(dbOrder);

                //下单成功短信通知
                support.smsNotify(dbOrder);

                //保存返回信息
                list.add(new PaySuccessResult(orderId, userId, firstOrder));

            } else {
                log.debug("订单已经处理成功!");
            }

        });

        return Result.succ(list);
    }

    private void paySuccessAfter(List<PaySuccessResult> list) {

        if (list.size() > 0) {

            PaySuccessResult paySuccessResult = list.get(0);
            //支付成功通知
            support.paySuccessAfter(paySuccessResult);

        }
    }


    /**
     * 微信订单支付回调
     * <p>
     * 1.回调信息验证
     * 2.验证订单状态是否已修改/支付成功
     * 3.检查支付金额
     * 4.已修改忽略/未修改 修改订单状态
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object wxPayNotify(String xmlResult) {
        log.debug("订单-微信付款成功回调...........");

        WxPayOrderNotifyResult result = PayUtil.getNotifyInfo(wxPayService, xmlResult);
        //订单编号
        String orderNumber = result.getOutTradeNo();

        //加锁
        Boolean run = lock.payLock(CacheLockUtil.orderPayNotifyLock(orderNumber), () -> {
            support.wxPayOrderDispose(result);
        });
        return run ? WxPayNotifyResponse.success("处理成功") : WxPayNotifyResponse.fail("正在处理");
    }


    /**
     * 订单申请退款
     * 1. 检测当前订单是否能够退款
     * 2. 修改订单状态为售后
     * 3. 添加退款订单表
     * 4. 添加退款审核信息/协商记录
     *
     * @param dto
     * @param refundDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result applyRefund(UserTokenDTO dto, RefundDTO refundDTO) {
        Long userId = dto.getUserId();
        Long orderId = Long.valueOf(refundDTO.getOrderId());
        Order order = support.queryOrder(orderId);

        Long shopId = order.getStoreId();
        ShopInfoDTO shop = shopClient.getShop(shopId);
        if (shop == null) {
            throw new CustomException("不可申请售后");
        }

        //加锁
        Boolean run = lock.payLock(CacheLockUtil.applyRefundLock(userId, orderId), () -> {

            if (!Objects.equals(order.getStatus(), OrderStatusEnum.DONE.getValue())) {
                throw new CustomException("订单状态不是已完成");
            }
            if (order.getAfterSales() || !order.getCanAfterSales()) {
                throw new CustomException(701, "订单已申请售后或订单已关闭");
            }

            //设置订单为售后
            support.updateAfterSales(orderId);

            //退款单号
            String refundNo = FinanceUtil.refundNo(shop.getShopCode());

            //添加退款单
            Long refundId = support.saveRefundOrder(refundNo, order);

            //添加退款审核
            support.saveAudit(shopId, refundNo, refundId, refundDTO);

            //添加退款进度
            support.saveOperation(orderId, refundId);

        });
        return run ? Result.succ("申请退款成功") : Result.fail("正在处理");
    }


    @Transactional(rollbackFor = Exception.class)
    public Result cancelGroupOrder(UserTokenDTO dto, Long orderId) {
        Long userId = dto.getUserId();

        //加锁处理(非堵塞锁)
        Boolean run = lock.payLock(CacheLockUtil.cancelGroupLock(orderId), () -> {

            //再次查询订单
            Order order = support.queryOrder(orderId);

            if (!Objects.equals(order.getStatus(), OrderStatusEnum.GROUP.getValue())) {
                throw new CustomException("订单状态不是团购中");
            }

            if (!Objects.equals(order.getStatus(), OrderStatusEnum.CANCELED.getValue())) {
                //更新订单状态为取消
                support.updateStatusToCanceled(order);

                //取消团购(退款)
                support.cancelGroupImpl(PayEnum.parse(order.getPayWay()), order);

                //添加订单进度(已取消/已退款)
                support.saveOrderOperation(order);

                //秒杀处理 通知
                support.sendSeckillMsg(orderId);

                //mq消息通知团购
                support.groupCancelNotify(orderId, userId);

            } else {
                log.debug("订单已取消成功");
            }
        });


        return Result.logicResult(run, "正在处理");
    }

    /**
     * 描述: 查询微信订单
     *
     * @param dto
     * @param orderNumber
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/4/11 15:24
     */
    public Result queryWxOrder(UserTokenDTO dto, String orderNumber) {
        return Result.succ(PayUtil.allinQueryOrder(orderNumber));
    }

    /**
     * 描述:是否已微信支付
     *
     * @param payDTO
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/4/11 15:28
     */
    public Result isHaveWxPay(PayDTO payDTO) {
        String tradingFlow = payDTO.getTradingFlow();
        if (StringUtils.isNotBlank(tradingFlow)) {
            try {
                AllinPayOrderDetailResult result = PayUtil.allinQueryOrder(tradingFlow);
                if (PayUtil.isHaveWxPay(result)) {
                    //加锁
                    lock.payLock(CacheLockUtil.orderPayNotifyLock(payDTO.getOrderNumber()), () -> {
                        //已微信支付 主动处理
                        support.wxPayOrderDispose(result);
                    });
                    Result<Boolean> res = new Result<>();
                    res.setMsg("已微信支付成功,订单正在处理,请勿支付!!");
                    res.setData(true);
                    return res;
                }
            } catch (Exception e) {
            }
        }
        // 如果是去支付的操作，验证订单商品是否下架
        if (WxOperationTypeEnum.PAY.getCode().equals(payDTO.getType())) {
            orderGoodsVerify(payDTO.getOrderId());
        }
        return Result.succ(false);
    }

    public void orderGoodsVerify(Long orderId) {
        payOrderGoodsSupport.orderGoodsVerify(orderId);
    }

    public Object allinPayNotify(String notifyData) {
        log.info("notifyData:{}", notifyData);

        NotifyResult notifyInfo = PayUtil.getNotifyInfo(notifyData);
        OrderNotifyResult result = notifyInfo.getResult();
        //加锁
        String meiYuanOrderNo = result.getBizOrderNo();
        Boolean run = lock.payLock(CacheLockUtil.orderPayNotifyLock(meiYuanOrderNo), () -> {
            support.wxPayOrderDispose(notifyInfo, result);
        });
        return run ? "success" : "正在处理";

    }
}
