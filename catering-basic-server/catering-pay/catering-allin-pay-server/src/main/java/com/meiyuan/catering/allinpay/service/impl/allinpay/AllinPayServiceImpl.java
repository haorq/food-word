package com.meiyuan.catering.allinpay.service.impl.allinpay;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.meiyuan.catering.allinpay.autoconfigure.AllinPayConfig;
import com.meiyuan.catering.allinpay.enums.service.ApiEnums;
import com.meiyuan.catering.allinpay.model.bean.notify.NotifyResult;
import com.meiyuan.catering.allinpay.model.param.base.QueryMerchantBalanceParams;
import com.meiyuan.catering.allinpay.model.result.base.QueryMerchantBalanceResult;
import com.meiyuan.catering.allinpay.service.BaseAllinPayService;
import com.meiyuan.catering.allinpay.utils.AllinPayOpenClient;
import com.meiyuan.catering.core.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.TreeMap;

/**
 * @author zengzhangni
 * @date 2020/9/27 9:34
 * @since v1.1.0
 */
@Service
public class AllinPayServiceImpl implements BaseAllinPayService {

    @Autowired
    private AllinPayOpenClient client;

    @Override
    public AllinPayConfig getConfig() {
        return client.getConfig();
    }

    @Override
    public Boolean checkSign(String signedValue, String sign) throws Exception {
        return client.checkSign(signedValue, sign);
    }

    @Override
    public String decrypt(String content) {
        return client.decrypt(content);
    }

    /**
     * 动态遍历获取所有收到的参数,此步非常关键,因为以后可能会加字段,动态获取可以兼容由于加字段而引起的签名异常
     * 示例：{appId=1250978269474349057&bizContent={"ContractNo":"1294180954914422784","bizUserId":"test0002","status":"OK"}&charset=utf-8&notifyId=1294180861993447426& notifyTime=2020-08-14 15:55:36&notifyType=allinpay.yunst.memberService.signContract&sign=Fq02JKa083LbNZPmYuXV6q3uM9xZKbnYSXCeRgjhKiixQpXG8sPDEIrzXaQLBR+MwFYk5LTIfShlui7mgmoTNTFM8zVX+gFAYC25cHE7k0sLo3viRiVPn4Q26hPj6j/I/dBzu7qVTHqf4hYF/d7K+7GxymKfnRjB31uUBCbumDKIB5WoptTPfmybtNytKsV6aLQ9Egwdqv1oVtnHpTyxqXj5hfA9mGUN3DFy5PhIcZ0k/9O0rKnFPVYUMOXT8IWlF1QSgSKRzYeCYUJyQWEFqtzGXEtiWmeoFm6flxhG5flR+KpR1yLBfvAhNKKePILaPY0Sve+OybQFOd3HZgwJEA==&signType=SHA256WithRSA&version=1.0}
     *
     * @param notifyParams 请求信息
     * @return String
     */
    @Override
    public TreeMap<String, String> getParams(String notifyParams) {
        TreeMap<String, String> map = new TreeMap<>();
        String[] notifyParamsArr = notifyParams.split("&");
        for (String notifyParam : notifyParamsArr) {
            String[] split = notifyParam.split("=");
            map.put(split[0], split[1]);
        }
        return map;
    }

    /**
     * 开放平台 SDK 提供了checkSign方法，可以使用client.checkSign(String signedValue,String sign)对通知报文验签，其中传入的
     * signedValue为上述报文体中除去sign、signType，其余参数皆是待验签参数，对待验签参数进行UrlDecode处理后则为signedValue。
     *
     * @param map 参数map
     * @return String
     * @throws UnsupportedEncodingException 异常
     */
    @Override
    public String getSignedValue(TreeMap<String, String> map) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        List<String> needRemoveKey = Lists.newArrayList("sign", "signType");
        map.forEach((key, value) -> {
            if (!needRemoveKey.contains(key)) {
                sb.append("&").append(key).append("=").append(value);
            }
        });
        return URLDecoder.decode(sb.substring(1), "UTF-8");
    }


    @Override
    public NotifyResult parseNotifyResult(String data) {

        try {
            Boolean aBoolean = client.verifyResult(data);

            if (aBoolean) {
                data = URLDecoder.decode(data, "UTF-8");
                return JSON.parseObject(JSON.toJSONString(getParams(data)), NotifyResult.class);
            } else {
                throw new CustomException("签名验证失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("解析失败");
        }
    }

    @Override
    public QueryMerchantBalanceResult queryMerchantBalance(String accountSetNo) {

        QueryMerchantBalanceParams params = QueryMerchantBalanceParams.builder()
                .accountSetNo(accountSetNo).build();

        return client.execute(ApiEnums.QUERY_MERCHANT_BALANCE, params, QueryMerchantBalanceResult.class);

    }

}
