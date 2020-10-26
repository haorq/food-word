package com.meiyuan.catering.marketing.service;

import com.meiyuan.catering.core.dto.es.MarketingSpecialSku;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.marketing.MarketingEsGoodsUpdateDTO;
import com.meiyuan.catering.marketing.dto.marketing.MarketingPcMenuUpdateSyncDTO;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialGoodsPageDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialSkuEntity;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialGoodsDetailVO;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialGoodsPageVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author GongJunZheng
 * @date 2020/09/03 09:09
 * @description 营销特价商品活动SKU信息服务接口
 **/

public interface CateringMarketingSpecialSkuService {

    /**
    * 根据营销特价商品活动ID物理删除商品SKU信息
    * @param ofId 营销特价商品活动ID
    * @author: GongJunZheng
    * @date: 2020/9/3 11:27
    * @return: void
    * @version V1.4.0
    **/
    void delByOfId(Long ofId);

    /**
    * 批量增加营销特价商品活动商品SKU信息
    * @param specialSkuEntityList 营销特价商品活动商品SKU信息集合
    * @author: GongJunZheng
    * @date: 2020/9/3 11:43
    * @return: void
    * @version V1.4.0
    **/
    void saveBath(List<CateringMarketingSpecialSkuEntity> specialSkuEntityList);

    /**
     * 根据营销特价商品活动ID查询营销特价商品SKU信息
     * @param specialId 营销特价商品活动ID
     * @author: GongJunZheng
     * @date: 2020/9/7 14:19
     * @return: {@link List<CateringMarketingSpecialSkuEntity>}
     * @version V1.4.0
     **/
    List<CateringMarketingSpecialSkuEntity> selectGoodsSkuList(Long specialId);

    /**
     * 根据营销特价商品活动ID查询营销特价商品信息
     * @param merchantId 商户ID
     * @param specialId 营销特价商品活动ID
     * @author: GongJunZheng
     * @date: 2020/9/3 13:47
     * @return: {@link MarketingSpecialGoodsDetailVO}
     * @version V1.4.0
     **/
    List<MarketingSpecialGoodsDetailVO> selectGoodsDetail(Long merchantId, Long specialId);

    /**
     * 查询详情中的分页列表商品信息
     * @param dto 查询条件
     * @author: GongJunZheng
     * @date: 2020/9/3 14:47
     * @return: {@link PageData<MarketingSpecialGoodsPageVO>}
     * @version V1.4.0
     **/
    PageData<MarketingSpecialGoodsPageVO> selectDetailGoodsPage(MarketingSpecialGoodsPageDTO dto);

    /**
     * 商品删除，同步营销特价商品活动的商品信息
     * @param merchantId 店铺ID
     * @param goodsId 商品ID
     * @author: GongJunZheng
     * @date: 2020/9/8 10:07
     * @return: {@link Result}
     * @version V1.4.0
     **/
    void goodsDelSync(Long merchantId, Long goodsId);

    /**
     * 修改营销特价商品活动的商品信息
     * @param merchantId 商户ID
     * @param goodsId 商品ID
     * @param goodsName 商品名称
     * @author: GongJunZheng
     * @date: 2020/9/8 10:58
     * @return: {@link Result}
     * @version V1.4.0
     **/
    void updateGoodsInfo(Long merchantId, Long goodsId, String goodsName);

    /**
     * 营销特价商品SKU信息编辑
     * @param merchantId 商户ID
     * @param goodsId 商品ID
     * @param newGoodsList 商品SKU信息集合
     * @author: GongJunZheng
     * @date: 2020/9/8 11:08
     * @return: void
     * @version V1.4.0
     **/
    void removeDelSkuGoods(Long merchantId, Long goodsId, List<MarketingEsGoodsUpdateDTO> newGoodsList);

    /**
     * 商户PC端菜单修改同步营销商品表
     * @param shopId 店铺ID
     * @param skuCodeSet 最新菜单SKU编码
     * @author: GongJunZheng
     * @date: 2020/9/8 14:20
     * @return: {@link MarketingPcMenuUpdateSyncDTO}
     * @version V1.4.0
     **/
    MarketingPcMenuUpdateSyncDTO pcMenuUpdateSync(Long shopId, Set<String> skuCodeSet);

    /**
     * 通过店铺ID集合查询店铺的特价商品SKU信息
     * @param shopIds 店铺ID集合
     * @author: GongJunZheng
     * @date: 2020/9/14 9:05
     * @return: {@link Map<Long, Map<String, MarketingSpecialSku>>}
     * @version V1.4.0
     **/
    Map<Long, Map<String, MarketingSpecialSku>> selectGoodsSkuByShopIds(Set<Long> shopIds);

    /**
     * 通过店铺ID以及商品ID查询店铺的特价商品SKU信息
     * @param shopId 店铺ID
     * @param goodsId 商品ID
     * @author: GongJunZheng
     * @date: 2020/9/14 10:26
     * @return: {@link Map<String, MarketingSpecialSku>}
     * @version V1.4.0
     **/
    Map<String, MarketingSpecialSku> selectGoodsSkuByShopIdAndGoodsId(Long shopId, Long goodsId);

    /**
     * 根据商品ID查询特价商品信息
     * @param goodsId 商品ID
     * @author: GongJunZheng
     * @date: 2020/9/14 11:27
     * @return: {@link Map<Long, Map<String, MarketingSpecialSku>>}
     * @version V1.4.0
     **/
    Map<Long, Map<String, MarketingSpecialSku>> selectGoodsSkuByGoodsId(Long goodsId);

    /**
    * 根据ID集合查询正在参加特价活动的商品信息
    * @param merchantId 商户ID
    * @param goodsIds 商品ID集合
    * @author: GongJunZheng
    * @date: 2020/9/18 14:32
    * @return: {@link List<CateringMarketingSpecialSkuEntity>}
    * @version V1.4.0
    **/
    List<CateringMarketingSpecialSkuEntity> isJoinActivity(Long merchantId, List<Long> goodsIds);
}
