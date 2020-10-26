package com.meiyuan.catering.allinpay.service.impl.allinpay;


import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.allinpay.enums.service.ApiEnums;
import com.meiyuan.catering.allinpay.enums.service.DeviceTypeEnums;
import com.meiyuan.catering.allinpay.enums.service.order.IndustryEnums;
import com.meiyuan.catering.allinpay.enums.service.order.PayMethodKeyEnums;
import com.meiyuan.catering.allinpay.enums.service.order.TradeCodeEnums;
import com.meiyuan.catering.allinpay.enums.service.order.ValidateTypeEnums;
import com.meiyuan.catering.core.exception.AllinpayException;
import com.meiyuan.catering.allinpay.model.bean.order.AllinReciever;
import com.meiyuan.catering.allinpay.model.bean.paymethod.ScanWechatPay;
import com.meiyuan.catering.allinpay.model.bean.paymethod.ScanedPayVsp;
import com.meiyuan.catering.allinpay.model.bean.paymethod.WechatPayPublic;
import com.meiyuan.catering.allinpay.model.param.order.*;
import com.meiyuan.catering.allinpay.model.result.order.*;
import com.meiyuan.catering.allinpay.service.IOrderService;
import com.meiyuan.catering.allinpay.utils.AllinPayOpenClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengzhangni
 */

@Service
public class AllinPayOrderServiceImpl extends AllinPayServiceImpl implements IOrderService {

    @Autowired
    private AllinPayOpenClient client;

    @Override
    public AllinPayAgentCollectApplyResult unifiedOrder(UnifiedOrderParams params) {
        // 收款列表
        List<AllinReciever> recieverList = new ArrayList<>();
        params.getProfitReceivers().forEach(p -> {
            recieverList.add(new AllinReciever(p.getUniqueIdentity(), p.getAmount()));
        });

        // 支付方式
        JSONObject payMethod = builderPayMethod(params);

        AllinPayAgentCollectApplyParams allinpayParams = AllinPayAgentCollectApplyParams.builder()
                .bizOrderNo(params.getPayOrderNo())
                .payerId(params.getUniqueIdentity())
                .recieverList(recieverList)
                .tradeCode(TradeCodeEnums.E_COMMERCE_AGENT_COLLECT.getCode())
                .amount(params.getTotalFee())
                .fee(params.getFee())
                .backUrl(params.getNotifyUrl())
                .orderExpireDatetime(params.getOrderExpireDatetime())
//                .payMethod(payMethod)
                .validateType(ValidateTypeEnums.NO_VALIDATE.getType())
                .industryCode(IndustryEnums.E_COMMERCE_PLATFORM.getCode())
                .industryName(IndustryEnums.E_COMMERCE_PLATFORM.getName())
                .source(DeviceTypeEnums.PC.getType())
                .summary("测试")
                .extendInfo(params.getBody())
                .build();
        return client.execute(ApiEnums.AGENT_COLLECT_APPLY, allinpayParams, AllinPayAgentCollectApplyResult.class);
    }

    @Override
    public AllinPayAgentCollectApplyResult agentCollectApply(AllinPayAgentCollectApplyParams params) {
        return client.execute(ApiEnums.AGENT_COLLECT_APPLY, params, AllinPayAgentCollectApplyResult.class);
    }

    @Override
    public AllinPayPayByPwdResult payByPwd(AllinPayPayByPwdParams params) {
        return client.execute(ApiEnums.PAY_BY_PWD, params, AllinPayPayByPwdResult.class);
    }

    @Override
    public AllinPayPayByBackSmsResult payByBackSms(AllinPayPayByBackSmsParams params) {
        return client.execute(ApiEnums.PAY_BY_BACK_SMS, params, AllinPayPayByBackSmsResult.class);
    }

    @Override
    public AllinPayPayBySmsResult payBySms(AllinPayPayBySmsParams params) {
        return client.execute(ApiEnums.PAY_BY_SMS, params, AllinPayPayBySmsResult.class);
    }

    private JSONObject builderPayMethod(UnifiedOrderParams params) {
        JSONObject payMethod;
        switch (params.getTradeType()) {
            case MINIPROGRAM:
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("limitPay", "");
                jsonObject2.put("vspCusid", "55265105812D73F");
                jsonObject2.put("amount", params.getTotalFee());
                jsonObject2.put("acct", "ojK6g4viwiQSQ0wOT_cOcwtsMl2Q");
                jsonObject.put(PayMethodKeyEnums.WECHATPAY_MINIPROGRAM_ORG.getCode(), jsonObject2);
//                WechatPayMiniProgram payMiniProgramOpen = WechatPayMiniProgram.builder()
////                        .subAppid("00197968")
//                        .amount(params.getTotalFee())
//                        .acct("ojK6g4sVK5arbDt9DROAxpS7aqVU")
//                        .build();
                payMethod = jsonObject;
                break;
            case WECHATPAY_PUBLIC:
                WechatPayPublic wechatPayPublic = WechatPayPublic.builder()
                        .subAppid(params.getSubAppid())
                        .amount(params.getTotalFee())
                        .acct(params.getUniqueIdentity())
                        .build();
                payMethod = wechatPayPublic.toJsonObject();
                break;
            case WECHATPAY_NATIVE:
                ScanWechatPay scanWechatPay = ScanWechatPay.builder()
                        .amount(params.getTotalFee())
                        .build();
                payMethod = scanWechatPay.toJsonObject();
                break;
            case WECHATPAY_NATIVE_ORG:
                JSONObject json = new JSONObject();
                JSONObject json2 = new JSONObject();
                json2.put("limitPay", "");
                json2.put("vspCusid", "55265105812D73F");
                json2.put("amount", params.getTotalFee());
                json.put(PayMethodKeyEnums.SCAN_WEIXIN_ORG.getCode(), json2);
                payMethod = json;
                break;
            case MICROPAY:
                ScanedPayVsp scanedPayVsp = ScanedPayVsp.builder()
                        .amount(params.getTotalFee())
                        .authcode(params.getAuthcode())
                        .build();
                payMethod = scanedPayVsp.toJsonObject();
                break;
            default:
                throw new AllinpayException("the pay method is not supported.");
        }
        return payMethod;
    }

    @Override
    public AllinPayRefundResult refund(AllinPayRefundParams params) {
        return client.execute(ApiEnums.REFUND, params, AllinPayRefundResult.class);
    }

    @Override
    public AllinPayOrderDetailResult orderDetail(AllinPayOrderDetailParams params) {
        return client.execute(ApiEnums.GET_ORDER_DETAIL, params, AllinPayOrderDetailResult.class);
    }

    @Override
    public AllinPaySignalAgentPayResult signalAgentPay(AllinPaySignalAgentPayParams params) {
        return client.execute(ApiEnums.SIGNAL_AGENT_PAY, params, AllinPaySignalAgentPayResult.class);
    }

    @Override
    public AllinPayBatchAgentPayResult batchAgentPay(AllinPayBatchAgentPayParams params) {
        return client.execute(ApiEnums.BATCH_AGENT_PAY, params, AllinPayBatchAgentPayResult.class);
    }

    @Override
    public AllinPayWithdrawApplyResult withdrawApply(AllinPayWithdrawApplyParams params) {
        return client.execute(ApiEnums.WITHDRAW_APPLY, params, AllinPayWithdrawApplyResult.class);
    }

    @Override
    public AllinPayQueryInExpDetailResult queryInExpDetail(AllinPayQueryInExpDetailParams params) {
        return client.execute(ApiEnums.QUERY_INEXP_DETAIL, params, AllinPayQueryInExpDetailResult.class);
    }

    @Override
    public ApplicationTransferResult applicationTransfer(ApplicationTransferParams params) {
        return client.execute(ApiEnums.APPLICATION_TRANSFER, params, ApplicationTransferResult.class);
    }

    @Override
    public AllinPayDepositApplyResult depositApply(AllinPayDepositApplyParams params) {
        return client.execute(ApiEnums.DEPOSIT_APPLY, params, AllinPayDepositApplyResult.class);
    }

    @Override
    public AllinpayQueryBalanceResult queryBalance(AllinpayQueryBalanceParams params) {
        return client.execute(ApiEnums.QUERY_BALANCE, params, AllinpayQueryBalanceResult.class);
    }
}
