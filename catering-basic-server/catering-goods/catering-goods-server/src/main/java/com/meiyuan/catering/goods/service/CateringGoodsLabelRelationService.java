package com.meiyuan.catering.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.goods.dto.es.GoodsEsGoodsDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.dto.label.LabelGoodDTO;
import com.meiyuan.catering.goods.dto.label.LabelRelationDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsLabelRelationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签商品关联表(CateringGoodsLabelRelation)服务层
 *
 * @author wxf
 * @since 2020-03-09 18:05:09
 */
public interface CateringGoodsLabelRelationService  extends IService<CateringGoodsLabelRelationEntity> {
    /**
     * 获取标签id和标签名称
     *
     * @author: wxf
     * @date: 2020/3/23 20:38
     * @param goodsIdList 商品id集合
     * @param merchantId 商户id
     * @return: {@link List <  GoodsCategoryAndLabelDTO >}
     **/
    List<GoodsCategoryAndLabelDTO> listByGoodsIdList(Long merchantId,List<Long> goodsIdList);


    /**
     * 描述：获取标签id和标签名称
     * @author lhm
     * @date 2020/7/13
     * @param goodsIdList
     * @param merchantId 商户id
     * @return {@link List< GoodsCategoryAndLabelDTO>}
     * @version 1.2.0
     **/
    List<GoodsCategoryAndLabelDTO> listByGoodsIdListAndMerchant(List<Long> goodsIdList,Long merchantId);

    /**
     * 批量获取根据标签id
     *
     * @author: wxf
     * @date: 2020/5/19 15:55
     * @param labelId 标签id
     * @return: {@link List< LabelRelationDTO>}
     * @version 1.0.1
     **/
    List<LabelRelationDTO> listByLabelId(Long labelId);

    /**
     * 描述：保存标签信息
     * @author lhm
     * @date 2020/7/7
     * @param labelIdList
     * @param goodsId
     * @param merchantId
     * @param esGoodsDTO
     * @return {@link }
     * @version 1.2.0
     **/
    void saveGoodsLabel(List<Long> labelIdList, long goodsId, Long merchantId, GoodsEsGoodsDTO esGoodsDTO);


    /**
     * 方法描述   获取商户标签集合
     * @author: lhm
     * @date: 2020/8/5 10:39
     * @param merchantId
     * @param goodsIds
     * @return: {@link }
     * @version 1.3.0
     **/
    LabelGoodDTO getLabelList(Long merchantId, List<Long> goodsIds);
}

