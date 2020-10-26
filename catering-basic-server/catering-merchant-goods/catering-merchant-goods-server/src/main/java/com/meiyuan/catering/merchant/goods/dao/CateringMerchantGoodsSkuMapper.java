package com.meiyuan.catering.merchant.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantGoodsSkuEntity;
import com.meiyuan.catering.merchant.goods.vo.MarketingSpecialGoodsMinQuantityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */

@Mapper
public interface CateringMerchantGoodsSkuMapper extends BaseMapper<CateringMerchantGoodsSkuEntity> {

    /**
     * 描述：商家商品表sku最大值
     * @author lhm
     * @date 2020/7/6
     * @param merchantGoodsId
     * @return {@link CateringMerchantGoodsSkuEntity}
     * @version 1.2.0
     **/
    CateringMerchantGoodsSkuEntity maxDbCode(Long merchantGoodsId);

    /**
    * 根据商品扩展表id以及商品SKU编码查询商品SKU信息
    * @param extendId 商品扩展表id
    * @param skuCode 商品SKU编码
    * @author: GongJunZheng
    * @date: 2020/8/28 18:34
    * @return: {@link }
    * @version V1.3.0
    **/
    CateringMerchantGoodsSkuEntity getGoodsSkuInfo(@Param("extendId") Long extendId,
                                                   @Param("skuCode") String skuCode);

    /**
     * 根据商户ID、SKU编码集合查询商品的起售数量
     * @param goodsExtendIds 商品的商家商品扩展表ID集合
     * @param skuCodeSet SKU编码集合
     * @author: GongJunZheng
     * @date: 2020/9/10 9:27
     * @return: {@link List<MarketingSpecialGoodsMinQuantityVO>}
     * @version V1.4.0
     **/
    List<MarketingSpecialGoodsMinQuantityVO> selectGoodsMinQuantity(@Param("goodsExtendIds") List<Long> goodsExtendIds,
                                                                    @Param("skuCodeSet") Set<String> skuCodeSet);
}
