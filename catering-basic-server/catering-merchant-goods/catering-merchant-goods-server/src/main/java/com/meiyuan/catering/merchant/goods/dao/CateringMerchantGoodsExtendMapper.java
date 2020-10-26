package com.meiyuan.catering.merchant.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantGoodsExtendEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Mapper
public interface CateringMerchantGoodsExtendMapper extends BaseMapper<CateringMerchantGoodsExtendEntity> {

    /**
    * 根据商户ID以及商品ID查询最新创建的商品信息
    * @param merchantId 门店ID
    * @param goodsId 商品ID
    * @author: GongJunZheng
    * @date: 2020/8/28 18:29
    * @return: {@link CateringMerchantGoodsExtendEntity}
    * @version V1.3.0
    **/
    CateringMerchantGoodsExtendEntity getGoodsInfoByGoodsId(@Param("merchantId") Long merchantId,
                                                            @Param("goodsId") Long goodsId);
    /**
     * 通过merchantId查询商户最新的商品的商家商品扩展表ID
     * @param merchantId 商户ID
     * @author: GongJunZheng
     * @date: 2020/9/24 10:36
     * @return: {@link List<Long>}
     * @version V1.4.0
     **/
    List<Long> listLastIdByMerchantId(@Param("merchantId") Long merchantId);
}
