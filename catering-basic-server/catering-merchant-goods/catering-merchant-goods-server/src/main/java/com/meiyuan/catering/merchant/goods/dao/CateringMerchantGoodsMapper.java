package com.meiyuan.catering.merchant.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.dto.es.MerchantBaseGoods;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.vo.wxcategory.WxCategoryGoodsVO;
import com.meiyuan.catering.goods.dto.goods.*;
import com.meiyuan.catering.merchant.goods.dto.goods.*;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantAppGoodsDetailsDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsMenuQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsQueryDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantGoodsEntity;
import com.meiyuan.catering.merchant.goods.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */

@Mapper
public interface CateringMerchantGoodsMapper extends BaseMapper<CateringMerchantGoodsEntity> {

    /**
     * 描述：获取spu最大值
     *
     * @param
     * @return {@link CateringMerchantGoodsEntity}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    CateringMerchantGoodsEntity maxDbCode();


    /**
     * 通过GOODSID查询，不区分is_del
     *
     * @param goodsId
     * @return
     */
    CateringMerchantGoodsEntity selectByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * 通过GOODSID查询，不区分is_del
     *
     * @param mGoodsId 活动商品表ID
     * @return 商品名称
     */
    String selectByMgoodsId(@Param("mGoodsId") Long mGoodsId);


    /**
     * 描述：商户商品列表 v1.2.0
     *
     * @param page
     * @param dto
     * @return {@link IPage< MerchantGoodsListVO>}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    IPage<MerchantGoodsListVO> merchantGoodsList(Page page, @Param("dto") MerchantGoodsQueryDTO dto);

    /**
     * describe: 销售菜单-选择商品
     *
     * @param page
     * @param dto
     * @param skuCodeSet
     * @author: yy
     * @date: 2020/7/10 14:24
     * @return: {com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO>}
     * @version 1.2.0
     **/
    IPage<MerchantGoodsMenuListVO> queryMenuPageList(@Param("page") Page<MerchantGoodsMenuQueryDTO> page,
                                                     @Param("dto") MerchantGoodsMenuQueryDTO dto,
                                                     @Param("skuCodeSet") Set<String> skuCodeSet);

    /**
     * describe: 销售菜单-已选择商品
     *
     * @param page
     * @param dto
     * @author: yy
     * @date: 2020/7/9 11:29
     * @return: {com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO>}
     * @version 1.2.0
     **/
    IPage<MerchantGoodsMenuListVO> queryMenuExistencePageList(@Param("page") Page<MerchantGoodsMenuQueryDTO> page,
                                                              @Param("dto") MerchantGoodsMenuQueryDTO dto);

    /**
     * 方法描述: 查询商户APP商品详情<br>
     *
     * @param goodsId
     * @param shopId
     * @author: gz
     * @date: 2020/7/8 17:45
     * @return: {@link MerchantAppGoodsDetailsDTO}
     * @version 1.2.0
     **/
    MerchantAppGoodsDetailsDTO merchantAppGoodsDetails(@Param("goodsId") Long goodsId, @Param("shopId") Long shopId);

    /**
     * 商户APP商品列表
     *
     * @param dto           page
     * @param goodsNameCode 名称
     * @param categoryId    分类id
     * @param goodsStatus   上下架
     * @param merchantId    商户id
     * @author: yaozou
     * @date: 2020/3/16 18:58
     * @return: {@link List <  CateringGoodsEntity >}
     **/
    IPage<GoodsListDTO> listLimitForMerchant(@Param("page") Page<GoodsLimitQueryDTO> dto,
                                             @Param("goodsNameCode") String goodsNameCode,
                                             @Param("categoryId") Long categoryId,
                                             @Param("goodsStatus") Integer goodsStatus,
                                             @Param("merchantId") Long merchantId);


    /**
     * 描述： to pc  获取已授权商品信息
     *
     * @param page
     * @param dto
     * @return {@link IPage< GoodsPushList>}
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    IPage<GoodsPushList> toPcGoodsList(@Param("page") Page page, @Param("dto") GoodsPushListQueryDTO dto);

    /**
     * 方法描述: 秒杀/团购选择商品<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/7/10 14:45
     * @return: {@link List< MarketingSelectGoodsVO>}
     * @version 1.2.0
     **/
    List<MarketingSelectGoodsVO> listMarketingSelectGoods(@Param("dto") MarketingSelectGoodsQueryDTO dto);

    /**
     * 方法描述: V1.3.0秒杀/团购选择商品<br>
     *
     * @param dto 查询参数
     * @author: GongJunZheng
     * @date: 2020/8/10 14:29
     * @return: {@link List<MarketingSelectGoodsVO>}
     * @version V1.3.0
     **/
    List<MarketingSelectGoodsVO> marketingGoodsSelectQuery(@Param("dto") MarketingGoodsSelectDTO dto);

    /**
     * describe: 查询商户商品及关联详细信息
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/13 11:37
     * @return: {com.meiyuan.catering.merchant.goods.vo.MerchantGoodsVO}
     * @version 1.2.0
     **/
    MerchantGoodsVO queryGoodsVo(@Param("dto") MarketingGoodsQueryDTO dto);

    /**
     * 通过商品skuCode获取商品数据
     *
     * @param skuCodeList
     * @param merchantId
     * @return
     */
    List<MerchantGoodsMenuListVO> listMenuGoods(@Param("merchantId") Long merchantId, @Param("list") List<String> skuCodeList);


    /**
     * 描述： 自动更新库存
     *
     * @param
     * @return {@link }
     * @author lhm
     * @date 2020/7/14
     * @version 1.2.0
     **/
    void updateStock();

    /**
     * 方法描述: 通过sku编码集合获取门店商品信息<br>
     *
     * @param skuCodeList
     * @param shopId      店铺ID
     * @author: gz
     * @date: 2020/7/15 11:43
     * @return: {@link List<MarketingGoodsSkuDTO>}
     * @version 1.2.0
     **/
    List<MarketingGoodsSkuDTO> listGoodsBySkuCode(@Param("list") List<String> skuCodeList, @Param("shopId") Long shopId);

    /**
     * 定时任务--更新预售商品结束上下架状态
     *
     * @return
     */
    int updateGoodsUpDownStatus();

    /**
     * 描述:查询商家商品基础数据信息
     *
     * @param goodsIds
     * @param merchantId
     * @return java.util.List<com.meiyuan.catering.core.dto.es.MerchantBaseGoods>
     * @author zengzhangni
     * @date 2020/8/7 9:22
     * @since v1.3.0
     */
    List<MerchantBaseGoods> goodsBaseInfo(@Param("goodsIds") Collection<Long> goodsIds, @Param("merchantId") Long merchantId);

    /**
     * describe: 小程序类目与广告-选择商品
     * @author: yy
     * @date: 2020/9/2 12:05
     * @param page
     * @param dto
     * @return: {@link IPage< MerchantGoodsWxCategoryPageVO>}
     * @version 1.4.0
     **/
    IPage<MerchantGoodsWxCategoryPageVO> queryPageMerchantGoods(@Param("page") Page page, @Param("dto") MerchantGoodsWxCategoryPageDTO dto);

    /**
     * describe: 根据商品扩展表id集合查询商品
     * @author: yy
     * @date: 2020/9/2 14:51
     * @param list
     * @return: {@link List< WxCategoryGoodsVO>}
     * @version 1.4.0
     **/
    List<WxCategoryGoodsVO> queryByShopGoodsId(@Param("list") List<Long> list);

    /**
     * 查询指定门店的所有商品信息（分页）
     * @param dto 查询条件
     * @param pageCondition 分页条件
     * @author: GongJunZheng
     * @date: 2020/9/3 15:44
     * @return: {@link IPage<MarketingSpecialGoodsShopAllVO>}
     * @version V1.4.0
     **/
    IPage<MarketingSpecialGoodsShopAllVO> selectDetailGoodsPage(@Param("page") Page<Object> pageCondition,
                                                                @Param("dto") MarketingGoodsSelectDTO dto);

    /**
     * 方法描述
     * @author: lhm
     * @date: 2020/9/19 14:15
     * @param
     * @return: {@link }
     * @version 1.3.0
     **/
    Integer updateGoodsUpDownStatusForShop();


}
