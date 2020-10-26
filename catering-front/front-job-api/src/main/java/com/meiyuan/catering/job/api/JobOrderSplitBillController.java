package com.meiyuan.catering.job.api;

import com.meiyuan.catering.job.service.order.JobOrderSplitBillService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GongJunZheng
 * @date 2020/10/10 11:10
 * @description 分账异常的定时处理
 **/

@Slf4j
@RestController
@RequestMapping(value = "/splitBill")
public class JobOrderSplitBillController {

    @Autowired
    private JobOrderSplitBillService splitBillService;

    @ApiOperation(value = "处理异常分账信息（每天晚上2点开始执行）")
    @GetMapping("/makeAbnormalSplitBill")
    public void makeAbnormalSplitBill() {
        log.info("开始执行异常分账处理定时任务...");
        splitBillService.makeAbnormalSplitBill();
    }

}
