package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.MarketingGoodsInfoDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponGoodsDetailVO;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSeckillGrouponPageQueryVO;
import com.meiyuan.catering.marketing.vo.seckill.MarketingSeckillGoodsDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 营销商品表(CateringMarketingGoods)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-10 11:25:05
 */
@Mapper
public interface CateringMarketingGoodsMapper extends BaseMapper<CateringMarketingGoodsEntity>{
    /**
     * 通过关联id删除
     * @param ofId
     * @return
     */
    int removeGoods(Long ofId);

    /**
     * 获取商品信息
     * @param goodsId
     * @param sku
     * @param shopId
     * @return
     */
    List<MarketingGoodsInfoDTO> selectGoodsInfo(@Param("goodsId") Long goodsId,
                                                @Param("list") List<String> sku,
                                                @Param("shopId") Long shopId,
                                                @Param("merchantId") Long merchantId);

    /**
    * 根据秒杀场次关系ID集合删除营销秒杀商品信息
    * @param relationIds 秒杀场次关系ID集合
    * @author: GongJunZheng
    * @date: 2020/8/6 16:48
    * @return: {@link }
    **/
    int delSeckillGoods(@Param("list") List<Long> relationIds);

    /**
    * 根据秒杀活动ID查询营销秒杀商品
    * @param seckillId 秒杀活动ID
    * @param type 是否需要查询已被删除的商品信息 0-否 1-是
    * @author: GongJunZheng
    * @date: 2020/8/6 16:47
    * @return: {@link List<MarketingSeckillGoodsDetailVO>}
    **/
    List<MarketingSeckillGoodsDetailVO> detailSeckillGoods(@Param("seckillId") Long seckillId,
                                                           @Param("type") Integer type);

    /**
     * 秒杀营销商品列表分页查询
     * @param pageCondition 分页条件
     * @param seckillId 秒杀活动ID
     * @author: GongJunZheng
     * @date: 2020/8/17 11:23
     * @return: {@link Page<MarketingSeckillGoodsDetailVO>}
     * @version V1.3.0
     **/
    IPage<MarketingSeckillGoodsDetailVO> detailSeckillGoodsPage(@Param("page") Page<Object> pageCondition,
                                                                @Param("seckillId") Long seckillId);

    /**
     * 团购营销商品列表查询
     *
     * @param grouponId 团购活动ID
     * @param type 是否需要查询已被删除的商品信息 0-否 1-是
     * @author: GongJunZheng
     * @date: 2020/8/9 16:20
     * @return: {@link List<MarketingGrouponGoodsDetailVO>}
     * @version V1.3.0
     **/
    List<MarketingGrouponGoodsDetailVO> detailGrouponGoods(@Param("grouponId") Long grouponId,
                                                           @Param("type") Integer type);

    /**
     * 团购详情商品分页查询
     * @param pageCondition 分页条件
     * @param grouponId 团购活动ID
     * @author: GongJunZheng
     * @date: 2020/8/17 10:49
     * @return: {@link Page<MarketingGrouponGoodsDetailVO>}
     * @version V1.3.0
     **/
    IPage<MarketingGrouponGoodsDetailVO> detailGrouponGoodsPage(@Param("page") Page<Object> pageCondition,
                                                                     @Param("grouponId") Long grouponId);

    /**
    * 自己的营销活动分页查询
    * @param pageCondition 分页条件
    * @param marketingId 营销活动ID
    * @author: GongJunZheng
    * @date: 2020/8/19 14:55
    * @return: {@link }
    * @version V1.3.0
    **/
    IPage<CateringMarketingGoodsEntity> mySelectPage(@Param("page") Page<CateringMarketingGoodsEntity> pageCondition,
                                                     @Param("marketingId") Long marketingId);

    /**
    * 查询所有的营销商品集合（包括已删除商品）
    * @param marketingId 营销活动ID
    * @author: GongJunZheng
    * @date: 2020/8/19 16:35
    * @return: {@link }
    * @version V1.3.0
    **/
    List<CateringMarketingGoodsEntity> mySelectList(@Param("marketingId") Long marketingId);

    /**
     * 根据商品ID查询营销商品ID集合
     * @param goodsId 商品ID
     * @author: GongJunZheng
     * @date: 2020/8/25 15:57
     * @return: {@link List<Long>}
     * @version V1.3.0
     **/
    List<Long> listIdsByGoodsId(@Param("goodsId") Long goodsId);

    /**
    * 根据营销活动ID集合查询所有的商品信息（包括已删除）（店铺自己创建的商品没有推送操作，不查询店铺自创的商品）
    * @param marketingIds 营销活动ID集合
    * @author: GongJunZheng
    * @date: 2020/8/26 11:36
    * @return: {@link }
    * @version V1.3.0
    **/
    List<CateringMarketingGoodsEntity> myListByMarketingIds(@Param("marketingIds") List<Long> marketingIds);

    /**
    * 修改营销商品删除状态
    * @param ids 营销商品ID集合
    * @param delStatus 营销商品删除状态
    * @author: GongJunZheng
    * @date: 2020/8/26 11:40
    * @return: {@link }
    * @version V1.3.0
    **/
    void updateDelByIds(@Param("ids") List<Long> ids,
                        @Param("delStatus") Integer delStatus);

    /**
    * 根据营销活动ID修改营销商品删除状态
    * @param marketingIds 营销活动ID集合
    * @param delStatus 营销商品删除状态
    * @author: GongJunZheng
    * @date: 2020/8/26 17:56
    * @return: {@link }
    * @version V1.3.0
    **/
    void updateDelByMarketIds(@Param("marketingIds") List<Long> marketingIds,
                              @Param("delStatus") Integer delStatus);

    /**
    * 根据商户ID、商品ID修改商品上下架状态
    * @param merchantId 商户ID
    * @param goodsId 商品ID
    * @param upDown 上下架状态
    * @author: GongJunZheng
    * @date: 2020/8/26 13:35
    * @return: void
    * @version V1.3.0
    **/
    void updateGoodsUpDownByMerchantId(@Param("merchantId") Long merchantId,
                                       @Param("goodsId") Long goodsId,
                                       @Param("upDown") Integer upDown);

    /**
    * 根据活动ID集合以及商品ID修改商品上下架状态
    * @param marketingIdList 营销活动ID集合
    * @param goodsId
    * @param upDown
    * @author: GongJunZheng
    * @date: 2020/8/26 13:39
    * @return: void
    * @version V1.3.0
    **/
    void updateGoodsUpdownByMarketingIds(@Param("marketingIdList") List<Long> marketingIdList,
                                         @Param("goodsId") Long goodsId,
                                         @Param("upDown") Integer upDown);

    /**
    * 修改
    * @param entity 实体类
    * @author: GongJunZheng
    * @date: 2020/8/26 14:18
    * @return: void
    * @version V1.3.0
    **/
    void myUpdateById(@Param("entity") CateringMarketingGoodsEntity entity);

    /**
    * 修改指定营销商品的SKU值
    * @param id 营销商品ID
    * @param newSkuValue 新的SKU值
    * @param now 当前时间
    * @author: GongJunZheng
    * @date: 2020/8/26 15:14
    * @return: {@link }
    * @version V1.3.0
    **/
    void updateSkuValueById(@Param("id") Long id,
                            @Param("newSkuValue") String newSkuValue,
                            @Param("now") LocalDateTime now);

    /**
    * 根据商户ID/商品ID删除营销商品
    * @param merchantId 商户ID
    * @param goodsId 商品ID
    * @author: GongJunZheng
    * @date: 2020/8/26 17:45
    * @return: {@link }
    * @version V1.3.0
    **/
    void goodsDelAndCancelSync(@Param("merchantId") Long merchantId,
                               @Param("goodsId") Long goodsId);

    /**
    * 根据商品ID查询营销商品信息
    * @param merchantId 商户ID
    * @param goodsId 商品ID
    * @author: GongJunZheng
    * @date: 2020/8/26 18:08
    * @return: {@link List<CateringMarketingGoodsEntity>}
    * @version V1.3.0
    **/
    List<CateringMarketingGoodsEntity> myListByGoodsId(@Param("merchantId") Long merchantId,
                                                       @Param("goodsId") Long goodsId);

    /**
    * 活动为未开始状态，物理删除秒杀商品
    * @param merchantId 商户ID
    * @param goodsId 商品ID
    * @param now 当前时间
    * @author: GongJunZheng
    * @date: 2020/8/27 10:14
    * @return: void
    * @version V1.3.0
    **/
    void delNoBeginSeckillGoods(@Param("merchantId") Long merchantId,
                                @Param("goodsId") Long goodsId,
                                @Param("now") LocalDateTime now);

    /**
     * 活动为未开始状态，物理删除团购商品
     * @param merchantId 商户ID
     * @param goodsId 商品ID
     * @param now 当前时间
     * @author: GongJunZheng
     * @date: 2020/8/27 10:14
     * @return: void
     * @version V1.3.0
     **/
    void delNoBeginGrouponGoods(@Param("merchantId") Long merchantId,
                                @Param("goodsId") Long goodsId,
                                @Param("now") LocalDateTime now);

    /**
     * 活动不为未开始状态，逻辑删除团购商品
     * @param merchantId 商户ID
     * @param goodsId 商品ID
     * @param now 当前时间
     * @author: GongJunZheng
     * @date: 2020/8/27 10:14
     * @return: void
     * @version V1.3.0
     **/
    void logicDelSeckillGoods(@Param("merchantId") Long merchantId,
                              @Param("goodsId") Long goodsId,
                              @Param("now") LocalDateTime now);

    /**
     * 活动为不未开始状态，逻辑删除团购商品
     * @param merchantId 商户ID
     * @param goodsId 商品ID
     * @param now 当前时间
     * @author: GongJunZheng
     * @date: 2020/8/27 10:14
     * @return: void
     * @version V1.3.0
     **/
    void logicDelGrouponGoods(@Param("merchantId") Long merchantId,
                              @Param("goodsId") Long goodsId,
                              @Param("now") LocalDateTime now);


    /**
     * 活动为未开始状态，物理删除秒杀商品（根据营销商品ID集合）
     * @param delIds 营销商品ID集合
     * @param now 当前时间
     * @author: GongJunZheng
     * @date: 2020/8/27 11:04
     * @return: void
     * @version V1.3.0
     **/
    void delNoBeginSeckillGoodsByIds(@Param("delIds") List<Long> delIds,
                                     @Param("now") LocalDateTime now);

    /**
     * 活动为不未开始状态，逻辑删除秒杀商品（根据营销商品ID集合）
     * @param delIds 营销商品ID集合
     * @param now 当前时间
     * @author: GongJunZheng
     * @date: 2020/8/27 11:04
     * @return: void
     * @version V1.3.0
     **/
    void logicDelSeckillGoodsByIds(@Param("delIds") List<Long> delIds,
                                          @Param("now") LocalDateTime now);

    /**
     * 活动不为未开始状态，物理删除团购商品（根据营销商品ID集合）
     * @param delIds 营销商品ID集合
     * @param now 当前时间
     * @author: GongJunZheng
     * @date: 2020/8/27 11:12
     * @return: void
     * @version V1.3.0
     **/
    void delNoBeginGrouponGoodsByIds(@Param("delIds") List<Long> delIds,
                                     @Param("now") LocalDateTime now);

    /**
     * 活动为不未开始状态，逻辑删除团购商品（根据营销商品ID集合）
     * @param delIds 营销商品ID集合
     * @param now 当前时间
     * @author: GongJunZheng
     * @date: 2020/8/27 11:12
     * @return: void
     * @version V1.3.0
     **/
    void logicDelGrouponGoodsByIds(@Param("delIds") List<Long> delIds,
                                          @Param("now") LocalDateTime now);

    /**
    * 判断正在参加营销秒杀/团购活动的商品信息
    * @param merchantId 商户ID
    * @param goodsIds 商品ID集合
    * @param now 当前时间
    * @author: GongJunZheng
    * @date: 2020/9/18 14:04
    * @return: {@link List<CateringMarketingGoodsEntity>}
    * @version V1.4.0
    **/
    List<CateringMarketingGoodsEntity> isJoinActivity(@Param("merchantId") Long merchantId,
                                                      @Param("goodsIds") List<Long> goodsIds,
                                                      @Param("now") LocalDateTime now);
}