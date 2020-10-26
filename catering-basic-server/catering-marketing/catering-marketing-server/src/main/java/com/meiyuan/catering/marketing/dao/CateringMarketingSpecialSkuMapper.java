package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.dto.es.MarketingSpecialSku;
import com.meiyuan.catering.marketing.dto.marketing.MarketingPcMenuUpdateSpecialReturnDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialSkuEntity;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialGoodsDetailVO;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialGoodsPageVO;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialGoodsUnitVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author GongJunZheng
 * @date 2020/09/03 09:09
 * @description 营销特价商品活动SKU信息Mapper
 **/

@Mapper
public interface CateringMarketingSpecialSkuMapper extends BaseMapper<CateringMarketingSpecialSkuEntity> {

    /**
     * 根据营销特价商品活动ID查询营销特价商品信息
     * @param merchantId 商户ID
     * @param specialId 营销特价商品活动ID
     * @author: GongJunZheng
     * @date: 2020/9/3 13:47
     * @return: {@link List<MarketingSpecialGoodsDetailVO>}
     * @version V1.4.0
     **/
    List<MarketingSpecialGoodsDetailVO> selectGoodsDetail(@Param("merchantId") Long merchantId,
                                                          @Param("specialId") Long specialId);

    /**
     * 查询营销特价商品活动详情中的分页列表商品信息
     * @param pageCondition 分页条件
     * @param specialId 营销特价商品活动ID
     * @param merchantId 商户ID
     * @param categoryId 商品分类ID
     * @param goodsName 商品名称
     * @author: GongJunZheng
     * @date: 2020/9/3 14:47
     * @return: {@link IPage<MarketingSpecialGoodsPageVO>}
     * @version V1.4.0
     **/
    IPage<MarketingSpecialGoodsPageVO> selectDetailGoodsPage(@Param("page") Page<Object> pageCondition,
                                                             @Param("specialId") Long specialId,
                                                             @Param("merchantId") Long merchantId,
                                                             @Param("categoryId") Long categoryId,
                                                             @Param("goodsName") String goodsName);

    /**
    * 物理删除还未开始的营销特价商品活动的商品信息
    * @param merchantId 店铺ID
    * @param goodsId 商品ID
    * @author: GongJunZheng
    * @date: 2020/9/8 10:16
    * @return: void
    * @version V1.4.0
    **/
    void delNoBeginGoods(@Param("merchantId") Long merchantId,
                         @Param("goodsId") Long goodsId);

    /**
    * 销特价商品活动不为未开始，逻辑删除商品信息
    * @param merchantId 店铺ID
    * @param goodsId 商品ID
    * @param now 当前时间
    * @author: GongJunZheng
    * @date: 2020/9/8 10:34
    * @return: void
    * @version V1.4.0
    **/
    void logicDelGoods(@Param("merchantId") Long merchantId,
                       @Param("goodsId") Long goodsId,
                       @Param("now") LocalDateTime now);

    /**
    * 根据商品ID修改商品名称
    * @param merchantId 商户ID
    * @param goodsId 商品ID
    * @param goodsName 商品名称
    * @author: GongJunZheng
    * @date: 2020/9/8 11:02
    * @return: void
    * @version V1.4.0
    **/
    void updateGoodsNameByGoodsId(@Param("merchantId") Long merchantId,
                                  @Param("goodsId") Long goodsId,
                                  @Param("goodsName") String goodsName);

    /**
    * 物理删除状态为未开始的营销特价商品活动的商品
    * @param delIdList 需要删除的特价商品ID集合
    * @author: GongJunZheng
    * @date: 2020/9/8 11:30
    * @return: void
    * @version V1.4.0
    **/
    void delNoBeginGoodsByIds(@Param("delIdList") List<Long> delIdList);

    /**
    * 逻辑删除状态不为未开始的营销特价商品活动的商品
    * @param delIdList 需要删除的特价商品ID集合
    * @param now 当前时间
    * @author: GongJunZheng
    * @date: 2020/9/8 11:38
    * @return: void
    * @version V1.4.0
    **/
    void logicDelGoodsByIds(@Param("delIdList") List<Long> delIdList,
                            @Param("now") LocalDateTime now);

    /**
    * 根据店铺ID查询正在进行中的营销特价商品活动的商品信息（不查询店铺自创的商品）
    * @param shopId 店铺ID
    * @author: GongJunZheng
    * @date: 2020/9/8 14:53
    * @return: {@link List<MarketingPcMenuUpdateSpecialReturnDTO>}
    * @version V1.4.0
    **/
    List<MarketingPcMenuUpdateSpecialReturnDTO> selectHavingListByShopId(@Param("shopId") Long shopId);

    /**
    * 通过店铺ID集合查询店铺的特价商品SKU信息
    * @param shopIds 店铺ID集合
    * @author: GongJunZheng
    * @date: 2020/9/14 9:09
    * @return: {@link List<CateringMarketingSpecialSkuEntity>}
    * @version V1.4.0
    **/
    List<MarketingSpecialSku> selectGoodsSkuByShopIds(@Param("shopIds") Set<Long> shopIds);

    /**
     * 通过店铺ID以及商品ID查询店铺的特价商品SKU信息
     * @param shopId 店铺ID
     * @param goodsId 商品ID
     * @author: GongJunZheng
     * @date: 2020/9/14 10:26
     * @return: {@link List<MarketingSpecialSku>}
     * @version V1.4.0
     **/
    List<MarketingSpecialSku> selectGoodsSkuByShopIdAndGoodsId(@Param("shopId") Long shopId,
                                                               @Param("goodsId") Long goodsId);

    /**
     * 根据商品ID查询特价商品信息
     * @param goodsId 商品ID
     * @author: GongJunZheng
     * @date: 2020/9/14 11:27
     * @return: {@link List<MarketingSpecialSku>}
     * @version V1.4.0
     **/
    List<MarketingSpecialSku> selectGoodsSkuByGoodsId(@Param("goodsId") Long goodsId);

    /**
    * 根据商品ID集合查询正在参加活动的商品信息集合
    * @param goodsIds 商品ID集合
    * @author: GongJunZheng
    * @date: 2020/9/18 14:34
    * @return: {@link List<CateringMarketingSpecialSkuEntity>}
    * @version V1.4.0
    **/
    List<CateringMarketingSpecialSkuEntity> isJoinActivity(@Param("merchantId") Long merchantId,
                                                           @Param("goodsIds") List<Long> goodsIds);

    /**
    * 查询商品规格的单位
    * @param goodsExtentIdList 商品扩展表ID集合
    * @author: GongJunZheng
    * @date: 2020/9/30 9:59
    * @return: {@link List<MarketingSpecialGoodsUnitVO>}
    * @version V1.5.0
    **/
    List<MarketingSpecialGoodsUnitVO> selectGoodsUnit(@Param("goodsExtentIdList") List<Long> goodsExtentIdList);

    /**
    * 根据商户ID和商品ID查询特价商品信息
    * @param merchantId 商户ID
    * @param goodsId 商品ID
    * @author: GongJunZheng
    * @date: 2020/9/30 9:59
    * @return: {@link List<CateringMarketingSpecialSkuEntity>}
    * @version V1.5.0
    **/
    List<CateringMarketingSpecialSkuEntity> listByMerchantIdAndGoodsId(@Param("merchantId") Long merchantId,
                                                                       @Param("goodsId") Long goodsId);
}
