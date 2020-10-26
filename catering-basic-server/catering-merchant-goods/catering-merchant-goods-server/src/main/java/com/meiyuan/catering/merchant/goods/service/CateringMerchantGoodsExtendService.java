package com.meiyuan.catering.merchant.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantCategoryEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantGoodsExtendEntity;
import com.meiyuan.catering.merchant.goods.vo.MarketingSpecialGoodsMinQuantityVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
public interface CateringMerchantGoodsExtendService extends IService<CateringMerchantGoodsExtendEntity> {

    /**
     * describe: 将id分类下的商品关联到默认分类下
     * @author: yy
     * @date: 2020/7/8 17:10
     * @param id
     * @param defaultEntity
     * @return: {java.lang.String}
     * @version 1.2.0
     **/
    String updateByCategoryId(Long id, CateringMerchantCategoryEntity defaultEntity);

    /**
     * 方法描述: 通过商品id获取推送的门店id及时间<br>
     *
     * @author: gz
     * @date: 2020/7/10 18:21
     * @param goodsId 商品id
     * @return: {@link Map< Long, Object>}
     * @version 1.2.0
     **/
    Map<Long, LocalDateTime> listMerchantIdByGoodsId(Long goodsId);

    /**
     * 方法描述   商户商品删除
     * @author: lhm
     * @date: 2020/8/4 14:04
     *
     * @param goodsId
     * @return: {@link }
     * @version 1.3.0
     **/
    Boolean deleteMerchantGoods( Long goodsId);

    /**
    * 通过门店ID以及商品ID查询商品信息
    * @param merchantId 门店ID
    * @param goodsId 商品ID
    * @author: GongJunZheng
    * @date: 2020/8/14 13:54
    * @return: {@link }
    * @version V1.3.0
    **/
    CateringMerchantGoodsExtendEntity getByMerchantIdAndGoodsId(Long merchantId, Long goodsId);

    /**
    * 查询指定商户、指定商品ID的信息
    * @param merchantId 门店ID
    * @param goodsId 商品ID
    * @author: GongJunZheng
    * @date: 2020/8/28 14:43
    * @return: {@link CateringMerchantGoodsExtendEntity}
    * @version V1.3.0
    **/
    CateringMerchantGoodsExtendEntity getGoodsInfoByGoodsId(Long merchantId, Long goodsId);

    /**
    * 通过merchantId查询商户最新的商品的商家商品扩展表ID
    * @param merchantId 商户ID
    * @author: GongJunZheng
    * @date: 2020/9/24 10:36
    * @return: {@link List<Long>}
    * @version V1.4.0
    **/
    List<Long> listLastIdByMerchantId(Long merchantId);
}
