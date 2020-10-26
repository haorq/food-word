package com.meiyuan.catering.es.fegin;

import com.meiyuan.catering.core.dto.base.WxCategoryGoodsDTO;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsDTO;
import com.meiyuan.catering.core.dto.goods.MerchantGoodsToEsDTO;
import com.meiyuan.catering.core.dto.goods.RecommendDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.*;
import com.meiyuan.catering.es.dto.marketing.EsMarketingDTO;
import com.meiyuan.catering.es.dto.merchant.DiscountQuery;
import com.meiyuan.catering.es.dto.merchant.EsMerchantListParamDTO;
import com.meiyuan.catering.es.dto.sku.EsSkuCodeAndGoodsIdDTO;
import com.meiyuan.catering.es.dto.wx.EsWxIndexSearchQueryDTO;
import com.meiyuan.catering.es.entity.EsGoodsEntity;
import com.meiyuan.catering.es.service.EsGoodsService;
import com.meiyuan.catering.es.service.EsMarketingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yaoozu
 * @description es商品数据
 * @date 2020/5/1917:05
 * @since v1.0.0
 */
@Service
@Slf4j
public class EsGoodsClient {
    @Resource
    private EsGoodsService goodsService;
    @Resource
    private EsMarketingService marketingService;

    /**
     * 新增修改数据
     *
     * @param dto 新增修改数据
     * @author: wxf
     * @date: 2020/3/23 14:04
     **/
    public void saveUpdate(EsGoodsDTO dto) {
        goodsService.saveUpdate(dto);
    }

    /**
     * 新增修改数据
     *
     * @param dtoList 新增修改数据集合
     * @author: wxf
     * @date: 2020/3/23 14:04
     **/
    public void saveUpdateBatch(List<EsGoodsDTO> dtoList) {
        goodsService.saveUpdateBatch(dtoList);
    }


    /**
     * 方法描述   批量修改数据
     *
     * @param dtoList
     * @author: lhm
     * @date: 2020/7/21 15:19
     * @return: {@link }
     * @version 1.1.0
     **/
    public void updateBatch(List<EsGoodsDTO> dtoList) {
        goodsService.updateBatch(dtoList);
    }


    /**
     * 获取ES商品根据商品id
     *
     * @param id 商品id
     * @author: wxf
     * @date: 2020/3/24 13:09
     * @return: {@link EsGoodsDTO}
     **/
    public Result<EsGoodsDTO> getById(Long id) {
        return this.getById(id, true);
    }

    /**
     * 获取ES商品根据商品id
     *
     * @param goodsId 商品id
     * @param flag    是否过滤sku true 过滤 false 不过滤
     * @author: wxf
     * @date: 2020/3/24 13:09
     * @return: {@link EsGoodsDTO}
     **/
    public Result<EsGoodsDTO> getById(Long goodsId, Boolean flag) {
        return Result.succ(goodsService.getById(goodsId, flag));
    }

    /**
     * 批量获取根据商品id集合
     *
     * @param goodsIdList 商品id集合
     * @param flag        是否包含推送给商家的商品 true包含（商家） false不包含（平台） null所有（包含平台和商家）
     * @author: wxf
     * @date: 2020/5/28 19:15
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.0.1
     **/
    public Result<List<EsGoodsDTO>> listByGoodsIdList(List<Long> goodsIdList, Boolean flag, EsGoodsDTO dto) {
        return Result.succ(goodsService.listByGoodsIdList(goodsIdList, flag, dto));
    }

    public Result<List<EsGoodsDTO>> listEsGoods(EsGoodsQueryConditionDTO dto) {
        return Result.succ(goodsService.listEsGoods(dto));
    }

    public Result<List<EsGoodsDTO>> queryCategoryByGoodsIds(List<Long> goodsIdList, Long shopId) {
        return Result.succ(goodsService.queryCategoryByGoodsIds(goodsIdList, shopId));
    }

    /**
     * skuCode集合获取商品
     *
     * @param list 查询参数集合
     * @author: wxf
     * @date: 2020/3/24 13:45
     * @return: {@link List< EsGoodsDTO>}
     **/
    public Result<List<EsGoodsDTO>> listBySkuCodeList(List<EsSkuCodeAndGoodsIdDTO> list) {
        return Result.succ(goodsService.listBySkuCodeList(list));
    }

    /**
     * 获取商户商品根据skuCode
     *
     * @param shopId  商户id
     * @param goodsId 商品id
     * @param skuCode skuCode
     * @author: wxf
     * @date: 2020/3/24 13:46
     * @return: {@link EsGoodsDTO}
     **/
    public Result<EsGoodsDTO> getBySkuCode(String shopId, String goodsId, String skuCode) {
        return Result.succ(goodsService.getBySkuCode(shopId, goodsId, skuCode));
    }

    /**
     * 获取ES(团购/秒杀)商品根据活动商品id
     *
     * @param id 活动商品id
     * @author: wxf
     * @date: 2020/3/24 13:09
     * @return: {@link EsGoodsDTO}
     **/
    public Result<EsMarketingDTO> getMarketingGoodsById(Long id) {
        return Result.succ(marketingService.getBymGoodsId(id));
    }

    /**
     * 描述:获取ES(团购/秒杀)商品根据活动商品id
     *
     * @param id
     * @param isJudgeDel 是否验证删除
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.es.dto.marketing.EsMarketingDTO>
     * @author zengzhangni
     * @date 2020/8/28 9:31
     * @since v1.3.0
     */
    public Result<EsMarketingDTO> getMarketingGoodsById(Long id, Boolean isJudgeDel) {
        return Result.succ(marketingService.getBymGoodsId(id, isJudgeDel));
    }

    /**
     * 微信首页搜索
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/4/1 14:11
     * @return: {@link PageData<  EsWxIndexSearchDTO >}
     **/
    public Result<PageData<EsGoodsDTO>> wxIndexSearch(EsWxIndexSearchQueryDTO dto) {
        return Result.succ(goodsService.wxIndexSearch(dto));
    }

    /**
     * 分页查询商品商家数据
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/20 14:28
     * @return: {@link  PageData<  EsGoodsListDTO >}
     * @version 1.0.1
     **/
    public Result<PageData<EsGoodsDTO>> limitByGoodsId(EsGoodsMerchantListQueryDTO dto) {
        return Result.succ(goodsService.limitByGoodsId(dto));
    }

    /**
     * 商品对应的推送商家
     *
     * @param goodsIdList 商品id集合
     * @author: wxf
     * @date: 2020/5/29 13:48
     * @return: {@link Map <String ,List<EsGoodsDTO>>}
     * @version 1.0.1
     **/
    public Result<Map<String, List<EsGoodsDTO>>> goodsRelationMerchant(List<String> goodsIdList, String cityCode) {
        return Result.succ(goodsService.goodsRelationMerchant(goodsIdList, cityCode));
    }

    /**
     * 有商品的商户集合
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/29 15:09
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.0.1
     **/
    public Result<List<EsGoodsDTO>> merchantHaveGoodsList(EsMerchantListParamDTO dto) {
        return Result.succ(goodsService.merchantHaveGoodsList(dto));
    }

    /**
     * 批量获取根据商户id
     *
     * @param shopId 商户id
     * @author: wxf
     * @date: 2020/6/5 10:27
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.1.0
     **/
    public Result<List<EsGoodsDTO>> listByShopId(Long shopId) {
        return Result.succ(goodsService.listByShopId(shopId));
    }

    public Result<List<EsGoodsDTO>> listUpperByShopId(Long shopId, Boolean isCompanyUser) {
        return Result.succ(goodsService.listUpperByShopId(shopId, isCompanyUser));
    }

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
    public Result<List<EsGoodsDTO>> listByGoodsNameAndMerchantId(String goodsName, String shopId, int size, Boolean isCompanyUser) {
        return Result.succ(goodsService.listByGoodsNameAndMerchantId(goodsName, shopId, size, isCompanyUser));
    }

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
    public Result<EsGoodsDTO> getByGoodsIdAndMerchantId(Long goodsId, Long merchantId) {
        return Result.succ(goodsService.getByGoodsIdAndMerchantId(goodsId, merchantId));
    }

    /**
     * 描述：获取根据商品id和门店id
     *
     * @param goodsId
     * @param shopId
     * @return {@link Result< EsGoodsDTO>}
     * @author lhm
     * @date 2020/7/14
     * @version 1.2.0
     **/
    public Result<EsGoodsDTO> getByGoodsIdAndShopIdId(Long goodsId, Long shopId) {
        return Result.succ(goodsService.getByGoodsIdAndShopIdId(goodsId, shopId));
    }

    /**
     * 批量获取根据首页分类id
     *
     * @param wxIndexCategoryId 首页分类id
     * @author: wxf
     * @date: 2020/5/20 14:05
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.0.1
     **/
    @Deprecated
    public Result<List<EsGoodsDTO>> listByWxIndexCategoryId(String wxIndexCategoryId) {
        return Result.succ(goodsService.listByWxIndexCategoryId(wxIndexCategoryId));
    }

    /**
     * 描述：批量删除推送到门店的商品
     *
     * @param flag  true 删除全部门店的数据 false 删除通过菜单推送到门店的商品数据
     * @param shops
     * @return {@link }
     * @author lhm
     * @date 2020/7/11
     * @version 1.2.0
     **/
    public void deleteGoodsToShop(List<Long> shops, boolean flag) {
        goodsService.deleteGoodsToShop(shops, flag);
    }

    /**
     * 方法描述: 平台/app商品信息变更同步es<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/7/13 12:39
     * @return: {@link }
     * @version 1.2.0
     **/
    public void goodsExtUpdate(GoodsExtToEsDTO dto) {
        goodsService.goodsExtUpdate(dto);
    }

    /**
     * 方法描述: 商户商品分类信息变更同步es<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/7/13 12:39
     * @return: {@link }
     * @version 1.2.0
     **/
    public void goodsCategoryUpdate(MerchantGoodsToEsDTO dto) {
        goodsService.goodsCategoryUpdate(dto);
    }

    /**
     * 方法描述: 删除ES商品数据<br>
     *
     * @param goodsId
     * @author: gz
     * @date: 2020/7/13 13:06
     * @return: {@link }
     * @version 1.2.0
     **/
    public void delGoods(Long goodsId) {
        goodsService.delGoods(goodsId);
    }

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
    public void cancelAuthGoods(Long goodsId, Long merchantId) {
        goodsService.cancelAuthGoods(goodsId, merchantId);
    }

    /**
     * 方法描述: 预售商品结束定时下架任务--同步ES数据<br>
     *
     * @param
     * @author: gz
     * @date: 2020/7/18 10:42
     * @return: {@link }
     * @version 1.2.0
     **/
    public void goodsPresellTaskSyncEs() {
        goodsService.goodsPresellTaskSyncEs();
    }

    /**
     * 方法描述 : 根据店铺id删除商品信息
     *
     * @param shopId 请求参数
     * @Author: MeiTao
     * @Date: 2020/8/5 0005 15:41
     * @return: void
     * @Since version-1.3.0
     */
    public void delGoodsByShopId(Long shopId) {
        goodsService.delGoodsByShopId(shopId);
    }

    public List<String> queryDiscountShop(DiscountQuery query) {
        return goodsService.queryDiscountShop(query);
    }

    public PageData<EsGoodsDTO> queryRecommendList(RecommendDTO dto, List<WxCategoryGoodsDTO> list, Boolean isCompanyUser) {
        return goodsService.queryRecommendList(dto, list, isCompanyUser);
    }

    /**
     * 根据店铺ID以及商品ID集合查询ES商品数据
     *
     * @param shopId      店铺ID
     * @param goodsIdsSet 商品ID集合
     * @param flag        是否查询店铺自创的商品 false-不查询 true-查询
     * @author: GongJunZheng
     * @date: 2020/9/7 14:55
     * @return: {@link List<EsGoodsEntity>}
     * @version V1.4.0
     **/
    public Result<List<EsGoodsEntity>> selectListByShopIdAndGoodsIds(Long shopId, Set<Long> goodsIdsSet, Boolean flag) {
        return Result.succ(goodsService.selectListByShopIdAndGoodsIds(shopId, goodsIdsSet, flag));
    }

    /**
     * 批量保存商品信息
     *
     * @param esGoodsList 商品信息
     * @author: GongJunZheng
     * @date: 2020/9/7 15:32
     * @return: void
     * @version V1.4.0
     **/
    public void saveBath(List<EsGoodsEntity> esGoodsList) {
        goodsService.saveBath(esGoodsList);
    }

    public Double queryMinDiscount(Long shopId) {
        return goodsService.queryMinDiscount(shopId);
    }

    public Map<String, Double> queryMinDiscount(Set<String> matchShopIds) {
        return goodsService.queryMinDiscount(matchShopIds);
    }
}
