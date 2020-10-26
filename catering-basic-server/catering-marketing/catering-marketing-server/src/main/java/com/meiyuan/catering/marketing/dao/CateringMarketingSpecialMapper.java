package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/09/03 09:09
 * @description 营销特价商品活动信息Mapper
 **/

@Mapper
public interface CateringMarketingSpecialMapper extends BaseMapper<CateringMarketingSpecialEntity> {

    /**
    * 根据门店ID查询正在进行的有效的营销特价商品活动名称
    * @param shopId 门店ID
    * @param id 活动ID
    * @author: GongJunZheng
    * @date: 2020/9/3 10:43
    * @return: {@link List<String>}
    * @version V1.4.0
    **/
    List<String> verifySpecialInfo(@Param("shopId") Long shopId, @Param("id") Long id);

    /**
    * 根据门店ID，商品SKU集合查询正在进行的有效的营销特价商品SKU信息
    * @param shopId 门店ID
    * @param goodsSkuList 商品SKU集合
    * @param id 活动ID
    * @param beginTime 开始时间
    * @param endTime 结束时间
    * @author: GongJunZheng
    * @date: 2020/9/3 10:59
    * @return: {@link }
    * @version V1.4.0
    **/
    List<String> verifySkuInfo(@Param("shopId") Long shopId,
                               @Param("goodsSkuList") List<String> goodsSkuList,
                               @Param("id") Long id,
                               @Param("beginTime") LocalDateTime beginTime,
                               @Param("endTime") LocalDateTime endTime);

    /**
    * 修改指定营销商品的进行状态
    * @param specialId 营销特价商品活动ID
    * @param status 进行状态
    * @param now 当前时间
    * @author: GongJunZheng
    * @date: 2020/9/7 17:07
    * @return: {@link }
    * @version V1.4.0
    **/
    void updateStatusById(@Param("specialId") Long specialId,
                          @Param("status") Integer status,
                          @Param("now") LocalDateTime now);

    /**
    * 查询需要延迟发送开始消息的活动
    * @author: GongJunZheng
    * @date: 2020/9/7 17:33
    * @return: List<CateringMarketingSpecialEntity>
    * @version V1.4.0
    **/
    List<CateringMarketingSpecialEntity> needBeginList();

    /**
     * 查询需要延迟发送开始消息的活动
     * @author: GongJunZheng
     * @date: 2020/9/7 17:33
     * @return: List<CateringMarketingSpecialEntity>
     * @version V1.4.0
     **/
    List<CateringMarketingSpecialEntity> needEndList();

    /**
     * 修改营销特价商品活动上/下架状态
     * @param specialId 营销特价商品活动ID
     * @param upDownStatus 营销特价商品活动上/下架状态
     * @param now 当前时间
     * @author: GongJunZheng
     * @date: 2020/9/7 18:53
     * @return: void
     * @version V1.4.0
     **/
    void updateUpDown(@Param("specialId") Long specialId,
                      @Param("upDownStatus") Integer upDownStatus,
                      @Param("now") LocalDateTime now);

    /**
    * 判断所选时间段是否有有效的活动存在
    * @param shopId 店铺ID
    * @param id 活动ID
    * @param beginTime 开始时间
    * @param endTime 结束时间
    * @author: GongJunZheng
    * @date: 2020/9/14 16:01
    * @return: {@link int}
    * @version V1.4.0
    **/
    List<String> verifySpecialExisted(@Param("shopId") Long shopId,
                             @Param("id") Long id,
                             @Param("beginTime") LocalDateTime beginTime,
                             @Param("endTime") LocalDateTime endTime);
}
