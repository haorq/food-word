package com.meiyuan.catering.marketing.service;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.MarketingGoodsUpdateDTO;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponAloneTestDTO;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName CateringMarketingEsService
 * @Description 活动Es数据处理Service
 * @Author gz
 * @Date 2020/3/24 19:48
 * @Version 1.1
 */
public interface CateringMarketingEsService {
    /**
     * 获取所有的团购/秒杀商品数据
     * @return
     */
    List<MarketingToEsDTO> findAll();

    /**
     * 方法描述: 平台编辑商品同步ES<br>
     *
     * @author: gz
     * @date: 2020/7/21 14:38
     * @param dto
     * @return: {@link Result}
     * @version 1.2.0
     **/
    Result updateMarketingGoods(MarketingGoodsUpdateDTO dto);

    /**
     * V1.4.0 版本测试团购时间修改并同步ES（测试专用）
     * @param dto 条件
     * @author: GongJunZheng
     * @date: 2020/9/23 9:56
     * @return: LocalDateTime 旧的结束时间
     * @version V1.4.0
     **/
    LocalDateTime updateGrouponTime(MarketingGrouponAloneTestDTO dto);

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
    void sendGrouponTimedTaskMsg(Long id, LocalDateTime endTime, MarketingUpDownStatusEnum down);
}
