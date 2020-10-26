package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.marketing.entity.CateringMarketingTicketActivityShopEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName CateringMarketingTicketActivityShopMapper
 * @Description
 * @Author gz
 * @Date 2020/8/5 17:29
 * @Version 1.3.0
 */
@Mapper
public interface CateringMarketingTicketActivityShopMapper extends BaseMapper<CateringMarketingTicketActivityShopEntity> {
    /**
     * 方法描述: 通过活动id删除关联门店信息<br>
     *
     * @author: gz
     * @date: 2020/8/5 18:22
     * @param activityId
     * @return: {@link int}
     * @version 1.3.0
     **/
    int removeByActivityId(Long activityId);
    /**
     * 方法描述: 冻结门店优惠券活动<br>
     *
     * @author: gz
     * @date: 2020/8/6 15:26
     * @param shopId
     * @param activityId
     * @return: {@link int}
     * @version 1.3.0
     **/
    int updateShopTicketStatus(@Param("shopId") Long shopId, @Param("activityId") Long activityId);
}
