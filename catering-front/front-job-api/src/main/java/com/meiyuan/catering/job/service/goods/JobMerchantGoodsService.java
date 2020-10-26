package com.meiyuan.catering.job.service.goods;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.marketing.feign.MarketingTicketActivityClient;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lhm
 * @date 2020/7/14
 * @description
 **/
@Slf4j
@Service
public class JobMerchantGoodsService {
    @Autowired
    private MerchantGoodsClient merchantGoodsClient;
    @Autowired
    private EsGoodsClient esGoodsClient;
    @Autowired
    private MarketingTicketActivityClient activityClient;
    /**
     * 描述：凌晨自动自满库存
     *
     * @param
     * @return {@link }
     * @author lhm
     * @date 2020/7/14
     * @version 1.2.0
     **/
    public void isFullStock() {
        merchantGoodsClient.isFullStock();
        // 自动置满品牌券每日库存
        activityClient.fillTicketStock();
    }
    /**
     * 方法描述: 处理预售商品结束下架任务<br>
     *
     * @author: gz
     * @date: 2020/7/18 10:11
     * @param
     * @return: {@link Result}
     * @version 1.2.0
     **/
    public Result goodsPresellTask() {
        log.debug("处理预售商品结束下架任务 开始");
        boolean flag = true;
        try {
            Integer task = merchantGoodsClient.goodsPresellTask();
            Integer task1 = merchantGoodsClient. goodsPresellTaskForShop();
            if(task>0 || task1>0){
                // 处理Es数据
                esGoodsClient.goodsPresellTaskSyncEs();
            }
        }catch (Exception e){
            log.error("执行预售商品结束下架任务异常:{}",e);
            flag = false;
        }
        log.debug("处理预售商品结束下架任务 结束");
        return Result.logicResult(flag);
    }
}
