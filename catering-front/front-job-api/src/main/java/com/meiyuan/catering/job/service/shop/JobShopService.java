package com.meiyuan.catering.job.service.shop;

import com.meiyuan.catering.pay.util.PaySupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author GongJunZheng
 * @date 2020/10/10 18:10
 * @description 商家定时任务服务
 **/

@Slf4j
@Service
public class JobShopService {

    @Autowired
    private PaySupport paySupport;

    public void allinPayWithdraw() {
        try {
            paySupport.allinPayWithdraw();
        } catch (Exception e) {
            log.error("执行商家通联账户余额提现定时任务异常。", e);
        }

    }
}
