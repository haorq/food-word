package com.meiyuan.catering.es.service;

import com.meiyuan.catering.core.dto.base.WxCategoryGoodsDTO;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsDTO;
import com.meiyuan.catering.core.dto.goods.MerchantGoodsToEsDTO;
import com.meiyuan.catering.core.dto.goods.RecommendDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.*;
import com.meiyuan.catering.es.dto.merchant.DiscountQuery;
import com.meiyuan.catering.es.dto.merchant.EsMerchantListParamDTO;
import com.meiyuan.catering.es.dto.sku.EsSkuCodeAndGoodsIdDTO;
import com.meiyuan.catering.es.dto.wx.EsWxIndexSearchQueryDTO;
import com.meiyuan.catering.es.entity.EsGoodsEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wxf
 * @date 2020/3/23 14:00
 * @description 简单描述
 **/
public interface EsGoodsService {

    /**
     * 新增修改数据
     *
     * @param dto 新增修改数据
     * @author: wxf
     * @date: 2020/3/23 14:04
     **/
    void saveUpdate(EsGoodsDTO dto);

    /**
     * 新增修改数据
     *
     * @param dtoList 新增修改数据集合
     * @author: wxf
     * @date: 2020/3/23 14:04
     **/
    void saveUpdateBatch(List<EsGoodsDTO> dtoList);


    /**
     * 方法描述   批量修改数据
     *
     * @param dtoList
     * @author: lhm
     * @date: 2020/7/21 15:17
     * @return: {@link }
     * @version 1.1.0
     **/
    void updateBatch(List<EsGoodsDTO> dtoList);

    /**
     * 获取ES商品根据商品id
     *
     * @param goodsId 商品id
     * @param flag    是否过滤sku true 过滤 false 不过滤
     * @author: wxf
     * @date: 2020/3/24 13:09
     * @return: {@link EsGoodsDTO}
     **/
    EsGoodsDTO getById(Long goodsId, Boolean flag);

    /**
     * 批量获取根据商品id集合
     *
     * @param goodsIdList 商品id集合
     * @param flag        是否包含推送给商家的商品 true 包含 false不包含 null 符合商品id的都查
     * @author: wxf
     * @date: 2020/5/28 19:15
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.0.1
     **/
    List<EsGoodsDTO> listByGoodsIdList(List<Long> goodsIdList, Boolean flag, EsGoodsDTO dto);

    /**
     * 方法描述 : 查询所有商品信息
     * 1、查询条件：店铺信息，商品信息，其他信息
     *
     * @param dto 若字段值为空，不设置该查询条件
     * @Author: MeiTao
     * @Date: 2020/7/18 0018 10:23
     * @return: java.util.List<com.meiyuan.catering.es.dto.goods.EsGoodsDTO>
     * @Since version-1.2.0
     */
    List<EsGoodsDTO> listEsGoods(EsGoodsQueryConditionDTO dto);

    /**
     * 描述:通过商品批量查询商品分类id集合
     *
     * @param goodsIdList
     * @param shopId
     * @return java.util.List<java.lang.Long>
     * @author zengzhangni
     * @date 2020/7/15 19:32
     * @since v1.2.0
     */
    List<EsGoodsDTO> queryCategoryByGoodsIds(List<Long> goodsIdList, Long shopId);

    /**
     * skuCode集合获取商品
     *
     * @param list 查询参数集合
     * @author: wxf
     * @date: 2020/3/24 13:45
     * @return: {@link List< EsGoodsDTO>}
     **/
    List<EsGoodsDTO> listBySkuCodeList(List<EsSkuCodeAndGoodsIdDTO> list);

    /**
     * 获取商户商品根据skuCode
     *
     * @param shopId  门店ID
     * @param goodsId 商品id
     * @param skuCode skuCode
     * @author: wxf
     * @date: 2020/3/24 13:46
     * @return: {@link EsGoodsDTO}
     **/
    EsGoodsDTO getBySkuCode(String shopId, String goodsId, String skuCode);

    /**
     * 微信首页搜索
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/4/1 14:11
     * @return: {@link List<  EsGoodsDTO >}
     **/
    PageData<EsGoodsDTO> wxIndexSearch(EsWxIndexSearchQueryDTO dto);

    /**
     * 分页查询商品商家数据
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/20 14:28
     * @return: {@link  PageData<  EsGoodsDTO >}
     * @version 1.0.1
     **/
    PageData<EsGoodsDTO> limitByGoodsId(EsGoodsMerchantListQueryDTO dto);

    /**
     * 商品对应的推送商家
     *
     * @param goodsIdList 商品id集合
     * @param cityCode    城市编码
     * @author: wxf
     * @date: 2020/5/29 13:48
     * @return: {@link Map<String ,List<EsGoodsDTO>>}
     * @version 1.0.1
     **/
    Map<String, List<EsGoodsDTO>> goodsRelationMerchant(List<String> goodsIdList, String cityCode);

    /**
     * 有商品的商户集合
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/29 15:09
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.0.1
     **/
    List<EsGoodsDTO> merchantHaveGoodsList(EsMerchantListParamDTO dto);

    /**
     * 批量获取根据商户id
     *
     * @param shopId 商户id集合
     * @author: wxf
     * @date: 2020/6/5 10:27
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.0.1
     **/
    List<EsGoodsDTO> listByShopId(Long shopId);


    List<EsGoodsDTO> listUpperByShopId(Long shopId, Boolean isCompanyUser);

    /**
     * 批量获取根据商品名称和商户id
     *
     * @param goodsName 商品名称
     * @param shopId    商户id
     * @param size      条数
     * @author: wxf
     * @date: 2020/6/8 15:00
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.1.0
     **/
    List<EsGoodsDTO> listByGoodsNameAndMerchantId(String goodsName, String shopId, int size, Boolean isCompanyUser);

    /**
     * 获取根据商品id和商户id
     *
     * @param goodsId    商品id
     * @param merchantId 商户id
     * @author: wxf
     * @date: 2020/6/9 11:40
     * @return: {@link  EsGoodsDTO}
     * @version 1.1.0
     **/
    EsGoodsDTO getByGoodsIdAndMerchantId(Long goodsId, Long merchantId);

    /**
     * 批量获取根据首页分类id
     *
     * @param wxIndexCategoryId 首页分类id
     * @author: wxf
     * @date: 2020/5/20 14:05
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.0.1
     **/
    List<EsGoodsDTO> listByWxIndexCategoryId(String wxIndexCategoryId);


    /**
     * 描述：批量删除推送到门店的商品
     *
     * @param shops true 删除全部门店的数据 false 删除通过菜单推送到门店的商品数据
     * @return {@link }
     * @author lhm
     * @date 2020/7/11
     * @version 1.2.0
     **/
    void deleteGoodsToShop(List<Long> shops, boolean flag);

    void deleteSkuToShop(List<Long> shops, List<String> removeSkuCode);

    /**
     * 方法描述: 平台/app商品信息变更同步es<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/7/13 12:39
     * @return: {@link }
     * @version 1.2.0
     **/
    void goodsExtUpdate(GoodsExtToEsDTO dto);

    /**
     * 方法描述: 删除ES商品数据<br>
     *
     * @param goodsId
     * @author: gz
     * @date: 2020/7/13 13:06
     * @return: {@link }
     * @version 1.2.0
     **/
    void delGoods(Long goodsId);

    /**
     * 方法描述: 取消授权商品-同步ES<br>
     *
     * @param goodsId
     * @param merchantId
     * @author: gz
     * @date: 2020/7/13 18:58
     * @return: {@link }
     * @version 1.2.0
     **/
    void cancelAuthGoods(Long goodsId, Long merchantId);

    /**
     * 描述：获取根据商品id和门店id
     *
     * @param goodsId
     * @param shopId
     * @return {@link Result < EsGoodsDTO>}
     * @author lhm
     * @date 2020/7/14
     * @version 1.2.0
     **/
    EsGoodsDTO getByGoodsIdAndShopIdId(Long goodsId, Long shopId);

    /**
     * 方法描述: 商户商品分类信息变更同步es<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/7/13 12:39
     * @return: {@link }
     * @version 1.2.0
     **/
    void goodsCategoryUpdate(MerchantGoodsToEsDTO dto);

    /**
     * 方法描述: 预售商品结束定时下架任务--同步ES数据<br>
     *
     * @param
     * @author: gz
     * @date: 2020/7/18 10:42
     * @return: {@link }
     * @version 1.2.0
     **/
    void goodsPresellTaskSyncEs();

    /**
     * 方法描述 : 根据店铺id删除商品信息
     *
     * @param shopId 请求参数
     * @Author: MeiTao
     * @Date: 2020/8/5 0005 15:41
     * @return: void
     * @Since version-1.3.0
     */
    void delGoodsByShopId(Long shopId);

    /**
     * 根据店铺ID以及商品ID集合查询ES商品数据
     *
     * @param shopId      店铺ID
     * @param goodsIdsSet 商品ID集合
     * @param flag        是否查询店铺自创的商品
     * @author: GongJunZheng
     * @date: 2020/9/7 14:55
     * @return: {@link List<EsGoodsEntity>}
     * @version V1.4.0
     **/
    List<EsGoodsEntity> selectListByShopIdAndGoodsIds(Long shopId, Set<Long> goodsIdsSet, Boolean flag);

    /**
     * 批量保存商品信息
     *
     * @param esGoodsList 商品信息
     * @author: GongJunZheng
     * @date: 2020/9/7 15:32
     * @return: void
     * @version V1.4.0
     **/
    void saveBath(List<EsGoodsEntity> esGoodsList);

    List<String> queryDiscountShop(DiscountQuery query);

    PageData<EsGoodsDTO> queryRecommendList(RecommendDTO dto, List<WxCategoryGoodsDTO> list, Boolean isCompanyUser);

    Double queryMinDiscount(Long shopId);

    Map<String, Double> queryMinDiscount(Set<String> matchShopIds);
}
