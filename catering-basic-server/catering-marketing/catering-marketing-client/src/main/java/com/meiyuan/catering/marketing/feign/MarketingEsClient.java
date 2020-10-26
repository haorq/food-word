package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.MarketingGoodsUpdateDTO;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponAloneTestDTO;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.service.CateringMarketingEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName MarketingEsClient
 * @Description
 * @Author gz
 * @Date 2020/5/20 10:50
 * @Version 1.1
 */
@Service
public class MarketingEsClient {
    @Autowired
    private CateringMarketingEsService marketingEsService;

    /**
     * 获取所有的团购/秒杀商品数据
     * @return
     */
    public Result<List<MarketingToEsDTO>>  findAll(){
        return Result.succ(marketingEsService.findAll());
    }

    /**
     * 方法描述: 平台编辑商品同步ES<br>
     *
     * @author: gz
     * @date: 2020/7/21 14:38
     * @param dto
     * @return: {@link Result}
     * @version 1.2.0
     **/
    public Result updateMarketingGoods(MarketingGoodsUpdateDTO dto){
        return marketingEsService.updateMarketingGoods(dto);
    }

    /**
    * V1.4.0 版本测试团购时间修改并同步ES（测试专用）
    * @param dto 条件
    * @author: GongJunZheng
    * @date: 2020/9/23 9:56
    * @return: LocalDateTime 旧的结束时间
    * @version V1.4.0
    **/
    public LocalDateTime updateGrouponTime(MarketingGrouponAloneTestDTO dto) {
        return marketingEsService.updateGrouponTime(dto);
    }

    /**
    * V1.4.0 发送团购活动结束MQ消息（测试专用）
    * @param id 团购活动ID
    * @param endTime 团购活动结束时间
    * @param down 上/下架枚举
    * @author: GongJunZheng
    * @date: 2020/9/23 10:15
    * @return: void
    * @version V1.4.0
    **/
    public void sendGrouponTimedTaskMsg(Long id, LocalDateTime endTime, MarketingUpDownStatusEnum down) {
        marketingEsService.sendGrouponTimedTaskMsg(id, endTime, down);
    }
}
