package com.meiyuan.catering.marketing.service;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialAddOrEditDTO;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialBeginOrEndMsgDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialEntity;

import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/09/03 09:09
 * @description 营销特价商品活动服务接口
 **/

public interface CateringMarketingSpecialService {

    /**
     * 校验营销特价活动信息
     * @param dto 特价活动信息
     * @author: GongJunZheng
     * @date: 2020/9/3 10:22
     * @return: void
     * @version V1.4.0
     **/
    void verifyInfo(MarketingSpecialAddOrEditDTO dto);

    /**
     * 创建/编辑营销特价活动活动
     * @param dto 营销特价商品活动信息
     * @author: GongJunZheng
     * @date: 2020/9/3 11:11
     * @return: {@link Result}
     * @version V1.4.0
     **/
    MarketingSpecialBeginOrEndMsgDTO createOrEdit(MarketingSpecialAddOrEditDTO dto);

    /**
     * 冻结营销特价商品活动
     * @param specialId 营销特价商品活动ID
     * @author: GongJunZheng
     * @date: 2020/9/3 13:04
     * @return: {@link Boolean}
     * @version V1.4.0
     **/
    Boolean freeze(Long specialId);

    /**
     * 删除营销特价商品活动
     * @param specialId 营销特价商品活动ID
     * @author: GongJunZheng
     * @date: 2020/9/3 13:14
     * @return: {@link Boolean}
     * @version V1.4.0
     **/
    Boolean del(Long specialId);

    /**
     * 根据营销特价商品活动ID查询营销特价商品活动信息
     * @param specialId 营销特价商品活动ID
     * @author: GongJunZheng
     * @date: 2020/9/3 13:47
     * @return: {@link CateringMarketingSpecialEntity}
     * @version V1.4.0
     **/
    CateringMarketingSpecialEntity findById(Long specialId);

    /**
     * 修改营销特价商品活动的进行状态 1-未开始 2-进行中 3-已结束
     * @param specialId 营销特价商品活动ID
     * @param status 活动状态
     * @author: GongJunZheng
     * @date: 2020/9/7 15:41
     * @return: void
     * @version V1.4.0
     **/
    void updateStatus(Long specialId, Integer status);

    /**
     * 营销特价商品活动状态改变发送消息
     * @param targetTime 时间
     * @param msgDTO 消息实体
     * @author: GongJunZheng
     * @date: 2020/9/7 15:51
     * @return: void
     * @version V1.4.0
     **/
    void sendMsg(LocalDateTime targetTime, MarketingSpecialBeginOrEndMsgDTO msgDTO);

    /**
     * 执行延迟定时开始/结束营销特价商品活动任务
     * @author: GongJunZheng
     * @date: 2020/9/7 17:31
     * @return: void
     * @version V1.4.0
     **/
    void beginOrEndTimedTask();

    /**
     * 修改营销特价商品活动上/下架状态
     * @param specialId 营销特价商品活动ID
     * @param upDownStatus 营销特价商品活动上/下架状态
     * @author: GongJunZheng
     * @date: 2020/9/7 18:53
     * @return: void
     * @version V1.4.0
     **/
    void updateUpDown(Long specialId, Integer upDownStatus);
}
