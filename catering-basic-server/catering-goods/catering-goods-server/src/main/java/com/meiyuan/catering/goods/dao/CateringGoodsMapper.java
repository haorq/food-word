package com.meiyuan.catering.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.goods.dto.goods.*;
import com.meiyuan.catering.goods.entity.CateringGoodsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品基本信息(SPU)表(CateringGoods)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-09 17:53:09
 */
@Mapper
public interface CateringGoodsMapper extends BaseMapper<CateringGoodsEntity>{
    /**
     * 商品列表
     *
     * @author: wxf
     * @date: 2020/3/16 18:58
     * @param dto page
     * @param goodsNameCode 商品名称/编码
     * @param categoryId 分类id
     * @param goodsStatus 上下架
     * @param merchantId 上下架
     * @param startCreateTime 开始时间
     * @param endCreateTime 结束时间
     * @return: {@link IPage<GoodsListDTO>}
     **/
    IPage<GoodsListDTO> listLimit(@Param("dto") Page<GoodsLimitQueryDTO> dto,
                                  @Param("goodsNameCode") String goodsNameCode,
                                  @Param("categoryId") Long categoryId,
                                  @Param("goodsStatus") Integer goodsStatus,
                                  @Param("merchantId") Long merchantId,
                                  @Param("startCreateTime") LocalDateTime startCreateTime,
                                  @Param("endCreateTime")  LocalDateTime endCreateTime);

    /**
     * 团购秒杀商品列表
     *
     * @author: wxf
     * @date: 2020/3/19 16:16
     * @param goodsNameCode 商品名称
     * @param categoryId 分类id
     * @param merchantId 商户id
     * @param list 不包含的商品id
     * @param goodsIdList 商家对应
     * @param startSellTime 开始时间
     * @return: {@link List< GroupBuySeckillGoodsDTO>}
     **/
    List<GroupBuySeckillGoodsDTO> groupBuySeckillGoodsList(@Param("goodsNameCode") String goodsNameCode,
                                                           @Param("categoryId") Long categoryId,
                                                           @Param("merchantId") Long merchantId,
                                                           @Param("list") List<Long> list,
                                                           @Param("goodsIdList") List<Long> goodsIdList,
                                                           @Param("startSellTime")LocalDateTime startSellTime);

    /**
     * 获取分类id根据商品id集合
     *
     * @author: wxf
     * @date: 2020/3/19 17:38
     * @param goodsIdList 商品id集合
     * @return: {@link List< GoodsIdAndCategoryIdDTO>}
     **/
    List<GoodsIdAndCategoryIdDTO> getCategoryIdByGoodsIdList(List<Long> goodsIdList);


    /**
     * 商户商品列表
     *
     * @author: yaozou
     * @date: 2020/3/16 18:58
     * @param dto page
     * @param goodsNameCode 名称
     * @param categoryId 分类id
     * @param goodsStatus 上下架
     * @param merchantId 商户id
     * @return: {@link List< CateringGoodsEntity>}
     **/
    IPage<GoodsListDTO> listLimitForMerchant(@Param("page") Page<GoodsLimitQueryDTO> dto,
                                             @Param("goodsNameCode") String goodsNameCode,
                                             @Param("categoryId") Long categoryId,
                                             @Param("goodsStatus") Integer goodsStatus,
                                             @Param("merchantId") Long merchantId);



    /**
     * 批量获取根据查询条件
     *
     * @author: wxf
     * @date: 2020/5/21 14:12
     * @param goodsNameCode 名称
     * @param categoryId 分类id
     * @param idList 不包含的id集合
     * @return: {@link List< GoodsListDTO>}
     * @version 1.0.1
     **/
    List<GoodsListDTO> list(@Param("goodsNameCode") String goodsNameCode,
                            @Param("categoryId") Long categoryId,
                            @Param("idList") List<Long> idList);


    /**
     * 根据商品分类集合查询商品列表
     * @param categoryIdList
     * @author lh
     * @version 1.2.0
     * @return
     */
    List<GoodsListDTO> listByCategoryIdList(@Param("categoryIdList") List<Long> categoryIdList);


    /**
     * 最大code
     *
     * @author: wxf
     * @date: 2020/6/22 11:48
     * @return: {@link CateringGoodsEntity}
     * @version 1.1.1
     **/
    CateringGoodsEntity maxDbCode();

    /**
     * 描述：品牌管理--商品授权列表
     * @author lhm
     * @date 2020/7/6
     * @param dto
     * @param page
     * @return {@link IPage<GoodsPushList>}
     * @version 1.2.0
     **/
    IPage<GoodsPushList> pushGoodsList(Page page, @Param("dto") GoodsPushListQueryDTO dto);
}
