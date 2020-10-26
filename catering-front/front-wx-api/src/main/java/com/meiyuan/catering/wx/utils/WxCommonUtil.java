package com.meiyuan.catering.wx.utils;

import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.dto.sku.EsGoodsSkuDTO;
import com.meiyuan.catering.es.enums.marketing.MarketingUsingObjectEnum;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.merchant.WxMerchantGoodsSkuDTO;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 描述:
 *
 * @author zengzhangni
 * @date 2020/9/14 9:24
 * @since v1.4.0
 */
public class WxCommonUtil {
    private static final String NULL_STR = "";

    public static boolean isCompanyUser(UserTokenDTO token) {
        return token != null && token.isCompanyUser();
    }

    public static Integer getMarketingUserType(UserTokenDTO token) {
        return token != null ? token.getMarketingUserType() : MarketingUsingObjectEnum.PERSONAL.getStatus();
    }


    public static String getDiscountByUserType(UserTokenDTO token, WxMerchantGoodsSkuDTO skuDTO) {
        if (isCompanyUser(token)) {
            BigDecimal marketPrice = skuDTO.getMarketPrice();
            BigDecimal enterprisePrice = skuDTO.getEnterprisePrice();
            return BaseUtil.discountLabel(marketPrice, enterprisePrice);
        } else {
            return BaseUtil.discountLabel(skuDTO.getSpecialNumber());
        }
    }

    public static String getDiscountByUserType(UserTokenDTO token, EsGoodsDTO goods) {
        if (isCompanyUser(token)) {
            EsGoodsSkuDTO skuDTO = goods.getSkuList().stream()
                    //过滤企业价为空的
                    .filter(sku -> !BaseUtil.isNullOrNegativeOne(sku.getEnterprisePrice()))
                    //取企业价与原价差值最大的sku
                    .max(Comparator.comparing(sku -> sku.getMarketPrice().subtract(sku.getEnterprisePrice())))
                    .orElse(null);
            if (skuDTO != null) {
                BigDecimal marketPrice = skuDTO.getMarketPrice();
                BigDecimal enterprisePrice = skuDTO.getEnterprisePrice();
                return BaseUtil.discountLabel(marketPrice, enterprisePrice);
            }
        } else {
            EsGoodsSkuDTO skuDTO = goods.getSkuList().stream()
                    //过滤折扣为空的
                    .filter(sku -> !BaseUtil.isNullOrNegativeOne(sku.getSpecialNumber()))
                    //过滤取最小的折扣
                    .min(Comparator.comparing(EsGoodsSkuDTO::getSpecialNumber))
                    .orElse(null);
            if (skuDTO != null) {
                BigDecimal decimal = skuDTO.getSpecialNumber();
                if (decimal != null) {
                    return BaseUtil.discountLabel(decimal);
                }
            }
        }
        return NULL_STR;
    }

//    public static BigDecimal getSalesPrice(UserTokenDTO token, BigDecimal marketPrice, BigDecimal salesPrice, BigDecimal enterprisePrice) {
//        if (isCompanyUser(token)) {
//            return !BaseUtil.isNegativeOne(enterprisePrice) ? enterprisePrice : marketPrice;
//        } else {
//            return !BaseUtil.isNegativeOne(salesPrice) ? salesPrice : marketPrice;
//        }
//    }

//    public static void getDiscountByUserType(UserTokenDTO token, EsGoodsDTO goods, EsSimpleGoodsInfoDTO vo) {
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
//        }
//    }


//    public static void setSalesPrice(UserTokenDTO token, EsGoodsDTO esGoodsDTO, EsSimpleGoodsInfoDTO goods) {
//        List<EsGoodsSkuDTO> skuList = esGoodsDTO.getSkuList();
//        int size = skuList.size();
//
//        if (isCompanyUser(token)) {
//            long eCount = skuList.stream().filter(e -> !BaseUtil.isNullOrNegativeOne(e.getEnterprisePrice())).count();
//            EsGoodsSkuDTO enterpriseSku;
//
//            if (eCount > 0) {
//                enterpriseSku = skuList.stream().filter(sku -> !BaseUtil.isNegativeOne(sku.getEnterprisePrice())).min(Comparator.comparing(EsGoodsSkuDTO::getEnterprisePrice)).get();
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
//
//            } else {
//                enterpriseSku = skuList.stream().filter(sku -> !BaseUtil.isNegativeOne(sku.getMarketPrice())).min(Comparator.comparing(EsGoodsSkuDTO::getMarketPrice)).get();
//            }
//
//            goods.setMarketPrice(enterpriseSku.getMarketPrice());
//            BigDecimal enterprisePrice = enterpriseSku.getEnterprisePrice();
//            if (!BaseUtil.isNullOrNegativeOne(enterprisePrice)) {
//                goods.setSalesPrice(enterprisePrice);
//            } else {
//                goods.setSalesPrice(enterpriseSku.getMarketPrice());
//            }
//        } else {
//            long sCount = skuList.stream().filter(e -> !BaseUtil.isNullOrNegativeOne(e.getSalesPrice())).count();
//            EsGoodsSkuDTO salesSku;
//
//            if (sCount > 0) {
//                salesSku = skuList.stream().filter(sku -> !BaseUtil.isNegativeOne(sku.getSalesPrice())).min(Comparator.comparing(EsGoodsSkuDTO::getSalesPrice)).get();
//                EsGoodsSkuDTO marketSku = skuList.stream()
//                        .filter(sku -> BaseUtil.isNegativeOne(sku.getSalesPrice()))
//                        .filter(sku -> !BaseUtil.isNegativeOne(sku.getMarketPrice()))
//                        .min(Comparator.comparing(EsGoodsSkuDTO::getMarketPrice)).orElse(null);
//
//                if (marketSku != null) {
//                    BigDecimal salesPrice = salesSku.getSalesPrice();
//                    BigDecimal marketPrice = marketSku.getMarketPrice();
//                    if (BaseUtil.priceIsLt(marketPrice, salesPrice)) {
//                        salesSku = marketSku;
//                    }
//                }
//            } else {
//                salesSku = skuList.stream().filter(sku -> !BaseUtil.isNegativeOne(sku.getMarketPrice())).min(Comparator.comparing(EsGoodsSkuDTO::getMarketPrice)).get();
//            }
//
//
//            goods.setMarketPrice(salesSku.getMarketPrice());
//            BigDecimal salesPrice = salesSku.getSalesPrice();
//            if (!BaseUtil.isNullOrNegativeOne(salesPrice)) {
//                goods.setSalesPrice(salesPrice);
//            } else {
//                goods.setSalesPrice(salesSku.getMarketPrice());
//            }
//
//        }
//    }


    public static Integer getLowestBuy(UserTokenDTO token, WxMerchantGoodsSkuDTO sku) {
        return getLowestBuy(isCompanyUser(token), sku.getLowestBuy(), sku.getMinQuantity());
    }

    public static Integer getLowestBuy(Boolean isCompanyUser, Integer lowestBuy, Integer minQuantity) {
        if (isCompanyUser) {
            return lowestBuy;
        }
        return BaseUtil.isNullOrNegativeOne(minQuantity) ? lowestBuy : minQuantity;
    }

}
