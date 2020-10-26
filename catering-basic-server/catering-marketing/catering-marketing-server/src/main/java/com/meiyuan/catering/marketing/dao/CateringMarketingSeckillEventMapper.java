package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEventEntity;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/04 15:08
 * @description 平台秒杀活动场次表(catering_event)数据库访问层
 **/

@Mapper
public interface CateringMarketingSeckillEventMapper extends BaseMapper<CateringMarketingSeckillEventEntity> {


    /**
     * 校验当前场次是否存在商户端在使用
     * @param eventId 场次ID
     * @param now 当前时间
     *
     * @return 结果大于0，说明在使用，否则未使用
     */
    int verificationUse(@Param("eventId") Long eventId, @Param("now") LocalDateTime now);

}
