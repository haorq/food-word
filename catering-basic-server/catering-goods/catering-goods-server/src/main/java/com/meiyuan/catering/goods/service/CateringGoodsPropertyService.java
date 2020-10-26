package com.meiyuan.catering.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.goods.entity.CateringGoodsPropertyEntity;

import java.util.List;

/**
 * 商品属性表(CateringGoodsProperty)服务层
 *
 * @author wxf
 * @since 2020-03-09 18:05:09
 */
public interface CateringGoodsPropertyService extends IService<CateringGoodsPropertyEntity> {
    /**
     * 获取集合根据商品id
     *
     * @author: wxf
     * @date: 2020/3/21 14:15
     * @param goodsId 商品id
     * @return: {@link List < CateringGoodsPropertyEntity>}
     **/
    List<CateringGoodsPropertyEntity> listByGoodsId(Long goodsId);

}