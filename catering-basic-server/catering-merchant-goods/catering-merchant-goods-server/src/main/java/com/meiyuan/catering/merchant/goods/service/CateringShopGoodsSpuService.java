package com.meiyuan.catering.merchant.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.es.ShopGoodsSku;
import com.meiyuan.catering.core.dto.es.ShopGoodsStatusMap;
import com.meiyuan.catering.core.vo.wxcategory.WxCategoryGoodsVO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsSkuDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantSortDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSpuEntity;

import java.util.List;
import java.util.Set;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
public interface CateringShopGoodsSpuService extends IService<CateringShopGoodsSpuEntity> {

    /**
     * 修改门店商品  上下架状态
     * 描述：
     *
     * @param merchantGoodsStatus
     * @param merchantGoodsId
     * @param shopId
     * @return {@link Boolean}
     * @author lhm
     * @date 2020/7/5
     * @version 1.2.0
     **/
    Boolean updateGoodsStatus(Integer merchantGoodsStatus, Long merchantGoodsId, Long shopId);

    /**
     * 方法描述: 保存门店商品关联数据<br>
     *
     * @param merchantId    商户ID
     * @param removeShopIds 移除门店ID
     * @param menuId        菜单ID
     * @author: gz
     * @date: 2020/7/24 11:32
     * @return: {@link }
     * @version 1.2.0
     **/
    void saveShopGoods(Long merchantId, List<Long> removeShopIds, Long menuId);

    /**
     * 方法描述: 修改门店商品价格及库存数据<br>
     *
     * @param ids
     * @param merchantGoodsSkuDTOList
     * @author: gz
     * @date: 2020/7/15 17:03
     * @return: {@link }
     * @version 1.2.0
     **/
    void updateShopSku(List<Long> ids, List<MerchantGoodsSkuDTO> merchantGoodsSkuDTOList);


    /**
     * describe: 保存门店商品关联数据
     *
     * @param merchantId
     * @param deleteSkuList
     * @param addSkuList
     * @param deleteShopList
     * @param addShopIdList
     * @param oldShopIdList
     * @param oldSkuList
     * @author: yy
     * @date: 2020/7/24 11:56
     * @return: {@link Boolean}
     * @version 1.2.0
     **/
    Boolean saveShopGoodsTwo(Long merchantId, List<String> deleteSkuList, List<String> addSkuList, List<Long> deleteShopList,
                             List<Long> addShopIdList, List<Long> oldShopIdList, List<String> oldSkuList);

    /**
     * 方法描述   刷新门店es
     *
     * @param goodsId    商品ID
     * @param merchantId 商户ID
     * @author: lhm
     * @date: 2020/7/18 11:18
     * @return: {@link }
     * @version 1.1.0
     **/
    void pushMerchantGoodsEs(Long goodsId, Long merchantId);

    /**
     * describe: 清楚关联商品为空的数据
     *
     * @param
     * @author: yy
     * @date: 2020/7/24 9:08
     * @return: {@link Boolean}
     * @version 1.2.0
     **/
    Boolean deleteIsGoodsNull();


    /**
     * 方法描述   获取门店上下架状态
     *
     * @param goodsId
     * @param shopId
     * @author: lhm
     * @date: 2020/7/25 9:49
     * @return: {@link }
     * @version 1.1.0
     **/
    Integer getShopStatus(Long goodsId, Long shopId);

    /**
     * 描述:获取门店状态
     *
     * @param shopIds
     * @return java.util.List<com.meiyuan.catering.core.dto.es.ShopGoodsStatusMap>
     * @author zengzhangni
     * @date 2020/8/27 9:59
     * @since v1.3.0
     */
    List<ShopGoodsStatusMap> getShopGoodsStatus(Set<Long> shopIds);

    /**
     * 描述:获取门店sku
     *
     * @param shopIds
     * @return java.util.List<com.meiyuan.catering.core.dto.es.ShopGoodsSku>
     * @author zengzhangni
     * @date 2020/8/27 9:59
     * @since v1.3.0
     */
    List<ShopGoodsSku> getShopGoodsSkus(Set<Long> shopIds);

    /**
     * 描述:获取门店存在的sku
     *
     * @param shopIds
     * @param goodsMerchantId
     * @return java.util.List<java.lang.Long>
     * @author zengzhangni
     * @date 2020/8/27 10:00
     * @since v1.3.0
     */
    List<Long> existSkuShop(List<Long> shopIds, Long goodsMerchantId);


    /**
     * 方法描述   门店商品排序
     * @author: lhm
     * @date: 2020/8/24 13:56
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    Boolean updateGoodsSort(MerchantCategoryOrGoodsSortDTO dto);


    /**
     * 方法描述   app--商品、分类置顶
     * @author: lhm
     * @date: 2020/9/1 13:44
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    Boolean updateSortToUp(MerchantSortDTO dto);


    /**
     * 根据品牌ID以及商品ID查询门店商品SPU信息
     *
     * @param merchantId 品牌ID
     * @param goodsId    商品ID
     * @author: GongJunZheng
     * @date: 2020/8/14 14:20
     * @return: {@link List<CateringShopGoodsSpuEntity>}
     * @version V1.3.0
     **/
    List<CateringShopGoodsSpuEntity> selectShopGoodsSpu(Long merchantId, Long goodsId);

    /**
     * describe: 根绝关联集合id查询商品
     * @author: yy
     * @date: 2020/9/14 17:05
     * @param spuIdList
     * @return: {@link List< WxCategoryGoodsVO>}
     * @version 1.4.0
     **/
    List<WxCategoryGoodsVO> queryByIdList(List<Long> spuIdList);

    String pushGoodsEsToDb();


    /**
     * 方法描述   处理商品分类数据正确性
     * @author: lhm
     * @date: 2020/10/15 13:53
     * @param
     * @return: {@link }
     * @version 1.3.0
     **/
    Boolean updateCategory();


}
