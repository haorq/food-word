package com.meiyuan.catering.job.api;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.job.service.goods.JobMerchantGoodsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhm
 * @date 2020/7/14
 * @description
 **/
@RestController
@RequestMapping(value = "merchant/goods")
@Slf4j
public class JobMerchantGoodsUpOrDownController {


    @Autowired
    private JobMerchantGoodsService jobMerchantGoodsService;


    /**
     * 描述：库存自动自满--凌晨执行
     * @author lhm
     * @date 2020/7/14
     * @param
     * @return {@link }
     * @version 1.2.0
     **/
    @ApiOperation("库存自动自满")
    @GetMapping("/isFullStock")
    public void isFullStock() {
        log.debug("库存自动自满 开始");
        jobMerchantGoodsService.isFullStock();
        log.debug("库存自动自满 结束");
    }
    /**
     * 方法描述: 处理商品预售结束定时任务 凌晨00：02执行<br>
     *
     * @author: gz
     * @date: 2020/7/18 9:52
     * @param
     * @return: {@link Result}
     * @version 1.2.0
     **/
    @ApiOperation(value = "预售商品结束定时下架操作")
    @GetMapping(value = "goodsPresellTask")
    public Result goodsPresellTask(){
        return jobMerchantGoodsService.goodsPresellTask();
    }
}
