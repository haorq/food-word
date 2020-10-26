package com.meiyuan.catering.job.api;

import com.meiyuan.catering.job.service.goods.JobGoodsDataService;
import com.meiyuan.catering.job.service.order.JobOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wxf
 * @date 2020/4/22 16:14
 * @description 简单描述
 **/
@RestController
@RequestMapping("goods/data")
@Slf4j
public class JobGoodsDataController {
    @Resource
    JobGoodsDataService goodsDataService;
    @Resource
    private JobOrderService jobOrderService;

    /**
     * 月销
     *
     * @author: wxf
     * @date: 2020/4/22 15:54
     **/
    @RequestMapping("/monthSales")
    public void goodsMonthSales() {

        log.debug("开始执行门店商品月销量统计任务（每5分钟执行一次）");

        Integer today = 0;
        goodsDataService.goodsMonthSales(today);

        log.debug("开始执行预定配送单下发到达达");
        // 批量发单到达达
        jobOrderService.batchAddOrderToDada();
    }

    @RequestMapping("/resetMonthSales")
    public void resetMonthSales() {
        goodsDataService.resetMonthSales();
    }

    /**
     * 更新昨天的销量并加上今天的月销
     *
     * @author: wxf
     * @date: 2020/4/24 13:42
     **/
    @RequestMapping("/updateYesterdayGoodsSales")
    public void updateYesterdayGoodsSales() {
        goodsDataService.updateYesterdayGoodsSales();
    }
}
