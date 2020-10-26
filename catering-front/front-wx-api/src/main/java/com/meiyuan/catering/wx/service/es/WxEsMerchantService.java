package com.meiyuan.catering.wx.service.es;

import com.meiyuan.catering.core.dto.base.*;
import com.meiyuan.catering.core.enums.base.DiscountTypeEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.merchant.EsMerchantListParamDTO;
import com.meiyuan.catering.es.dto.merchant.ShopTicketPageDto;
import com.meiyuan.catering.es.dto.wx.EsWxMerchantDTO;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.es.util.EsUtil;
import com.meiyuan.catering.es.vo.ShopDiscountInfoVo;
import com.meiyuan.catering.es.vo.WxGoodCategoryExtVo;
import com.meiyuan.catering.es.vo.WxShopPageVo;
import com.meiyuan.catering.marketing.dto.groupon.ShopGrouponGoodsDTO;
import com.meiyuan.catering.marketing.enums.MarketingTicketActivityTypeEnum;
import com.meiyuan.catering.marketing.feign.MarketingGrouponClient;
import com.meiyuan.catering.marketing.feign.MarketingSeckillClient;
import com.meiyuan.catering.marketing.feign.MarketingTicketActivityClient;
import com.meiyuan.catering.marketing.feign.MarketingTicketClient;
import com.meiyuan.catering.marketing.vo.ticket.WxMerchantIndexTicketInfoVO;
import com.meiyuan.catering.marketing.vo.ticket.WxShopTicketInfoVo;
import com.meiyuan.catering.merchant.enums.BusinessSupportEnum;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopDiscountGoodsDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopGoodsDiscountDTO;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsSkuClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.wx.dto.DiscountDataMap;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.service.merchant.WxMerchantListService;
import com.meiyuan.catering.wx.utils.WechatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/5/6 16:12
 * @description 简单描述
 **/
@Service
public class WxEsMerchantService {
    @Resource
    EsMerchantClient esMerchantClient;
    @Resource
    EsGoodsClient esGoodsClient;
    @Resource
    MerchantUtils merchantUtils;
    @Resource
    WechatUtils wechatUtils;
    @Resource
    ShopGoodsSkuClient shopGoodsSkuClient;
    @Autowired
    MarketingTicketActivityClient marketingTicketActivityClient;
    @Autowired
    MarketingGrouponClient marketingGrouponClient;
    @Autowired
    MarketingSeckillClient marketingSeckillClient;
    @Autowired
    MarketingTicketClient marketingTicketClient;
    @Autowired
    private WxMerchantListService wxMerchantListService;

    /**
     * 方法描述 : 通过筛选条件过滤出类目中不需要的店铺id
     * @Author: MeiTao
     * @Date: 2020/8/7 0007 9:54
     * @param dto
     * @param storyList 请求参数
     * @return: java.util.List
     * @Since version-1.3.0
     */
    private List dropShopIds(EsMerchantListParamDTO dto, List<String> storyList) {
        Map<String, String> shopIdsMap = storyList.stream().collect(Collectors.toMap(String::toString,i -> i));
        //1、店铺配送方式
        if (!ObjectUtils.isEmpty(dto.getDeliveryType())){
            storyList.forEach(shopId->{
                ShopConfigInfoDTO shopConfigInfo = merchantUtils.getShopConfigInfo(Long.valueOf(shopId));
                //店铺已被删除
                if (!ObjectUtils.isEmpty(shopConfigInfo)){
                    //配送方式不匹配
                    Boolean b = Objects.equals(dto.getDeliveryType(),shopConfigInfo.getBusinessSupport())||Objects.equals(BusinessSupportEnum.ALL.getStatus(),shopConfigInfo.getBusinessSupport());
                    if (!b){
                        shopIdsMap.remove(shopId);
                    }
                }else {
                    shopIdsMap.remove(shopId);
                }
            });
        }
        List<Long> shopIds = shopIdsMap.entrySet().stream().map(e -> Long.valueOf(e.getValue())).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(dto.getActivityType())&&BaseUtil.judgeList(shopIds)){
            DiscountTypeEnum discountTypeEnum = DiscountTypeEnum.parse(dto.getActivityType());
            switch (discountTypeEnum){
                //1：折扣商品
                case GOODS_DISCOUNT:
                    ShopDiscountGoodsDTO shopDiscountGoods = new ShopDiscountGoodsDTO();
                    shopDiscountGoods.setShopIds(shopIds);
                    shopDiscountGoods.setUserType(dto.getCompanyUser()?1:2);
                    shopIds = shopGoodsSkuClient.listShopDiscountGoods(shopDiscountGoods).getData();
                    break;
                //2：进店领券/店内领券
                case TICKET_GET_DISCOUNT:
                    shopIds = marketingTicketActivityClient.findTicketShop(1, shopIds);
                    break;
                //3：满减优惠券/(店外发券/平台优惠券)
                case TICKET_SEND_DISCOUNT:
                    shopIds = marketingTicketActivityClient.findTicketShop(2, shopIds);
                    break;
                //4，秒杀活动
                case SECKILL_DISCOUNT:
                    shopIds = marketingSeckillClient.listShopHaveSeckill(shopIds, dto.getCompanyUser()).getData();
                    break;
                //5：团购活动
                case GROUPON_DISCOUNT:
                    shopIds = marketingGrouponClient.listShopHaveGroupon(shopIds, dto.getCompanyUser()).getData();
                    break;
                default :
                    break;
            }
        }
        return shopIds;
    }

    public Result<RedisWxCategoryDTO> brandInfoV101(Long brandId) {
        return Result.succ(wechatUtils.getBrand(brandId));
    }

    /**
     * @description 获取当前城市可售卖商品的商户
     * @author yaozou
     * @date 2020/5/11 14:37
     * @param
     * @since v1.0.1
     * @return
     */
    public List<Long> merchantIdsHasSellingGoods(String cityCode,String location){
        return merchantIds(cityCode,location,true,null);
    }

    /**
     * 获取商户ID
     * @param cityCode 所在城市编码
     * @param location 当前经纬度
     * @param haveGoodsFlag 是否有可售卖商品
     * @param flag 是否查询菜单模式下的商家
     * @return List<Long>
     */
    private List<Long> merchantIds(String cityCode,String location,Boolean haveGoodsFlag,Boolean flag){
        double lat = new Double(location.split(",")[1]), lng = new Double(location.split(",")[0]);

        EsMerchantListParamDTO paramDTO = new EsMerchantListParamDTO();
        paramDTO.setPageSize(1000L);

        paramDTO.setCityCode(cityCode);
        if (haveGoodsFlag != null){
            paramDTO.setHaveGoodsFlag(haveGoodsFlag);
        }
        paramDTO.setLat(lat);
        paramDTO.setLng(lng);

        if (flag != null) {
            paramDTO.setFlag(flag);
        }
        List<Long> merchantIds = new ArrayList<>();
        Result<PageData<EsWxMerchantDTO>> pageDataResult = esMerchantClient.listLimit(paramDTO);
        if (pageDataResult.success() && null != pageDataResult.getData()) {
            if (BaseUtil.judgeList(pageDataResult.getData().getList())) {
                List<EsWxMerchantDTO> list = pageDataResult.getData().getList();
                list.forEach(dto -> merchantIds.add(new Long(dto.getMerchantId())));
            }
        }
        return merchantIds;
    }

    public Result<List<String>> brandImgListV101(Long brandId) {
        RedisWxCategoryDTO brand = wechatUtils.getBrand(brandId);
        if (brand != null) {
            return Result.succ(brand.getImgList());
        }
        return Result.succ(new ArrayList<>(1));
    }

    public Result<List<RedisWxCategoryExtDTO>> wxCategoryExtList(Long brandId) {
        RedisWxCategoryDTO brand = wechatUtils.getBrand(brandId);
        if (brand != null) {
            return Result.succ(brand.getWxCategoryExtList());
        }
        return Result.succ(new ArrayList<>(1));
    }

    /**
     * 品牌商户列表分页V3
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/6 17:33
     * @return: {@link PageData<   EsWxMerchantDTO  >}
     **/
    public Result<WxShopPageVo> brandListLimitV3(UserTokenDTO token,EsMerchantListParamDTO dto) {
        WxShopPageVo wxShopPageVo = new WxShopPageVo();
        RedisWxCategoryDTO brand = wechatUtils.getBrand(dto.getBrandId());
        if (ObjectUtils.isEmpty(brand)){
            return Result.succ(wxShopPageVo);
        }
        //1、类目介绍数据组装
        if (BaseUtil.judgeList(brand.getWxCategoryExtList())){
            wxShopPageVo.setWxCategoryExtList(BaseUtil.objToObj(brand.getWxCategoryExtList(), WxGoodCategoryExtVo.class));
        }

        List<String> storyList = brand.getStoryList();

        if (!BaseUtil.judgeList(storyList)){
            return Result.succ(wxShopPageVo);
        }
        dto.setShopIdList(storyList);
        PageData<EsWxMerchantDTO> data = wxMerchantListService.wxMerchantLimit(token, dto).getData();
        wxShopPageVo.setMerchantPage(data);
        return Result.succ(wxShopPageVo);
//        //2、筛选出符合条件的店铺id
//        List shopIds = this.dropShopIds(dto,brand.getStoryList());
//        if (!BaseUtil.judgeList(shopIds)){
//            return Result.succ(wxShopPageVo);
//        }
//        //过滤无商品店铺
//        ShopDiscountGoodsDTO goodsQuery = new ShopDiscountGoodsDTO();
//        goodsQuery.setShopIds(shopIds);
//        goodsQuery.setUserType(dto.getCompanyUser() ? 1:2);
//        shopIds = shopGoodsSkuClient.listShopHaveGoods(goodsQuery).getData();
//
//        if (!BaseUtil.judgeList(shopIds)){
//            return Result.succ(wxShopPageVo);
//        }
//
//        dto.setShopIdList(shopIds);
//        Result<PageData<EsWxMerchantDTO>> pageDataResult = esMerchantClient.brandListLimit(dto);
//        if (BaseUtil.judgeResultObject(pageDataResult)) {
//            PageData<EsWxMerchantDTO> pageData = pageDataResult.getData();
//            setMerchantInfo(dto, pageData);
//        }
//        wxShopPageVo.setMerchantPage(pageDataResult.getData());
//        return Result.succ(wxShopPageVo);
    }

    /**
     * 方法描述 : 通过优惠券获取店铺列表
     * @Author: MeiTao
     * @Date: 2020/8/14 0014 16:52
     * @param dto 请求参数
     * @return: Result<PageData<EsWxMerchantDTO>>
     * @Since version-1.3.0
     */
    public Result<PageData<EsWxMerchantDTO>> shopByTicketPage(ShopTicketPageDto dto,UserTokenDTO tokenDTO) {
        //1、当前优惠券可使用门店、及门店对应的优惠券信息
        List<WxShopTicketInfoVo> ticketShopList = marketingTicketClient.findTicketCanUseShop(dto.getUserTicketId());
        if (!BaseUtil.judgeList(ticketShopList)){
            return Result.succ(new PageData<>(dto.getPage()));
        }

        List<String> shopIds = new ArrayList<>();
        ticketShopList.forEach(s->{
            shopIds.add(String.valueOf(s.getShopId()));
        });

        //2、查询门店基本信息
        EsMerchantListParamDTO queryDto = BaseUtil.objToObj(dto,EsMerchantListParamDTO.class);
        queryDto.setShopIdList(shopIds);
        queryDto.setSearchType(1);
        queryDto.setCityCode(dto.getCityCode());
        Result<PageData<EsWxMerchantDTO>> pageDataResult = esMerchantClient.brandListLimit(queryDto);

        if (!BaseUtil.judgeResultObject(pageDataResult)||!BaseUtil.judgeList(pageDataResult.getData().getList())) {
            return Result.succ(new PageData<>(dto.getPage()));
        }

        PageData<EsWxMerchantDTO> pageData = pageDataResult.getData();
        Set<String> matchShopIds = pageData.getList().stream().map(EsWxMerchantDTO::getShopId).collect(Collectors.toSet());
        //查询对应门店的优惠信息
        DiscountDataMap dataMap = wxMerchantListService.getDataMap(matchShopIds, tokenDTO);

        //组装结果数据
        EsMerchantListParamDTO paramDto = new EsMerchantListParamDTO();
        paramDto.setLat(dto.getLat());
        paramDto.setLng(dto.getLng());
        wxMerchantListService.setResultData(paramDto, pageDataResult, dataMap);

//
//        EsMerchantListParamDTO shopDto = BaseUtil.objToObj(dto, EsMerchantListParamDTO.class);
//        //3、设置门店基本信息
//        this.setMerchantBaseInfo(pageData.getList(), shopDto);
//
//        //4、设置门店优惠活动相关信息
//        List<EsWxMerchantDTO> pageDataList = pageData.getList();
//        Map<Long, List<ShopDiscountInfoVo>> shopDiscountMap = this.setDiscountInfo(pageDataList,dto.getCompanyUser());
//        pageDataList.forEach(wxShop->{
//            List<ShopDiscountInfoVo> shopDiscountInfo = shopDiscountMap.get(Long.valueOf(wxShop.getShopId()));
//            if (BaseUtil.judgeList(shopDiscountInfo)){
//                //优惠券排序
//                List<Integer> integers = Arrays.asList(1, 2, 4, 5, 3, 6);
//                shopDiscountInfo = this.sortDiscountInfo(shopDiscountInfo,integers);
//                wxShop.setDiscountInfoList(shopDiscountInfo);
//            }
//        });
        return Result.succ(pageData);
    }

    /**
     * 方法描述 : 设置店铺信息
     *          1、设置店铺基本信息
     *          2、设置店铺优惠信息
     * @Author: MeiTao
     * @Date: 2020/8/14 0014 18:01
     * @param dto
     * @param pageData 请求参数
     * @return: void
     * @Since version-1.3.0
     */
    private void setMerchantInfo(EsMerchantListParamDTO dto, PageData<EsWxMerchantDTO> pageData) {
        List<EsWxMerchantDTO> list = pageData.getList();
        if (!BaseUtil.judgeList(list)){
            pageData.setLastPage(Boolean.TRUE);
            return;
        }
        this.setMerchantBaseInfo(list,dto);

        //设置活动相关信息参数
        Map<Long, List<ShopDiscountInfoVo>> shopDiscountMap = this.setDiscountInfo(list,dto.getCompanyUser());
        list.forEach(wxShop->{
            List<ShopDiscountInfoVo> shopDiscountInfo = shopDiscountMap.get(Long.valueOf(wxShop.getShopId()));
            if (BaseUtil.judgeList(shopDiscountInfo)){
                //排序
                List<ShopDiscountInfoVo> shopDiscountInfoVos = this.sortDiscountInfo(shopDiscountInfo, Arrays.asList(1, 2, 4, 5, 3,6));
                wxShop.setDiscountInfoList(shopDiscountInfoVos);
            }
        });
    }

    /**
     * 方法描述 : 设置店铺折扣信息
     * @Author: MeiTao
     * @Date: 2020/8/14 0014 18:03
     * @param list 店铺信息集合
     * @param companyUser  是否是企业用户 ： true：是
     * @return: java.util.Map<List<ShopDiscountInfoVo>>
     * @Since version-1.3.0
     */
    private Map<Long, List<ShopDiscountInfoVo>> setDiscountInfo(List<EsWxMerchantDTO> list,Boolean companyUser){
        List<Long> shopIds = new ArrayList<>();
        list.forEach(s->shopIds.add(Long.valueOf(s.getShopId())));
        //获取店铺活动折扣相关信息
        Map<Long, List<ShopDiscountInfoVo>> shopDiscountMap = this.listShopDiscountInfo(shopIds, companyUser);
        //获取优惠券信息
        Map<Long, List<WxMerchantIndexTicketInfoVO>> shopTicketMap = marketingTicketActivityClient.findShopTicket(shopIds);
        //设置优惠券信息
        this.setShopTicket(shopDiscountMap, shopTicketMap, shopIds);

        //设置配送订单相关优惠信息
        shopIds.forEach(shopId->{
            ShopConfigInfoDTO shopConfigInfo = merchantUtils.getShopConfigInfo(shopId);
            String dispatching = shopConfigInfo.getDispatching();
            if (!ObjectUtils.isEmpty(shopConfigInfo) &&  BaseUtil.judgeString(dispatching)){
                List<ShopDiscountInfoVo> shopDiscountInfo = shopDiscountMap.get(shopId);
                ShopDiscountInfoVo info = new ShopDiscountInfoVo();
                info.setDiscountStr(Arrays.asList(dispatching));
                info.setDiscountType(6);
                shopDiscountInfo.add(info);
            }
        });
        return shopDiscountMap;
    }

    /**
     * 方法描述 : 批量设置商户基本信息
     * @Author: MeiTao
     * @Date: 2020/8/14 0014 18:01
     * @param list
     * @param dto 请求参数
     * @return: void
     * @Since version-1.3.0
     */
    private void setMerchantBaseInfo(List<EsWxMerchantDTO> list, EsMerchantListParamDTO dto) {
        list.forEach(
                i -> {
                    Long merchantId = Long.valueOf(i.getMerchantId());
                    Long shopId = Long.valueOf(i.getShopId());
                    ShopInfoDTO shop = merchantUtils.getShop(shopId);
                    MerchantInfoDTO merchant = merchantUtils.getMerchant(merchantId);
                    i.setMerchantAttribute(merchant.getMerchantAttribute());
                    // 门头图
                    if (BaseUtil.judgeString(shop.getDoorHeadPicture())) {
                        i.setDoorHeadPicture(shop.getDoorHeadPicture());
                    }
                    ShopConfigInfoDTO shopConfigInfo = merchantUtils.getShopConfigInfo(shopId);
                    // 订单起送金额
                    if (null != shopConfigInfo.getLeastDeliveryPrice()) {
                        i.setLeastDeliveryPrice(shopConfigInfo.getLeastDeliveryPrice().doubleValue());
                    }
                    // 配送费
                    if (null != shopConfigInfo.getDeliveryPrice()) {
                        i.setDeliveryPrice(shopConfigInfo.getDeliveryPrice().doubleValue());
                    }
                    i.setBusinessSupport(shopConfigInfo.getBusinessSupport());
                    // 商户标签
                    List<ShopTagInfoDTO> shopTag = merchantUtils.getShopTag(shop.getMerchantId());
                    if (BaseUtil.judgeList(shopTag)) {
                        List<String> shopTagNameList = shopTag.stream().limit(3).map(ShopTagInfoDTO::getTagName).collect(Collectors.toList());
                        i.setShopTag(shopTagNameList);
                    }
                    // 距离多少米
                    i.setLocation(EsUtil.convertLocation(dto.getLat(), dto.getLng(), i.getLocation().split(",")));
                    //设置门店是否营业
                    i.setBusinessStatus(shop.getBusinessStatus());
                }
        );
    }

    /**
     * 方法描述 : 设置门店优惠信息
     *            1、门店最低折扣商品折扣值
     *            2、设置门店团购活动商品最低价
     *            3、设置门店秒杀活动商品最低价
     * @Author: MeiTao
     * @Date: 2020/8/12 0012 10:19
     * @param shopIds
     * @param type 请求参数
     * @return: Map<jList<com.meiyuan.catering.es.vo.ShopDiscountInfoVo>>
     * @Since version-1.3.0
     */
    private Map<Long,List<ShopDiscountInfoVo>> listShopDiscountInfo(List<Long> shopIds, Boolean type) {
        //组装数据
        Map<Long, List<ShopDiscountInfoVo>> resultMap = new HashMap<>(15);
        shopIds.forEach(s->{
            resultMap.put(s,new ArrayList<ShopDiscountInfoVo>());
        });
        //获取折扣商品最小值
        ShopDiscountGoodsDTO dto = new ShopDiscountGoodsDTO();
        dto.setShopIds(shopIds);
        dto.setUserType(type?UserTypeEnum.COMPANY.getStatus():UserTypeEnum.PERSONAL.getStatus());
        List<ShopGoodsDiscountDTO> shopGoodsDiscount = shopGoodsSkuClient.listShopGoodsDiscount(dto).getData();

        shopGoodsDiscount.forEach(s->{
            List<ShopDiscountInfoVo> shopDiscountInfoList = resultMap.get(s.getShopId());
            //最低最高折扣
            Double min = 0.1;
            Double max = 10.0;
            if(shopDiscountInfoList != null && s.getPrice()>= min && s.getPrice() < max){
                ShopDiscountInfoVo info = new ShopDiscountInfoVo();
                info.setDiscountStr(Arrays.asList("折扣商品" + s.getPrice() + "折起"));
                info.setDiscountType(1);
                shopDiscountInfoList.add(info);
            }
        });

        //团购活动最低商品价格获取
        List<ShopGrouponGoodsDTO> listShopGrouponGoods = marketingGrouponClient.listGoodsMinPriceByShop(shopIds,type).getData();
        listShopGrouponGoods.forEach(g->{
            List<ShopDiscountInfoVo> shopDiscountInfoList = resultMap.get(g.getShopId());
            if (shopDiscountInfoList != null){
                ShopDiscountInfoVo info = new ShopDiscountInfoVo();
                info.setDiscountStr(Arrays.asList("团购商品" + BaseUtil.toPlainString(g.getActivityPrice(),2) + "元起"));
                info.setDiscountType(5);
                shopDiscountInfoList.add(info);
            }
        });

        //秒杀活动最低商品价格获取
        List<ShopGrouponGoodsDTO> listShopSeckillGoods = marketingSeckillClient.listGoodsMinPriceByShop(shopIds,type).getData();
        listShopSeckillGoods.forEach(s->{
            List<ShopDiscountInfoVo> shopDiscountInfoList = resultMap.get(s.getShopId());
            if (!ObjectUtils.isEmpty(shopDiscountInfoList)){
                ShopDiscountInfoVo info = new ShopDiscountInfoVo();
                info.setDiscountStr(Arrays.asList("秒杀商品" + BaseUtil.toPlainString(s.getActivityPrice())+ "元起"));
                info.setDiscountType(4);
                shopDiscountInfoList.add(info);
            }
        });

        return resultMap;
    }

    /**
     * 方法描述 : 设置门店优惠券信息
     * @Author: MeiTao
     * @Date: 2020/8/14 0014 16:50
     * @param resultMap
     * @param shopTicketMap
     * @param shopIds 请求参数
     * @return: java.util.Map<java.lang.Long,java.util.List<com.meiyuan.catering.es.vo.ShopDiscountInfoVo>>
     * @Since version-1.3.0
     */
    private Map<Long, List<ShopDiscountInfoVo>> setShopTicket(Map<Long, List<ShopDiscountInfoVo>> resultMap,
                                                              Map<Long, List<WxMerchantIndexTicketInfoVO>> shopTicketMap,
                                                              List<Long> shopIds){
        //获取门店对应所有优惠券信息
        shopIds.forEach(s->{
            //店铺优惠券信息
            List<WxMerchantIndexTicketInfoVO> shopTicket = shopTicketMap.get(s);
            if (BaseUtil.judgeList(shopTicket)){
                //返回结果
                List<ShopDiscountInfoVo> shopDiscountInfoList = resultMap.get(s);
                //店内领券
                List<WxMerchantIndexTicketInfoVO> couponList = shopTicket.stream().filter(e -> Objects.equals(e.getActivityType(), MarketingTicketActivityTypeEnum.SHOP_IN_GET.getStatus())).collect(Collectors.toList());
                this.setShopTicketInfo(couponList,shopDiscountInfoList,DiscountTypeEnum.TICKET_GET_DISCOUNT.getStatus());

                //店外发券（顺序：先平台：后店外发券）
                List<WxMerchantIndexTicketInfoVO> deductionList = new ArrayList<>();
                List<WxMerchantIndexTicketInfoVO> shopPlatformSubsidy = new ArrayList<>();
                List<WxMerchantIndexTicketInfoVO> shopOutGetTicket = new ArrayList<>();
                shopTicket.forEach(ticket->{
                    if (Objects.equals(MarketingTicketActivityTypeEnum.PLATFORM_SUBSIDY.getStatus(),ticket.getActivityType())){
                        shopPlatformSubsidy.add(ticket);
                    }else if(Objects.equals(MarketingTicketActivityTypeEnum.SHOP_OUT_GET.getStatus(),ticket.getActivityType())){
                        shopOutGetTicket.add(ticket);
                    }
                });
                deductionList.addAll(shopPlatformSubsidy);
                deductionList.addAll(shopOutGetTicket);
                this.setShopTicketInfo(deductionList,shopDiscountInfoList,DiscountTypeEnum.TICKET_SEND_DISCOUNT.getStatus());
            }
        });
        return resultMap;
    }

    /**
     * 方法描述 : 店铺优惠券信息组装
     * @Author: MeiTao
     * @Date: 2020/8/14 0014 13:47
     * @param deductionList  待组装优惠券
     * @param shopDiscountInfoList 组装结果
     * @param discountType 优惠券类型
     * @return: void
     * @Since version-1.3.0
     */
    public void setShopTicketInfo(List<WxMerchantIndexTicketInfoVO> deductionList,List<ShopDiscountInfoVo> shopDiscountInfoList,Integer discountType){
        if (BaseUtil.judgeList(deductionList)) {
            ShopDiscountInfoVo info = new ShopDiscountInfoVo();
            info.setDiscountType(discountType);
            deductionList.forEach(ticket ->{
                String ticketStr = BaseUtil.ticketInfoStr(ticket.getAmount(),ticket.getConsumeCondition(),ticket.getActivityType());
                List<String> discountStr = info.getDiscountStr();
                //设置领卷信息
                if (!BaseUtil.judgeList(discountStr)){
                    List<String> list = new ArrayList<>();
                    list.add(ticketStr);
                    info.setDiscountStr(list);
                }else {
                    discountStr.add(ticketStr);
                }
            });
            shopDiscountInfoList.add(info);
        }
    }

    /**
     * 方法描述 : 门店优惠信息排序
     *          1.第一，显示行膳平台发的券，『满XX减xx』；多张则并列显示；
     *          2.第二，显示商家店外发券的优惠，『满xx减xx』、『XXX元券』；『XXX元券』为无门槛的券显示形式；多张则并列显示；
     *          3.第三，显示商家店内领券的优惠，『领X元券』；多张则并列显示；
     *          4.第四，显示商品优惠/秒杀/团购商品的折扣，『X.x折』；显示商品的最低折扣；
     * @Author: MeiTao
     * @Date: 2020/8/12 0012 18:25
     * @param shopDiscountInfo 请求参数
     * @return: java.util.List<com.meiyuan.catering.es.vo.ShopDiscountInfoVo>
     * @Since version-1.3.0
     */
    private List<ShopDiscountInfoVo> shopDiscountInfoSort(List<ShopDiscountInfoVo> shopDiscountInfo) {
        List<ShopDiscountInfoVo> result = new ArrayList();
        Map<Integer, ShopDiscountInfoVo> discountInfoMap = shopDiscountInfo.stream().collect(Collectors.toMap(ShopDiscountInfoVo::getDiscountType, account -> account));
        this.addDiscountInfo(result,discountInfoMap.get(DiscountTypeEnum.TICKET_SEND_DISCOUNT.getStatus()));
        this.addDiscountInfo(result,discountInfoMap.get(DiscountTypeEnum.TICKET_GET_DISCOUNT.getStatus()));
        this.addDiscountInfo(result,discountInfoMap.get(DiscountTypeEnum.GOODS_DISCOUNT.getStatus()));
        this.addDiscountInfo(result,discountInfoMap.get(DiscountTypeEnum.SECKILL_DISCOUNT.getStatus()));
        this.addDiscountInfo(result,discountInfoMap.get(DiscountTypeEnum.GROUPON_DISCOUNT.getStatus()));
        return result;
    }

    /**
     * 方法描述 : 门店优惠信息排序
     *          特价商品>进店领券>秒杀活动>团购活动>平台补贴发券>商家店外发券>配送费优惠
     * @Author: MeiTao
     * @Date: 2020/8/12 0012 18:25
     * @param shopDiscountInfo 请求参数
     * @return: java.util.List<com.meiyuan.catering.es.vo.ShopDiscountInfoVo>
     * @Since version-1.3.0
     */
    private List<ShopDiscountInfoVo> sortDiscountInfo(List<ShopDiscountInfoVo> shopDiscountInfo,List<Integer> sortRule) {
        List<ShopDiscountInfoVo> result = new ArrayList();
        Map<Integer, ShopDiscountInfoVo> discountInfoMap = shopDiscountInfo.stream().collect(Collectors.toMap(ShopDiscountInfoVo::getDiscountType, account -> account));
        sortRule.forEach(r->{
            this.addDiscountInfo(result,discountInfoMap.get(r));
        });
        return result;
    }

    private void addDiscountInfo(List<ShopDiscountInfoVo> result,ShopDiscountInfoVo discountInfoVo){
        if (!ObjectUtils.isEmpty(discountInfoVo)){
            result.add(discountInfoVo);
        }
    }
}
