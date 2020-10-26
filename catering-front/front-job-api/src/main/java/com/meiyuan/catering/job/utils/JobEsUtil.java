package com.meiyuan.catering.job.utils;

import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.es.MarketingSpecialSku;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.dto.goods.GoodsSortDTO;
import com.meiyuan.catering.es.dto.sku.EsGoodsSkuDTO;
import com.meiyuan.catering.marketing.enums.MarketingSpecialFixTypeEnum;
import com.meiyuan.catering.merchant.goods.enums.GoodsAddTypeEnum;
import com.meiyuan.catering.merchant.utils.MerchantUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/5/29 9:43
 * @description 简单描述
 **/

public class JobEsUtil {

    public static void setEsGoodsBaseInfo(EsGoodsDTO setGoods, EsGoodsDTO goodsInfo, MerchantUtils merchantUtils, Map<String, MarketingSpecialSku> shopSpecialSkuMap) {
        setGoods.setGoodsName(goodsInfo.getGoodsName())
                .setSpuCode(goodsInfo.getSpuCode())
                .setCategoryId(goodsInfo.getCategoryId())
                .setCategoryName(goodsInfo.getCategoryName())
                .setListPicture(goodsInfo.getListPicture())
                .setInfoPicture(goodsInfo.getInfoPicture())
                .setSalesChannels(goodsInfo.getSalesChannels())
                .setPresellFlag(goodsInfo.getPresellFlag())
                .setGoodsDescribeText(goodsInfo.getGoodsDescribeText())
                .setGoodsSpecType(goodsInfo.getGoodsSpecType())
                .setLabelList(goodsInfo.getLabelList())
                .setGoodsId(goodsInfo.getGoodsId())
                .setStartSellTime(goodsInfo.getStartSellTime())
                .setEndSellTime(goodsInfo.getEndSellTime())
                .setSellWeekTime(goodsInfo.getSellWeekTime())
                .setCloseSellTime(goodsInfo.getCloseSellTime())
                .setGoodsSynopsis(goodsInfo.getGoodsSynopsis());


    if(goodsInfo.getCategoryGoodsSort()!=null){
        setGoods.setCategoryGoodsSort(goodsInfo.getCategoryGoodsSort());
    }

        //过滤skuCode
        List<EsGoodsSkuDTO> dbSkuList = goodsInfo.getSkuList();

        // 是否是特价商品（需要重新判断，特殊情况-设置了特价商品信息的商品SKU全部被删除了）
        AtomicReference<Boolean> atomicSpecialState = new AtomicReference<>(Boolean.FALSE);

        //门店自创的商品  编辑sku直接全部同步门店
        if(goodsInfo.getGoodsAddType()!=null&&goodsInfo.getGoodsAddType().equals(GoodsAddTypeEnum.SHOP.getStatus())){
            setGoods.setSkuList(dbSkuList);
            // 同步修改店铺自创商品的特价商品折扣信息
            setGoods.getSkuList().forEach(sku -> {
                Boolean returnBoolean = setGoodsSpecialInfo(sku, shopSpecialSkuMap);
                if(Boolean.FALSE.equals(atomicSpecialState.get())) {
                    atomicSpecialState.set(returnBoolean);
                }
            });
        }
        else{
            //商户自创和平台推送的商品 有条件同步
            List<EsGoodsSkuDTO> esSkuList = setGoods.getSkuList();
            List<String> collect = esSkuList.stream().map(EsGoodsSkuDTO::getSkuCode).collect(Collectors.toList());
            List<EsGoodsSkuDTO> skuDtoS = dbSkuList.stream().filter(sku -> collect.contains(sku.getSkuCode())).collect(Collectors.toList());

            //这一步非常关键 skuDTOs 转换成 newSkuDTOs 去修改价格,使用skuDTOs会导致价格覆盖
            List<EsGoodsSkuDTO> newSkuDtoS = skuDtoS.stream().map(sku -> BaseUtil.objToObj(sku, EsGoodsSkuDTO.class)).collect(Collectors.toList());

            ShopInfoDTO shop = merchantUtils.getShop(Long.valueOf(setGoods.getShopId()));
            Boolean flag = shop.getChangeGoodPrice();

            newSkuDtoS.forEach(sku -> {
                sku.setGoodsId(setGoods.getGoodsId());
                sku.setDel(false);
                if (flag) {
                    EsGoodsSkuDTO skuDTO = setGoods.getSkuList().stream().filter(i -> i.getSkuCode().equals(sku.getSkuCode())).findFirst().get();
                    sku.setMarketPrice(skuDTO.getMarketPrice());
                    sku.setSalesPrice(skuDTO.getSalesPrice());
                    sku.setEnterprisePrice(skuDTO.getEnterprisePrice());
                }
                // 修改商品价格，重新计算折扣相关信息
                // 当判断商品是特价商品，shopSpecialSkuMap才可能会有值，否则一直为null
                Boolean returnBoolean = setGoodsSpecialInfo(sku, shopSpecialSkuMap);
                if(Boolean.FALSE.equals(atomicSpecialState.get())) {
                    atomicSpecialState.set(returnBoolean);
                }
            });

            setGoods.setSkuList(newSkuDtoS);
        }
        setGoods.setSpecialState(atomicSpecialState.get());


        if (BaseUtil.judgeList(goodsInfo.getPropertyList())) {
            setGoods.setPropertyList(goodsInfo.getPropertyList());
        }
//        if (null != goodsInfo.getMarketPrice()) {
//            setGoods.setMarketPrice(goodsInfo.getMarketPrice());
//        }
//        if (null != goodsInfo.getEnterprisePrice()) {
//            setGoods.setEnterprisePrice(goodsInfo.getEnterprisePrice());
//        }
//        if (null != goodsInfo.getSalesPrice()) {
//            setGoods.setSalesPrice(goodsInfo.getSalesPrice());
//        }



        //pc端编辑分类，门店的分类同步修改，并排序
        List<GoodsSortDTO> sortDTOS = goodsInfo.getSortDTOS();
        if(BaseUtil.judgeList(sortDTOS)){
           GoodsSortDTO dto = sortDTOS.stream().filter(i ->i.getShopId().toString().equals(setGoods.getShopId())).findFirst().get();
            setGoods.setCategoryGoodsSort(dto.getSort());
        }
    }

    private static Boolean setGoodsSpecialInfo(EsGoodsSkuDTO sku, Map<String, MarketingSpecialSku> shopSpecialSkuMap) {
        if(null != shopSpecialSkuMap && shopSpecialSkuMap.containsKey(sku.getSkuCode())) {
            MarketingSpecialSku marketingSpecialSku = shopSpecialSkuMap.get(sku.getSkuCode());
            if(MarketingSpecialFixTypeEnum.FIXED.getStatus().equals(marketingSpecialSku.getFixType())) {
                // 定价方式为固定价
                sku.setSalesPrice(marketingSpecialSku.getActivityPrice());
                sku.setSpecialNumber(BaseUtil.discountOther(marketingSpecialSku.getActivityPrice(), sku.getMarketPrice()));
            } else {
                // 定价方式为折扣，包括统一折扣、折扣
                BigDecimal specialNum = marketingSpecialSku.getSpecialNumber().divide(BigDecimal.TEN, 2, BigDecimal.ROUND_DOWN);
                sku.setSalesPrice(sku.getMarketPrice().multiply(specialNum).setScale(2, BigDecimal.ROUND_DOWN));
                sku.setSpecialNumber(marketingSpecialSku.getSpecialNumber());
            }
            sku.setDiscountLimit(null == marketingSpecialSku.getDiscountLimit() ? Integer.valueOf(-1) : marketingSpecialSku.getDiscountLimit());
            sku.setMinQuantity(null == marketingSpecialSku.getMinQuantity() ? Integer.valueOf(-1) : marketingSpecialSku.getMinQuantity());
            sku.setSpecialFixType(marketingSpecialSku.getFixType());
            sku.setSpecialId(marketingSpecialSku.getSpecialId().toString());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
