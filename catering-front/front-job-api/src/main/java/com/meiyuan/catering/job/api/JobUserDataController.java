package com.meiyuan.catering.job.api;

import com.meiyuan.catering.job.service.sharebill.JobShareBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yaoozu
 * @description 用户数据处理
 * @date 2020/3/3110:45
 * @since v1.0.0
 */
@RestController
@RequestMapping("user")
public class JobUserDataController {
    @Autowired
    private JobShareBillService shareBillService;

    /**
     * @description 定时清理商品模式拼单数据(拼单人、购物车)
     * @author yaozou
     * @date 2020/3/31 10:55
     * @since v1.0.0
     */
    @RequestMapping("/cancelShareBillForGoodsModel")
    public void cancelShareBillForGoodsModel() {
        shareBillService.autoCancelForGoodsModel();
    }
}
