package com.meiyuan.catering.admin.service.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.meiyuan.catering.admin.dto.wxcategory.v101.WxCategoryDetailV101DTO;
import com.meiyuan.catering.admin.enums.base.RelevanceTypeEnum;
import com.meiyuan.catering.admin.fegin.WxCategoryClient;
import com.meiyuan.catering.admin.utils.AdminUtils;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.base.WxCategoryGoodsDTO;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.dto.marketing.EsMarketingDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantSimpleGoods;
import com.meiyuan.catering.es.dto.wx.EsIndexCategoryGoodsRelationDTO;
import com.meiyuan.catering.es.entity.EsGoodsEntity;
import com.meiyuan.catering.es.entity.EsMarketingEntity;
import com.meiyuan.catering.es.entity.EsMerchantEntity;
import com.meiyuan.catering.es.enums.merchant.MerchantHaveGoodsEnum;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.es.index.ElasticsearchIndex;
import com.meiyuan.catering.es.repository.ElasticsearchTemplate;
import com.meiyuan.catering.goods.dto.es.GoodsEsGoodsDTO;
import com.meiyuan.catering.goods.dto.es.GoodsMerchantMenuGoodsDTO;
import com.meiyuan.catering.goods.feign.GoodsClient;
import com.meiyuan.catering.goods.feign.MerchantMenuGoodsRelationClient;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponAloneTestDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.feign.MarketingEsClient;
import com.meiyuan.catering.marketing.feign.MarketingGoodsClient;
import com.meiyuan.catering.marketing.feign.MarketingSeckillEventRelationClient;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventIdsVO;
import com.meiyuan.catering.merchant.dto.merchant.ShopDTO;
import com.meiyuan.catering.merchant.feign.MerchantClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.vo.MerchantCountVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/26 15:26
 * @description 简单描述
 **/
@Service
@Slf4j
public class AdminEsService {
    @Resource
    MarketingEsClient marketingEsClient;
    @Resource
    GoodsClient goodsClient;
    @Resource
    MerchantMenuGoodsRelationClient merchantMenuGoodsRelationClient;
    @Resource
    EsGoodsClient esGoodsClient;
    @Resource
    EsMarketingClient esMarketingClient;
    @Resource
    MerchantClient merchantClient;
    @Resource
    ElasticsearchIndex elasticsearchIndex;
    @Resource
    AdminUtils adminUtils;
    @Resource
    WxCategoryClient wxCategoryClient;
    @Resource
    EsMerchantClient esMerchantClient;
    @Resource
    MerchantUtils merchantUtils;
    @Autowired
    private ShopGoodsClient shopGoodsClient;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private MarketingSeckillEventRelationClient seckillEventRelationClient;
    @Autowired
    MarketingGoodsClient marketingGoodsClient;


    /**
     *
     * 同步ES
     * 1.同步全部商品表的数据
     * 2.同步所有推送给商户的商品数据
     * 3.同步商户自己新增的商品数据
     * 4.同步活动数据
     * 5.同步商户数据
     * 6.删除ES中索引 创建索引新增数据
     *
     * @author: wxf
     * @date: 2020/5/26 13:48
     * @return: {@link Result< String>}
     * @version 1.0.1
     **/
    public Result<String> pushEs() {
        // 1.同步全部商品表中的数据
        Result<List<GoodsEsGoodsDTO>> goodsListResult = goodsClient.pushEs();
        // 配置的首页分类/类目 关联的商品/商家 集合数据
        Result<List<WxCategoryDetailV101DTO>> wxCategoryListResult = wxCategoryClient.listByRelevanceType();
        List<EsGoodsDTO> esGoodsIndexDtoList = setEsGoodsIndexData(goodsListResult, wxCategoryListResult);
        // 2.同步所有推送给商户的商品数据
        Result<List<GoodsMerchantMenuGoodsDTO>> merchantMenuGoodsListResult =
                merchantMenuGoodsRelationClient.listByPushMerchantGoods();
        // 所有非自提点的商户数据集合
        Result<List<ShopDTO>> merchantListResult = merchantClient.allMerchant(false);
        Map<String, ShopDTO> merchantMap = new HashMap<>(16);
        if (BaseUtil.judgeResultList(merchantListResult)) {
            List<ShopDTO> merchantList = merchantListResult.getData();
            merchantMap = merchantList.stream().collect(Collectors.toMap(i -> i.getMerchantId().toString(), Function.identity()));
        }
        // 商家商品集合
        List<EsGoodsDTO> esMerchantGoodsList = setMerchantGoodsData(merchantMenuGoodsListResult, merchantMap, goodsListResult);
        // 3.同步商户自己新增的商品数据 现在需求放在下一版
        // 4.同步活动数据
        Result<List<MarketingToEsDTO>> marketingEsDtoResult = marketingEsClient.findAll();
        List<EsMarketingDTO> esMarketingIndexDtoList = Collections.emptyList();
        if (BaseUtil.judgeResultList(marketingEsDtoResult)) {
            esMarketingIndexDtoList = setEsMarketingIndexData(merchantMap, marketingEsDtoResult.getData());
        }
        // 5.同步商户数据
        List<EsMerchantDTO> esMerchantIndexDtoList = Collections.emptyList();
        if (BaseUtil.judgeResultList(merchantListResult)) {
            esMerchantIndexDtoList = setEsMerchantIndexData(merchantListResult, merchantMap, wxCategoryListResult, merchantMenuGoodsListResult);
        }
        // 6.删除对应索引重建索引
        // 新增数据
        // 新增ES商品索引
        if (BaseUtil.judgeList(esGoodsIndexDtoList)) {
            if (BaseUtil.judgeList(esMerchantGoodsList)) {
                esGoodsIndexDtoList.addAll(esMerchantGoodsList);
            }
            esGoodsClient.saveUpdateBatch(esGoodsIndexDtoList);
        }
        // 新增活动索引
        if (BaseUtil.judgeList(esMarketingIndexDtoList)) {
            esMarketingClient.saveUpdateBatch(esMarketingIndexDtoList);
        }
        // 新增商户索引
        if (BaseUtil.judgeList(esMerchantIndexDtoList)) {
            esMerchantClient.saveUpdateBatch(esMerchantIndexDtoList);
        }
        return Result.succ("同步成功");
    }


    /**
     * 设置ES商户索引数据
     *
     * @author: wxf
     * @date: 2020/5/26 17:50
     * @param merchantListResult 数据库商户信息
     * @param merchantMap 商户信息Map
     * @param wxCategoryListResult 分类/类目 商户/商品关系集合
     * @param merchantMenuGoodsListResult 推送给商家的商品关系集合
     * @return: {@link List<  EsMerchantDTO >}
     * @version 1.0.1
     **/
    private List<EsMerchantDTO> setEsMerchantIndexData(Result<List<ShopDTO>> merchantListResult,
                                                       Map<String, ShopDTO> merchantMap,
                                                       Result<List<WxCategoryDetailV101DTO>> wxCategoryListResult,
                                                       Result<List<GoodsMerchantMenuGoodsDTO>> merchantMenuGoodsListResult) {
        List<ShopDTO> merchantList = merchantListResult.getData();
        List<EsMerchantDTO> esMerchantDtoList = BaseUtil.objToObj(merchantList, EsMerchantDTO.class);
        Map<String, List<GoodsMerchantMenuGoodsDTO>> merchantGoodsListMap = new HashMap<>(16);
        if (BaseUtil.judgeResultList(merchantMenuGoodsListResult)) {
            merchantGoodsListMap = merchantMenuGoodsListResult.getData().stream().collect(Collectors.groupingBy(i -> i.getMerchantId().toString()));
        }
        Map<String, List<GoodsMerchantMenuGoodsDTO>> finalMerchantGoodsListMap = merchantGoodsListMap;
        esMerchantDtoList.forEach(
                i -> {
                    String merchantId = i.getMerchantId();
                    MerchantCountVO merchantCount = adminUtils.getMerchantCount(Long.valueOf(merchantId));
                    if (null != merchantCount) {
                        i.setShopGrade(merchantCount.getShopGrade());
                        i.setOrdersNum(null == merchantCount.getOrdersNum() ? 0 : merchantCount.getOrdersNum());
                    } else {
                        i.setOrdersNum(0);
                    }
                    ShopDTO shop = merchantMap.getOrDefault(merchantId, null);
                    i.setId(merchantId);
                    i.setMerchantName(shop.getShopName());
                    i.setLocation(BaseUtil.locationToEsConver(shop.getMapCoordinate()));
                    i.setProvinceCode(shop.getAddressProvinceCode());
                    i.setEsCityCode(shop.getAddressCityCode());
                    i.setAreaCode(shop.getAddressAreaCode());
                    List<GoodsMerchantMenuGoodsDTO> merchantGoodsList = finalMerchantGoodsListMap.getOrDefault(merchantId, null);
                    if (BaseUtil.judgeList(merchantGoodsList)) {
                        i.setHaveGoodsFlag(MerchantHaveGoodsEnum.HAVE.getFlag());
                        List<EsMerchantSimpleGoods> esMerchantSimpleGoodsList = merchantGoodsList.stream().map(
                                p -> {
                                    EsMerchantSimpleGoods esMerchantSimpleGoods = new EsMerchantSimpleGoods();
                                    esMerchantSimpleGoods.setGoodsId(p.getGoodsId().toString());
                                    esMerchantSimpleGoods.setGoodsName(p.getGoodsName());
                                    esMerchantSimpleGoods.setGoodsStatus(p.getStatus());
                                    return esMerchantSimpleGoods;
                                }
                        ).collect(Collectors.toList());
                        i.setMerchantGoodsList(esMerchantSimpleGoodsList);
                    } else {
                        i.setHaveGoodsFlag(MerchantHaveGoodsEnum.NO_HAVE.getFlag());
                    }
                }
        );
        return esMerchantDtoList;
    }

    /**
     * 设置ES商品索引相关数据
     *
     * @author: wxf
     * @date: 2020/5/26 14:46
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.0.1
     **/
    private List<EsGoodsDTO> setEsGoodsIndexData(Result<List<GoodsEsGoodsDTO>> goodsListResult,
                                                 Result<List<WxCategoryDetailV101DTO>> wxCategoryListResult) {
        if (BaseUtil.judgeResultList(goodsListResult)) {

            // 处理商品数据
            // 转换 ES商品索引的DTO类
            List<GoodsEsGoodsDTO> goodsList = goodsListResult.getData();
            return BaseUtil.objToObj(goodsList, EsGoodsDTO.class);
        }
        return null;
    }

    /**
     * 设置 商品对应的首页分类关系集合
     *
     * @author: wxf
     * @date: 2020/5/26 14:32
     * @param wxCategoryList 关系集合
     * @param esGoodsDtoList es索引商品数据
     * @version 1.0.1
     **/
    private void setWxIndexCategoryGoodsRelation(List<WxCategoryDetailV101DTO> wxCategoryList, List<EsGoodsDTO> esGoodsDtoList) {
        List<EsIndexCategoryGoodsRelationDTO> indexCategoryGoodsRelationList = convertIndexCategoryList(wxCategoryList, RelevanceTypeEnum.GOODS.getStatus());
        Map<String, List<EsIndexCategoryGoodsRelationDTO>> indexCategoryGoodsRelationMap =
                indexCategoryGoodsRelationList.stream().collect(Collectors.groupingBy(EsIndexCategoryGoodsRelationDTO::getGoodsIdOrMerchantId));
        esGoodsDtoList.forEach(
                i -> {
                    String id = i.getId();
                    List<EsIndexCategoryGoodsRelationDTO> list = indexCategoryGoodsRelationMap.getOrDefault(id, null);
                    if (BaseUtil.judgeList(list)) {
                        i.setIndexCategoryGoodsList(list);
                    }
                }
        );
    }


    private List<EsIndexCategoryGoodsRelationDTO> convertIndexCategoryList(List<WxCategoryDetailV101DTO> wxCategoryList, Integer relevanceType) {
        return wxCategoryList.stream().flatMap(
                    i -> {
                        Long indexCategoryId = i.getId();
                        List<EsIndexCategoryGoodsRelationDTO> list = new ArrayList<>();
                        if (relevanceType.equals(RelevanceTypeEnum.MERCHANT.getStatus())) {
                            JSONArray array = JSON.parseArray(JSON.toJSONString(i.getStoryList()));
                            List<String> merchantIdList = array.toJavaList(String.class);
                            merchantIdList.forEach(
                                    p -> {
                                        EsIndexCategoryGoodsRelationDTO dto = new EsIndexCategoryGoodsRelationDTO();
                                        dto.setGoodsIdOrMerchantId(p);
                                        dto.setIndexCategoryId(indexCategoryId.toString());
                                        list.add(dto);
                                    }
                            );
                        }
                        if (relevanceType.equals(RelevanceTypeEnum.GOODS.getStatus())) {
                            JSONArray array = JSON.parseArray(JSON.toJSONString(i.getStoryGoodsList()));
                            List<WxCategoryGoodsDTO> categoryGoodsList = array.toJavaList(WxCategoryGoodsDTO.class);
                            categoryGoodsList.forEach(
                                    p -> {
                                        EsIndexCategoryGoodsRelationDTO dto = new EsIndexCategoryGoodsRelationDTO();
                                        dto.setGoodsIdOrMerchantId(p.getGoodsId());
                                        dto.setIndexCategoryId(indexCategoryId.toString());
                                        list.add(dto);
                                    }
                            );
                        }
                        return list.stream();
                    }
            ).collect(Collectors.toList());
    }

    /**
     * 设置商家对应的商品数据
     *
     * @author: wxf
     * @date: 2020/5/26 16:19
     * @param merchantMenuGoodsListResult 推送商品的商家的关系集合
     * @param merchantMap 商家信息map
     * @param goodsListResult 商品集合
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.0.1
     **/
    private List<EsGoodsDTO> setMerchantGoodsData(Result<List<GoodsMerchantMenuGoodsDTO>> merchantMenuGoodsListResult,
                                  Map<String, ShopDTO> merchantMap,
                                  Result<List<GoodsEsGoodsDTO>> goodsListResult) {
        Map<String, GoodsEsGoodsDTO> goodsMap;
        if (BaseUtil.judgeResultList(goodsListResult)) {
            List<GoodsEsGoodsDTO> goodsList = goodsListResult.getData();
            goodsMap =
                    goodsList.stream().collect(Collectors.toMap(i -> i.getId().toString(), Function.identity()));
            if (BaseUtil.judgeResultList(merchantMenuGoodsListResult)) {
                List<GoodsMerchantMenuGoodsDTO> merchantMenuGoodsList = merchantMenuGoodsListResult.getData();
                // 遍历 设置 商品对应商家信息
                // 商家商品信息集合
                return merchantMenuGoodsList.stream().map(
                        i -> {
                            String goodsId = i.getGoodsId().toString();
                            GoodsEsGoodsDTO goods = goodsMap.getOrDefault(goodsId, null);
                            EsGoodsDTO esGoodsDto = BaseUtil.objToObj(goods, EsGoodsDTO.class);
                            // 更换id
                            esGoodsDto.setId(i.getId().toString());
                            // 商户对应上下架
                            esGoodsDto.setMerchantGoodsStatus(i.getStatus());
                            // 更改标识
                            esGoodsDto.setMerchantGoodsFlag(true);
                            String merchantId = i.getMerchantId().toString();
                            ShopDTO shop = merchantMap.getOrDefault(merchantId, null);
                            esGoodsDto.setMerchantId(merchantId);
                            esGoodsDto.setMerchantName(shop.getShopName());
                            esGoodsDto.setLocation(BaseUtil.locationToEsConver(shop.getMapCoordinate()));
                            esGoodsDto.setProvinceCode(shop.getAddressProvinceCode());
                            esGoodsDto.setEsCityCode(shop.getAddressCityCode());
                            esGoodsDto.setAreaCode(shop.getAddressAreaCode());
                            return esGoodsDto;
                        }
                ).collect(Collectors.toList());
            }
        }
        return null;
    }

    /**
     * 删除 / 创建 对应索引
     *
     * @author: wxf
     * @date: 2020/5/26 17:04
     * @param
     * @version 1.0.1
     **/
    public void delAndCreateEsIndex() {
        try {
            // 删除 创建 商品索引
            elasticsearchIndex.dropIndex(EsGoodsEntity.class);
            elasticsearchIndex.createIndex(EsGoodsEntity.class);
//            // 删除 创建 活动索引
//            elasticsearchIndex.dropIndex(EsMarketingEntity.class);
//            elasticsearchIndex.createIndex(EsMarketingEntity.class);
//            // 删除 创建 商户索引
//            elasticsearchIndex.dropIndex(EsMerchantEntity.class);
//            elasticsearchIndex.createIndex(EsMerchantEntity.class);
        } catch (Exception e) {
            log.error("删除 / 创建 索引异常", e);
        }

    }

    public Result<String> pushShopEs() {
        boolean exists = Boolean.FALSE;
        try {
            exists = elasticsearchIndex.exists(EsMerchantEntity.class);
        } catch (Exception e) {
            log.error("店铺索引查询失败",e);
        }
        if (!exists){
            try {
                elasticsearchIndex.createIndex(EsMerchantEntity.class);
            } catch (Exception e) {
                log.error("店铺索引创建失败",e);
            }
        }
        //删除门店现有店铺数据
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        try {
            elasticsearchTemplate.deleteByCondition(queryBuilder,EsMerchantEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Result<List<ShopDTO>> shopList = merchantClient.allMerchant(false);
        List<ShopDTO> listShop = shopList.getData();

        if (!BaseUtil.judgeList(listShop)){
            return Result.succ("没有任何店铺");
        }

        List<EsMerchantDTO> esShopList = new ArrayList<>();
        listShop.forEach(shop->{
            //店铺数据同步
            ShopInfoDTO shopInfo = merchantUtils.getShop(shop.getId());
            EsMerchantDTO esMerchantDto = BaseUtil.objToObj(shop, EsMerchantDTO.class);
            esMerchantDto.setId(shop.getId().toString());
            esMerchantDto.setShopService(1);
            if (BaseUtil.judgeString(shopInfo.getShopService())){
                esMerchantDto.setShopService(shopInfo.getShopServiceType());
            }
            esMerchantDto.setShopId(shop.getId().toString());
            esMerchantDto.setMerchantName(shop.getShopName());
            esMerchantDto.setLocation(BaseUtil.locationToEsConver(shop.getMapCoordinate()));
            esMerchantDto.setProvinceCode(shop.getAddressProvinceCode());
            esMerchantDto.setEsCityCode(shop.getAddressCityCode());
            esMerchantDto.setAreaCode(shop.getAddressAreaCode());

            //商户数据同步
            MerchantInfoDTO merchantInfo = merchantUtils.getMerchant(shop.getMerchantId());
            if (!ObjectUtils.isEmpty(merchantInfo)){
                esMerchantDto.setMerchantId(shop.getMerchantId().toString());
                esMerchantDto.setAuditStatus(merchantInfo.getAuditStatus());
                esMerchantDto.setMerchantStatus(merchantInfo.getMerchantStatus());
            }

            //店铺月销数据同步
            MerchantCountVO merchantCount = adminUtils.getMerchantCount(Long.valueOf(shop.getId()));
            if (null != merchantCount) {
                esMerchantDto.setShopGrade(merchantCount.getShopGrade());
                esMerchantDto.setOrdersNum(null == merchantCount.getOrdersNum() ? 0 : merchantCount.getOrdersNum());
            } else {
                esMerchantDto.setOrdersNum(0);
            }

            //商品相关数据同步
            esMerchantDto.setHaveGoodsFlag(Boolean.FALSE);
            esShopList.add(esMerchantDto);
        });

        esMerchantClient.saveUpdateBatch(esShopList);
        return Result.succ("添加成功");
    }


    /**
     * 方法描述 刷新门店es
     * @author: lhm
     * @date: 2020/7/18 10:33
     * @param
     * @return: {@link }
     * @version 1.1.0
     **/
    public Result<String> pushMerchantGoodsEs() {
        try {
            // 同步门店商品ES
            shopGoodsClient.pushMerchantGoodsEs(null,null);
        } catch (Exception e) {
            log.error("刷新商品ES失败:{}",e.getMessage());
            throw new CustomException("刷新商品ES失败!");
        }
        return Result.succ("同步成功");
    }

    /**
    * 设置秒杀活动ES中的场次ID信息
    * @author: GongJunZheng
    * @date: 2020/9/2 11:20
    * @return: {@link String}
    * @version V1.4.0
    **/
    public Result<String> setSeckillEventIdsEs() {
        // 从ES中查询出所有的秒杀活动信息
        Result<List<EsMarketingEntity>> seckillListResult = esMarketingClient.findAllSeckill();
        List<EsMarketingEntity> seckillList = seckillListResult.getData();
        if(BaseUtil.judgeList(seckillList)) {
            Set<Long> seckillIds = new HashSet<>();
            seckillList.forEach(item -> seckillIds.add(Long.parseLong(item.getId())));
            // 查询秒杀场次信息
            Result<List<MarketingSeckillEventIdsVO>> seckillEventListResult = seckillEventRelationClient.selectEventIdsBySeckillIds(seckillIds);
            List<MarketingSeckillEventIdsVO> seckillEventList = seckillEventListResult.getData();
            if(BaseUtil.judgeList(seckillEventList)) {
                Map<Long, String> eventMap = seckillEventList.stream().collect(Collectors.toMap(MarketingSeckillEventIdsVO::getSeckillId,
                        MarketingSeckillEventIdsVO::getSeckillEventIds));
                seckillList.forEach(item -> {
                    String eventIds = eventMap.get(Long.parseLong(item.getId()));
                    item.setSeckillEventIds(eventIds);
                });
                // 保存新的秒杀活动信息
                esMarketingClient.updateBatch(seckillList);
            }
        }
        return Result.succ("设置秒杀活动ES中的场次ID信息成功");
    }

    public Result<String> setMarketingGoodsAddType() {
        // 查询ES中所有的营销秒杀/团购商品信息
        Result<List<EsMarketingEntity>> allListResult = esMarketingClient.findAll();
        List<EsMarketingEntity> allList = allListResult.getData();
        if(BaseUtil.judgeList(allList)) {
            // 查询数据库中所有的营销秒杀/团购商品信息
            Result<List<CateringMarketingGoodsEntity>> allMarketingGoodsListResult = marketingGoodsClient.findAllByGoodsAddType();
            List<CateringMarketingGoodsEntity> allMarketingGoodsList = allMarketingGoodsListResult.getData();
            Map<Long, Integer> map = allMarketingGoodsList.stream().filter(item -> null != item.getGoodsAddType())
                    .collect(Collectors.toMap(CateringMarketingGoodsEntity::getId,
                    CateringMarketingGoodsEntity::getGoodsAddType));
            List<EsMarketingEntity> result = new ArrayList<>();
            allList.forEach(item -> {
                String mGoodsId = item.getMGoodsId();
                if(null != mGoodsId) {
                    Integer goodsAddType = map.get(Long.valueOf(mGoodsId));
                    if(null == goodsAddType) {
                        item.setGoodsAddType(-1);
                    } else {
                        item.setGoodsAddType(goodsAddType);
                    }
                    result.add(item);
                }
            });
            esMarketingClient.updateBatch(result);
        }
        return Result.succ("设置营销团购/秒杀活动ES中的商品添加类型信息成功");
    }

    public Result<String> setMarketingGoodsSaleChannels() {
        // 查询ES中所有的营销秒杀/团购商品信息
        Result<List<EsMarketingEntity>> allListResult = esMarketingClient.findAll();
        List<EsMarketingEntity> allList = allListResult.getData();
        if(BaseUtil.judgeList(allList)) {
            // 查询数据库中所有的营销秒杀/团购商品信息
            Result<List<CateringMarketingGoodsEntity>> allMarketingGoodsListResult = marketingGoodsClient.findAllByGoodsSaleChannels();
            List<CateringMarketingGoodsEntity> allMarketingGoodsList = allMarketingGoodsListResult.getData();
            Map<Long, Integer> map = allMarketingGoodsList.stream().filter(item -> null != item.getGoodsSalesChannels())
                    .collect(Collectors.toMap(CateringMarketingGoodsEntity::getId,
                    CateringMarketingGoodsEntity::getGoodsSalesChannels));
            List<EsMarketingEntity> result = new ArrayList<>();
            allList.forEach(item -> {
                String mGoodsId = item.getMGoodsId();
                if(null != mGoodsId) {
                    Integer goodsSalesChannels = map.get(Long.valueOf(mGoodsId));
                    if(null == goodsSalesChannels) {
                        item.setGoodsSalesChannels(-1);
                    } else {
                        item.setGoodsSalesChannels(goodsSalesChannels);
                    }
                    result.add(item);
                }
            });
            esMarketingClient.updateBatch(result);
        }
        return Result.succ("设置营销团购/秒杀活动ES中的商品销售渠道类型信息成功");
    }

    public Result<String> pushMarketingEs() {
        try{
            // 查询所有的门店信息（包括被删除、被禁用）
            Result<List<ShopDTO>> merchantListResult = merchantClient.findAll();
            List<ShopDTO> merchantList = merchantListResult.getData();
            if (BaseUtil.judgeList(merchantList)) {
                Map<String, ShopDTO> merchantMap = merchantList.stream().collect(Collectors.toMap(i -> i.getId().toString(), Function.identity()));
                // 查询所有的秒杀/团购信息
                Result<List<MarketingToEsDTO>> marketingListResult = marketingEsClient.findAll();
                List<MarketingToEsDTO> marketingList = marketingListResult.getData();
                if(BaseUtil.judgeList(marketingList)) {
                    List<EsMarketingDTO> esMarketingIndexDtoList = setEsMarketingIndexData(merchantMap, marketingList);
                    // 新增活动索引
                    if (BaseUtil.judgeList(esMarketingIndexDtoList)) {
                        esMarketingClient.saveUpdateBatch(esMarketingIndexDtoList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("同步营销秒杀/团购活动至ES失败");
        }
        return Result.succ("同步营销秒杀/团购活动至ES成功");
    }

    /**
     * 设置es活动索引的数据
     *
     * @author: wxf
     * @date: 2020/5/26 16:43
     * @param merchantMap 商户信息集合
     * @param marketingList 数据库的活动信息集合
     * @return: {@link List<EsMarketingDTO>}
     * @version 1.0.1
     **/
    private List<EsMarketingDTO> setEsMarketingIndexData(Map<String, ShopDTO> merchantMap, List<MarketingToEsDTO> marketingList) {
        // 设置ES中营销商品字段
        marketingList.forEach(item -> {
            ShopInfoDTO shop = merchantUtils.getShop(item.getShopId());
            if(null != shop) {
                item.setShopState(shop.getShopStatus());
                item.setShopServiceType(shop.getShopServiceType());
                MerchantInfoDTO merchant = merchantUtils.getMerchant(item.getMerchantId());
                item.setMerchantState(merchant.getMerchantStatus());
            } else {
                // shop == null，说明门店被删除了，营销商品直接设置为true
                item.setDel(DelEnum.DELETE.getFlag());
            }
        });

        List<EsMarketingDTO> esMarketingDtoList = BaseUtil.objToObj(marketingList, EsMarketingDTO.class);
        esMarketingDtoList.forEach(
                i -> {
                    ShopDTO shop = merchantMap.getOrDefault(i.getShopId(), null);
                    if(null != shop) {
                        if(null != shop.getMapCoordinate()) {
                            i.setLocation(BaseUtil.locationToEsConver(shop.getMapCoordinate()));
                        }
                        i.setProvinceCode(shop.getAddressProvinceCode());
                        i.setEsCityCode(shop.getAddressCityCode());
                        i.setAreaCode(shop.getAddressAreaCode());
                    }
                }
        );
        return esMarketingDtoList;
    }



    /**
     * 方法描述   处理数据库分类排序和商品排序
     * @author: lhm
     * @date: 2020/9/16 11:02
     * @param
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<String> pushGoodsEsToDb() {
      return   shopGoodsClient.pushGoodsEsToDb();
    }

    public Result<String> grouponAloneTest(MarketingGrouponAloneTestDTO dto) {
        String existedToken = "Fa0PVkYVCsXipROr";
        if(!existedToken.equals(dto.getToken())) {
            throw new CustomException("请输入正确的token");
        }
        LocalDateTime now = LocalDateTime.now();
        // 修改数据库
        LocalDateTime oldEndTime = marketingEsClient.updateGrouponTime(dto);
        // 修改ES
        esMarketingClient.updateGrouponTime(dto.getId(), dto.getBeginTime(), dto.getEndTime());
        if(!oldEndTime.isEqual(dto.getEndTime())) {
            // 延迟定时结束消息
            long hours = Math.abs(ChronoUnit.HOURS.between(now, dto.getEndTime()));
            int dayHour = 24;
            //与结束时间相差小于24小时，则直接发送mq定时结束延迟消息
            if (hours <= dayHour) {
                marketingEsClient.sendGrouponTimedTaskMsg(dto.getId(), dto.getEndTime(), MarketingUpDownStatusEnum.DOWN);
            }
        }
        return Result.succ("团购活动时间修改，并同步ES成功");
    }

    /**
    * V1.5.0 设置营销商品的规格类型
    *
    * @author: GongJunZheng
    * @date: 2020/9/29 17:19
    * @return: {@link String}
    * @version V1.5.0
    **/
    public Result<String> setMarketingGoodsSpecType() {
        // 查询ES中所有的营销秒杀/团购商品信息
        Result<List<EsMarketingEntity>> allListResult = esMarketingClient.findAll();
        List<EsMarketingEntity> allList = allListResult.getData();
        if(BaseUtil.judgeList(allList)) {
            // 查询数据库中所有的营销秒杀/团购商品信息
            Result<List<CateringMarketingGoodsEntity>> allMarketingGoodsListResult = marketingGoodsClient.findAllByGoodsSpecType();
            List<CateringMarketingGoodsEntity> allMarketingGoodsList = allMarketingGoodsListResult.getData();
            Map<Long, Integer> map = allMarketingGoodsList.stream().filter(item -> null != item.getGoodsSpecType())
                    .collect(Collectors.toMap(CateringMarketingGoodsEntity::getId,
                            CateringMarketingGoodsEntity::getGoodsSpecType));
            List<EsMarketingEntity> result = new ArrayList<>();
            allList.forEach(item -> {
                String mGoodsId = item.getMGoodsId();
                if(null != mGoodsId) {
                    Integer goodsSpecType = map.get(Long.valueOf(mGoodsId));
                    item.setGoodsSpecType(goodsSpecType);
                    if(0 == item.getPackPrice()) {
                        item.setPackPrice(-1d);
                    }
                    result.add(item);
                }
            });
            esMarketingClient.updateBatch(result);
        }
        return Result.succ("设置营销团购/秒杀活动ES中的商品规格类型信息成功");
    }




    public Result<Boolean> updateCategory() {
     return   Result.succ(shopGoodsClient.updateCategory());

    }
}


