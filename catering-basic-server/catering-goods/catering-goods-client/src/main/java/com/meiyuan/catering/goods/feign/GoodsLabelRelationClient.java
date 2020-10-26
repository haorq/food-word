package com.meiyuan.catering.goods.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.es.GoodsEsGoodsDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.dto.label.LabelRelationDTO;
import com.meiyuan.catering.goods.service.CateringGoodsLabelRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/5/19 15:52
 * @description 简单描述
 **/
@Service
public class GoodsLabelRelationClient {
    @Resource
    CateringGoodsLabelRelationService labelRelationService;

    /**
     * 获取标签id和标签名称
     *
     * @author: wxf
     * @date: 2020/3/23 20:38
     * @param goodsIdList 商品id集合
     * @return: {@link List <  GoodsCategoryAndLabelDTO >}
     **/
    public Result<List<GoodsCategoryAndLabelDTO>> listByGoodsIdList(Long merchantId,List<Long> goodsIdList) {
        return Result.succ(labelRelationService.listByGoodsIdList(merchantId,goodsIdList));
    }

    /**
     * 批量获取根据标签id
     *
     * @author: wxf
     * @date: 2020/5/19 15:55
     * @param labelId 标签id
     * @return: {@link List<  LabelRelationDTO >}
     * @version 1.0.1
     **/
    public Result<List<LabelRelationDTO>> listByLabelId(Long labelId) {
        return Result.succ(labelRelationService.listByLabelId(labelId));
    }

    public void saveGoodsLabel(List<Long> labelIdList, long goodsId, long merchantId, GoodsEsGoodsDTO esGoodsDTO) {
        labelRelationService.saveGoodsLabel(labelIdList, goodsId,  merchantId, esGoodsDTO);
    }
}
