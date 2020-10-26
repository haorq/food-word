package com.meiyuan.catering.allinpay.utils;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.allinpay.autoconfigure.AllinPayConfig;
import com.meiyuan.catering.allinpay.core.OpenClient;
import com.meiyuan.catering.allinpay.core.bean.OpenResponse;
import com.meiyuan.catering.allinpay.enums.CommonReturnCodeEnums;
import com.meiyuan.catering.allinpay.enums.ServiceResultCodeEnums;
import com.meiyuan.catering.allinpay.enums.service.ApiEnums;
import com.meiyuan.catering.core.exception.AllinpayException;
import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author zengzhangni
 * @date 2020/9/24 16:51
 * @since v1.1.0
 */
@Slf4j
public class AllinPayOpenClient {

    private OpenClient client;

    public AllinPayOpenClient(OpenClient client) {
        this.client = client;
    }

    public <T> T execute(ApiEnums api, AllinPayBaseServiceParams param, Class<T> t) {
        try {

            OpenResponse response = client.execute(api.getMethod(), param.toBizParameter());

            if (!Objects.equals(response.getCode(), CommonReturnCodeEnums.SUCCESS.getCode())) {
                log.error(response.getSubMsg());
                throw new AllinpayException(response.getCode(), response.getSubMsg());
            }
            if (!Objects.equals(response.getSubCode(), ServiceResultCodeEnums.SUCCESS.getCode())) {
                log.error(response.getSubMsg());
                throw new AllinpayException(response.getSubCode(), response.getSubMsg());
            }

            String data = response.getData();
            return JSON.parseObject(data, t);
        } catch (AllinpayException e) {
            throw e;
        } catch (Exception e) {
            log.error("exception.", e);
            throw new AllinpayException(e);
        }
    }

    public String concatUrlParams(ApiEnums api, AllinPayBaseServiceParams param) {
        try {
            return client.concatUrlParams(api.getMethod(), param.toBizParameter());
        } catch (Exception e) {
            log.error("exception.", e);
            throw new AllinpayException(e);
        }
    }

    public String encrypt(String content) {
        return client.encrypt(content);
    }

    public boolean checkSign(String signedValue, String sign) throws Exception {
        return client.checkSign(signedValue, sign);
    }

    public String decrypt(String content) {
        return client.decrypt(content);
    }


    public AllinPayConfig getConfig() {
        return client.getConfig();
    }

    public Boolean verifyResult(String result) throws Exception {
        return client.verifyResult(result);
    }
}
