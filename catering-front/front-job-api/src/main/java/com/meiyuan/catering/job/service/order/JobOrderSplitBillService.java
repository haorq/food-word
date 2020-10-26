package com.meiyuan.catering.job.service.order;

import com.meiyuan.catering.pay.util.PaySupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author GongJunZheng
 * @date 2020/10/10 11:10
 * @description 分账异常的定时处理服务
 **/

@Slf4j
@Service
public class JobOrderSplitBillService {

    @Autowired
    private PaySupport paySupport;

    public void makeAbnormalSplitBill() {
        try {
            paySupport.makeAbnormalSplitBill();
        }catch (Exception e) {
            log.error("处理异常分账信息定时任务异常。", e);
        }
    }

}
