package com.meiyuan.catering.pay.service.impl.allin;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.meiyuan.catering.allinpay.enums.service.DeviceTypeEnums;
import com.meiyuan.catering.allinpay.enums.service.order.*;
import com.meiyuan.catering.allinpay.model.bean.order.BatchPay;
import com.meiyuan.catering.allinpay.model.bean.order.CollectPay;
import com.meiyuan.catering.allinpay.model.bean.order.SplitRuleDetail;
import com.meiyuan.catering.allinpay.model.param.order.*;
import com.meiyuan.catering.allinpay.model.result.order.*;
import com.meiyuan.catering.allinpay.service.IOrderService;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.AllinpayException;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.feign.ShopBankClient;
import com.meiyuan.catering.merchant.vo.shop.bank.BankInfoVo;
import com.meiyuan.catering.merchant.vo.shop.bank.ShopBankInfoVo;
import com.meiyuan.catering.order.dto.splitbill.SubsidyFlowStatusUpdateDTO;
import com.meiyuan.catering.order.dto.splitbill.WithdrawFlowStatusUpdateDTO;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillEntity;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillOrderFlowEntity;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillSubsidyFlowEntity;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillWithdrawFlowEntity;
import com.meiyuan.catering.order.enums.SplitTypeEnum;
import com.meiyuan.catering.order.enums.TradeStatusEnum;
import com.meiyuan.catering.order.feign.OrdersSplitBillClint;
import com.meiyuan.catering.order.feign.OrdersSplitBillOrderFlowClint;
import com.meiyuan.catering.order.feign.OrdersSplitBillSubsidyFlowClint;
import com.meiyuan.catering.order.feign.OrdersSplitBillWithdrawFlowClint;
import com.meiyuan.catering.order.vo.splitbill.CateringOrdersSplitBillOrderFlowVO;
import com.meiyuan.catering.pay.enums.NotifyEnum;
import com.meiyuan.catering.pay.service.MyOrderService;
import com.meiyuan.catering.pay.util.PayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述: 订单服务
 *
 * @author zengzhangni
 * @date 2020/10/9 9:27
 * @since v1.5.0
 */
@Service
@Slf4j
public class MyOrderServiceImpl implements MyOrderService {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private ShopBankClient shopBankClient;
    @Autowired
    private OrdersSplitBillClint ordersSplitBillClint;
    @Autowired
    private OrdersSplitBillOrderFlowClint ordersSplitBillOrderFlowClint;
    @Autowired
    private OrdersSplitBillSubsidyFlowClint ordersSplitBillSubsidyFlowClint;
    @Autowired
    private OrdersSplitBillWithdrawFlowClint ordersSplitBillWithdrawFlowClint;

    private static final Long AMOUNT_ZERO = 0L;

    private static final Long CAN_WITHDRAW_MIN_AMOUNT = 10000L;

    @Override
    public AllinPaySignalAgentPayResult signalAgentPay(Long orderId) {
        CateringOrdersSplitBillEntity billEntity = ordersSplitBillClint.getByOrderId(orderId);
        if (billEntity == null) {
            throw new CustomException("分账信息不存在");
        }
        Long billEntityId = billEntity.getId();
        List<CateringOrdersSplitBillOrderFlowEntity> orderBillList = ordersSplitBillOrderFlowClint.listByBillId(billEntityId);
        if (CollectionUtils.isEmpty(orderBillList)) {
            throw new CustomException("订单分账信息不存在");
        }
        CateringOrdersSplitBillOrderFlowEntity orderFlowEntity = orderBillList.get(0);

        String orderNumber = billEntity.getOrderNumber();
        Long amount = PayUtil.yuanToFenL(billEntity.getOrderAmount());
        List<CollectPay> collectPayList = new ArrayList<>(1);
        collectPayList.add(new CollectPay(orderNumber, amount));

        AllinPaySignalAgentPayParams params = AllinPaySignalAgentPayParams.builder()
                .bizUserId(orderBillList.get(0).getCollectionUser())
                .bizOrderNo(PayUtil.getAgentPayOrderNo())
                .amount(amount)
                .fee(AMOUNT_ZERO)
                .accountSetNo(PayUtil.getAccountSetNo(orderService))
                .collectPayList(collectPayList)
                .tradeCode(TradeCodeEnums.E_COMMERCE_AGENT_CPAY.getCode())
                .backUrl(PayUtil.getNotifyUrl(orderService, NotifyEnum.AGENT_PAY))
                .extendInfo(orderFlowEntity.getId().toString())
                .build();

        return orderService.signalAgentPay(params);
    }

    @Override
    public AllinPayBatchAgentPayResult batchAgentPay(Long orderId) {
        CateringOrdersSplitBillEntity billEntity = ordersSplitBillClint.getByOrderId(orderId);
        if (billEntity == null) {
            throw new CustomException("分账信息不存在");
        }
        Long billEntityId = billEntity.getId();
        List<CateringOrdersSplitBillOrderFlowEntity> orderBillList = ordersSplitBillOrderFlowClint.listByBillId(billEntityId);
        if (CollectionUtils.isEmpty(orderBillList)) {
            throw new CustomException("订单分账信息不存在");
        }

        String orderNumber = billEntity.getOrderNumber();

        List<BatchPay> batchPayList = getPayList(orderNumber, orderBillList);


        AllinPayBatchAgentPayParams params = AllinPayBatchAgentPayParams.builder()
                .bizBatchNo(PayUtil.getAgentPayBatchNo())
                .tradeCode(TradeCodeEnums.E_COMMERCE_AGENT_CPAY.getCode())
                .batchPayList(batchPayList).build();

        return orderService.batchAgentPay(params);
    }

    private List<BatchPay> getPayList(String orderNumber, List<CateringOrdersSplitBillOrderFlowEntity> orderBillList) {

        //分账列表
        List<CateringOrdersSplitBillOrderFlowEntity> splitList = orderBillList.stream()
                .filter(e -> SplitTypeEnum.SPLIT.getType().equals(e.getSplitType())).collect(Collectors.toList());
        //<分账Id , 内扣列表>
        Map<Long, List<CateringOrdersSplitBillOrderFlowEntity>> innerMap = orderBillList.stream()
                .filter(e -> SplitTypeEnum.INNER_BUCKLE.getType().equals(e.getSplitType()))
                .collect(Collectors.groupingBy(CateringOrdersSplitBillOrderFlowEntity::getSplitBillId));


        return splitList.stream().map(e -> {
            Long amount = PayUtil.yuanToFenL(e.getOrderSplitAmount());
            List<CollectPay> collectPayList = new ArrayList<>(1);
            collectPayList.add(new CollectPay(orderNumber, amount));

            List<CateringOrdersSplitBillOrderFlowEntity> innerList = innerMap.get(e.getSplitBillId());

            List<SplitRuleDetail> ruleDetails = null;
            if (innerList != null) {
                ruleDetails = innerList.stream().map(bill -> {
                    Long splitAmount = PayUtil.yuanToFenL(bill.getOrderSplitAmount());
                    SplitRuleDetail splitRuleDetail = new SplitRuleDetail();
                    splitRuleDetail.setBizUserId(bill.getCollectionUser());
                    splitRuleDetail.setAccountSetNo(PayUtil.getSplitAccountNo(orderService));
                    splitRuleDetail.setAmount(splitAmount);
                    splitRuleDetail.setFee(0L);
                    return splitRuleDetail;
                }).collect(Collectors.toList());
            }

            return BatchPay.builder()
                    .bizUserId(e.getCollectionUser())
                    .bizOrderNo(PayUtil.getAgentPayOrderNo())
                    .amount(amount)
                    .fee(AMOUNT_ZERO)
                    .accountSetNo(PayUtil.getAccountSetNo(orderService))
                    .backUrl(PayUtil.getNotifyUrl(orderService, NotifyEnum.AGENT_PAY))
                    .extendInfo(e.getSplitBillId().toString())
                    .collectPayList(collectPayList)
                    .splitRuleList(ruleDetails)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public void applicationTransfer(Long orderId) {
        // 查询分账信息
        CateringOrdersSplitBillEntity splitBillEntity = ordersSplitBillClint.getByOrderId(orderId);
        if (null == splitBillEntity) {
            throw new CustomException("分账信息不存在");
        }
        List<CateringOrdersSplitBillSubsidyFlowEntity> splitBillSubsidyFlowEntityList =
                ordersSplitBillSubsidyFlowClint.listBySplitBillId(splitBillEntity.getId(), TradeStatusEnum.NOT.getStatus());
        if (BaseUtil.judgeList(splitBillSubsidyFlowEntityList)) {
            splitBillSubsidyFlowEntityList.forEach(this::makeApplicationTransfer);
        }
    }

    private SubsidyFlowStatusUpdateDTO setSubsidyStatusUpdateDTO(Long id, String thirdTradeNumber, TradeStatusEnum tradeStatus,
                                                                 String failMessage, LocalDateTime successTime, String requestMessage) {
        SubsidyFlowStatusUpdateDTO updateDTO = new SubsidyFlowStatusUpdateDTO();
        updateDTO.setId(id);
        updateDTO.setThirdTradeNumber(thirdTradeNumber);
        updateDTO.setTradeStatus(tradeStatus.getStatus());
        updateDTO.setFailMessage(failMessage);
        updateDTO.setSuccessTime(successTime);
        updateDTO.setRequestMessage(requestMessage);
        return updateDTO;
    }

    private void makeApplicationTransfer(CateringOrdersSplitBillSubsidyFlowEntity item) {
        ApplicationTransferParams params = ApplicationTransferParams.builder()
                .bizTransferNo(PayUtil.getTransferNo())
                .sourceAccountSetNo(PayUtil.getSubsidyAccountNo(orderService))
                .targetBizUserId(item.getCollectionUser())
                .targetAccountSetNo(PayUtil.getAccountSetNo(orderService))
                .amount(PayUtil.yuanToFenL(item.getOrderSubsidyAmount()))
                .extendInfo("平台" + SubsidyTypeEnums.parse(item.getType()) + "补贴")
                .build();
        try {
            ApplicationTransferResult result = orderService.applicationTransfer(params);
            SubsidyFlowStatusUpdateDTO statusUpdateDTO = setSubsidyStatusUpdateDTO(item.getId(), result.getTransferNo(),
                    TradeStatusEnum.SUCCESS, null, LocalDateTime.now(), JSON.toJSONString(params));
            ordersSplitBillSubsidyFlowClint.updateTradeStatus(statusUpdateDTO);
        } catch (AllinpayException e) {
            SubsidyFlowStatusUpdateDTO statusUpdateDTO = setSubsidyStatusUpdateDTO(item.getId(), null,
                    TradeStatusEnum.FAIL, e.getMessage(), null, JSON.toJSONString(params));
            ordersSplitBillSubsidyFlowClint.updateTradeStatus(statusUpdateDTO);
        }
    }

    @Override
    public void withdrawApply(Long shopId) {
        // 查询门店账户余额信息
        AllinpayQueryBalanceParams queryBalanceParams = AllinpayQueryBalanceParams.builder()
                .accountSetNo(PayUtil.getAccountSetNo(orderService))
                .build();
        queryBalanceParams.setBizUserId(shopId.toString());
        AllinpayQueryBalanceResult queryBalanceResult;
        try {
            queryBalanceResult = orderService.queryBalance(queryBalanceParams);
        } catch (Exception e) {
            log.warn("调用通联账户余额查询接口异常：{}", e.getMessage());
            return;
        }
        // 总金额
        Long allAmount = queryBalanceResult.getAllAmount();
        // 冻结金额
        Long freezenAmount = queryBalanceResult.getFreezenAmount();
        // 可提现金额
        long withdrawAmountLong = allAmount - freezenAmount;

        if (withdrawAmountLong == AMOUNT_ZERO) {
            // 没有可提现的金额
            return;
        }
        // 判断可提现金额是否大于100元。小于不体现；大于进行提现
        if (withdrawAmountLong < CAN_WITHDRAW_MIN_AMOUNT) {
            log.warn("最低可提现金额为100元，当前账户余额为{}元，暂无法进行提现操作", PayUtil.fenToYuan(Integer.valueOf(Long.toString(withdrawAmountLong))));
            return;
        }

        // 保存分账提现流水信息
        BigDecimal withdrawAmountBigDecimal = PayUtil.fenToYuan(Long.toString(withdrawAmountLong));
        CateringOrdersSplitBillWithdrawFlowEntity insertEntity = setSplitBillWithdrawFlow(shopId, withdrawAmountBigDecimal);
        if (null == insertEntity) {
            return;
        }
        ordersSplitBillWithdrawFlowClint.insert(insertEntity);
        // 调用提现接口
        AllinPayWithdrawApplyParams params = AllinPayWithdrawApplyParams.builder()
                .bizOrderNo(insertEntity.getId().toString())
                .bizUserId(shopId.toString())
                .accountSetNo(PayUtil.getAccountSetNo(orderService))
                .amount(withdrawAmountLong)
                .fee(PayUtil.yuanToFenL(insertEntity.getFee()))
                .validateType(ValidateTypeEnums.NO_VALIDATE.getType())
                .backUrl(PayUtil.getNotifyUrl(orderService, NotifyEnum.WITHDRAW_APPLY))
                .bankCardNo(insertEntity.getCollectionAccount())
                .bankCardPro(insertEntity.getCollectionAccountPro())
                .withdrawType(WithdrawTypeEnums.D1.getCode())
                .industryCode(IndustryEnums.CATERING_PLATFORM.getCode())
                .industryName(IndustryEnums.CATERING_PLATFORM.getName())
                .source(DeviceTypeEnums.PC.getType())
                .extendInfo(insertEntity.getId().toString())
                .build();
        try {
            AllinPayWithdrawApplyResult withdrawApplyResult = orderService.withdrawApply(params);
            WithdrawFlowStatusUpdateDTO updateDTO;
            if (PayStatusEnums.SUCCESS.getCode().equals(withdrawApplyResult.getPayStatus())) {
                updateDTO = setWithdrawStatusUpdateDTO(insertEntity.getId(), withdrawApplyResult.getOrderNo(),
                        TradeStatusEnum.SUCCESS, null, LocalDateTime.now(), JSON.toJSONString(params));
            } else if (PayStatusEnums.PENDING.getCode().equals(withdrawApplyResult.getPayStatus())) {
                updateDTO = setWithdrawStatusUpdateDTO(insertEntity.getId(), withdrawApplyResult.getOrderNo(),
                        TradeStatusEnum.TRADE, null, null, JSON.toJSONString(params));
            } else {
                updateDTO = setWithdrawStatusUpdateDTO(insertEntity.getId(), withdrawApplyResult.getOrderNo(),
                        TradeStatusEnum.FAIL, withdrawApplyResult.getPayFailMessage(), null, JSON.toJSONString(params));
            }
            ordersSplitBillWithdrawFlowClint.updateTradeStatus(updateDTO);
        } catch (AllinpayException e) {
            WithdrawFlowStatusUpdateDTO updateDTO = setWithdrawStatusUpdateDTO(insertEntity.getId(), null,
                    TradeStatusEnum.FAIL, e.getMessage(), null, JSON.toJSONString(params));
            ordersSplitBillWithdrawFlowClint.updateTradeStatus(updateDTO);
        }
    }

    private CateringOrdersSplitBillWithdrawFlowEntity setSplitBillWithdrawFlow(Long shopId, BigDecimal withdrawAmount) {
        // 查询门店银行卡信息
        Result<ShopBankInfoVo> shopBankInfoResult = shopBankClient.getShopBankInfo(shopId);
        ShopBankInfoVo shopBankInfo = shopBankInfoResult.getData();
        List<BankInfoVo> bankInfoList = shopBankInfo.getBankInfoList();
        if (BaseUtil.judgeList(bankInfoList)) {
            // 设置提现信息
            CateringOrdersSplitBillWithdrawFlowEntity entity = new CateringOrdersSplitBillWithdrawFlowEntity();
            entity.setId(IdWorker.getId());
            entity.setTradingNumber(entity.getId().toString());
            entity.setCollectionUser(shopId.toString());
            entity.setCollectionAccount(bankInfoList.get(0).getBankCardNo());
            entity.setCollectionAccountPro(shopBankInfo.getBankCardPro());
            entity.setWithdrawAmount(withdrawAmount);
            entity.setFeeRate(BigDecimal.ZERO);
            entity.setFee(BigDecimal.ZERO);
            entity.setType(WithdrawTypeEnums.D1.getCode());
            entity.setTradeStatus(TradeStatusEnum.NOT.getStatus());
            entity.setDel(DelEnum.NOT_DELETE.getFlag());
            entity.setCreateTime(LocalDateTime.now());
            return entity;
        }
        return null;
    }

    private WithdrawFlowStatusUpdateDTO setWithdrawStatusUpdateDTO(Long id, String thirdTradeNumber,
                                                                   TradeStatusEnum tradeStatus, String failMessage,
                                                                   LocalDateTime successTime, String requestMessage) {
        WithdrawFlowStatusUpdateDTO updateDTO = new WithdrawFlowStatusUpdateDTO();
        updateDTO.setId(id);
        updateDTO.setThirdTradeNumber(thirdTradeNumber);
        updateDTO.setTradeStatus(tradeStatus.getStatus());
        updateDTO.setFailMessage(failMessage);
        updateDTO.setSuccessTime(successTime);
        updateDTO.setRequestMessage(requestMessage);
        return updateDTO;
    }

    @Override
    public void makeOrderAbnormalSplitBill() {
        List<CateringOrdersSplitBillOrderFlowVO> orderFlowList = ordersSplitBillOrderFlowClint.listAbnormal();
        if (BaseUtil.judgeList(orderFlowList)) {
            Map<Long, List<CateringOrdersSplitBillOrderFlowVO>> collect = orderFlowList.stream()
                    .collect(Collectors.groupingBy(CateringOrdersSplitBillOrderFlowVO::getSplitBillId));
            collect.forEach((splitBillId, list) -> {
                // 过滤未完成结算信息录入的商家
                List<CateringOrdersSplitBillOrderFlowVO> finalCollect = list.stream()
                        .filter(item -> (Objects.equals(item.getSplitType(), SplitTypeEnum.INNER_BUCKLE.getType())
                                || (Objects.equals(item.getSplitType(), SplitTypeEnum.SPLIT.getType()) && PayUtil.shopSignStatus(Long.parseLong(item.getCollectionUser())))))
                        .collect(Collectors.toList());
                if (BaseUtil.judgeList(finalCollect)) {

                    // 调用通联接口
                    List<BatchPay> batchPayList = getPayList(finalCollect.get(0).getOrderNumber(),
                            BaseUtil.objToObj(finalCollect, CateringOrdersSplitBillOrderFlowEntity.class));

                    AllinPayBatchAgentPayParams params = AllinPayBatchAgentPayParams.builder()
                            .bizBatchNo(PayUtil.getAgentPayBatchNo())
                            .tradeCode(TradeCodeEnums.E_COMMERCE_AGENT_CPAY.getCode())
                            .batchPayList(batchPayList).build();
                    orderService.batchAgentPay(params);
                }
            });
        }
    }

    @Override
    public void makeSubsidyAbnormalSplitBill() {
        List<CateringOrdersSplitBillSubsidyFlowEntity> subsidyFlowList = ordersSplitBillSubsidyFlowClint.listAbnormal();
        if (BaseUtil.judgeList(subsidyFlowList)) {
            Map<Long, List<CateringOrdersSplitBillSubsidyFlowEntity>> collect = subsidyFlowList.stream()
                    .collect(Collectors.groupingBy(CateringOrdersSplitBillSubsidyFlowEntity::getSplitBillId));
            collect.forEach((splitBillId, list) -> {
                // 过滤未完成结算信息录入的商家
                List<CateringOrdersSplitBillSubsidyFlowEntity> finalCollect = list.stream()
                        .filter(item -> PayUtil.shopSignStatus(Long.parseLong(item.getCollectionUser()))).collect(Collectors.toList());
                if (BaseUtil.judgeList(finalCollect)) {
                    finalCollect.forEach(this::makeApplicationTransfer);
                }
            });
        }
    }

    @Override
    public void withdrawApply(Set<Long> shopIdList) {
        shopIdList.forEach(this::withdrawApply);
    }


    @Override
    public AllinpayQueryBalanceResult queryBalance(Long shopId) {
        AllinpayQueryBalanceParams queryBalanceParams = AllinpayQueryBalanceParams.builder()
                .accountSetNo(PayUtil.getAccountSetNo(orderService))
                .build();
        queryBalanceParams.setBizUserId(shopId.toString());
        return orderService.queryBalance(queryBalanceParams);

    }
}
