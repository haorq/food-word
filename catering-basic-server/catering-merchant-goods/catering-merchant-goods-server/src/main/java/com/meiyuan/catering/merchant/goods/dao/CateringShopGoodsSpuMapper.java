package com.meiyuan.catering.merchant.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.core.dto.es.ShopGoodsSku;
import com.meiyuan.catering.core.dto.es.ShopGoodsStatusMap;
import com.meiyuan.catering.core.dto.goods.GoodsSort;
import com.meiyuan.catering.core.vo.wxcategory.WxCategoryGoodsVO;
import com.meiyuan.catering.goods.dto.goods.GoodsSortDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.GoodsSortMaxDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MerchantCategoryGoodsDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSpuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Mapper
public interface CateringShopGoodsSpuMapper extends BaseMapper<CateringShopGoodsSpuEntity> {


    Integer deleteIsGoodsNull();

    /**
     * 描述:获取门店状态
     *
     * @param shopIds
     * @return java.util.List<com.meiyuan.catering.core.dto.es.ShopGoodsStatusMap>
     * @author zengzhangni
     * @date 2020/8/27 9:59
     * @since v1.3.0
     */
    List<ShopGoodsStatusMap> getShopGoodsStatus(@Param("shopIds") Set<Long> shopIds);

    /**
     * 描述:获取门店sku
     *
     * @param shopIds
     * @return java.util.List<com.meiyuan.catering.core.dto.es.ShopGoodsSku>
     * @author zengzhangni
     * @date 2020/8/27 9:59
     * @since v1.3.0
     */
    List<ShopGoodsSku> getShopGoodsSkus(@Param("shopIds") Set<Long> shopIds);

    /**
     * 方法描述   获取分类下商品最大值
     * @author: lhm
     * @date: 2020/9/14 10:38
     * @param categoryIds
     * @param shopId
     * @return: {@link }
     * @version 1.3.0
     **/
    List<GoodsSortMaxDTO> getShopGoodsSortMaxList(@Param("categoryIds") Set<Long> categoryIds, @Param("shopId") Long shopId);


    /**
     * 方法描述   商品的分类
     * @author: lhm
     * @date: 2020/9/14 10:38
     * @param goodsIds
     * @param merchantId
     * @return: {@link }
     * @version 1.3.0
     **/
    List<GoodsSortMaxDTO> selectGoods(@Param("goodsIds") Set<Long> goodsIds, @Param("merchantId") Long merchantId);

/**
 * 方法描述   分类下门店商品的最大值
 * @author: lhm
 * @date: 2020/9/14 10:38
 * @param categoryId
 * @param shopId
 * @return: {@link }
 * @version 1.3.0
 **/
    GoodsSortMaxDTO getShopGoodsSortMax(@Param("categoryId") Long categoryId, @Param("shopId") Long shopId);

    /**
     * 方法描述
     * @author: lhm
     * @date: 2020/9/14 10:39
     * @param categoryId
     * @param shopId
     * @return: {@link }
     * @version 1.3.0
     **/
    List<GoodsSort> getShopGoodsSort(@Param("categoryId") Long categoryId, @Param("merchantId") Long merchantId);

    /**
     * 方法描述
     * @author: lhm
     * @date: 2020/9/14 10:39
     * @param categoryId
     * @param shopId
     * @return: {@link }
     * @version 1.3.0
     **/
    List<GoodsSortMaxDTO> getSortMaxList(@Param("categoryId") Long categoryId, @Param("shopIds") Set<Long> shopId);


    /**
     * 方法描述
     * @author: lhm
     * @date: 2020/9/14 10:39
     * @param categoryId
     * @param shopId
     * @return: {@link }
     * @version 1.3.0
     **/
    GoodsSortMaxDTO getShopGoodsSortMin(@Param("categoryId") Long categoryId, @Param("shopId") Long shopId);

    /**
     * 描述:门店存在的sku
     *
     * @param shopIds
     * @param goodsMerchantId
     * @return java.util.List<java.lang.Long>
     * @author zengzhangni
     * @date 2020/8/27 9:59
     * @since v1.3.0
     */
    List<Long> existSkuShop(@Param("shopIds") List<Long> shopIds, @Param("goodsMerchantId") Long goodsMerchantId);

    /**
     * 方法描述
     * @author: lhm
     * @date: 2020/9/14 10:40
     * @param goodsId
 * @param merchantId
     * @return: {@link }
     * @version 1.3.0
     **/
    List<Long> getIdByGoodsId(@Param("goodsId") Long goodsId, @Param("merchantId") Long merchantId);

    /**
     * describe: 根绝关联集合id查询商品
     * @author: yy
     * @date: 2020/9/14 17:07
     * @param spuIdList
     * @return: {@link List< WxCategoryGoodsVO>}
     * @version 1.4.0
     **/
    List<WxCategoryGoodsVO> queryByIdList(@Param("spuIdList") List<Long> spuIdList);


    /**
     * 方法描述   获取商户下对应分类id
     * @author: lhm
     * @date: 2020/9/16 11:27
     * @param merchantId
     * @param goodsIds
     * @return: {@link }
     * @version 1.3.0
     **/
    List<MerchantCategoryGoodsDTO> selectCategoryId(@Param("merchantId") Long merchantId, @Param("goodsIds") Set<Long> goodsIds);

    void deleteCategoty(@Param("shopId") Long shopId, @Param("categoryIds") Set<Long> categoryIds);




    /**
     * 方法描述
     * @author: lhm
     * @date: 2020/9/18 16:10
     * @param dto
     * @param merchantId
     * @return: {@link }
     * @version 1.3.0
     **/
    List<CateringShopGoodsSpuEntity> getSortForMerchant(@Param("dto") MerchantCategoryOrGoodsSortDTO dto);

    List<Long> selectMerchantId();
}
