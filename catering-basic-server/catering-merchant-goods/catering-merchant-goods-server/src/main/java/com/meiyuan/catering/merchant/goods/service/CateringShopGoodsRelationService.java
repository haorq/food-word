package com.meiyuan.catering.merchant.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsRelationEntity;

import java.util.List;

/**
 * 类目商品关联表(CateringGoodsCategoryRelation)服务层
 *
 * @author lhm
 * @since 2020-03-09 18:05:09
 */
public interface CateringShopGoodsRelationService extends IService<CateringShopGoodsRelationEntity> {


    /**
     * 方法描述   商户app--商品排序修改
     * @author: lhm
     * @date: 2020/8/24 15:59
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    Boolean updateGoodsSort(MerchantCategoryOrGoodsSortDTO dto);
}
