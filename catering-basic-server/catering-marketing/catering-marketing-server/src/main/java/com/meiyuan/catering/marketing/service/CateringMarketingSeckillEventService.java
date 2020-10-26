package com.meiyuan.catering.marketing.service;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.seckillevent.MarketingSeckillEventPageQueryDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEventEntity;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventDetailVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventPageQueryVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author GongJunZheng
 * @date 2020/08/04 16:08
 * @description 平台秒杀活动场次服务层
 **/

public interface CateringMarketingSeckillEventService {

    /**
    * 秒杀场次信息列表分页查询
    * @param dto 查询条件
    * @author: GongJunZheng
    * @date: 2020/8/28 14:31
    * @return: {@link PageData<CateringMarketingSeckillEventEntity>}
    * @version V1.3.0
    **/
    PageData<CateringMarketingSeckillEventEntity> pageQuery(MarketingSeckillEventPageQueryDTO dto);

    /**
    * 添加/编辑秒杀场次信息
    * @param seckillEventEntity 添加/编辑的秒杀场次信息
    * @author: GongJunZheng
    * @date: 2020/8/28 14:31
    * @return: {@link Boolean}
    * @version V1.3.0
    **/
    Boolean addOrEdit(CateringMarketingSeckillEventEntity seckillEventEntity);

    /**
    * 删除指定ID的场次信息
    * @param eventId 场次ID
    * @author: GongJunZheng
    * @date: 2020/8/28 14:32
    * @return: {@link Boolean}
    * @version V1.3.0
    **/
    Boolean del(Long eventId);

    /**
    * 校验当前场次是否存在商户端在使用
    * @param eventId 场次ID
    * @param dateTime 日期
    * @author: GongJunZheng
    * @date: 2020/8/28 14:33
    * @return: {@link Boolean}
    * @version V1.3.0
    **/
    Boolean canDel(Long eventId, LocalDateTime dateTime);

    /**
    * 根据场次ID集合查询场次信息
    * @param ids 场次ID集合
    * @author: GongJunZheng
    * @date: 2020/8/28 14:34
    * @return: {@link List<CateringMarketingSeckillEventEntity>}
    * @version V1.3.0
    **/
    List<CateringMarketingSeckillEventEntity> listByIds(List<Long> ids);

    /**
    * 获取指定ID的场次信息
    * @param eventId 场次ID
    * @author: GongJunZheng
    * @date: 2020/8/28 14:36
    * @return: {@link CateringMarketingSeckillEventEntity}
    * @version V1.3.0
    **/
    CateringMarketingSeckillEventEntity get(Long eventId);

    /**
    * 根据场次开始时间正序排序查询列表（无条件）
    * @param
    * @author: GongJunZheng
    * @date: 2020/8/28 14:37
    * @return: {@link List<CateringMarketingSeckillEventEntity>}
    * @version V1.3.0
    **/
    List<CateringMarketingSeckillEventEntity> entityList();

    /**
     * 根据秒杀场次ID集合查询秒杀场次信息集合
     * @param eventIds 秒杀场次ID集合
     * @author: GongJunZheng
     * @date: 2020/9/2 16:21
     * @return: {@link List<CateringMarketingSeckillEntity>}
     * @version V1.4.0
     **/
    List<CateringMarketingSeckillEventEntity> selectListByIds(Set<Long> eventIds);
}
