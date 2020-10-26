package com.meiyuan.catering.merchant.service.notify;

import com.meiyuan.catering.allinpay.model.bean.notify.NotifyResult;
import com.meiyuan.catering.allinpay.model.bean.notify.SignContractNotifyResult;
import com.meiyuan.catering.merchant.feign.ShopExtClient;
import com.meiyuan.catering.pay.util.PayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zengzhangni
 * @date 2020/10/9 10:30
 * @since v1.1.0
 */
@Service
@Slf4j
public class MerchantAllinNotifyService {

    @Autowired
    private ShopExtClient shopExtClient;

    public Boolean signContractNotify(String data) {

        log.info("result:{}", data);
        NotifyResult notifyInfo = PayUtil.getNotifyInfo(data);
        SignContractNotifyResult result = notifyInfo.getResult();

        String contractNo = result.getContractNo();
        String bizUserId = result.getBizUserId();

        return shopExtClient.updateSignContract(bizUserId, contractNo);
    }
}
