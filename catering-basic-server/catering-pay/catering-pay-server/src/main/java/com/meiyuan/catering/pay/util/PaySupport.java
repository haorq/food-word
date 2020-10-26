package com.meiyuan.catering.pay.util;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.meiyuan.catering.allinpay.commons.AllinpayConstant;
import com.meiyuan.catering.allinpay.enums.service.order.PayMethodKeyEnums;
import com.meiyuan.catering.allinpay.enums.service.order.PayStatusEnums;
import com.meiyuan.catering.allinpay.enums.service.order.SubsidyTypeEnums;
import com.meiyuan.catering.allinpay.enums.service.order.TradeCodeEnums;
import com.meiyuan.catering.allinpay.model.bean.notify.NotifyResult;
import com.meiyuan.catering.allinpay.model.bean.notify.OrderNotifyResult;
import com.meiyuan.catering.allinpay.model.bean.order.AllinReciever;
import com.meiyuan.catering.allinpay.model.result.order.AllinPayOrderDetailResult;
import com.meiyuan.catering.core.config.EncryptPasswordProperties;
import com.meiyuan.catering.core.dto.base.GrouponMemberQuitDTO;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.order.delivery.OrderDelivery;
import com.meiyuan.catering.core.dto.order.goods.OrderGoods;
import com.meiyuan.catering.core.dto.pay.ChargeOrder;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.account.BalanceAccountInfo;
import com.meiyuan.catering.core.dto.pay.recharge.RechargeRule;
import com.meiyuan.catering.core.dto.pay.wx.RefundDTO;
import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.core.exception.AllinpayException;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.notify.NotifyService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.util.*;
import com.meiyuan.catering.core.util.dada.DadaUtils;
import com.meiyuan.catering.es.util.EsUtil;
import com.meiyuan.catering.finance.feign.*;
import com.meiyuan.catering.merchant.feign.ShopExtClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.dto.OrderPickSmsMqDTO;
import com.meiyuan.catering.order.dto.splitbill.WithdrawFlowStatusUpdateDTO;
import com.meiyuan.catering.order.entity.*;
import com.meiyuan.catering.order.enums.*;
import com.meiyuan.catering.order.feign.*;
import com.meiyuan.catering.order.utils.OrderUtils;
import com.meiyuan.catering.pay.dto.BasePayDto;
import com.meiyuan.catering.pay.dto.PaySuccessResult;
import com.meiyuan.catering.pay.dto.pay.WxPayDto;
import com.meiyuan.catering.pay.enums.NotifyEnum;
import com.meiyuan.catering.pay.enums.PayEnum;
import com.meiyuan.catering.pay.service.MyOrderService;
import com.meiyuan.catering.pay.service.MyPayService;
import com.meiyuan.catering.user.fegin.address.AddressClient;
import com.meiyuan.catering.user.vo.address.AddressDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 描述: 支付数据处理
 *
 * @author zengzhangni
 * @date 2020/3/31 10:38
 */
@Slf4j
@Component
public class PaySupport {

    @Resource
    private UserBalanceAccountRecordClient accountRecordClient;
    @Resource
    private UserChargeOrderClient chargeOrderClient;
    @Resource
    private UserChargeRecordClient chargeRecordClient;
    @Resource
    private OrderTransactionFlowClient flowClient;
    @Resource
    private UserBalanceAccountClient accountClient;
    @Resource
    private OrderClient orderClient;
    @Autowired
    private OrdersSplitBillClint ordersSplitBillClint;
    @Autowired
    private OrdersSplitBillOrderFlowClint ordersSplitBillOrderFlowClint;
    @Autowired
    private OrdersSplitBillSubsidyFlowClint ordersSplitBillSubsidyFlowClint;
    @Autowired
    private OrdersSplitBillWithdrawFlowClint ordersSplitBillWithdrawFlowClint;
    @Autowired
    private OrdersShopDebtClient ordersShopDebtClient;
    @Autowired
    private OrdersShopDebtFlowClient ordersShopDebtFlowClient;
    @Resource
    private OrderRefundAuditClient refundAuditClient;
    @Resource
    private OrderRefundClient refundClient;
    @Resource
    private RechargeRuleClient ruleClient;
    @Resource
    private RefundOrderOperationClient refundOrdersOperationClient;
    @Resource
    private OrderOperationClient operationClient;
    @Resource
    private OrderDeliveryClient deliveryClient;
    @Autowired
    private OrderGoodsClient orderGoodsClient;
    @Autowired
    private OrderMqSenderClient senderClient;

    @Autowired
    private NotifyService notifyService;
    @Resource
    private MerchantUtils merchantUtils;
    @Resource
    private EncryptPasswordProperties encryptPasswordProperties;
    @Resource
    private MyOrderService myOrderService;

    @Resource
    private DadaUtils dadaUtils;

    @Resource
    private ShopExtClient shopExtClient;
    @Resource
    private AddressClient addressClient;


    public Object wxPay(Order order, String tradingFlow, String openId) {
        BigDecimal laterFee = order.getDiscountLaterFee();
        WxPayDto wxPayDto = pay(tradingFlow, openId, laterFee);
        wxPayDto.setBody("订单:" + tradingFlow);
        wxPayDto.setAttach("");
        wxPayDto.setNotifyEnum(NotifyEnum.ORDER);
        wxPayDto.setFee(BigDecimal.ZERO);
        wxPayDto.setUserId(order.getMemberId());

        wxPayDto.setPayMethod(PayMethodKeyEnums.WECHATPAY_MINIPROGRAM_ORG);
        //添加分账人
        wxPayDto.addProfitReceivers(new AllinReciever(order.getStoreId(), laterFee));

        MyPayService payService = PayContext.getPayService(PayEnum.WX_ALLIN);
        return payService.pay(wxPayDto);
    }

    /**
     * 描述:余额支付
     *
     * @param userId
     * @param fee
     * @return java.lang.Object
     * @author zengzhangni
     * @date 2020/4/2 15:40
     */
    public Object balancePay(Long userId, BigDecimal fee) {
        MyPayService payService = SpringContextUtils.getBean(PayEnum.BALANCE.getImpl(), MyPayService.class);
        BasePayDto payDto = new BasePayDto();
        payDto.setUserId(userId);
        payDto.setPayFee(fee);
        return payService.pay(payDto);
    }


    /**
     * 描述: 微信支付 公共请求
     */
    private WxPayDto pay(String orderNo, String openId, BigDecimal fee) {
        WxPayDto wxPayDto = new WxPayDto();
        wxPayDto.setOrderNumber(orderNo);
        wxPayDto.setOpenId(openId);
        wxPayDto.setPayFee(fee);
        wxPayDto.setIp(HttpContextUtils.getRealIp());
        return wxPayDto;
    }


    /**
     * 描述: 添加充值明细
     *
     * @param order         充值订单
     * @param rechargeNo    充值订单号
     * @param transactionId 交易流水号
     * @return void
     * @author zengzhangni
     * @date 2020/3/28 10:17
     */
    public void saveChargeRecord(ChargeOrder order, String rechargeNo, String transactionId) {
        chargeRecordClient.saveChargeRecord(order, rechargeNo, transactionId);
    }

    /**
     * 描述:更新账户信息
     *
     * @param userId         用户id
     * @param receivedAmount 存在金额
     * @param cashCoupon     赠送金额/现金劵
     * @return void
     * @author zengzhangni
     * @date 2020/3/28 10:20
     */
    public void updateAccountInfo(Long userId, BigDecimal receivedAmount, BigDecimal cashCoupon) {
        accountClient.balanceAdd(userId, receivedAmount.add(cashCoupon), receivedAmount, cashCoupon);
    }

    /**
     * 描述: 更新充值订单
     *
     * @param id             充值订单id
     * @param receivedAmount 充值金额
     * @param cashCoupon     赠送金额
     * @param transactionId  交易流水号
     * @return void
     * @author zengzhangni
     * @date 2020/3/28 10:31
     */
    public void updateChargeOrder(Long id, BigDecimal receivedAmount, BigDecimal cashCoupon, String transactionId) {
        //修改充值订单状态
        chargeOrderClient.updateChargeOrder(id, receivedAmount, cashCoupon, transactionId);
    }

    /**
     * 描述: 添加余额消费明细
     *
     * @param userId         用户id
     * @param title          明细标题
     * @param receivedAmount 充值金额
     * @param type           资金类别：1--收入，2--支出
     * @param fundType       款项类别，1--充值，2--订单支付，3--订单退款，4--余额退款
     * @param transactionId  交易流水
     * @return void
     * @author zengzhangni
     * @date 2020/3/28 11:24
     */
    public void saveBalanceAccountRecord(Long userId, String title, BigDecimal receivedAmount, Integer type, Integer fundType, String transactionId) {
        accountRecordClient.saveBalanceAccountRecord(userId, title, receivedAmount, type, fundType, transactionId);
    }

    /**
     * 描述: 保存支付流水/交易流水
     *
     * @param order         订单
     * @param xmlResult     支付回调信息
     * @param transactionId 交易流水
     * @param payWayEnum    支付方式
     * @return void
     * @author zengzhangni
     * @date 2020/3/28 14:59
     */
    public void saveOrdersTransactionFlow(Order order, String xmlResult, String transactionId, PayWayEnum payWayEnum, TransactionFlowEnum flowEnum) {
        flowClient.saveOrdersTransactionFlow(order, xmlResult, transactionId, payWayEnum, flowEnum);
    }


    /**
     * 描述: 设置订单状态
     * 团购订单 支付成功设置无团购中
     * <p>
     * 普通订单 支付成功
     * -----------配送:待配送
     * -----------手动接单:待接单
     * -----------自动接单:待自取
     *
     * @param order
     * @return void
     * @author zengzhangni
     * @date 2020/3/28 15:02
     */
    public Integer orderStatus(Order order) {
        Integer orderType = order.getOrderType();
        //团购订单
        if (Objects.equals(orderType, OrderTypeEnum.BULK.getStatus())) {
            return OrderStatusEnum.GROUP.getValue();
        } else {
            //普通订单
            Integer deliveryWay = order.getDeliveryWay();
            boolean isDelivery = Objects.equals(deliveryWay, DeliveryWayEnum.Delivery.getCode());
            //是否为配送
            if (isDelivery) {
                return OrderStatusEnum.WAIT_DELIVERY.getValue();
            } else {
                //自取
                //商户是否为自动接单
                if (merchantUtils.isAutoReceipt(order.getStoreId())) {
                    return OrderStatusEnum.WAIT_TAKEN.getValue();
                } else {
                    return OrderStatusEnum.WAIT_ORDERS.getValue();
                }
            }
        }

    }


    /**
     * 描述:查询订单
     *
     * @param orderId
     * @return com.meiyuan.catering.core.dto.pay.Order
     * @author zengzhangni
     * @date 2020/5/19 17:57
     * @since v1.1.0
     */
    public Order queryOrder(Long orderId) {
        Order order = ClientUtil.getDate(orderClient.getOrderById(orderId));
        if (order == null) {
            throw new CustomException("订单不存在");
        }
        return order;
    }

    /**
     * 描述:订单状态验证 并返回订单
     *
     * @param orderId
     * @return com.meiyuan.catering.core.dto.pay.Order
     * @author zengzhangni
     * @date 2020/5/19 17:57
     * @since v1.1.0
     */
    public Order orderVerify(Long orderId) {
        Order order = queryOrder(orderId);
        //不是待支付状态
        if (!Objects.equals(OrderStatusEnum.getByValue(order.getStatus()), OrderStatusEnum.UNPAID)) {
            throw new CustomException(ErrorCode.ORDER_PAY_TIMEOUT, "订单支付已超时！");
        }
        return order;
    }

    /**
     * 描述: 支付成功通知
     *
     * @param orderId
     * @param userId
     * @return void
     * @author zengzhangni
     * @date 2020/3/31 10:50
     */
    public void paySuccessNotify(Long orderId, Long userId, Boolean firstOrder) {
        log.debug("用户:{},订单id:{}--支付成功通知", userId, orderId);
        Order order = queryOrder(orderId);
        //支付成功通知
        senderClient.sendPaySuccessMsg(order, firstOrder);
    }


    /**
     * 描述:是否是首单
     *
     * @param userId
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/4/17 16:17
     */
    public Boolean firstOrder(Long userId) {
        return ClientUtil.getDate(orderClient.firstOrder(userId));
    }


    /**
     * 描述: 添加退款进度
     *
     * @param orderId
     * @param refundId
     * @return void
     * @author zengzhangni
     * @date 2020/3/31 16:04
     */
    public void saveOperation(Long orderId, Long refundId) {
        refundOrdersOperationClient.saveOperation(orderId, refundId, RefundOperationEnum.SUBMIT, null);
    }

    /**
     * 描述:添加退款审核
     *
     * @param shopId
     * @param refundNo
     * @param refundId
     * @param refundDTO
     * @return void
     * @author zengzhangni
     * @date 2020/3/31 16:04
     */
    public void saveAudit(Long shopId, String refundNo, Long refundId, RefundDTO refundDTO) {
        ShopInfoDTO shop = merchantUtils.getShopIsNullThrowEx(shopId);
        refundAuditClient.saveAudit(shopId, shop.getShopName(), refundNo, refundId, refundDTO);
    }

    public ShopInfoDTO getShopIsNullThrowEx(Long shopId) {
        return merchantUtils.getShopIsNullThrowEx(shopId);
    }

    public MerchantInfoDTO getMerchantIsNullThrowEx(Long merchantId) {
        return merchantUtils.getMerchantIsNullThrowEx(merchantId);
    }

    public Boolean shopAndMerchantIsNull(Long shopId) {
        try {
            ShopInfoDTO shop = getShopIsNullThrowEx(shopId);
            getMerchantIsNullThrowEx(shop.getMerchantId());
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 描述:添加退款订单 并返回ic
     *
     * @param refundNo
     * @param order
     * @return java.lang.Long
     * @author zengzhangni
     * @date 2020/5/20 9:45
     * @since v1.1.0
     */
    public Long saveRefundOrder(String refundNo, Order order) {
        return ClientUtil.getDate(refundClient.saveRefundOrder(refundNo, order));
    }


    /**
     * 描述: 设置订单为售后
     *
     * @param orderId
     * @return void
     * @author zengzhangni
     * @date 2020/3/31 16:59
     */
    public void updateAfterSales(Long orderId) {
        orderClient.updateAfterSales(orderId);
    }


    /**
     * 描述: 添加充值订单
     *
     * @param userId
     * @param userType
     * @param ruleId
     * @param rechargeOrderNo
     * @param rechargeAccount
     * @param givenAccount
     * @param ip
     * @return void
     * @author zengzhangni
     * @date 2020/3/31 17:38
     */
    public void saveChargeOrder(Long userId, Integer userType, Long ruleId, String rechargeOrderNo, BigDecimal rechargeAccount, BigDecimal givenAccount, String ip) {
        chargeOrderClient.saveChargeOrder(userId, userType, ruleId, rechargeOrderNo, rechargeAccount, givenAccount, ip);
    }


    /**
     * 描述: 订单是否已经支付完成
     *
     * @param order
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/3/31 18:06
     */
    public Boolean isPaySuccess(Order order) {
        Integer status = order.getStatus();
        return OrderUtils.SUCCESS_STATUS.contains(status);
    }

    /**
     * 描述:支付成功更新订单
     *
     * @param order
     * @param paidTime
     * @param amount
     * @param payWay
     * @return void
     * @author zengzhangni
     * @date 2020/3/31 18:26
     */
    public void updateOrderByPaySuccess(Order order, LocalDateTime paidTime, BigDecimal amount, PayWayEnum payWay) {
        Order newOrder = new Order();
        newOrder.setId(order.getId());
        newOrder.setStatus(orderStatus(order));
        newOrder.setPaidAmount(amount);
        newOrder.setPaidTime(paidTime);
        newOrder.setPayWay(payWay.getPayWay());
        orderClient.updateOrderByPaySuccess(newOrder);
        //订单为配送订单-更新立即配送时间[按照支付时间为准计算一次]
        if (DeliveryWayEnum.Delivery.getCode().equals(order.getDeliveryWay())) {
            ShopInfoDTO shop = merchantUtils.getShop(order.getStoreId());
            Result<OrderDelivery> orderDeliveryData = deliveryClient.getByOrderId(order.getId());
            OrderDelivery orderDelivery = orderDeliveryData.getData();
            if (Objects.nonNull(shop) && Objects.nonNull(orderDelivery)) {
                //是否是立即配送
                if (Objects.nonNull(orderDelivery.getImmediateDeliveryTime())) {
                    // 获取用户收货地址信息
                    Result<AddressDetailVo> addressDetailVoResult = this.addressClient.userAddressDetail(orderDelivery.getDeliveryId());
                    if (addressDetailVoResult.failure()) {
                        log.error("获取用户收货地址信息失败【Msg：{}】", addressDetailVoResult.getMsg());
                        throw new CustomException("获取收货地址信息失败");
                    }
                    AddressDetailVo addressDetailVo = addressDetailVoResult.getData();
                    String userMapCoordinate = addressDetailVo.getMapCoordinate();
                    String bdLocation = GpsCoordinateUtils.calGCJ02toBD09(userMapCoordinate);
                    double distance = EsUtil.distance(bdLocation, shop.getMapCoordinate());
                    //获取需要添加的分钟数
                    double addMin = EsUtil.calMinuteByDistance(distance);
                    LocalDateTime immediateDeliveryTime = LocalDateTime.now().plusMinutes(Math.round(addMin));
                    orderDelivery.setImmediateDeliveryTime(immediateDeliveryTime);
                    deliveryClient.updateOrderDelivery(BaseUtil.objToObj(orderDelivery, CateringOrdersDeliveryEntity.class));
                }
            }
        }

    }


    /**
     * 描述: 更新订单状态为取消
     *
     * @param order
     * @return void
     * @author zengzhangni
     * @date 2020/4/1 15:29
     */
    public void updateStatusToCanceled(Order order) {
        orderClient.updateStatusToCanceled(order.getId(), "您的订单已取消", order.getMemberId(), order.getMemberName());
    }


    /**
     * 描述: 取消团购
     *
     * @param payWay
     * @param order
     * @return void
     * @author zengzhangni
     * @date 2020/4/2 16:25
     */
    public void cancelGroupImpl(PayEnum payWay, Order order) {
        MyPayService payService = PayContext.getPayService(payWay);
        payService.cancel(order);
    }

    /**
     * 描述: 用户取消团购订单 通知秒杀
     *
     * @param orderId
     * @return void
     * @author zengzhangni
     * @date 2020/4/1 16:54
     */
    @Async
    public void sendSeckillMsg(Long orderId) {
        orderClient.sendSeckillMsg(orderId, true);
    }

    /**
     * 描述:解码支付密码
     *
     * @param password
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/4/2 16:24
     */
    public String desEncrypt(String password) {
        return AesEncryptUtil.desEncrypt(password, encryptPasswordProperties.getKey(), encryptPasswordProperties.getIv());
    }

    /**
     * 描述:查询充值订单
     *
     * @param rechargeNo
     * @return com.meiyuan.catering.finance.entity.CateringUserChargeOrderEntity
     * @author zengzhangni
     * @date 2020/4/2 16:27
     */
    public ChargeOrder getByRechargeNo(String rechargeNo) {
        return ClientUtil.getDate(chargeOrderClient.getByRechargeNo(rechargeNo));
    }

    /**
     * 描述:查询充值规则
     *
     * @param ruleId
     * @return com.meiyuan.catering.core.dto.pay.recharge.RechargeRule
     * @author zengzhangni
     * @date 2020/5/20 9:32
     * @since v1.1.0
     */
    public RechargeRule getRuleById(Long ruleId, Long activityId) {
        RechargeRule rechargeRule = ClientUtil.getDate(ruleClient.getRuleById(ruleId, activityId));
        if (rechargeRule == null) {
            throw new CustomException("暂无可用充值活动，请刷新后重新进行充值");
        }
        return rechargeRule;
    }


    /**
     * 描述:查询用户账户信息
     *
     * @param userId
     * @return com.meiyuan.catering.finance.entity.CateringUserBalanceAccountEntity
     * @author zengzhangni
     * @date 2020/4/2 16:32
     */
    public BalanceAccountInfo userAccountInfo(Long userId) {
        return ClientUtil.getDate(accountClient.userAccountInfo(userId));
    }

    /**
     * 描述:通过订单编号查询订单
     *
     * @param orderNumber
     * @return com.meiyuan.catering.order.entity.CateringOrdersEntity
     * @author zengzhangni
     * @date 2020/4/2 16:33
     */
    public Order getByOrderNumber(String orderNumber) {
        return ClientUtil.getDate(orderClient.getByOrderNumber(orderNumber));
    }

    /**
     * 描述:通过系统流水号查询订单
     *
     * @param tradingFlow
     * @return com.meiyuan.catering.order.entity.CateringOrdersEntity
     * @author zengzhangni
     * @date 2020/4/2 16:33
     */
    public Order getByTradingFlow(String tradingFlow) {
        return ClientUtil.getDate(orderClient.getByTradingFlow(tradingFlow));
    }


    public void groupCancelNotify(Long orderId, Long userId) {
        GrouponMemberQuitDTO quitDTO = new GrouponMemberQuitDTO();
        List<OrderGoods> list = ClientUtil.getDate(orderGoodsClient.getByOrderId(orderId));

        if (list.size() > 0) {
            OrderGoods entity = list.get(0);
            quitDTO.setMGoodsId(entity.getGoodsId());
            quitDTO.setOrderNumber(entity.getOrderNumber());
            quitDTO.setUserId(userId);
            quitDTO.setGoodsNumber(entity.getQuantity());
            // V1.3.0 添加订单ID
            quitDTO.setOrderId(orderId);
            senderClient.sendGroupOrderCancelMsg(quitDTO);
        } else {
            log.debug("没有查询到团购信息");
        }
    }

    /**
     * 描述:下单成功短信通知
     *
     * @param order
     * @return void
     * @author zengzhangni
     * @date 2020/4/7 11:47
     */
    @Async
    public void smsNotify(Order order) {
        OrderDelivery delivery = ClientUtil.getDate(deliveryClient.getByOrderId(order.getId()));
        String code = delivery.getConsigneeCode();
        String storeName = delivery.getStoreName();
        //不是团购订单
        if (!Objects.equals(order.getOrderType(), OrderTypeEnum.BULK.getStatus())) {
            if (Objects.equals(order.getDeliveryWay(), DeliveryWayEnum.invite.getCode())) {
                //发送自提短信
                String[] paramStr = new String[]{code, storeName, String.valueOf(order.getId())};
                notifyService.paySmsTemplate(delivery.getConsigneePhone(), NotifyType.SELF_PICKUP_NOTIFY, paramStr);
                //下单成功距自提1小时短信通知
                sendPickSmsPushMsg(order.getId(), code, delivery);
            } else {
                //发送配送短信
                String[] paramStr = new String[]{code, String.valueOf(order.getId())};
                notifyService.paySmsTemplate(delivery.getConsigneePhone(), NotifyType.HOME_DELIVERY_NOTIFY, paramStr);
            }
        }
    }

    /**
     * 描述:下单成功距自提1小时短信通知
     *
     * @param orderId
     * @param code
     * @param delivery
     * @return void
     * @author zengzhangni
     * @date 2020/4/11 10:06
     */
    private void sendPickSmsPushMsg(Long orderId, String code, OrderDelivery delivery) {
        OrderPickSmsMqDTO smsMqDTO = new OrderPickSmsMqDTO();
        smsMqDTO.setOrderId(orderId);
        smsMqDTO.setConsigneeCode(code);
        smsMqDTO.setCreateTime(delivery.getCreateTime());
        smsMqDTO.setEstimateTime(delivery.getEstimateTime());
        smsMqDTO.setEstimateEndTime(delivery.getEstimateEndTime());
        smsMqDTO.setPhone(delivery.getConsigneePhone());
        senderClient.sendPickSmsPushMsg(smsMqDTO);
    }

    /**
     * 描述:添加订单进度
     *
     * @param order
     * @return void
     * @author zengzhangni
     * @date 2020/4/10 16:09
     */
    @Async
    public void saveOrderOperation(Order order) {
        operationClient.saveOperation(order, "您的订单已取消", OrderOperationEnum.CANCELED, OrderOperationTypeEnum.MEMBER);
        operationClient.saveOperation(order, "订单已退款", OrderOperationEnum.REFUND, OrderOperationTypeEnum.MEMBER);
    }

    @Async
    public void wxPayOrderDispose(AllinPayOrderDetailResult result) {
        String transactionId = result.getBuyerBizUserId();
        LocalDateTime payTime = result.getPayDatetime();
        String outTradeNo = result.getBizOrderNo();
        BigDecimal payAmount = PayUtil.fenToYuan(result.getAmount().toString());

        PaySuccessResult paySuccessResult = payOrderDispose(transactionId, payAmount, outTradeNo, payTime, JSON.toJSONString(result), PayWayEnum.WX_ALLIN);

        paySuccessAfter(paySuccessResult);
    }

    /**
     * 描述:同步处理微信支付订单
     *
     * @param result
     * @return void
     * @author zengzhangni
     * @date 2020/4/11 16:48
     */
    public void wxPayOrderDispose(WxPayOrderNotifyResult result) {
        String transactionId = result.getTransactionId();
        String outTradeNo = result.getOutTradeNo();
        LocalDateTime payTime = DateTimeUtil.parsePayTime(result.getTimeEnd());
        BigDecimal payAmount = PayUtil.fenToYuan(result.getTotalFee());
        PaySuccessResult paySuccessResult = payOrderDispose(transactionId, payAmount, outTradeNo, payTime, result.getXmlString(), PayWayEnum.WX);

        paySuccessAfter(paySuccessResult);
    }


    public void wxPayOrderDispose(NotifyResult notifyInfo, OrderNotifyResult result) {

        String allinOrderNo = result.getOrderNo();
        String meiYuanOrderNo = result.getBizOrderNo();
        LocalDateTime payTime = result.getPayDatetime();
        BigDecimal payAmount = PayUtil.fenToYuan(result.getAmount().toString());
        String jsonStringParams = notifyInfo.getBizContent();

        PaySuccessResult paySuccessResult = payOrderDispose(allinOrderNo, payAmount, meiYuanOrderNo, payTime, jsonStringParams, PayWayEnum.WX_ALLIN);

        paySuccessAfter(paySuccessResult);
    }


    public void paySuccessAfter(PaySuccessResult result) {
        if (result == null) {
            log.info("忽略paySuccessAfter()");
            return;
        }
        log.info("PaySuccessResult:{}", result);

        Long orderId = result.getOrderId();
        Long userId = result.getUserId();
        Boolean firstOrder = result.getFirstOrder();

        paySuccessNotify(orderId, userId, firstOrder);

    }


    /**
     * 描述: 处理支付订单
     *
     * @param transactionId 微信流水号
     * @param payAmount     支付金额
     * @param orderNumber   订单编号
     * @param payTime       支付时间
     * @param xmlResult     xml信息
     * @return void
     * @author zengzhangni
     * @date 2020/4/11 16:48
     */
    @Transactional(rollbackFor = Exception.class)
    public PaySuccessResult payOrderDispose(String transactionId, BigDecimal payAmount, String orderNumber, LocalDateTime payTime, String xmlResult, PayWayEnum payWay) {
        //查询订单
        Order order = getByTradingFlow(orderNumber);

        log.debug("order:" + order);

        //如果订单支付超时已取消/门店删除,关闭微信订单,退款
        if (shopAndMerchantIsNull(order.getStoreId()) || isSystemCanceled(order)) {
            //关闭订单,退款
            PayContext.getPayService(payWay).closeOrder(order);
            log.debug("订单支付超时已取消或门店删除");
            return null;
        }

        // 检查支付订单金额
        if (!BaseUtil.priceEquals(payAmount, order.getDiscountLaterFee())) {
            throw new CustomException("支付金额不符合");
        }
        //是否支付成功
        Boolean paySuccess = isPaySuccess(order);
        if (!paySuccess) {

            Boolean firstOrder = firstOrder(order.getMemberId());

            //支付成功更新订单
            updateOrderByPaySuccess(order, payTime, payAmount, payWay);

            //添加订单交易流水
            saveOrdersTransactionFlow(order, xmlResult, transactionId, payWay, TransactionFlowEnum.PAY);

            //保存分账信息
            saveOrderSplitBill(order, payAmount);

            //下单成功短信通知
            smsNotify(order);

            return new PaySuccessResult(order.getId(), order.getMemberId(), firstOrder);
        } else {
            log.debug("订单已经处理成功!");
        }
        return null;
    }

    /**
     * 描述:余额支付 or 0元支付
     * <p>
     * 只判断是否存在补贴信息
     *
     * @param order
     * @return void
     * @author zengzhangni
     * @date 2020/10/16 16:18
     * @since v1.5.0
     */
    public void saveOrderSplitBill(Order order) {

        long billEntityId = IdWorker.getId();
        CateringOrdersSplitBillEntity billEntity = new CateringOrdersSplitBillEntity();
        billEntity.setId(billEntityId);
        billEntity.setOrderId(order.getId());
        billEntity.setOrderNumber(order.getTradingFlow());
        billEntity.setPayUser(order.getMemberId().toString());
        billEntity.setTradeCode(TradeCodeEnums.E_COMMERCE_AGENT_COLLECT.getCode());

        List<CateringOrdersSplitBillSubsidyFlowEntity> subsidyFlowList = new ArrayList<>();
        //计算补贴金额
        BigDecimal subsidyAmount = calculateSubsidyAmount(order, billEntityId, subsidyFlowList);

        if (BaseUtil.isGtZero(subsidyAmount)) {
            //分账金额 = 补贴金额
            billEntity.setSplitAmount(subsidyAmount);
            billEntity.setOrderAmount(BigDecimal.ZERO);
            billEntity.setSubsidyAmount(subsidyAmount);
            //保存分账主信息
            ordersSplitBillClint.save(billEntity);
            //保存补贴转账信息
            ordersSplitBillSubsidyFlowClint.saveBatch(subsidyFlowList);
        }


    }

    public void saveOrderSplitBill(Order order, BigDecimal payAmount) {

        long billEntityId = IdWorker.getId();
        CateringOrdersSplitBillEntity billEntity = new CateringOrdersSplitBillEntity();
        billEntity.setId(billEntityId);
        billEntity.setOrderId(order.getId());
        billEntity.setOrderNumber(order.getTradingFlow());
        billEntity.setPayUser(order.getMemberId().toString());
        billEntity.setTradeCode(TradeCodeEnums.E_COMMERCE_AGENT_COLLECT.getCode());

        List<CateringOrdersSplitBillOrderFlowEntity> orderFlowList = new ArrayList<>();
        //计算订单分账金额
        calculateOrderAmount(order, payAmount, billEntityId, orderFlowList);

        List<CateringOrdersSplitBillSubsidyFlowEntity> subsidyFlowList = new ArrayList<>();
        //计算补贴金额
        BigDecimal subsidyAmount = calculateSubsidyAmount(order, billEntityId, subsidyFlowList);

        //分账金额 = 订单金额 + 补贴金额
        billEntity.setSplitAmount(payAmount.add(subsidyAmount));
        billEntity.setOrderAmount(payAmount);
        billEntity.setSubsidyAmount(subsidyAmount);
        //保存分账主信息
        ordersSplitBillClint.save(billEntity);
        //保存订单分账信息
        ordersSplitBillOrderFlowClint.saveBatch(orderFlowList);
        //保存补贴转账信息
        ordersSplitBillSubsidyFlowClint.saveBatch(subsidyFlowList);

    }

    private void calculateOrderAmount(Order order, BigDecimal payAmount, long billEntityId, List<CateringOrdersSplitBillOrderFlowEntity> orderFlowList) {
        Long shopId = order.getStoreId();

        //添加门店分账信息
        addOrderFlow(orderFlowList, billEntityId, shopId, payAmount, OrderSplitBillTypeEnum.ORDER.getType());

        CateringOrdersShopDebtEntity debtEntity = ordersShopDebtClient.queryByShopId(shopId);
        //查询商家负债金额
        BigDecimal debtAmount = debtEntity.getAmount();

        if (debtAmount.doubleValue() > 0) {
            //如果本次订单金额大于等于负债总金额  本次还款负债总金额  否则  本次还款门店分账总金额
            debtAmount = payAmount.subtract(debtAmount).doubleValue() >= 0 ? debtAmount : payAmount;

            //更新负债信息(减去门店还款分账金额)
            Boolean update = ordersShopDebtClient.subtractDebtAmount(shopId, debtAmount);

            if (update) {
                // 添加分账信息 平台获取的门店还款分账金额
                addOrderFlow(orderFlowList, billEntityId, AllinpayConstant.PLATEFORM_BIZ_USER_ID, debtAmount, OrderSplitBillTypeEnum.DEBT.getType());

                //新增还款信息
                saveDebtFlow(order, shopId, debtEntity, debtAmount, 2, "订单还款");
            }
        }
    }

    private void addOrderFlow(List<CateringOrdersSplitBillOrderFlowEntity> orderFlowList, long billEntityId, Long collectionUser, BigDecimal orderSplitAmount, Integer type) {
        CateringOrdersSplitBillOrderFlowEntity shopSplitBill =
                new CateringOrdersSplitBillOrderFlowEntity(billEntityId, collectionUser, orderSplitAmount, type, SplitTypeEnum.SPLIT.getType());
        orderFlowList.add(shopSplitBill);
    }

    private void addOrderFlow(List<CateringOrdersSplitBillOrderFlowEntity> orderFlowList, long billEntityId, String collectionUser, BigDecimal orderSplitAmount, Integer type) {
        CateringOrdersSplitBillOrderFlowEntity shopSplitBill =
                new CateringOrdersSplitBillOrderFlowEntity(billEntityId, collectionUser, orderSplitAmount, type, SplitTypeEnum.INNER_BUCKLE.getType());
        orderFlowList.add(shopSplitBill);
    }

    private void saveDebtFlow(Order order, Long shopId, CateringOrdersShopDebtEntity debtEntity, BigDecimal debtAmount, int amountType, String remark) {
        CateringOrdersShopDebtFlowEntity debtFlowEntity = new CateringOrdersShopDebtFlowEntity();
        debtFlowEntity.setShopDebtId(debtEntity.getId());
        debtFlowEntity.setShopId(shopId);
        debtFlowEntity.setOrderId(order.getId());
        debtFlowEntity.setAmount(debtAmount);
        debtFlowEntity.setDebtType(1);
        debtFlowEntity.setAmountType(amountType);
        debtFlowEntity.setRemarks(remark);
        ordersShopDebtFlowClient.save(debtFlowEntity);
    }

    private BigDecimal calculateSubsidyAmount(Order order, long billEntityId, List<CateringOrdersSplitBillSubsidyFlowEntity> subsidyFlowList) {
        //补贴金额
        BigDecimal subsidyAmount = BigDecimal.ZERO;
        //查询订单补贴金额
        BigDecimal orderSubsidyAmount = orderClient.getOrderSubsidyAmount(order.getId());
        if (orderSubsidyAmount != null) {
            CateringOrdersSplitBillSubsidyFlowEntity splitBillSubsidyFlowEntity = new CateringOrdersSplitBillSubsidyFlowEntity();
            splitBillSubsidyFlowEntity.setId(IdWorker.getId());
            splitBillSubsidyFlowEntity.setSplitBillId(billEntityId);
            splitBillSubsidyFlowEntity.setCollectionUser(order.getStoreId().toString());
            splitBillSubsidyFlowEntity.setOrderSubsidyAmount(orderSubsidyAmount);
            splitBillSubsidyFlowEntity.setType(SubsidyTypeEnums.TICKET.getType());
            subsidyFlowList.add(splitBillSubsidyFlowEntity);
            //补贴金额累加
            subsidyAmount = subsidyAmount.add(orderSubsidyAmount);
        }
        return subsidyAmount;
    }


    /**
     * 描述:更新订单系统流水
     *
     * @param id
     * @param wxPayNo
     * @return void
     * @author zengzhangni
     * @date 2020/5/21 9:19
     * @since v1.1.0
     */
    public void updateOrderTradingFlow(Long id, String wxPayNo) {
        orderClient.updateOrderTradingFlow(id, wxPayNo);
    }

    /**
     * 描述: 是否是系统取消订单
     *
     * @param order
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/8/27 8:59
     * @since v1.3.0
     */
    private Boolean isSystemCanceled(Order order) {
        return Objects.equals(order.getStatus(), OrderStatusEnum.CANCELED.getValue())
                && Objects.equals(order.getUpdateName(), OrderOffTypeEnum.AUTO_OFF.getDesc());
    }

    @Async
    public void asyncDisposeSplitBill(String msg, Boolean verifyAfterSales) {

        log.info("处理分账开始");

        Long orderId = Long.valueOf(msg);
        Order order = queryOrder(orderId);
        if (order == null) {
            log.error("订单不存在,orderId:{}", orderId);
            return;
        }

        if (!Objects.equals(order.getPayWay(), PayWayEnum.WX_ALLIN.getPayWay()) && !Objects.equals(order.getPayWay(), PayWayEnum.ZERO_PAY.getPayWay())) {
            log.error("订单:{},不是微信支付,暂不处理分账", orderId);
            return;
        }

        //是否验证售后 并且 订单已申请售后
        if (verifyAfterSales && order.getAfterSales()) {
            log.info("{}:订单已申请售后【AfterSales：{}】,暂不处理分账", orderId, order.getAfterSales());
            return;
        }

        Long shopId = order.getStoreId();
        //商家已完成结算信息录入
        if (PayUtil.shopSignStatus(shopId)) {

            //订单分账
            batchAgentPay(orderId, order.getPayWay());

            //补贴转账
            applicationTransfer(orderId);

        } else {

            log.info("商家未完成结算信息录入");

            updateToWaiting(orderId);
        }

        log.info("处理分账结束");
    }

    private void batchAgentPay(Long orderId, Integer payWay) {
        try {
            log.info("订单分账");
            //订单分账
            if (Objects.equals(payWay, PayWayEnum.ZERO_PAY.getPayWay())) {
                log.info("0元订单不执行订单分账操作，订单ID：{}", orderId);
                return;
            }
            myOrderService.batchAgentPay(orderId);
        } catch (AllinpayException e) {
            log.info("{}:订单分账发生异常,{}", orderId, e);
            //更新订单分账信息为失败
            updateOrderToFail(orderId, e.getMessage());
        } catch (Exception ex) {
            log.info("{}:订单分账发生异常,{}", orderId, ex);
        }
    }

    private void applicationTransfer(Long orderId) {
        try {
            log.info("补贴转账");
            //补贴转账
            myOrderService.applicationTransfer(orderId);
        } catch (AllinpayException e) {
            log.info("{}:补贴转账发生异常,{}", orderId, e);
            //更新订单分账信息为失败
            updateSubsidyToFail(orderId, e.getMessage());
        } catch (Exception ex) {
            log.info("{}:补贴转账发生异常,{}", orderId, ex);
        }
    }


    private void updateToWaiting(Long orderId) {
        //修改分账信息未待分账
        CateringOrdersSplitBillEntity billEntity = ordersSplitBillClint.getByOrderId(orderId);
        Long billId = billEntity.getId();

        //更新订单分账信息为待交易
        ordersSplitBillOrderFlowClint.updateToWaiting(billId, ErrorCode.SHOP_SIGN_STATUS_FAIL_MSG);
        //更新补贴信息为待交易
        ordersSplitBillSubsidyFlowClint.updateToWaiting(billId, ErrorCode.SHOP_SIGN_STATUS_FAIL_MSG);
    }

    private void updateOrderToFail(Long orderId, String failMsg) {
        //修改分账信息未待分账
        CateringOrdersSplitBillEntity billEntity = ordersSplitBillClint.getByOrderId(orderId);
        Long billId = billEntity.getId();
        //更新订单分账信息为待交易
        ordersSplitBillOrderFlowClint.updateToWaiting(billId, failMsg);
    }

    private void updateSubsidyToFail(Long orderId, String failMsg) {
        //修改分账信息未待分账
        CateringOrdersSplitBillEntity billEntity = ordersSplitBillClint.getByOrderId(orderId);
        Long billId = billEntity.getId();
        //更新订单分账信息为待交易
        ordersSplitBillSubsidyFlowClint.updateToWaiting(billId, failMsg);
    }


    /**
     * 修改分账提现流水状态
     *
     * @param notifyResult 提现通知结果
     * @param notifyStr    完整提现通知结果
     * @author: GongJunZheng
     * @date: 2020/10/10 10:09
     * @return: void
     * @version V1.5.0
     **/
    public void withdrawApplyDispose(OrderNotifyResult notifyResult, String notifyStr) {
        WithdrawFlowStatusUpdateDTO updateDTO = new WithdrawFlowStatusUpdateDTO();
        updateDTO.setId(Long.parseLong(notifyResult.getExtendInfo()));
        updateDTO.setNotifyMessage(notifyStr);
        updateDTO.setSuccessTime(notifyResult.getPayDatetime());
        String status = notifyResult.getStatus();
        if (PayStatusEnums.OK.getCode().equals(status)) {
            updateDTO.setTradeStatus(TradeStatusEnum.SUCCESS.getStatus());
        } else if (PayStatusEnums.PENDING.getCode().equals(status)) {
            updateDTO.setTradeStatus(TradeStatusEnum.TRADE.getStatus());
        } else {
            updateDTO.setTradeStatus(TradeStatusEnum.FAIL.getStatus());
        }
        ordersSplitBillWithdrawFlowClint.updateTradeStatus(updateDTO);
    }

    /**
     * 处理异常分账信息
     *
     * @author: GongJunZheng
     * @date: 2020/10/10 11:23
     * @return: void
     * @version V1.5.0
     **/
    @Async
    public void makeAbnormalSplitBill() {

        // 处理异常订单分账信息

        myOrderService.makeOrderAbnormalSplitBill();

        // 处理异常补贴分账信息

        myOrderService.makeSubsidyAbnormalSplitBill();

    }

    @Async
    public void allinPayWithdraw() {

        // 查询已完成结算信息录入的商家ID集合
        Result<List<Long>> shopIdListResult = shopExtClient.listFinalAuditStatus();
        List<Long> shopIdList = shopIdListResult.getData();
        if (BaseUtil.judgeList(shopIdList)) {
            Set<Long> finalShopIdList = new HashSet<>(shopIdList);
            myOrderService.withdrawApply(finalShopIdList);
        }

    }

}
