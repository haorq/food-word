package com.meiyuan.catering.wx.utils.support;

import com.meiyuan.catering.core.dto.PresellFlagDTO;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.cart.Cart;
import com.meiyuan.catering.core.util.*;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.dto.marketing.EsMarketingDTO;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.es.util.EsUtil;
import com.meiyuan.catering.goods.dto.category.CategoryDTO;
import com.meiyuan.catering.goods.feign.GoodsCacheUtilClient;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingTicketActivityTypeEnum;
import com.meiyuan.catering.marketing.feign.MarketingGrouponClient;
import com.meiyuan.catering.marketing.feign.MarketingRepertoryClient;
import com.meiyuan.catering.marketing.feign.MarketingSeckillEventRelationClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventVO;
import com.meiyuan.catering.marketing.vo.ticket.WxMerchantIndexTicketInfoVO;
import com.meiyuan.catering.merchant.goods.fegin.MerchantCategoryClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsSkuClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.order.vo.MerchantCountVO;
import com.meiyuan.catering.user.dto.cart.CartCountSelectedNumDTO;
import com.meiyuan.catering.user.fegin.cart.CartClient;
import com.meiyuan.catering.user.fegin.sharebill.CartShareBillClient;
import com.meiyuan.catering.user.utils.CartUtil;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.goods.WxCategoryDTO;
import com.meiyuan.catering.wx.dto.merchant.*;
import com.meiyuan.catering.wx.dto.merchant.v1.WxMerchantIndexSeckillV1DTO;
import com.meiyuan.catering.wx.dto.merchant.v1.WxMerchantIndexV1RequestDTO;
import com.meiyuan.catering.wx.dto.merchant.v1.WxMerchantSeckillGoodsDTO;
import com.meiyuan.catering.wx.dto.merchant.v13.WxMerchantActivityVO;
import com.meiyuan.catering.wx.utils.WechatUtils;
import com.meiyuan.catering.wx.utils.WxCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020/7/9 11:17
 * @since v1.1.0
 */
@Slf4j
@Component
public class MerchantIndexSupport {
    @Autowired
    private EsGoodsClient esGoodsClient;
    @Autowired
    private MerchantCategoryClient merchantCategoryClient;
    @Resource
    private EsMarketingClient esMarketingClient;
    @Autowired
    private CartClient cartClient;
    @Autowired
    private ShopGoodsSkuClient shopGoodsSkuClient;
    @Autowired
    private MarketingRepertoryClient repertoryClient;
    @Autowired
    private WechatUtils wechatUtils;
    @Autowired
    private CartShareBillClient cartShareBillClient;
    @Autowired
    private MerchantUtils merchantUtils;
    @Autowired
    private OrderClient orderClient;
    @Resource
    private GoodsCacheUtilClient goodsCacheUtilClient;
    @Resource
    private UserTicketClient userTicketClient;
    @Resource
    private MarketingGrouponClient marketingGrouponClient;
    @Resource
    private MarketingSeckillEventRelationClient seckillEventRelationClient;


    public void setCommonInfo(WxMerchantIndexV13DTO indexDTO, UserTokenDTO token, WxMerchantIndexV1RequestDTO dto) {

        //经纬度
        String location = dto.getLocation();
        dto.setLocation(StringUtils.isBlank(location) ? wechatUtils.getdefaultLocation() : location);

        // 拼单处理
        // 0、获取用户拼单信息(发起人)
        if (StringUtils.isBlank(dto.getShareBillNo()) && token != null && token.getUserId() != null) {
            // 当前用户是否在此商户下发起过拼单(未生成订单的拼单)
            String shareBillNo = ClientUtil.getDate(cartShareBillClient.getNotPayShareBill(dto.getMerchantId(), dto.getShopId(), token.getUserId()));
            dto.setShareBillNo(shareBillNo);
        }

        indexDTO.setShareBillNo(dto.getShareBillNo());
        indexDTO.setShareBillFlag(StringUtils.isNotBlank(dto.getShareBillNo()));
        // 是否可以创建拼单
        indexDTO.setCreateShareBillFlag(!WxCommonUtil.isCompanyUser(token));
    }

    public void setBaseInfo(WxMerchantIndexV13DTO indexDTO, UserTokenDTO token, WxMerchantIndexV1RequestDTO dto) {

        Long shopId = dto.getShopId();

        // 1、商家基本信息
        ShopInfoDTO shopInfoDTO = merchantUtils.getShopIsNullThrowEx(shopId);

        indexDTO.setMerchantId(shopInfoDTO.getMerchantId());
        indexDTO.setShopId(shopId);
        WxMerchantBaseInfoDTO baseInfo = ConvertUtils.sourceToTarget(shopInfoDTO, WxMerchantBaseInfoDTO.class);
        baseInfo.setFoodBusinessLicense(shopInfoDTO.getFoodBusinessLicense());
        baseInfo.setBusinessLicense(shopInfoDTO.getBusinessLicense());
        baseInfo.setOpeningTime(shopInfoDTO.getOpeningTime());
        baseInfo.setClosingTime(shopInfoDTO.getClosingTime());

        //用户商家距离计算
        baseInfo.setDistanceStr(EsUtil.distanceStr(dto.getLocation(), shopInfoDTO.getMapCoordinate()));


        MerchantInfoDTO merchant = merchantUtils.getMerchantIsNullThrowEx(shopInfoDTO.getMerchantId());
        baseInfo.setMerchantAttribute(merchant.getMerchantAttribute());
        baseInfo.setMerchantStatus(merchant.getMerchantStatus());


        indexDTO.setBaseInfo(baseInfo);

        MerchantCountVO merchantCount = orderClient.getMerchantCount(shopId);
        indexDTO.setAppraiseInfo(merchantCount);

    }

    public void setConfigInfo(WxMerchantIndexV13DTO indexDTO, UserTokenDTO token, WxMerchantIndexV1RequestDTO dto) {
        Long shopId = dto.getShopId();
        ShopInfoDTO shopInfo = merchantUtils.getShopIsNullThrowEx(shopId);
        ShopConfigInfoDTO shopConfigInfoDTO = merchantUtils.getShopConfigInfo(shopId);
        if (shopConfigInfoDTO != null) {
            WxMerchantConfigInfoDTO configInfoDTO = new WxMerchantConfigInfoDTO();
            configInfoDTO.setDeliveryPrice(shopConfigInfoDTO.getDeliveryPrice());
            BigDecimal freeDeliveryPrice = shopConfigInfoDTO.getFreeDeliveryPrice();
            configInfoDTO.setFreeDeliveryPrice(freeDeliveryPrice != null ? freeDeliveryPrice : BaseUtil.PRICE);
            configInfoDTO.setLeastDeliveryPrice(shopConfigInfoDTO.getLeastDeliveryPrice());
            configInfoDTO.setBusinessSupport(shopConfigInfoDTO.getBusinessSupport());
            configInfoDTO.setShopImgList(shopInfo.getShopPictureList());
            indexDTO.setConfigInfo(configInfoDTO);
        } else {
            log.error("门店信息获取失败");
        }
    }

    public void setCategoryInfo(WxMerchantIndexV13DTO indexDTO, UserTokenDTO token, WxMerchantIndexV1RequestDTO dto) {
        //选中数量,库存
        DataMap dataMap = getDataMap(token, dto);

        //秒杀分类数据
        seckillCategory(indexDTO, dataMap, token, dto);

        //普通分类数据
        List<WxMerchantGoodsDTO> goodsDTOS = ordinaryCategory(indexDTO, dataMap, token, dto);

        //折扣分类数据
        discountCategory(indexDTO, token, goodsDTOS);

    }


    public void setDiscountsInfo(WxMerchantIndexV13DTO indexDTO, UserTokenDTO token, WxMerchantIndexV1RequestDTO dto) {
        Long shopId = dto.getShopId();
        Long userId = token != null ? token.getUserId() : null;

        //商品最低折扣
        goodsMinDiscount(indexDTO, shopId, token);

        //查询门店所有的卷 活动
        List<WxMerchantIndexTicketInfoVO> ticketInfoVoList = userTicketClient.listWxMerchantIndexTicket(shopId, userId);

        //店外发券  和 平台补贴(平台补贴 排到 店外发券 前面)
        storeOuterOrPlatformCoupon(indexDTO, ticketInfoVoList);

        //店内领券
        storeWithinCoupon(indexDTO, ticketInfoVoList);

        //秒杀最低价格
        seckillMinPrice(indexDTO, shopId, token);

        //团购最低价格
        grouponMinPrice(indexDTO, shopId, token);

        //满减配送 商家配置了配送费（不等于0），且配置了『订单满X元免配送费』，则显示配送费优惠类型。
        dispatching(indexDTO);
    }

    private void grouponMinPrice(WxMerchantIndexV13DTO indexDTO, Long shopId, UserTokenDTO token) {
        Integer marketingUserType = WxCommonUtil.getMarketingUserType(token);
        WxMerchantActivityVO activityVO = indexDTO.getDiscountsActivity();
        Double grouponMinPrice = esMarketingClient.queryMarketingDiscount(shopId.toString(), MarketingOfTypeEnum.GROUPON.getStatus(), marketingUserType).getData();
        if (grouponMinPrice != null) {
            String grouponMsg = DiscountInfo.grouponMsg(grouponMinPrice);
            activityVO.setGroupLowestAmount(grouponMsg);
            //活动数量加一
            indexDTO.activityNumberIncrement();
            indexDTO.addActivityStr(grouponMsg);
        }

    }

    private void seckillMinPrice(WxMerchantIndexV13DTO indexDTO, Long shopId, UserTokenDTO token) {
        Integer marketingUserType = WxCommonUtil.getMarketingUserType(token);
        WxMerchantActivityVO activityVO = indexDTO.getDiscountsActivity();
        Double seckillMinPrice = esMarketingClient.queryMarketingDiscount(shopId.toString(), MarketingOfTypeEnum.SECKILL.getStatus(), marketingUserType).getData();
        if (seckillMinPrice != null) {
            String seckillMsg = DiscountInfo.seckillMsg(seckillMinPrice);
            activityVO.setSeckillLowestAmount(seckillMsg);
            //活动数量加一
            indexDTO.activityNumberIncrement();

            indexDTO.addActivityStr(seckillMsg);
        }
    }

    private void goodsMinDiscount(WxMerchantIndexV13DTO indexDTO, Long shopId, UserTokenDTO token) {
        //企业用户不存在折扣
        if (WxCommonUtil.isCompanyUser(token)) {
            return;
        }

        WxMerchantActivityVO activityVO = indexDTO.getDiscountsActivity();
        Double minDiscount = esGoodsClient.queryMinDiscount(shopId);
        if (minDiscount != null) {
            activityVO.setOrdinaryLowestDiscount(DiscountInfo.goodsDiscountMsg(minDiscount));
            //活动数量加一
            indexDTO.activityNumberIncrement();
            //活动字符串记录
            indexDTO.addActivityStr(DiscountInfo.goodsDiscountSimpleMsg(minDiscount));
        }
    }

    private void dispatching(WxMerchantIndexV13DTO indexDTO) {
        Long shopId = indexDTO.getShopId();
        ShopConfigInfoDTO shopConfigInfo = merchantUtils.getShopConfigInfo(shopId);
        if (shopConfigInfo == null) {
            return;
        }
        String dispatchingStr = shopConfigInfo.getDispatching();
        if (dispatchingStr == null) {
            return;
        }

        WxMerchantActivityVO activity = indexDTO.getDiscountsActivity();
        activity.setDispatchingStr(dispatchingStr);
        //优惠活动 +1
        indexDTO.activityNumberIncrement();
        indexDTO.addActivityStr(dispatchingStr);
    }

    private void storeOuterOrPlatformCoupon(WxMerchantIndexV13DTO indexDTO, List<WxMerchantIndexTicketInfoVO> ticketInfoVoList) {
        //店外发券  和 平台补贴(平台补贴 排到 店外发券 前面)
        List<WxMerchantIndexTicketInfoVO> deductionList = ticketInfoVoList.stream().filter(e ->
                Objects.equals(e.getActivityType(), MarketingTicketActivityTypeEnum.SHOP_OUT_GET.getStatus())
                        || Objects.equals(e.getActivityType(), MarketingTicketActivityTypeEnum.PLATFORM_SUBSIDY.getStatus())
        ).sorted(Comparator.comparing(WxMerchantIndexTicketInfoVO::getActivityType).reversed()).collect(Collectors.toList());

        if (deductionList.size() > 0) {
            WxMerchantActivityVO activityVO = indexDTO.getDiscountsActivity();

            StringBuilder builder = new StringBuilder();
            deductionList.forEach(ticket ->
                    {
                        StringBuilder str = new StringBuilder();
                        BigDecimal consumeCondition = ticket.getConsumeCondition();
                        if (BaseUtil.isZero(consumeCondition)) {
                            str.append(DiscountInfo.sendTicketMsg(ticket.getAmount()));
                        } else {
                            str.append(DiscountInfo.sendTicketSubtractMsg(consumeCondition, ticket.getAmount()));
                        }
                        builder.append(str).append(";");
                        indexDTO.addActivityStr(str.toString());
                    }
            );

            //设置领卷信息
            activityVO.setDeductionStr(builder.toString());
            //活动数量加一
            indexDTO.activityNumberIncrement();
        }
    }

    private void storeWithinCoupon(WxMerchantIndexV13DTO indexDTO, List<WxMerchantIndexTicketInfoVO> ticketInfoVoList) {
        //店内领券 这里不过滤库存的原因是 首页优惠活动的列表需要,和库存无关,过滤了就没有了
        List<WxMerchantIndexTicketInfoVO> couponList = ticketInfoVoList.stream().filter(e ->
                Objects.equals(e.getActivityType(), MarketingTicketActivityTypeEnum.SHOP_IN_GET.getStatus())
        ).sorted(Comparator.comparing(
                WxMerchantIndexTicketInfoVO::getPull).reversed()
                .thenComparing(WxMerchantIndexTicketInfoVO::getAmount).reversed()
                .thenComparing(WxMerchantIndexTicketInfoVO::getConsumeCondition)
        ).collect(Collectors.toList());

        //用户优惠卷主键id
        List<Long> userTicketIds = couponList.stream().filter(e -> e.getUserTicketId() != null).map(WxMerchantIndexTicketInfoVO::getUserTicketId).collect(Collectors.toList());
        //用户未使用的优惠卷id
        List<Long> noUsedIds = userTicketIds.size() > 0 ? userTicketClient.listUserNoUsedTicket(userTicketIds) : userTicketIds;

        //商家首页顶部只显示([用户未领取并且库存大于0]或者[用户已领取并且未使用])的优惠卷
        indexDTO.setCouponInfo(couponList.stream().filter(e ->
                (e.getPull() && noUsedIds.contains(e.getUserTicketId()))
                        || (!e.getPull() && e.getResidualInventory() > 0)
        ).collect(Collectors.toList()));

        if (couponList.size() > 0) {
            WxMerchantActivityVO activityVO = indexDTO.getDiscountsActivity();

            StringBuilder builder = new StringBuilder();
            couponList.forEach(ticket -> {
                        StringBuilder str = new StringBuilder();
                        str.append(DiscountInfo.getTicketMsg(ticket.getAmount()));
                        builder.append(str).append(";");

                        indexDTO.addActivityStr(str.toString());
                    }
            );
            //设置领卷信息
            activityVO.setCouponStr(builder.toString());
            //活动数量加一
            indexDTO.activityNumberIncrement();
        }
    }


    private void checkedCategory(WxMerchantIndexV13DTO indexDTO, Long categoryId) {
        List<WxCategoryDTO> categoryInfo = indexDTO.getCategoryInfo();
        if (categoryInfo.size() > 0) {
            //参数分类id不为null
            if (categoryId != null) {
                //查询匹配的分类id设置选中
                WxCategoryDTO wxCategoryDTO = categoryInfo.stream().filter(c -> Objects.equals(c.getId(), categoryId)).findFirst().orElse(null);
                if (wxCategoryDTO != null) {
                    wxCategoryDTO.setChecked(true);
                } else {
                    //默认选中第一个
                    WxCategoryDTO wxCategory = categoryInfo.get(0);
                    wxCategory.setChecked(true);
                }
            } else {
                //默认选中第一个
                WxCategoryDTO wxCategoryDTO = categoryInfo.get(0);
                wxCategoryDTO.setChecked(true);
            }
        }
    }

    private void discountCategory(WxMerchantIndexV13DTO indexDTO, UserTokenDTO token, List<WxMerchantGoodsDTO> goodsDTOS) {

        //企业用户不会存在折扣
        if (WxCommonUtil.isCompanyUser(token)) {
            return;
        }

        //创建折扣分类
        WxCategoryDTO discountCategory = WxCategoryDTO.getDiscountCategory();

        WxMerchantCategoryGoodsDTO categoryGoodsDTO = new WxMerchantCategoryGoodsDTO();

        //过滤出特价商品
        List<WxMerchantGoodsDTO> discountGoodsList = goodsDTOS.stream()
                .filter(e -> e.getSpecialState() != null && e.getSpecialState()).collect(Collectors.toList());

        if (discountGoodsList.size() > 0) {
            categoryGoodsDTO.setGoodsList(discountGoodsList);
            discountCategory.setCategoryGoodsDTO(categoryGoodsDTO);

            indexDTO.addDiscountCategory(discountCategory);
        }

    }


    private List<WxMerchantGoodsDTO> ordinaryCategory(WxMerchantIndexV13DTO indexDTO, DataMap dataMap, UserTokenDTO token, WxMerchantIndexV1RequestDTO dto) {
        Long shopId = dto.getShopId();

        //商品数据
        List<EsGoodsDTO> esGoodsDtoS = listUpperByShopId(shopId, token);

        //查询商品月销 k(shopId:goodsId)  -> v(月销量)
        Map<String, Long> monthSalesMap = getGoodsMonthSales(shopId, esGoodsDtoS);

        //转换商品数据
        List<WxMerchantGoodsDTO> goodsDtoList = getWxMerchantGoodsDtoList(dataMap, token, shopId, esGoodsDtoS, monthSalesMap);

        //获取分类信息
        List<WxCategoryDTO> wxCategoryList = getWxCategoryDtoList(dataMap, goodsDtoList, shopId);
        if (wxCategoryList != null) {
            indexDTO.addAllCategory(wxCategoryList);
        }

        return goodsDtoList;
    }

    private List<WxCategoryDTO> getWxCategoryDtoList(DataMap dataMap, List<WxMerchantGoodsDTO> goodsDtoList, Long shopId) {
        //预售排序,分类分组
        Map<Long, List<WxMerchantGoodsDTO>> categoryMap = goodsDtoList.stream()
                .sorted(Comparator.comparing(WxMerchantGoodsDTO::getPresellFlag))
                .collect(Collectors.groupingBy(WxMerchantGoodsDTO::getCategoryId));

        //查询分类
        Set<Long> categoryIds = categoryMap.keySet();

        if (categoryIds.size() <= 0) {
            return null;
        }

        List<CategoryDTO> categoryList = merchantCategoryClient.queryCategoryByIds(shopId, categoryIds).getData();

        //组装分类数据
        return categoryList.stream().map(dbCategory -> {
            WxCategoryDTO wxCategory = new WxCategoryDTO();
            wxCategory.setId(dbCategory.getId());
            wxCategory.setCategoryName(dbCategory.getCategoryName());
            wxCategory.setCategoryPicture(dbCategory.getCategoryPicture());
            wxCategory.setChecked(false);
            wxCategory.setSelectedNum(dataMap.getCategoryValue(dbCategory.getId()));

            WxMerchantCategoryGoodsDTO categoryGoodsDTO = new WxMerchantCategoryGoodsDTO();
            categoryGoodsDTO.setCategoryPicture(dbCategory.getCategoryPicture());
            categoryGoodsDTO.setGoodsList(categoryMap.get(dbCategory.getId()));

            wxCategory.setCategoryGoodsDTO(categoryGoodsDTO);

            return wxCategory;
        }).collect(Collectors.toList());
    }

    private List<WxMerchantGoodsDTO> getWxMerchantGoodsDtoList(DataMap dataMap, UserTokenDTO token, Long shopId, List<EsGoodsDTO> esGoodsDtoS, Map<String, Long> monthSalesMap) {
        //转换商品数据
        return esGoodsDtoS.stream().map(esGoods -> {
            WxMerchantGoodsDTO goodsDTO = BaseUtil.objToObj(esGoods, WxMerchantGoodsDTO.class);

            //根据用户类型赋值销售价
//            setSalesPriceByUserType(token, goodsDTO);

            WxMerchantGoodsSkuDTO skuDTO = goodsDTO.getSkuList().get(0);
            //shuCode
            goodsDTO.setSkuCode(skuDTO.getSkuCode());
            //x份优惠
            goodsDTO.setDiscountLimit(skuDTO.getDiscountLimit());
            //折扣
            goodsDTO.setDiscountLabel(WxCommonUtil.getDiscountByUserType(token, esGoods));
            //选中数量
            goodsDTO.setSelectedNum(dataMap.getGoodsValue(goodsDTO.getGoodsId()));
            //设置月销
            goodsDTO.setMonthSalesCount(monthSalesMap.get(BaseUtil.billKey(shopId, goodsDTO.getGoodsId())));

            //处理sku
            goodsDTO.getSkuList().forEach(sku -> {
//                //根据用户类型赋值价格
//                disposePriceByUserType(token, sku, goodsDTO);
                //选中数量
                sku.setSelectedNum(dataMap.getOrdinarySkuValue(sku.getSkuCode()));
                //库存
                sku.setResidualInventory(dataMap.getInventoryMapValue(sku.getSkuCode()));
                //sku折扣
                sku.setDiscountLabel(WxCommonUtil.getDiscountByUserType(token, sku));
                //sku起售数量
                sku.setLowestBuy(WxCommonUtil.getLowestBuy(token, sku));
            });

            return goodsDTO;
        }).collect(Collectors.toList());
    }


    private Map<String, Long> getGoodsMonthSales(Long shopId, List<EsGoodsDTO> esGoodsDtoS) {
        Set<String> stringSet = esGoodsDtoS.stream().map(e -> BaseUtil.billKey(shopId, e.getGoodsId())).collect(Collectors.toSet());
        return goodsCacheUtilClient.getMonthSalesAll(stringSet).getData();
    }

    private List<EsGoodsDTO> listUpperByShopId(Long shopId, UserTokenDTO token) {
        boolean isCompanyUser = WxCommonUtil.isCompanyUser(token);
        List<EsGoodsDTO> esGoodsDtoList = esGoodsClient.listUpperByShopId(shopId, isCompanyUser).getData();
        //预售处理
        esGoodsDtoList.forEach(this::presellJudge);

        return esGoodsDtoList;
    }

    private void seckillCategory(WxMerchantIndexV13DTO indexDTO, DataMap dataMap, UserTokenDTO token, WxMerchantIndexV1RequestDTO dto) {
        // 1、获得商户正在进行的秒杀活动
        Integer marketingUserType = WxCommonUtil.getMarketingUserType(token);
        List<EsMarketingDTO> seckillList = esMarketingClient.seckillListByShopId(dto.getShopId(), marketingUserType).getData();

        if (seckillList != null && seckillList.size() > 0) {

            //对秒杀商品数据分组
            Map<Long, List<EsMarketingDTO>> dtoMap = seckillList.stream().collect(Collectors.groupingBy(EsMarketingDTO::getId));

            //活动id
            List<Long> seckillIdList = seckillList.stream().map(EsMarketingDTO::getId).collect(Collectors.toList());
            //查询进行中的秒杀场次
            List<MarketingSeckillEventVO> seckillEventVoList = seckillEventRelationClient.selectUnderwayEventList(seckillIdList).getData();

            //分组 EventId(场次ID) - > [{关联的活动}]
            Map<Long, List<MarketingSeckillEventVO>> groupingVos = seckillEventVoList.stream().collect(Collectors.groupingBy(MarketingSeckillEventVO::getEventId));

            //去重 保留不相同的场次 (8:00 9:00)
            Map<Long, MarketingSeckillEventVO> eventVoMap = seckillEventVoList.stream().collect(Collectors.toMap(MarketingSeckillEventVO::getEventId, e -> e, (o, n) -> o = n));

            //获取封装之后的秒杀信息
            List<WxMerchantIndexSeckillV1DTO> indexSeckillV1DtoList = getWxMerchantIndexSeckillV1DtoList(dataMap, dtoMap, groupingVos, eventVoMap);

            if (indexSeckillV1DtoList.size() > 0) {
                // 创建秒杀分类
                WxCategoryDTO categoryDTO = WxCategoryDTO.getSeckillCategory();
                categoryDTO.setSelectedNum(dataMap.getCategoryValue(categoryDTO.getId()));

                indexSeckillV1DtoList.sort(Comparator.comparing(WxMerchantIndexSeckillV1DTO::getEventBeginTime));
                categoryDTO.setSeckillGoodsDTO(indexSeckillV1DtoList);
                //添加分类
                indexDTO.addCategory(categoryDTO);
            }


        }
    }

    private List<WxMerchantIndexSeckillV1DTO> getWxMerchantIndexSeckillV1DtoList(DataMap dataMap,
                                                                                 Map<Long, List<EsMarketingDTO>> dtoMap,
                                                                                 Map<Long, List<MarketingSeckillEventVO>> groupingVos,
                                                                                 Map<Long, MarketingSeckillEventVO> eventVoMap) {
        //根据场次构建 场次信息和场次商品信息
        return eventVoMap.keySet().stream().map(eventId -> {

            WxMerchantIndexSeckillV1DTO seckillV1DTO = new WxMerchantIndexSeckillV1DTO();

            MarketingSeckillEventVO eventVO = eventVoMap.get(eventId);
            seckillV1DTO.setSeckillName(eventVO.getEventBeginTime().toString());
            seckillV1DTO.setEventBeginTime(eventVO.getEventBeginTime());
            seckillV1DTO.setEventEndTime(eventVO.getEventEndTime());
            seckillV1DTO.setEventTime(eventVO.getEventTime());
            seckillV1DTO.setEventId(eventId);
            seckillV1DTO.setSeckillId(eventVO.getSeckillId());

            //创建场次商品信息 保存所有在这个场次的秒杀活动商品
            List<WxMerchantSeckillGoodsDTO> list = new ArrayList<>();


            //根据场次获取场次和活动的关联关系
            List<MarketingSeckillEventVO> marketingSeckillEventVoList = groupingVos.get(eventId);
            //去重
            List<MarketingSeckillEventVO> vos = marketingSeckillEventVoList.stream().distinct().collect(Collectors.toList());

            //循环创建商品信息
            vos.forEach(seckillEventVO -> {

                //通过活动id 获取商品信息 可能会存在多个
                List<EsMarketingDTO> esMarketingDtoList = dtoMap.get(seckillEventVO.getSeckillId());

                //构建这个活动的商品信息
                List<WxMerchantSeckillGoodsDTO> seckillGoodsDtoList = esMarketingDtoList.stream().map(esMarketingDTO -> {
                    WxMerchantSeckillGoodsDTO seckillGoodsDto = BaseUtil.objToObj(esMarketingDTO, WxMerchantSeckillGoodsDTO.class);
                    seckillGoodsDto.setGoodsSpecType(esMarketingDTO.getGoodsSpecType());
                    //秒杀场次id
                    seckillGoodsDto.setSeckillEventId(eventId);
                    //选中数量
                    seckillGoodsDto.setSelectedNum(dataMap.getSeckillSkuValue(CartUtil.seckillKey(seckillGoodsDto.getSku(), eventId)));
                    //库存
                    seckillGoodsDto.setResidualInventory(dataMap.getSeckillInventoryValue(BaseUtil.seckillKey(seckillGoodsDto.getMGoodsId(), eventId)));
                    //折扣
                    seckillGoodsDto.setDiscountLabel(BaseUtil.discountLabel(seckillGoodsDto.getStorePrice(), seckillGoodsDto.getActivityPrice()));
                    //场次结束时间
                    seckillGoodsDto.setEventEndTime(seckillEventVO.getEventEndTime());

                    return seckillGoodsDto;
                }).collect(Collectors.toList());

                //添加当前活动使用的商品信息
                list.addAll(seckillGoodsDtoList);
            });

            seckillV1DTO.setGoodsList(list);

            return seckillV1DTO;
        }).collect(Collectors.toList());
    }


    private void disposePriceByUserType(UserTokenDTO token, WxMerchantGoodsSkuDTO sku, WxMerchantGoodsDTO goodsDTO) {
        if (WxCommonUtil.isCompanyUser(token)) {
            //企业用户不享受每单限x份优惠
            goodsDTO.setDiscountLimit(BaseUtil.NULL_FLAG.intValue());
            sku.setDiscountLimit(BaseUtil.NULL_FLAG.intValue());
            if (!BaseUtil.isNegativeOne(sku.getEnterprisePrice())) {
                sku.setSalesPrice(sku.getEnterprisePrice());
            } else {
                sku.setSalesPrice(sku.getMarketPrice());
            }
        } else {
            if (BaseUtil.isNegativeOne(sku.getSalesPrice())) {
                sku.setSalesPrice(sku.getMarketPrice());
            }
        }
    }


    private DataMap getDataMap(UserTokenDTO token, WxMerchantIndexV1RequestDTO dto) {
        DataMap dataMap = new DataMap();

        //设置选中数量(分类,商品,sku)
        setSelectedMap(dataMap, token, dto);
        //设置商品sku库存
        setInventoryMap(dataMap, token, dto);

        return dataMap;

    }

    private void setInventoryMap(DataMap dataMap, UserTokenDTO token, WxMerchantIndexV1RequestDTO dto) {

        //普通商品sku库存
        Map<String, Integer> integerMap = shopGoodsSkuClient.getRemainStock(dto.getMerchantId(), dto.getShopId(), null).getData();
        dataMap.setInventoryMap(integerMap);

        //秒杀商品库存
        Integer marketingUserType = WxCommonUtil.getMarketingUserType(token);
        Map<String, Integer> inventoryMap = repertoryClient.getInventoryByOfId(dto.getShopId(), marketingUserType).getData();
        dataMap.setSeckillInventoryMap(inventoryMap);
    }


    private void setSelectedMap(DataMap dataMap, UserTokenDTO token, WxMerchantIndexV1RequestDTO dto) {
        if (token == null) {
            return;
        }

        CartCountSelectedNumDTO query = CartCountSelectedNumDTO.builder()
                .userId(token.getUserId())
                .shopId(dto.getShopId())
                .shareBillNo(dto.getShareBillNo()).build();

        List<Cart> carts = cartClient.countSelectedNumber(query);

        if (carts == null || carts.size() <= 0) {
            return;
        }

        //分类选中数量
        Map<Long, Integer> categoryMap = categorySelected(token, dto, carts);
        //普通商品sku选中数量
        Map<String, Integer> ordinarySkuMap = carts.stream().filter(cart -> CartUtil.isOrdinaryGoods(cart.getGoodsType())).collect(Collectors.toMap(Cart::getSkuCode, Cart::getNumber, (n, o) -> n + o));
        //秒杀商品sku选中数量
        Map<String, Integer> seckillSkuMap = carts.stream().filter(cart -> CartUtil.isSeckillGoods(cart.getGoodsType())).collect(Collectors.toMap(cart -> CartUtil.seckillKey(cart.getSkuCode(), cart.getSeckillEventId()), Cart::getNumber, (n, o) -> n + o));
        //普通商品选中数量
        Map<Long, Integer> goodsMap = carts.stream().filter(cart -> CartUtil.isOrdinaryGoods(cart.getGoodsType())).collect(Collectors.toMap(Cart::getGoodsId, Cart::getNumber, (newValue, oldValueList) -> oldValueList + newValue));


        //保存分类选中数量
        dataMap.setCategoryMap(categoryMap);
        //保存商品选中数量
        dataMap.setGoodsMap(goodsMap);
        //保存普通商品sku选中数量
        dataMap.setOrdinarySkuMap(ordinarySkuMap);
        //保存秒杀商品sku选中数量
        dataMap.setSeckillSkuMap(seckillSkuMap);
    }

    private Map<Long, Integer> categorySelected(UserTokenDTO token, WxMerchantIndexV1RequestDTO dto, List<Cart> carts) {
        //普通商品分类
        Map<Long, Integer> categoryMap = getOrdinaryCategory(dto, carts);

        Integer seckillNumber = getSeckillCategory(token, dto, carts);
        //秒杀分类 选中数量
        categoryMap.put(CartUtil.SECKILL_CATEGORY_ID, seckillNumber);
        return categoryMap;
    }

    private Integer getSeckillCategory(UserTokenDTO token, WxMerchantIndexV1RequestDTO dto, List<Cart> carts) {
        Set<Long> marketing = null;
        List<EsMarketingDTO> data = esMarketingClient.seckillListByShopId(dto.getShopId(), token.getMarketingUserType()).getData();
        if (data != null) {
            marketing = data.stream().map(EsMarketingDTO::getMGoodsId).collect(Collectors.toSet());
        }

        Set<Long> finalMarketing = marketing;

        return carts.stream().filter(cart ->
                CartUtil.isSeckillGoods(cart.getGoodsType())
                        && finalMarketing != null
                        && finalMarketing.contains(cart.getGoodsId())
        ).mapToInt(Cart::getNumber).sum();
    }

    private Map<Long, Integer> getOrdinaryCategory(WxMerchantIndexV1RequestDTO dto, List<Cart> carts) {
        //普通商品的商品id集合
        List<Long> collect = carts.stream().filter(cart -> CartUtil.isOrdinaryGoods(cart.getGoodsType())).map(Cart::getGoodsId).collect(Collectors.toList());
        //商品id的选中数量
        Map<Long, Integer> integerMap = carts.stream().collect(Collectors.toMap(Cart::getGoodsId, Cart::getNumber, (newValue, oldValueList) -> oldValueList + newValue));

        List<EsGoodsDTO> goodsDtoS = new ArrayList<>();

        if (collect.size() > 0) {
            //批量查询商品获取商品最新的分类id
            Result<List<EsGoodsDTO>> listResult = esGoodsClient.queryCategoryByGoodsIds(collect, dto.getShopId());
            goodsDtoS = listResult.getData();
        }

        //普通分类 选中数量
        return goodsDtoS.stream().collect(
                Collectors.toMap(goods -> Long.valueOf(goods.getCategoryId()),
                        goods -> integerMap.get(Long.valueOf(goods.getGoodsId())),
                        (newValue, oldValueList) -> oldValueList + newValue
                )
        );
    }


    private void presellJudge(EsGoodsDTO goods) {
        //未开启预售 截止时间设置为空
        if (!goods.getPresellFlag()) {
            goods.setCloseSellTime("");
            goods.setStartSellTime(null);
        } else {
            PresellFlagDTO flagDTO = new PresellFlagDTO()
                    .setPresellFlag(goods.getPresellFlag())
                    .setStartSellTime(goods.getStartSellTime())
                    .setEndSellTime(goods.getEndSellTime())
                    .setSellWeekTime(goods.getSellWeekTime())
                    .setCloseSellTime(goods.getCloseSellTime());
            Boolean flag = DateTimeUtil.nowPresellFlag(flagDTO);
            goods.setPresellFlag(flag);

            Date newStartSellTime = flagDTO.getNewStartSellTime();
            if (newStartSellTime != null) {
                goods.setStartSellTime(newStartSellTime);
                goods.setCloseSellTime("");
            } else {
                goods.setStartSellTime(null);
            }
        }
    }

}
