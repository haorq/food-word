package com.meiyuan.catering.merchant.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.es.MerchantBaseGoods;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsDTO;
import com.meiyuan.catering.core.page.PageData;
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
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantGoodsEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSkuEntity;
import com.meiyuan.catering.merchant.goods.vo.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
public interface CateringMerchantGoodsService extends IService<CateringMerchantGoodsEntity> {

    /**
     * 描述：pc端 新增、修改商品
     *
     * @param dto
     * @return {@link String}
     * @author lhm
     * @date 2020/7/5
     * @version 1.2.0
     **/
    Boolean saveOrUpdateMerchantGoods(MerchantGoodsDTO dto);

    /**
     * 描述：商户商品列表
     *
     * @param dto
     * @return {@link MerchantGoodsListVO}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    PageData<MerchantGoodsListVO> merchantGoodsList(MerchantGoodsQueryDTO dto);


    /**
     * 描述：门店商品修改库存 价格
     *
     * @param dto
     * @return {@link String}
     * @author lhm
     * @date 2020/7/7
     * @version 1.2.0
     **/
    Boolean changePriceStock(ShopGoodsUpdateDTO dto);

    /**
     * 描述：门店商品修改库存 价格回显
     *
     * @param shopId
     * @param goodsId
     * @return {@link List< ShopSkuDTO>}
     * @author lhm
     * @date 2020/7/7
     * @version 1.2.0
     **/
    List<ShopSkuDTO> detailPriceStock(Long shopId, Long goodsId);


    /**
     * 描述：pc端 商品详情
     *
     * @param goodsId
     * @param merchantId
     * @return {@link MerchantGoodsDetailsVO}
     * @author lhm
     * @date 2020/7/7
     * @version 1.2.0
     **/
    MerchantGoodsDetailsVO merchantGoodsDetail(Long goodsId, Long merchantId);

    /**
     * describe: 销售菜单-选择商品
     *
     * @param dto
     * @param skuCodeSet
     * @author: yy
     * @date: 2020/7/10 17:19
     * @return: {com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO>}
     * @version 1.2.0
     **/
    PageData<MerchantGoodsMenuListVO> queryMenuPageList(MerchantGoodsMenuQueryDTO dto, Set<String> skuCodeSet);

    /**
     * describe: 销售菜单-已选择商品
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/9 11:27
     * @return: {com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO>}
     * @version 1.2.0
     **/
    PageData<MerchantGoodsMenuListVO> queryMenuExistencePageList(MerchantGoodsMenuQueryDTO dto);

    /**
     * 描述：商品授权
     *
     * @param dto
     * @return {@link String}
     * @author lhm
     * @date 2020/7/7
     * @version 1.2.0
     **/
    String pushGoods(GoodsPushDTO dto);


    /**
     * 描述：商品取消授权
     *
     * @param dto
     * @return {@link String}
     * @author lhm
     * @date 2020/7/8
     * @version 1.2.0
     **/
    Boolean cancelPush(GoodsCancelDTO dto);


    /**
     * 描述：pc端 商品上下架
     *
     * @param dto
     * @return {@link Boolean}
     * @author lhm
     * @date 2020/7/8
     * @version 1.2.0
     **/
    Boolean merchantGoodsUpOrDown(MerchantGoodsUpOrDownDTO dto);

    /**
     * 方法描述: 商户APP-商品详情<br>
     *
     * @param goodsId
     * @param shopId
     * @author: gz
     * @date: 2020/7/8 17:37
     * @return: {@link Result<  MerchantAppGoodsDetailsDTO >}
     * @version 1.2.0
     **/
    Result<MerchantAppGoodsDetailsDTO> merchantAppGoodsDetail(Long goodsId, Long shopId);

    /**
     * 描述：平台修改同步商户修改
     *
     * @param entity
     * @return {@link Boolean}
     * @author lhm
     * @date 2020/7/8
     * @version 1.2.0
     **/
    Boolean updateToPc(CateringGoodsEntity entity);

    /**
     * 获取商户的商品信息
     *
     * @param dto        查询条件
     * @param merchantId 商户id
     * @return {@link PageData<GoodsListDTO>}
     * @description 获取商户的商品信息
     * @author yaozou
     * @date 2020/3/22 14:37
     * @version 1.0.1
     * @since v1.0.0
     */
    PageData<GoodsListDTO> listLimitForMerchant(GoodsLimitQueryDTO dto, Long merchantId);


    /**
     * 描述：查看已授权商品信息
     *
     * @param dto
     * @return {@link PageData< GoodsPushList>}
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    PageData<GoodsPushList> toPcGoodsList(GoodsPushListQueryDTO dto);

    /**
     * 描述：平台端删除商品
     *
     * @param goodsId
     * @return {@link Boolean}
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    Boolean deleteGoods(Long goodsId);

    /**
     * 方法描述: 秒杀/团购选择商品<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/7/10 14:45
     * @return: {@link List< MarketingSelectGoodsVO>}
     * @version 1.2.0
     **/
    List<MarketingSelectGoodsVO> listMarketingSelectGoods(MarketingSelectGoodsQueryDTO dto);

    /**
     * 方法描述: V1.3.0秒杀/团购选择商品<br>
     *
     * @param dto 查询参数
     * @author: GongJunZheng
     * @date: 2020/8/10 14:29
     * @return: {@link List<MarketingSelectGoodsVO>}
     * @version V1.3.0
     **/
    List<MarketingSelectGoodsVO> marketingGoodsSelectQuery(MarketingGoodsSelectDTO dto);

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
    List<GoodsEsGoodsDTO> getEsGoodsList(List<EsToPushShopQueryDTO> queryDtoList, Long merchantId);

    /**
     * describe: 查询商户商品及关联详细信息
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/13 11:36
     * @return: {com.meiyuan.catering.merchant.goods.vo.MerchantGoodsVO}
     * @version 1.2.0
     **/
    MerchantGoodsVO queryGoodsVo(MarketingGoodsQueryDTO dto);

    /**
     * 方法描述: 发送商户商品修改ES消息<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/7/13 14:36
     * @return: {@link }
     * @version 1.2.0
     **/
    void sendMerchantGoodsUpdateMsg(GoodsExtToEsDTO dto);


    /**
     * 描述：次日自动自满库存--定时任务
     *
     * @param
     * @return {@link }
     * @author lhm
     * @date 2020/7/14
     * @version 1.2.0
     **/
    void isFullStock();


    /**
     * 通过商品skuCode获取商品数据
     *
     * @param skuCodeList
     * @param merchantId
     * @return
     */
    List<MerchantGoodsMenuListVO> listMenuGoodsBySkuCode(Long merchantId, List<String> skuCodeList);

    /**
     * 方法描述: 通过sku编码集合获取门店商品信息<br>
     *
     * @param skuCodeList
     * @param shopId
     * @author: gz
     * @date: 2020/7/15 11:43
     * @return: {@link List<MarketingGoodsSkuDTO>}
     * @version 1.2.0
     **/
    List<MarketingGoodsSkuDTO> listGoodsBySkuCodeList(List<String> skuCodeList, Long shopId);

    /**
     * 方法描述: 处理预售商品结束下架任务<br>--商户pc
     *
     * @param
     * @author: gz
     * @date: 2020/7/18 10:11
     * @return: {@link Result}
     * @version 1.2.0
     **/
    Integer goodsPresellTask();


    /**
     * 方法描述   处理预售商品结束下架任务<br>--门店自创
     * @author: lhm
     * @date: 2020/9/19 14:12
     * @param
     * @return: {@link }
     * @version 1.3.0
     **/
    Integer goodsPresellTaskForShop();

    /**
     * 方法描述   查询门店下的sku集合
     *
     * @param shopId
     * @param goodsId
     * @author: lhm
     * @date: 2020/7/23 15:45
     * @return: {@link }
     * @version 1.1.0
     **/
    List<CateringShopGoodsSkuEntity> getShopSkuList(Long shopId, Long goodsId);


    /**
     * 方法描述   获取标签集合
     *
     * @param merchantId
     * @param goodsId
     * @author: lhm
     * @date: 2020/8/5 9:46
     * @return: {@link }
     * @version 1.3.0
     **/
    MerchantGoodsDetailsVO getLabelList(Long merchantId, Long goodsId);

    /**
     * 描述:查询商家商品基础数据信息
     *
     * @param goodsIds
     * @param merchantId
     * @return java.lang.Object
     * @author zengzhangni
     * @date 2020/8/7 9:21
     * @since v1.3.0
     */
    List<MerchantBaseGoods> goodsBaseInfo(Collection<Long> goodsIds, Long merchantId);


    /**
     * 方法描述   门店app--商品新增修改
     * @author: lhm
     * @date: 2020/8/12 16:30
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    Boolean saveOrUpdateShopGoods(MerchantGoodsDTO dto);
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
    Boolean goodsIsUpState(Long merchantId, Long goodsId);

    /**
     * 从数据库获取商品名称（针对商品被删除到场景）
     * @param goodsId
     * @return
     */
    String getGoodsNameFromDb(Long goodsId);

    /**
     * 从数据库获取商品名称（针对商品被删除到场景）
     * @param mGoodsId
     * @return
     */
    String getGoodsNameFromDbByMgoodsId(Long mGoodsId);

    /**
     * describe: 小程序类目与广告-选择商品
     * @author: yy
     * @date: 2020/9/2 12:02
     * @param dto
     * @return: {@link PageData< MerchantGoodsWxCategoryPageVO>}
     * @version 1.4.0
     **/
    PageData<MerchantGoodsWxCategoryPageVO> queryPageMerchantGoods(MerchantGoodsWxCategoryPageDTO dto);

    /**
     * describe: 根据商品扩展表id集合查询商品
     * @author: yy
     * @date: 2020/9/2 14:49
     * @param shopGoodsIdList
     * @return: {@link List< WxCategoryGoodsVO>}
     * @version 1.4.0
     **/
    List<WxCategoryGoodsVO> queryByShopGoodsId(List<Long> shopGoodsIdList);

    /**
     * 查询指定门店的所有商品信息（分页）
     * @param selectDTO 查询条件
     * @param pageNo 当前页码
     * @param pageSize 煤业查询条数
     * @author: GongJunZheng
     * @date: 2020/9/3 15:44
     * @return: {@link PageData<MarketingSpecialGoodsShopAllVO>}
     * @version V1.4.0
     **/
    PageData<MarketingSpecialGoodsShopAllVO> selectShopAllGoodsSelect(MarketingGoodsSelectDTO selectDTO, Long pageNo, Long pageSize);

}
