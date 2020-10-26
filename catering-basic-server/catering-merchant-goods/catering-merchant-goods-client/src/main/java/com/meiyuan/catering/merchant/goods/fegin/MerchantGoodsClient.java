package com.meiyuan.catering.merchant.goods.fegin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meiyuan.catering.core.dto.es.MerchantBaseGoods;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.vo.wxcategory.WxCategoryGoodsVO;
import com.meiyuan.catering.goods.dto.es.GoodsEsGoodsDTO;
import com.meiyuan.catering.goods.dto.goods.*;
import com.meiyuan.catering.goods.entity.CateringGoodsEntity;
import com.meiyuan.catering.merchant.goods.dto.es.EsToPushShopQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.*;
import com.meiyuan.catering.merchant.goods.dto.merchant.*;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopGoodsUpdateDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopSkuDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantGoodsExtendEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSkuEntity;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantGoodsExtendService;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantGoodsService;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantGoodsSkuService;
import com.meiyuan.catering.merchant.goods.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lhm
 * @date 2020/7/5
 * @description
 **/
@Service
public class MerchantGoodsClient {


    @Autowired
    private CateringMerchantGoodsService merchantGoodsService;
    @Autowired
    private CateringMerchantGoodsExtendService merchantGoodsExtendService;
    @Autowired
    private CateringMerchantGoodsSkuService merchantGoodsSkuService;

    /**
     * 描述：商户商品 新增/修改
     *
     * @param dto
     * @return {@link Result< String>}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    public Result<Boolean> saveOrUpdateMerchantGoods(MerchantGoodsDTO dto) {
        return Result.succ(merchantGoodsService.saveOrUpdateMerchantGoods(dto));

    }


    /**
     * 描述：商户商品列表
     *
     * @param dto
     * @return {@link Result< MerchantGoodsListVO>}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    public Result<PageData<MerchantGoodsListVO>> merchantGoodsList(MerchantGoodsQueryDTO dto) {
        return Result.succ(merchantGoodsService.merchantGoodsList(dto));
    }


    /**
     * 描述：门店商品修改价格 库存app
     *
     * @param dto
     * @return {@link Result< String>}
     * @author lhm
     * @date 2020/7/7
     * @version 1.2.0
     **/
    public Result<Boolean> changePriceStock(ShopGoodsUpdateDTO dto) {
        return Result.succ(merchantGoodsService.changePriceStock(dto));
    }

    /**
     * 描述：门店商品修改价格 库存回显  app
     *
     * @param shopId
     * @param goodsId
     * @return {@link Result< List< ShopSkuDTO>>}
     * @author lhm
     * @date 2020/7/7
     * @version 1.2.0
     **/
    public Result<List<ShopSkuDTO>> detailPriceStock(Long shopId, Long goodsId) {
        return Result.succ(merchantGoodsService.detailPriceStock(shopId, goodsId));
    }


    /**
     * 描述：商户pc 商品详情
     *
     * @param goodsId
     * @return {@link Result< MerchantGoodsDTO>}
     * @author lhm
     * @date 2020/7/7
     * @version 1.2.0
     **/
    public Result<MerchantGoodsDetailsVO> merchantGoodsDetail(Long goodsId, Long merchantId) {
        return Result.succ(merchantGoodsService.merchantGoodsDetail(goodsId, merchantId));
    }

    /**
     * describe: 销售菜单-选择商品
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 9:36
     * @return: {com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO>>}
     * @version 1.2.0
     **/
    public Result<PageData<MerchantGoodsMenuListVO>> queryMenuPageList(MerchantGoodsMenuQueryDTO dto) {
        return Result.succ(merchantGoodsService.queryMenuPageList(dto, null));
    }

    /**
     * describe: 销售菜单-已选择商品
     * * @author: yy
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/9 11:26
     * @return: {com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO>>}
     * @version 1.2.0
     **/
    public Result<PageData<MerchantGoodsMenuListVO>> queryMenuExistencePageList(MerchantGoodsMenuQueryDTO dto) {
        return Result.succ(merchantGoodsService.queryMenuExistencePageList(dto));
    }

    public Result<List<Long>> list(Long merchantId) {
        QueryWrapper<CateringMerchantGoodsExtendEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(CateringMerchantGoodsExtendEntity::getGoodsId).eq(CateringMerchantGoodsExtendEntity::getMerchantId, merchantId);
        List<Long> goodsIds = merchantGoodsExtendService.listObjs(queryWrapper, o -> Long.valueOf(o.toString()));
        return Result.succ(goodsIds);

    }

    public Result<String> pushGoods(GoodsPushDTO dto) {
        return Result.succ(merchantGoodsService.pushGoods(dto));
    }

    public Result<Boolean> cancelPush(GoodsCancelDTO dto) {
        return Result.succ(merchantGoodsService.cancelPush(dto));
    }

    /**
     * 描述：pc端商品上下架
     *
     * @param dto
     * @return {@link Result< Boolean>}
     * @author lhm
     * @date 2020/7/8
     * @version 1.2.0
     **/
    public Result<Boolean> merchantGoodsUpOrDown(MerchantGoodsUpOrDownDTO dto) {
        return Result.succ(merchantGoodsService.merchantGoodsUpOrDown(dto));
    }

    /**
     * 方法描述: 商户APP--商品详情<br>
     *
     * @param goodsId 商品id
     * @param shopId  门店id
     * @author: gz
     * @date: 2020/7/8 17:19
     * @return: {@link Result<  MerchantAppGoodsDetailsDTO >}
     * @version 1.2.0
     **/
    public Result<MerchantAppGoodsDetailsDTO> merchantAppGoodsDetails(Long goodsId, Long shopId) {
        return merchantGoodsService.merchantAppGoodsDetail(goodsId, shopId);
    }

    /**
     * @param dto 查询条件
     * @return {@link PageData<GoodsListDTO>}
     * @description 获取商户的商品信息
     * @author yaozou
     * @date 2020/3/22 14:37
     * @version 1.0.1
     * @since v1.0.0
     */
    public Result<PageData<GoodsListDTO>> listLimitForMerchant(GoodsLimitQueryDTO dto) {
        return Result.succ(merchantGoodsService.listLimitForMerchant(dto, dto.getShopId()));
    }

    /**
     * 描述：平台修改商品信息 同步pc
     *
     * @param dto
     * @return {@link Result< Boolean>}
     * @author lhm
     * @date 2020/7/8
     * @version 1.2.0
     **/
    public Result<Boolean> updateToPc(GoodsDTO dto) {
        CateringGoodsEntity entity = BaseUtil.objToObj(dto, CateringGoodsEntity.class);
        return Result.succ(merchantGoodsService.updateToPc(entity));
    }


    /**
     * 描述：to pc 查询已授权商品信息
     *
     * @param dto
     * @return {@link Result< PageData< GoodsPushList>>}
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    public Result<PageData<GoodsPushList>> toPcGoodsList(GoodsPushListQueryDTO dto) {
        return Result.succ(merchantGoodsService.toPcGoodsList(dto));
    }


    /**
     * 平台端  删除商品
     * 描述：
     *
     * @param goodsId
     * @return {@link Result< Boolean>}
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    public Result<Boolean> deleteGoods(Long goodsId) {
        return Result.succ(merchantGoodsService.deleteGoods(goodsId));
    }

    /**
     * 方法描述: 秒杀/团购选择商品<br>
     *
     * @param dto 查询参数
     * @author: gz
     * @date: 2020/7/10 14:45
     * @return: {@link List<  MarketingSelectGoodsVO >}
     * @version 1.2.0
     **/
    public List<MarketingSelectGoodsVO> listMarketingSelectGoods(MarketingSelectGoodsQueryDTO dto) {
        return merchantGoodsService.listMarketingSelectGoods(dto);
    }

    /**
     * 方法描述: V1.3.0秒杀/团购选择商品<br>
     *
     * @param dto 查询参数
     * @author: GongJunZheng
     * @date: 2020/8/10 14:29
     * @return: {@link List<MarketingSelectGoodsVO>}
     * @version V1.3.0
     **/
    public List<MarketingSelectGoodsVO> marketingGoodsSelectQuery(MarketingGoodsSelectDTO dto) {
        return merchantGoodsService.marketingGoodsSelectQuery(dto);
    }

    /**
     * 方法描述: 通过商品id获取推送的门店id及时间<br>
     *
     * @param goodsId 商品id
     * @author: gz
     * @date: 2020/7/10 18:21
     * @return: {@link   Map < Long, Object>}
     * @version 1.2.0
     **/
    public Map<Long, LocalDateTime> listMerchantIdByGoodsId(Long goodsId) {
        return merchantGoodsExtendService.listMerchantIdByGoodsId(goodsId);
    }


    /**
     * 描述：推送至门店，组装es数据
     *
     * @param queryDtoList
     * @param merchantId
     * @return {@link List< GoodsEsGoodsDTO>}
     * @author lhm
     * @date 2020/7/13
     * @version 1.2.0
     **/
    public List<GoodsEsGoodsDTO> getEsGoodsList(List<EsToPushShopQueryDTO> queryDtoList, Long merchantId) {
        return merchantGoodsService.getEsGoodsList(queryDtoList, merchantId);
    }

    /**
     * 方法描述   获取门店商品sku详情
     *
     * @param shopId
     * @param goodsId
     * @author: lhm
     * @date: 2020/7/23 15:48
     * @return: {@link }
     * @version 1.1.0
     **/
    public List<CateringShopGoodsSkuEntity> getShopSkuList(Long shopId, Long goodsId) {
        return merchantGoodsService.getShopSkuList(shopId, goodsId);
    }

    /**
     * 方法描述: 发送商户商品修改ES消息<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/7/13 14:36
     * @return: {@link }
     * @version 1.2.0
     **/
    public void sendMerchantGoodsUpdateMsg(GoodsExtToEsDTO dto) {
        merchantGoodsService.sendMerchantGoodsUpdateMsg(dto);
    }

    /**
     * 方法描述: 通过sku编码集合获取门店商品信息<br>
     *
     * @param skuCodeList
     * @author: gz
     * @date: 2020/7/15 11:43
     * @return: {@link List<MarketingGoodsSkuDTO>}
     * @version 1.2.0
     **/
    public List<MarketingGoodsSkuDTO> listGoodsBySkuCodeList(List<String> skuCodeList, Long shopId) {
        return merchantGoodsService.listGoodsBySkuCodeList(skuCodeList, shopId);
    }


    /**
     * 描述：定时任务---次日自动自满库存
     *
     * @return {@link }
     * @author lhm
     * @date 2020/7/14
     * @version 1.2.0
     **/
    public void isFullStock() {
        merchantGoodsService.isFullStock();
    }

    /**
     * 方法描述: 处理预售商品结束下架任务<br>
     *
     * @param
     * @author: gz
     * @date: 2020/7/18 10:11
     * @return: {@link Result}
     * @version 1.2.0
     **/
    public Integer goodsPresellTask() {
        return merchantGoodsService.goodsPresellTask();
    }

    public Integer goodsPresellTaskForShop() {
        return merchantGoodsService.goodsPresellTaskForShop();
    }


    /**
     * 方法描述   商户商品删除
     *
     * @param goodsId
     * @author: lhm
     * @date: 2020/8/4 14:03
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<Boolean> deleteMerchantGoods(Long goodsId) {
        return Result.succ(merchantGoodsExtendService.deleteMerchantGoods(goodsId));
    }


    /**
     * 方法描述   获取标签集合
     *
     * @param merchantId
     * @param goodsId
     * @author: lhm
     * @date: 2020/8/5 9:47
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<MerchantGoodsDetailsVO> getLabelList(Long merchantId, Long goodsId) {
        return Result.succ(merchantGoodsService.getLabelList(merchantId, goodsId));
    }


    /**
     * 描述:查询商家商品基础数据信息
     *
     * @param goodsIds
     * @return java.util.List<com.meiyuan.catering.core.dto.es.MerchantBaseGoods>
     * @author zengzhangni
     * @date 2020/8/7 9:20
     * @since v1.3.0
     */
    public List<MerchantBaseGoods> goodsBaseInfo(Collection<Long> goodsIds, Long merchantId) {
        return merchantGoodsService.goodsBaseInfo(goodsIds, merchantId);
    }

    /**
     * 判断品牌商品是否是上架状态
     *
     * @param merchantId 品牌ID
     * @param goodsId    商品ID
     * @author: GongJunZheng
     * @date: 2020/8/14 13:45
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    public Result<Boolean> goodsIsUpState(Long merchantId, Long goodsId) {
        return Result.succ(merchantGoodsService.goodsIsUpState(merchantId, goodsId));
    }


    /**
     * 从数据库读取商品名称：商品被删除的场景
     *
     * @param goodsId
     * @return
     */
    public Result<String> getGoodsNameFromDb(Long goodsId) {
        return Result.succ(merchantGoodsService.getGoodsNameFromDb(goodsId));
    }


    /**
     * 从数据库读取商品名称：商品被删除的场景
     *
     * @param mGoodsId 活动商品ID
     * @return
     */
    public Result<String> getGoodsNameFromDbByMgoodsId(Long mGoodsId) {
        return Result.succ(merchantGoodsService.getGoodsNameFromDbByMgoodsId(mGoodsId));
    }




    /**
     * 方法描述   门店app--商品新增修改
     * @author: lhm
     * @date: 2020/8/12 16:30
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<Boolean> saveOrUpdateShopGoods(MerchantGoodsDTO dto) {
        return Result.succ(merchantGoodsService.saveOrUpdateShopGoods(dto));
    }

    /**
     * describe: 小程序类目与广告-选择商品
     * @author: yy
     * @date: 2020/9/2 11:59
     * @param dto
     * @return: {@link Result< PageData< MerchantGoodsWxCategoryPageVO>>}
     * @version 1.4.0
     **/
    public Result<PageData<MerchantGoodsWxCategoryPageVO>> queryPageMerchantGoods(MerchantGoodsWxCategoryPageDTO dto) {
        return Result.succ(merchantGoodsService.queryPageMerchantGoods(dto));
    }

    /**
     * describe: 根据商品扩展表id集合查询商品
     * @author: yy
     * @date: 2020/9/2 14:48
     * @param shopGoodsIdList
     * @return: {@link List< WxCategoryGoodsVO>}
     * @version 1.4.0
     **/
    public List<WxCategoryGoodsVO> queryByShopGoodsId(List<Long> shopGoodsIdList) {
        return merchantGoodsService.queryByShopGoodsId(shopGoodsIdList);
    }

    /**
    * 查询指定门店的所有商品信息（分页）
    * @param selectDTO 查询条件
    * @param pageNo 当前页码
    * @param pageSize 煤业查询条数
    * @author: GongJunZheng
    * @date: 2020/9/3 15:44
    * @return: {@link List<MarketingSpecialGoodsShopAllVO>}
    * @version V1.4.0
    **/
    public Result<PageData<MarketingSpecialGoodsShopAllVO>> selectShopAllGoodsSelect(MarketingGoodsSelectDTO selectDTO, Long pageNo, Long pageSize) {
        return Result.succ(merchantGoodsService.selectShopAllGoodsSelect(selectDTO, pageNo, pageSize));
    }

    /**
    * 根据商户ID、SKU编码集合查询商品的起售数量
    * @param merchantId 商户ID
    * @param skuCodeSet SKU编码集合
    * @author: GongJunZheng
    * @date: 2020/9/10 9:27
    * @return: {@link List<MarketingSpecialGoodsMinQuantityVO>}
    * @version V1.4.0
    **/
    public Result<List<MarketingSpecialGoodsMinQuantityVO>> selectGoodsMinQuantity(Long merchantId, Set<String> skuCodeSet) {
        return Result.succ(merchantGoodsSkuService.selectGoodsMinQuantity(merchantId, skuCodeSet));
    }
}
