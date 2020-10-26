package com.meiyuan.catering.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.dto.label.LabelGoodDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsLabelRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 标签商品关联表(CateringGoodsLabelRelation)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-09 17:53:21
 */
@Mapper
public interface CateringGoodsLabelRelationMapper extends BaseMapper<CateringGoodsLabelRelationEntity> {

    /**
     * 获取标签id和标签名称
     *
     * @param goodsIdList 商品id集合
     * @param merchantId  商户id
     * @author: wxf
     * @date: 2020/3/23 20:38
     * @return: {@link List <  GoodsCategoryAndLabelDTO >}
     **/
    List<GoodsCategoryAndLabelDTO> listByGoodsIdList(@Param("merchantId") Long merchantId, @Param("list") List<Long> goodsIdList);


    /**
     * 描述： 获取标签id和标签名称
     *
     * @param goodsIdList
     * @param merchantId
     * @return {@link List< GoodsCategoryAndLabelDTO>}
     * @author lhm
     * @date 2020/7/13
     * @version 1.2.0
     **/
    List<GoodsCategoryAndLabelDTO> listByGoodsIdListAndMerchant(@Param("list") List<Long> goodsIdList, @Param("merchantId") Long merchantId);

    /**
     * 描述:查询商户标签id列表
     *
     * @param goodsId
     * @return java.util.Set<java.lang.Long>
     * @author zengzhangni
     * @date 2020/8/27 9:57
     * @since v1.3.0
     */
    Set<Long> selectMerchantIdsByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * 描述:查询标签列表
     *
     * @param merchantId
     * @param goodsIds
     * @return com.meiyuan.catering.goods.dto.label.LabelGoodDTO
     * @author zengzhangni
     * @date 2020/8/27 9:57
     * @since v1.3.0
     */
    LabelGoodDTO getLabelList(@Param("merchantId") Long merchantId, @Param("goodsIds") List<Long> goodsIds);
}
