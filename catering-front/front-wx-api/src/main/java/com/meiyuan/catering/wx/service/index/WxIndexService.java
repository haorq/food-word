package com.meiyuan.catering.wx.service.index;

import com.meiyuan.catering.core.dto.base.*;
import com.meiyuan.catering.core.dto.goods.RecommendDTO;
import com.meiyuan.catering.core.dto.user.Notice;
import com.meiyuan.catering.core.enums.base.WxCategoryTypeEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.*;
import com.meiyuan.catering.es.dto.goods.EsGoodsCategoryAndLabelDTO;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantListParamDTO;
import com.meiyuan.catering.es.dto.sku.EsGoodsSkuDTO;
import com.meiyuan.catering.es.dto.wx.EsWxMerchantDTO;
import com.meiyuan.catering.es.entity.EsGoodsEntity;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.es.util.EsUtil;
import com.meiyuan.catering.marketing.dto.ticket.TicketWxIndexDTO;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.user.enums.AdvertisingPositionEnum;
import com.meiyuan.catering.user.fegin.notice.NoticeClient;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.advertising.WxAdvertisingIndexDTO;
import com.meiyuan.catering.wx.dto.goods.WxIndexMarketingGoodsDTO;
import com.meiyuan.catering.wx.dto.index.WxCategoryDTO;
import com.meiyuan.catering.wx.dto.index.WxCategoryVO;
import com.meiyuan.catering.wx.dto.index.WxIndexDTO;
import com.meiyuan.catering.wx.dto.index.WxRecommendVO;
import com.meiyuan.catering.wx.service.goods.WxGoodsService;
import com.meiyuan.catering.wx.service.merchant.WxMerchantListService;
import com.meiyuan.catering.wx.utils.WechatUtils;
import com.meiyuan.catering.wx.utils.WxCommonUtil;
import com.meiyuan.catering.wx.vo.WxAdvertisingExtVO;
import com.meiyuan.catering.wx.vo.WxAdvertisingVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020/6/23 13:43
 * @since v1.1.0
 */
@Slf4j
@Service
public class WxIndexService {

    @Autowired
    private UserTicketClient ticketClient;
    @Autowired
    private NoticeClient noticeClient;
    @Autowired
    private MerchantUtils merchantUtils;
    @Autowired
    private WxGoodsService goodsService;
    @Autowired
    private WechatUtils wechatUtils;
    @Autowired
    private EsMerchantClient esMerchantClient;
    @Resource
    private WxMerchantListService wxMerchantListService;
    @Resource
    private EsGoodsClient esGoodsClient;


    private List<TicketWxIndexDTO> getCouponList(UserTokenDTO token) {
        if (token == null) {
            return new ArrayList<>();
        }
        List<TicketWxIndexDTO> list = ticketClient.indexTicketInfo(3, token.getUserId()).getData();

        if (CollectionUtils.isNotEmpty(list)) {
            wechatUtils.setCouponFlag(token.getUserId().toString());
        }

        return list;
    }

    public Result<WxIndexDTO> intraCityShop(UserTokenDTO token, String location) {
        WxIndexDTO wxIndexDTO = new WxIndexDTO();
        String[] split = location.split(BaseUtil.COMMA);
        double lat = new Double(split[1]), lng = new Double(split[0]);
        LocationDTO locationDTO = LatitudeUtils.reverseGeocodingForTencent(lat, lng);
        CateringRegionDTO region = merchantUtils.getRegionCache(locationDTO.getDistrictCode().toString());

        wxIndexDTO.setLocationName(locationDTO.getStreet());
        wxIndexDTO.setCityName(locationDTO.getCity());
        wxIndexDTO.setCityCode(region.getCityCode());
        if (locationDTO.getPoiList() != null && locationDTO.getPoiList().size() > 0) {
            wxIndexDTO.setLocationName(locationDTO.getPoiList().get(0).getName());
        }
        // 设置扫描地推员二维码提示信息
        log.debug("token={}", token);
        if (token != null && token.getGroundPusherId() != null) {
            wxIndexDTO.setPusherMsg(wechatUtils.getPusherMsg(token.getGroundPusherId(), token.getUserId()));
        }
        Long shopSum = esMerchantClient.cityShopSum(wxIndexDTO.getCityCode());
        wxIndexDTO.setCityShopSum(shopSum);
        return Result.succ(wxIndexDTO);
    }

    public Result<List<Notice>> noticeList(Integer categoryLimit) {
        PageData<Notice> list = ClientUtil.getDate(noticeClient.listV101(categoryLimit));
        return Result.succ(list.getList());
    }

    /**
     * describe: 获取缓存广告
     *
     * @param
     * @author: yy
     * @date: 2020/9/4 10:00
     * @return: {@link Result< WxAdvertisingVO>}
     * @version 1.4.0
     **/
    public Result<WxAdvertisingVO> advertisingList() {
        WxAdvertisingVO wxAdvertisingVO = new WxAdvertisingVO();
        // 取缓存中的数据
        List<RedisAdvertisingDTO> dtoList = wechatUtils.getAdvertisingList();

        List<WxAdvertisingIndexDTO> advertisingList = BaseUtil.objToObj(dtoList, WxAdvertisingIndexDTO.class);
        //顶部广告
        List<WxAdvertisingIndexDTO> topAdvertisingList = advertisingList.stream()
                .filter(advertising -> AdvertisingPositionEnum.TOP.getStatus().equals(advertising.getPosition()))
                .sorted(Comparator.comparing(WxAdvertisingIndexDTO::getSort)).collect(Collectors.toList());
        //中部广告
        List<WxAdvertisingIndexDTO> centerAdvertisingList = advertisingList.stream()
                .filter(advertising -> AdvertisingPositionEnum.CENTER.getStatus().equals(advertising.getPosition()))
                .sorted(Comparator.comparing(WxAdvertisingIndexDTO::getSort)).collect(Collectors.toList());
        wxAdvertisingVO.setAdvertisingList(topAdvertisingList);
        wxAdvertisingVO.setCenterAdvertisingList(centerAdvertisingList);
        return Result.succ(wxAdvertisingVO);
    }

    /**
     * describe: 获取广告二级页面
     *
     * @param id 广告id
     * @author: yy
     * @date: 2020/9/4 10:04
     * @return: {@link Result< WxAdvertisingVO>}
     * @version 1.4.0
     **/
    public Result<List<WxAdvertisingExtVO>> advertisingExtList(Long id) {
        RedisAdvertisingDTO dto = wechatUtils.getAdvertisingById(id);
        if (null == dto) {
            return Result.succ();
        }
        List<RedisAdvertisingExtDTO> advertisingExtList = dto.getAdvertisingExtList();
        if (CollectionUtils.isEmpty(advertisingExtList)) {
            return Result.succ();
        }
        List<WxAdvertisingExtVO> list = ConvertUtils.sourceToTarget(advertisingExtList, WxAdvertisingExtVO.class);
        return Result.succ(list);
    }

    public Result<WxCategoryVO> categoryList() {
        WxCategoryVO wxCategoryVO = new WxCategoryVO();
        // 5、小程序类目列表
        List<RedisWxCategoryDTO> categoryList = wechatUtils.getWxCategoryList();
        if (categoryList != null && !categoryList.isEmpty()) {
            List<WxCategoryDTO> wxCategoryList = categoryList.stream().filter(e -> e.getType() == 1).map(WxCategoryDTO::new).collect(Collectors.toList());
            List<WxCategoryDTO> wxTypeList = categoryList.stream().filter(e -> e.getType() == 2).map(WxCategoryDTO::new).collect(Collectors.toList());
            wxCategoryVO.setCategoryList(wxCategoryList);
            wxCategoryVO.setTypeList(wxTypeList);
        }
        return Result.succ(wxCategoryVO);
    }

    public Result<PageData<EsWxMerchantDTO>> merchantList(UserTokenDTO token, EsMerchantListParamDTO dto) {
        log.debug("首页搜索维度经度:" + dto.getLat() + "," + dto.getLng());
        double[] doubles = GpsCoordinateUtils.calGCJ02toBD09(dto.getLat(), dto.getLng());
        dto.setLng(doubles[1]);
        dto.setLat(doubles[0]);
        log.debug("首页搜索维度经度:" + dto.getLat() + "," + dto.getLng());
        return wxMerchantListService.wxMerchantLimit(token, dto);

    }

    public Result<List<TicketWxIndexDTO>> couponList(UserTokenDTO token) {
        //今日已弹窗提示了
        if (wechatUtils.getCouponFlag(token)) {
            return Result.succ();
        }
        List<TicketWxIndexDTO> couponList = getCouponList(token);
        return Result.succ(couponList);
    }

    public Result<List<WxIndexMarketingGoodsDTO>> killGoodsList(UserTokenDTO token, String cityCode) {
        //今日已弹窗提示了
        if (wechatUtils.getKillFlag(token)) {
            return Result.succ();
        }
        List<WxIndexMarketingGoodsDTO> wxIndexMarketingGoodsDTOS = goodsService.indexKillGoodsList(token, cityCode, "");
        return Result.succ(wxIndexMarketingGoodsDTOS);
    }

    public Result<PageData<WxRecommendVO>> recommendList(UserTokenDTO token, RecommendDTO dto) {
        PageData<WxRecommendVO> pageData = new PageData<>();

        List<RedisWxCategoryDTO> wxCategoryList = wechatUtils.getWxCategoryList();
        //平台配置的爆品橱窗只有一个
        List<WxCategoryGoodsDTO> list = wxCategoryList.stream().filter(e -> WxCategoryTypeEnum.HOT_MONEY.getStatus().equals(e.getType()))
                .map(RedisWxCategoryDTO::getStoryGoodsList).findFirst().orElse(null);

        if (CollectionUtils.isEmpty(list)) {
            return Result.succ(pageData);
        }

        boolean isCompanyUser = WxCommonUtil.isCompanyUser(token);
        PageData<EsGoodsDTO> entityPageData = esGoodsClient.queryRecommendList(dto, list, isCompanyUser);

        if (entityPageData != null) {
            String location = dto.getLocation();
            List<WxRecommendVO> dataList = entityPageData.getList().stream().map(esGoods -> {
                WxRecommendVO wxRecommendVO = new WxRecommendVO();
                wxRecommendVO.setGoodsId(esGoods.getGoodsId());
                wxRecommendVO.setGoodsName(esGoods.getGoodsName());
                wxRecommendVO.setShopId(esGoods.getShopId());
                wxRecommendVO.setShopName(esGoods.getShopName());
                wxRecommendVO.setInfoPicture(BaseUtil.subFirstByComma(esGoods.getInfoPicture()));
                wxRecommendVO.setMarketPrice(esGoods.getMarketPrice());
                wxRecommendVO.setSalesPrice(esGoods.getSalesPrice());

                String discountLabel;
                if (isCompanyUser) {
                    discountLabel = BaseUtil.discountLabel(esGoods.getMarketPrice(), esGoods.getSalesPrice());
                } else {
                    discountLabel = BaseUtil.discountLabel(esGoods.getSpecialNumber());
                }
                wxRecommendVO.setDiscountLabel(discountLabel);

//                getDiscountByUserType(token, esGoods, wxRecommendVO);

                EsGoodsCategoryAndLabelDTO label = BaseUtil.getListFirstOne(esGoods.getLabelList());
                wxRecommendVO.setLabelStr(label != null ? label.getName() : null);

                ShopInfoDTO shop = merchantUtils.getShop(Long.valueOf(esGoods.getShopId()));
                // 距离多少米
                wxRecommendVO.setDistanceStr(EsUtil.distanceStr(location, shop.getMapCoordinate()));
                wxRecommendVO.setDoorHeadPicture(shop.getDoorHeadPicture());
                return wxRecommendVO;
            }).collect(Collectors.toList());
            pageData.setTotal(entityPageData.getTotal());
            pageData.setLastPage(entityPageData.isLastPage());
            pageData.setList(dataList);
        }
        return Result.succ(pageData);
    }

//    public static void getDiscountByUserType(UserTokenDTO token, EsGoodsEntity goods, WxRecommendVO vo) {
//        List<EsGoodsSkuDTO> skuList = goods.getSkuList();
//
//        int size = skuList.size();
//
//        if (WxCommonUtil.isCompanyUser(token)) {
//            long eCount = skuList.stream().filter(e -> !BaseUtil.isNullOrNegativeOne(e.getEnterprisePrice())).count();
//            EsGoodsSkuDTO enterpriseSku;
//
//            if (eCount > 0) {
//                enterpriseSku = skuList.stream().filter(sku -> !BaseUtil.isNegativeOne(sku.getEnterprisePrice())).min(Comparator.comparing(EsGoodsSkuDTO::getEnterprisePrice)).get();
//
//                EsGoodsSkuDTO marketSku = skuList.stream()
//                        .filter(sku -> BaseUtil.isNegativeOne(sku.getEnterprisePrice()))
//                        .filter(sku -> !BaseUtil.isNegativeOne(sku.getMarketPrice()))
//                        .min(Comparator.comparing(EsGoodsSkuDTO::getMarketPrice)).orElse(null);
//
//                if (marketSku != null) {
//                    BigDecimal enterprisePrice = enterpriseSku.getEnterprisePrice();
//                    BigDecimal marketPrice = marketSku.getMarketPrice();
//                    if (BaseUtil.priceIsLt(marketPrice, enterprisePrice)) {
//                        enterpriseSku = marketSku;
//                    }
//                }
//            } else {
//                enterpriseSku = skuList.stream().filter(sku -> !BaseUtil.isNegativeOne(sku.getMarketPrice())).min(Comparator.comparing(EsGoodsSkuDTO::getMarketPrice)).get();
//            }
//
//            vo.setMarketPrice(enterpriseSku.getMarketPrice());
//            BigDecimal enterprisePrice = enterpriseSku.getEnterprisePrice();
//            if (!BaseUtil.isNullOrNegativeOne(enterprisePrice)) {
//                vo.setSalesPrice(enterprisePrice);
//            } else {
//                vo.setSalesPrice(enterpriseSku.getMarketPrice());
//            }
//            String discount = BaseUtil.discountLabel(enterpriseSku.getMarketPrice(), enterprisePrice);
//            vo.setDiscountLabel(discount);
//        } else {
//            long sCount = skuList.stream().filter(e -> !BaseUtil.isNullOrNegativeOne(e.getSalesPrice())).count();
//            EsGoodsSkuDTO salesSku;
//
//            if (sCount > 0) {
//                salesSku = skuList.stream().filter(sku -> !BaseUtil.isNegativeOne(sku.getSalesPrice())).min(Comparator.comparing(EsGoodsSkuDTO::getSalesPrice)).get();
//
//                EsGoodsSkuDTO marketSku = skuList.stream()
//                        .filter(sku -> BaseUtil.isNegativeOne(sku.getSalesPrice()))
//                        .filter(sku -> !BaseUtil.isNegativeOne(sku.getMarketPrice()))
//                        .min(Comparator.comparing(EsGoodsSkuDTO::getMarketPrice)).orElse(null);
//
//                if (marketSku != null) {
//                    BigDecimal salesPrice = salesSku.getSalesPrice();
//                    BigDecimal marketPrice = marketSku.getMarketPrice();
//
//                    if (BaseUtil.priceIsLt(marketPrice, salesPrice)) {
//                        salesSku = marketSku;
//                    }
//                }
//
//            } else {
//                salesSku = skuList.stream().filter(sku -> !BaseUtil.isNegativeOne(sku.getMarketPrice())).min(Comparator.comparing(EsGoodsSkuDTO::getMarketPrice)).get();
//            }
//
//
//            vo.setMarketPrice(salesSku.getMarketPrice());
//            BigDecimal salesPrice = salesSku.getSalesPrice();
//            if (!BaseUtil.isNullOrNegativeOne(salesPrice)) {
//                vo.setSalesPrice(salesPrice);
//            } else {
//                vo.setSalesPrice(salesSku.getMarketPrice());
//            }
//            BigDecimal decimal = salesSku.getSpecialNumber();
//            if (decimal != null) {
//                String discount = BaseUtil.discountLabel(decimal);
//                vo.setDiscountLabel(discount);
//            }
//        }
//    }


//    public static void getDiscountByUserType(UserTokenDTO token, EsGoodsEntity goods, WxRecommendVO vo) {
//        if (WxCommonUtil.isCompanyUser(token)) {
//            EsGoodsSkuDTO skuDTO = goods.getSkuList().stream()
//                    .filter(sku -> !BaseUtil.isNullOrNegativeOne(sku.getEnterprisePrice()))
//                    .min(Comparator.comparing(EsGoodsSkuDTO::getEnterprisePrice))
//                    .orElse(null);
//            if (skuDTO == null) {
//                skuDTO = goods.getSkuList().stream()
//                        .min(Comparator.comparing(EsGoodsSkuDTO::getMarketPrice))
//                        .orElse(null);
//            }
//            if (skuDTO == null) {
//                skuDTO = goods.getSkuList().get(0);
//            }
//
//            BigDecimal marketPrice = skuDTO.getMarketPrice();
//            BigDecimal enterprisePrice = skuDTO.getEnterprisePrice();
//            vo.setMarketPrice(marketPrice);
//            vo.setSalesPrice(BaseUtil.isNullOrNegativeOne(enterprisePrice) ? marketPrice : enterprisePrice);
//            String discount = BaseUtil.discountLabel(marketPrice, enterprisePrice);
//            vo.setDiscountLabel(discount);
//        } else {
//            EsGoodsSkuDTO skuDTO = goods.getSkuList().stream()
//                    .filter(sku -> !BaseUtil.isNullOrNegativeOne(sku.getSalesPrice()))
//                    .min(Comparator.comparing(EsGoodsSkuDTO::getSalesPrice))
//                    .orElse(null);
//            if (skuDTO == null) {
//                skuDTO = goods.getSkuList().stream()
//                        .min(Comparator.comparing(EsGoodsSkuDTO::getMarketPrice))
//                        .orElse(null);
//            }
//            if (skuDTO == null) {
//                skuDTO = goods.getSkuList().get(0);
//            }
//            BigDecimal marketPrice = skuDTO.getMarketPrice();
//            BigDecimal salesPrice = skuDTO.getSalesPrice();
//            vo.setMarketPrice(marketPrice);
//            vo.setSalesPrice(BaseUtil.isNullOrNegativeOne(salesPrice) ? marketPrice : salesPrice);
//            BigDecimal decimal = skuDTO.getSpecialNumber();
//            if (decimal != null) {
//                String discount = BaseUtil.discountLabel(decimal);
//                vo.setDiscountLabel(discount);
//            }
//        }
//    }

    private void setPrice(EsGoodsEntity entity, UserTokenDTO token, WxRecommendVO vo) {
        boolean companyUser = token != null && token.isCompanyUser();
        EsGoodsSkuDTO sku = null;
        if (companyUser) {


            sku = entity.getSkuList().stream().filter(e -> !BaseUtil.isNullOrNegativeOne(e.getEnterprisePrice()))
                    .min(Comparator.comparing(EsGoodsSkuDTO::getEnterprisePrice)).orElse(null);
        }

        if (sku == null && !companyUser) {
            sku = entity.getSkuList().stream().filter(e -> !BaseUtil.isNullOrNegativeOne(e.getSalesPrice()))
                    .min(Comparator.comparing(EsGoodsSkuDTO::getSalesPrice)).orElse(null);
        }

        if (sku == null) {
            sku = entity.getSkuList().stream().filter(e -> !BaseUtil.isNullOrNegativeOne(e.getMarketPrice()))
                    .min(Comparator.comparing(EsGoodsSkuDTO::getMarketPrice)).orElse(null);
        }

        if (sku != null) {
            BigDecimal marketPrice = sku.getMarketPrice();
            BigDecimal salesPrice = sku.getSalesPrice();
            BigDecimal enterprisePrice = sku.getEnterprisePrice();
            vo.setMarketPrice(marketPrice);

            if (companyUser) {
                vo.setSalesPrice(!BaseUtil.isNullOrNegativeOne(enterprisePrice) ?
                        enterprisePrice : marketPrice);
            } else {
                vo.setSalesPrice(!BaseUtil.isNullOrNegativeOne(salesPrice) ?
                        salesPrice : marketPrice);
                vo.setDiscountLabel(BaseUtil.discountLabel(sku.getSpecialNumber()));
            }
        }


    }
}















