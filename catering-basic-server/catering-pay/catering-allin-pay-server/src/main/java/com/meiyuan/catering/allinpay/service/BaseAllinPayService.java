package com.meiyuan.catering.allinpay.service;

import com.meiyuan.catering.allinpay.autoconfigure.AllinPayConfig;
import com.meiyuan.catering.allinpay.model.bean.notify.NotifyResult;
import com.meiyuan.catering.allinpay.model.result.base.QueryMerchantBalanceResult;

import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

/**
 * @author zengzhangni
 * @date 2020/9/27 9:28
 * @since v1.1.0
 */
public interface BaseAllinPayService {

    AllinPayConfig getConfig();


    Boolean checkSign(String signedValue, String sign) throws Exception;

    String decrypt(String content);

    TreeMap<String, String> getParams(String notifyParams);

    String getSignedValue(TreeMap<String, String> map) throws UnsupportedEncodingException;

    NotifyResult parseNotifyResult(String data);

    QueryMerchantBalanceResult queryMerchantBalance(String accountSetNo);
}
