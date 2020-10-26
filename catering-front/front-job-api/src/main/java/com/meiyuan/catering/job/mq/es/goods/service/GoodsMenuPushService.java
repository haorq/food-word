package com.meiyuan.catering.job.mq.es.goods.service;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Lists;
import java.util.Date;

import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.es.*;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.dto.goods.EsMerchantMenuGoodsDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantSimpleGoods;
import com.meiyuan.catering.es.dto.sku.EsGoodsSkuDTO;
import com.meiyuan.catering.es.enums.merchant.MerchantHaveGoodsEnum;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.goods.feign.LabelClient;
import com.meiyuan.catering.marketing.enums.MarketingSpecialFixTypeEnum;
import com.meiyuan.catering.marketing.feign.MarketingSpecialClient;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.meiyuan.catering.job.mq.marketing.GrouponUpDownMsgReceive.distinctByKey;

/**
 * describe:
 *
 * @author: zengzhangni
 **/
@Slf4j
@Component
public class GoodsMenuPushService {
    @Resource
    private EsGoodsClient esGoodsClient;
    @Resource
    private MerchantUtils merchantUtils;
    @Autowired
    private MerchantGoodsClient merchantGoodsClient;
    @Autowired
    private ShopGoodsClient shopGoodsClient;
    @Autowired
    private LabelClient labelClient;
    @Resource
    private EsMerchantClient esMerchantClient;
    @Autowired
    private MarketingSpecialClient specialClient;


    public void pushEs(List<EsMerchantMenuGoodsDTO> dtos) {

        Long merchantId = dtos.get(0).getMerchantId();

        //所有门店id
        Set<Long> shopIds = dtos.stream().map(EsMerchantMenuGoodsDTO::getShopId).collect(Collectors.toSet());
        //所有商品id
//        Set<Long> goodsIds = dtos.stream().map(EsMerchantMenuGoodsDTO::getGoodsId).collect(Collectors.toSet());
        //所有的商品id 改为从数据库中获取
        List<Long> goodsIds = shopGoodsClient.getShopGoodsId(shopIds);

        //所有组装数据
        EsDataMap dataMap = getEsDataMap(shopIds);

        //所有商户商品基础数据
        Map<String, List<MerchantBaseGoods>> listMap = getGoodsList(goodsIds, merchantId);


        //装所有组装好的门店商品数据
        List<EsGoodsDTO> entityList = new ArrayList<>();
        //表示为通过菜单推送的
//        int i = 1;

        List<MerchantBaseGoods> merchantBaseGoods = listMap.get("1");

        //为每一家推送的门店创建商品信息CateringMerchantGoodsServiceImpl
        shopIds.forEach(id -> {

            //通过商户商品基础信息集合创建一个新的门店商品信息集合
            List<EsGoodsDTO> newList = BaseUtil.objToObj(merchantBaseGoods, EsGoodsDTO.class);

            //获取门店商品
            List<MerchantBaseGoods> shopGoods = listMap.get(id.toString());
            if (BaseUtil.judgeList(shopGoods)) {
                List<EsGoodsDTO> newShopList = BaseUtil.objToObj(shopGoods, EsGoodsDTO.class);
                //合并品牌,门店商品信息
                newList.addAll(newShopList);
            }

            //门店商品状态
            Map<Long, ShopGoodsStatusMap> shopGoodsMap = dataMap.getShopGoods(id);
            //门店所有商品的sku
            Map<Long, List<ShopGoodsSku>> shopGoodsSku = dataMap.getShopSku(id);
            // 门店下的特价商品信息
            Map<String, MarketingSpecialSku> shopSpecialSku = dataMap.getShopSpecialSku(id);
            //门店信息
            ShopInfoDTO shop = dataMap.getShop(id.toString());

            //处理品牌/门店商品 给基础商品信息添加门店自己的属性
            entityList.addAll(
                    newList.stream().map(goods -> {
                                if (shop == null) {
                                    log.error("shop null");
                                    return null;
                                }
                                return setGoodsAll(id, shopGoodsMap, shopGoodsSku, shop, goods, shopSpecialSku);
                            }
                    ).filter(Objects::nonNull).collect(Collectors.toList())
            );


        });

        //更新Es
        esUpdate(entityList, shopIds);
        // 设置商户索引对应数据 需要穿shopIdList
        pushEsMerchant(shopIds, entityList);

    }

    private EsGoodsDTO setGoodsAll(Long id, Map<Long, ShopGoodsStatusMap> shopGoodsMap, Map<Long, List<ShopGoodsSku>> shopGoodsSku,
                                   ShopInfoDTO shop, EsGoodsDTO goods, Map<String, MarketingSpecialSku> shopSpecialSku) {

        //商户商品sku
        List<EsGoodsSkuDTO> skuList = goods.getSkuList();

        if (skuList == null || skuList.size() == 0) {
            log.error("skuList:{}", skuList);
            return null;
        }
        //门店商品sku
        List<ShopGoodsSku> shopSkus = shopGoodsSku.get(Long.valueOf(goods.getGoodsId()));

        if (shopSkus == null || shopSkus.size() == 0) {
            log.error("shopSkus:{}", shopSkus);
            return null;
        }
        //获取门店自己的商品状态
        ShopGoodsStatusMap goodsStatusMap = shopGoodsMap.get(Long.valueOf(goods.getGoodsId()));
        log.debug("goodsStatusMap:{}", goodsStatusMap);
        //门店授权的sku 分组(方便下面遍历时获取) 门店skuCode -> sku
        Map<String, ShopGoodsSku> shopSkuMap = shopSkus.stream().collect(Collectors.toMap(ShopGoodsSku::getSkuCode, e -> e));
        log.debug("shopSkuMap:{}", shopSkuMap);


        //esId 使用门店spuId
        String esId = shopSkus.get(0).getShopGoodsSpuId().toString();
        goods.setId(esId);
        goods.setMerchantId(shop.getMerchantId().toString());
        goods.setShopId(id.toString());
        goods.setShopName(shop.getShopName());
        goods.setProvinceCode(shop.getAddressProvinceCode());
        goods.setEsCityCode(shop.getAddressCityCode());
        goods.setAreaCode(shop.getAddressAreaCode());
        goods.setLocation(BaseUtil.locationToEsConver(shop.getMapCoordinate()));
        goods.setGoodsStatus(goodsStatusMap.getShopGoodsStatus());
        goods.setCategoryGoodsSort(goodsStatusMap.getSort());
        System.out.println(goodsStatusMap.getSort());
//        goods.setGoodsAddType(goodsAndType);

        AtomicReference<Boolean> atomicGoodsSpecialState = new AtomicReference<>(Boolean.FALSE);

        List<EsGoodsSkuDTO> newSkuList = skuList.stream()
                //过滤掉门店未授权的sku
                .filter(sku -> shopSkuMap.containsKey(sku.getSkuCode()))
                .peek(sku -> {
                    //价格使用门店sku价格
                    ShopGoodsSku goodsSku = shopSkuMap.get(sku.getSkuCode());
                    sku.setMarketPrice(goodsSku.getMarketPrice());
                    sku.setSalesPrice(goodsSku.getSalesPrice());
                    sku.setEnterprisePrice(goodsSku.getEnterprisePrice());
                    sku.setPackPrice(goodsSku.getPackPrice());
                    // 判断当前商品是否有特价商品信息
                    if (null != shopSpecialSku && shopSpecialSku.containsKey(sku.getSkuCode())) {
                        atomicGoodsSpecialState.set(Boolean.TRUE);
                        MarketingSpecialSku marketingSpecialSku = shopSpecialSku.get(sku.getSkuCode());
                        if (MarketingSpecialFixTypeEnum.FIXED.getStatus().equals(marketingSpecialSku.getFixType())) {
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
                    }
                }).collect(Collectors.toList());
        // 设置是否是特价商品标识
        goods.setSpecialState(atomicGoodsSpecialState.get());
        //设置新的sku属性
        goods.setSkuList(newSkuList);

        return goods;
    }

    private void pushEsMerchant(Set<Long> shopIds, List<EsGoodsDTO> entityList) {
        Result<List<EsMerchantDTO>> esMerchantListResult = esMerchantClient.listByMerchantIdList(new ArrayList(shopIds));
        if (BaseUtil.judgeResultList(esMerchantListResult)) {
            List<EsMerchantDTO> esMerchantDtoList = esMerchantListResult.getData();
            Map<String, List<EsGoodsDTO>> shopGoodsMap = entityList.stream().collect(Collectors.groupingBy(EsGoodsDTO::getShopId));
            esMerchantDtoList.forEach(
                    i -> {
                        String shopId = i.getShopId();
                        List<EsGoodsDTO> esGoodsDtoList = shopGoodsMap.get(shopId);
                        List<EsMerchantSimpleGoods> saveList = esGoodsDtoList.stream().map(
                                p -> {
                                    EsMerchantSimpleGoods esMerchantSimpleGoods = new EsMerchantSimpleGoods();
                                    esMerchantSimpleGoods.setGoodsId(p.getGoodsId());
                                    esMerchantSimpleGoods.setGoodsName(p.getGoodsName());
                                    esMerchantSimpleGoods.setGoodsStatus(p.getGoodsStatus());
                                    return esMerchantSimpleGoods;
                                }
                        ).collect(Collectors.collectingAndThen(Collectors.toCollection(
                                () -> new TreeSet<>(Comparator.comparing(EsMerchantSimpleGoods::getGoodsId))), ArrayList::new));
                        i.setMerchantGoodsList(saveList);
                        i.setHaveGoodsFlag(MerchantHaveGoodsEnum.HAVE.getFlag());
                    }
            );
            esMerchantClient.saveUpdateBatch(esMerchantDtoList);
        }
    }

    private void esUpdate(List<EsGoodsDTO> saveGoodsList, Set<Long> shopIds) {

        //删除es中的商品数据 门店的商品
        esGoodsClient.deleteGoodsToShop(new ArrayList<>(shopIds), false);

        //添加商品数据（菜单的商品数据）
        //门店自己创建的商品数据

        //通过商户商品基础信息集合创建一个新的门店商品信息集合
        List<EsGoodsDTO> distinctList = saveGoodsList.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(EsGoodsDTO::getKey))), ArrayList::new));
        if(BaseUtil.judgeList(distinctList)){
            esGoodsClient.saveUpdateBatch(distinctList);
        }

    }


    private Map<String, List<MerchantBaseGoods>> getGoodsList(Collection<Long> goodsIds, Long merchantId) {

        //商户商品基础信息
        List<MerchantBaseGoods> goodsBaseInfos = merchantGoodsClient.goodsBaseInfo(goodsIds, merchantId);

        //标签信息
        Map<Long, List<MerchantBaseLabel>> labelMap = getLabelMap(goodsIds);

        //添加对应标签
        goodsBaseInfos.forEach(wxGoods -> {
            List<MerchantBaseLabel> labels = labelMap.get(Long.valueOf(wxGoods.getGoodsId()));
            if(BaseUtil.judgeList(labels)){
                List<MerchantBaseLabel> baseLabels = labels.stream().filter(distinctByKey((p) -> (p.getId()))).collect(Collectors.toList());
                wxGoods.setLabelList(baseLabels);
            }
        });

        Map<String, List<MerchantBaseGoods>> listMap = goodsBaseInfos.stream().collect(Collectors.groupingBy(MerchantBaseGoods::getShopId));
        return listMap;

    }

    private EsDataMap getEsDataMap(Set<Long> shopIds) {
        EsDataMap dataMap = new EsDataMap();
        //门店信息
        Map<String, ShopInfoDTO> shopMap = getShopInfo(shopIds);
        //门店状态信息
        Map<Long, Map<Long, ShopGoodsStatusMap>> shopGoodsStatus = getShopGoodsStatus(shopIds);
        //门店sku信息listByGoodsIdListAndMerchant
        Map<Long, Map<Long, List<ShopGoodsSku>>> shopGoodsSkus = getShopGoodsSkus(shopIds);
        // V1.4.0 门店特价商品信息
        Result<Map<Long, Map<String, MarketingSpecialSku>>> shopSpecialSkuMapResult = specialClient.selectGoodsSkuByShopIds(shopIds);

        dataMap.setShopMap(shopMap);
        dataMap.setShopGoodsStatus(shopGoodsStatus);
        dataMap.setShopGoodsSkus(shopGoodsSkus);
        dataMap.setShopSpecialSkuMap(shopSpecialSkuMapResult.getData());

        return dataMap;
    }


    private Map<Long, List<MerchantBaseLabel>> getLabelMap(Collection<Long> goodsIds) {
        List<MerchantBaseLabel> labels = labelClient.getLabel(goodsIds);

        return labels.stream().collect(Collectors.groupingBy(MerchantBaseLabel::getGoodsId));
    }

    private Map<String, ShopInfoDTO> getShopInfo(Set<Long> shopIds) {
        Set<String> idSet = shopIds.stream().map(Objects::toString).collect(Collectors.toSet());
        return merchantUtils.getShops(idSet);
    }

    private Map<Long, Map<Long, ShopGoodsStatusMap>> getShopGoodsStatus(Set<Long> shopIds) {

        List<ShopGoodsStatusMap> shopGoodsStatusMaps = shopGoodsClient.getShopGoodsStatus(shopIds);

        //shopId -> { goodsId -> shopGoodsStatus}
        return shopGoodsStatusMaps.stream().collect(Collectors.groupingBy(ShopGoodsStatusMap::getShopId, Collectors.toMap(ShopGoodsStatusMap::getGoodsId, e -> e, (o, n) -> o)));
    }

    private Map<Long, Map<Long, List<ShopGoodsSku>>> getShopGoodsSkus(Set<Long> shopIds) {

        List<ShopGoodsSku> getShopGoodsSkus = shopGoodsClient.getShopGoodsSkus(shopIds);

        //shopId -> { goodsId -> shopGoodsSku}
        return getShopGoodsSkus.stream().collect(Collectors.groupingBy(ShopGoodsSku::getShopId, Collectors.groupingBy(ShopGoodsSku::getGoodsId)));
    }
}
