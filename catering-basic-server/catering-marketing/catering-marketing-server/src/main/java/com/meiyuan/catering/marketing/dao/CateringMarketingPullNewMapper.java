package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.marketing.entity.CateringMarketingPullNewEntity;
import com.meiyuan.catering.marketing.vo.pullnew.MarketingPullNewCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/04 15:08
 * @description 秒杀拉新用户明细表(catering_marketing_pull_new)数据库访问层
 **/

@Mapper
public interface CateringMarketingPullNewMapper extends BaseMapper<CateringMarketingPullNewEntity> {

    /**
    * 根据营销ID集合查询活动拉新人数统计
    * @param marketingIds 营销活动ID集合
    * @author: GongJunZheng
    * @date: 2020/8/28 14:27
    * @return: {@link List<MarketingPullNewCountVO>}
    * @version V1.3.0
    **/
    List<MarketingPullNewCountVO> pullNewCount(@Param("marketingIds") List<Long> marketingIds);
}
