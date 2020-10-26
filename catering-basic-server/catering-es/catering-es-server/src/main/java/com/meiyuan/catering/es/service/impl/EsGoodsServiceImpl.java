package com.meiyuan.catering.es.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meiyuan.catering.core.dto.base.WxCategoryGoodsDTO;
import com.meiyuan.catering.core.dto.es.MarketingSpecialSku;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsDTO;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsSkuDTO;
import com.meiyuan.catering.core.dto.goods.MerchantGoodsToEsDTO;
import com.meiyuan.catering.core.dto.goods.RecommendDTO;
import com.meiyuan.catering.core.enums.base.GoodsAddTypeEnum;
import com.meiyuan.catering.core.enums.base.SaleChannelsEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.es.constant.CommonConstant;
import com.meiyuan.catering.es.constant.EsIndexConstant;
import com.meiyuan.catering.es.constant.GoodsHelper;
import com.meiyuan.catering.es.dto.geo.EsGeoSearchDTO;
import com.meiyuan.catering.es.dto.geo.GeoLimitQueryDTO;
import com.meiyuan.catering.es.dto.goods.*;
import com.meiyuan.catering.es.dto.merchant.DiscountQuery;
import com.meiyuan.catering.es.dto.merchant.EsMerchantListParamDTO;
import com.meiyuan.catering.es.dto.sku.EsGoodsSkuDTO;
import com.meiyuan.catering.es.dto.sku.EsSkuCodeAndGoodsIdDTO;
import com.meiyuan.catering.es.dto.wx.EsWxIndexSearchQueryDTO;
import com.meiyuan.catering.es.entity.EsGoodsEntity;
import com.meiyuan.catering.es.enums.AggsType;
import com.meiyuan.catering.es.enums.goods.CategoryLabelTypeEnum;
import com.meiyuan.catering.es.enums.goods.GoodsSpecTypeEnum;
import com.meiyuan.catering.es.enums.goods.GoodsStatusEnum;
import com.meiyuan.catering.es.enums.goods.MerchantGoodsFlagEnum;
import com.meiyuan.catering.es.enums.marketing.MarketingEsSpecialFixTypeEnum;
import com.meiyuan.catering.es.enums.marketing.MarketingStatusEnum;
import com.meiyuan.catering.es.repository.Attach;
import com.meiyuan.catering.es.repository.ElasticsearchTemplate;
import com.meiyuan.catering.es.repository.PageSortHighLight;
import com.meiyuan.catering.es.repository.Sort;
import com.meiyuan.catering.es.service.EsGoodsService;
import com.meiyuan.catering.es.util.EsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/23 14:07
 * @description 简单描述
 **/
@Service
@Slf4j
public class EsGoodsServiceImpl implements EsGoodsService {
    @Resource
    ElasticsearchTemplate<EsGoodsEntity, String> elasticsearchTemplate;
    private Sort.Order categoryGoodsSortOrder = new Sort.Order(SortOrder.DESC, "createTime");
    private static final String GOODS_ID = "goodsId";
    private static final String START_SELL_TIME = "startSellTime";
    private static final String END_SELL_TIME = "endSellTime";

    /**
     * 新增修改数据
     *
     * @param dto 新增修改数据
     * @author: wxf
     * @date: 2020/3/23 14:04
     **/
    @Override
    public void saveUpdate(EsGoodsDTO dto) {
        if (null != dto) {
            try {
                EsGoodsEntity esGoodsEntity = BaseUtil.objToObj(dto, EsGoodsEntity.class);
                boolean flag = elasticsearchTemplate.save(esGoodsEntity);
                log.info(flag ? "单个新增/修改ES商品索引数据成功" : "单个新增/修改ES商品索引数据失败");
            } catch (Exception e) {
                log.error("单个新增/修改ES商品索引数据异常", e);
            }
        }
    }

    /**
     * 新增修改数据
     *
     * @param dtoList 新增修改数据集合
     * @author: wxf
     * @date: 2020/3/23 14:04
     **/
    @Override
    public void saveUpdateBatch(List<EsGoodsDTO> dtoList) {
        if (!BaseUtil.judgeList(dtoList)) {
            log.warn("新增数据为空");
        }
        try {
            List<EsGoodsEntity> goodsList = BaseUtil.objToObj(dtoList, EsGoodsEntity.class);
            BulkResponse responses = elasticsearchTemplate.save(goodsList);
            // Bulk响应提供了一种方法来快速检查一个或多个操作是否失败  hasFailures
            // 有一个操作失败，此方法将返回true 全部成功返回false
            if (responses.hasFailures()) {

                log.error("批量新增/修改商品索引数据失败:{}", responses.buildFailureMessage());
            } else {
                log.info("批量新增/修改商品索引数据新增成功");
            }
        } catch (Exception e) {
            log.error("批量新增/修改商品索引数据异常", e);
        }
    }


    /**
     * 方法描述   批量修改数据
     *
     * @param dtoList
     * @author: lhm
     * @date: 2020/7/21 15:17
     * @return: {@link }
     * @version 1.1.0
     **/
    @Override
    public void updateBatch(List<EsGoodsDTO> dtoList) {
        if (!BaseUtil.judgeList(dtoList)) {
            log.warn("新增数据为空");
        }
        try {
            List<EsGoodsEntity> goodsList = BaseUtil.objToObj(dtoList, EsGoodsEntity.class);
            BulkResponse responses = elasticsearchTemplate.updateRequest(goodsList);
            // Bulk响应提供了一种方法来快速检查一个或多个操作是否失败  hasFailures
            // 有一个操作失败，此方法将返回true 全部成功返回false
            if (responses.hasFailures()) {

                log.error("批量新增/修改商品索引数据失败:{}", responses.buildFailureMessage());
            } else {
                log.info("批量新增/修改商品索引数据新增成功");
            }
        } catch (Exception e) {
            log.error("批量新增/修改商品索引数据异常", e);
        }
    }

    /**
     * 获取ES商品根据商品id
     *
     * @param goodsId 商品id
     * @param flag    是否过滤sku true 过滤 false 不过滤
     * @author: wxf
     * @date: 2020/3/24 13:09
     * @return: {@link EsGoodsDTO}
     **/
    @Override
    public EsGoodsDTO getById(Long goodsId, Boolean flag) {
        EsGoodsEntity goods = null;
        try {
            goods = elasticsearchTemplate.getById(goodsId.toString(), EsGoodsEntity.class);
        } catch (Exception e) {
            log.error("获取ES商品根据商品id异常", e);
        }
        if (null == goods) {
            return null;
        }
        if (Boolean.TRUE.equals(flag)) {
            List<EsGoodsSkuDTO> skuList = goods.getSkuList();
            skuList = EsUtil.filterSkuList(skuList);
            goods.setSkuList(skuList);
        }

        EsGoodsDTO dto = BaseUtil.objToObj(goods, EsGoodsDTO.class);
        if (GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus().equals(dto.getGoodsSpecType())) {
            dto.setSkuCode(goods.getSkuList().get(0).getSkuCode());
        }
        return dto;
    }

    /**
     * 批量获取根据商品id集合
     *
     * @param goodsIdList 商品id集合
     * @param flag        是否包含推送给商家的商品 true 包含 false不包含 null 符合商品id的都查
     * @author: wxf
     * @date: 2020/5/28 19:15
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.0.1
     **/
    @Override
    public List<EsGoodsDTO> listByGoodsIdList(List<Long> goodsIdList, Boolean flag, EsGoodsDTO dto) {
        if (!BaseUtil.judgeList(goodsIdList)) {
            log.warn("商品id集合为空");
            return Lists.newArrayList();
        }
        List<EsGoodsDTO> esGoodsDtoList = Collections.emptyList();
        try {
            EsGoodsCommonQueryBuilderDTO queryBuilderDto = new EsGoodsCommonQueryBuilderDTO();
            // 匹配是否过滤推送给商家的商品
            if (null != flag) {
                queryBuilderDto.setMerchantGoodsFlag(flag);
            }
            queryBuilderDto.setGoodsIdList(goodsIdList);
            if (dto.getMerchantId() != null) {
                queryBuilderDto.setMerchantId(Long.valueOf(dto.getMerchantId()));
            }
            if (dto.getShopId() != null) {
                queryBuilderDto.setShopId(Long.valueOf(dto.getShopId()));
            }

            BoolQueryBuilder queryBuilder = commonQueryBuilder(queryBuilderDto);
            List<EsGoodsEntity> esGoodsEntityList = elasticsearchTemplate.merchantGoodsSearch(queryBuilder, EsGoodsEntity.class,
                    new Sort(categoryGoodsSortOrder), EsIndexConstant.getGoodsIndex());
            esGoodsDtoList = BaseUtil.objToObj(esGoodsEntityList, EsGoodsDTO.class);
        } catch (Exception e) {
            log.error("批量获取根据商品id集合异常.", e);
        }
        return esGoodsDtoList;
    }

    @Override
    public List<EsGoodsDTO> listEsGoods(EsGoodsQueryConditionDTO dto) {
        List<EsGoodsDTO> esGoodsDtoList = Collections.emptyList();
        try {
            EsGoodsCommonQueryBuilderDTO queryBuilderDto = BaseUtil.objToObj(dto, EsGoodsCommonQueryBuilderDTO.class);
            BoolQueryBuilder queryBuilder = commonQueryBuilder(queryBuilderDto);
            List<EsGoodsEntity> esGoodsEntityList = elasticsearchTemplate.merchantGoodsSearch(queryBuilder, EsGoodsEntity.class,
                    new Sort(categoryGoodsSortOrder), EsIndexConstant.getGoodsIndex());
            esGoodsDtoList = BaseUtil.objToObj(esGoodsEntityList, EsGoodsDTO.class);
        } catch (Exception e) {
            log.error("批量获取根据商品id集合异常", e);
        }
        return esGoodsDtoList;
    }

    @Override
    public List<EsGoodsDTO> queryCategoryByGoodsIds(List<Long> goodsIdList, Long shopId) {
        if (!BaseUtil.judgeList(goodsIdList)) {
            log.warn("商品id集合为空");
            return Lists.newArrayList();
        }
        List<EsGoodsDTO> esGoodsDtoList = Collections.emptyList();
        try {

            EsGoodsCommonQueryBuilderDTO builderDTO = new EsGoodsCommonQueryBuilderDTO();
            builderDTO.setGoodsIdList(goodsIdList);
            builderDTO.setShopId(shopId);
            builderDTO.setGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
            builderDTO.setMerchantGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
            builderDTO.setSalesChannels(SaleChannelsEnum.TS.getStatus());

            BoolQueryBuilder queryBuilder = commonQueryBuilder(builderDTO);
            // 匹配是否过滤推送给商家的商品
            List<EsGoodsEntity> esGoodsEntityList = elasticsearchTemplate.merchantGoodsSearch(queryBuilder, EsGoodsEntity.class,
                    new Sort(categoryGoodsSortOrder), EsIndexConstant.getGoodsIndex());
            esGoodsDtoList = BaseUtil.objToObj(esGoodsEntityList, EsGoodsDTO.class);
        } catch (Exception e) {
            log.error("批量获取根据商品id集合异常", e);
        }
        return esGoodsDtoList;
    }

    /**
     * skuCode集合获取商品
     *
     * @param list 查询参数集合
     * @author: wxf
     * @date: 2020/3/24 13:45
     * @return: {@link List<EsGoodsDTO>}
     **/
    @Override
    public List<EsGoodsDTO> listBySkuCodeList(List<EsSkuCodeAndGoodsIdDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            log.warn("查询参数集合为空");
            return Lists.newArrayList();
        }
        List<EsGoodsDTO> esGoodsIndexDtoList = new ArrayList<>();
        try {
            List<Long> goodsIdList = list.stream().map(EsSkuCodeAndGoodsIdDTO::getGoodsId).collect(Collectors.toList());
            EsGoodsCommonQueryBuilderDTO queryBuilderDto = new EsGoodsCommonQueryBuilderDTO();
            queryBuilderDto.setGoodsIdList(goodsIdList);
            BoolQueryBuilder queryBuilder = commonQueryBuilder(queryBuilderDto);
            List<EsGoodsEntity> esGoodsEntityList = elasticsearchTemplate.search(queryBuilder, EsGoodsEntity.class);
            // 过滤匹配的skuCode
            if (BaseUtil.judgeList(esGoodsEntityList)) {
                Map<String, EsGoodsEntity> goodsEntityMap =
                        esGoodsEntityList.stream().collect(Collectors.toMap(
                                EsGoodsEntity::getGoodsId, Function.identity(),
                                (oldValue, newValue) -> oldValue)
                        );
                list.forEach(
                        i -> {
                            String goodsId = i.getGoodsId().toString();
                            EsGoodsEntity esGoodsEntity = goodsEntityMap.getOrDefault(goodsId, null);
                            if (null != esGoodsEntity) {
                                EsGoodsDTO esGoodsDto =
                                        BaseUtil.objToObj(esGoodsEntity, EsGoodsDTO.class);
                                if (BaseUtil.judgeList(esGoodsEntity.getSkuList())) {
                                    List<EsGoodsSkuDTO> skuList = EsUtil.filterSkuList(esGoodsEntity.getSkuList());
                                    if (BaseUtil.judgeList(skuList)) {
                                        List<EsGoodsSkuDTO> setSkuList =
                                                skuList.stream().filter(p -> p.getSkuCode().equals(i.getSkuCode())).collect(Collectors.toList());
                                        if (BaseUtil.judgeList(setSkuList)) {
                                            EsGoodsSkuDTO skuDto = setSkuList.get(0);
                                            esGoodsDto.setSkuCode(skuDto.getSkuCode());
                                            if (null != skuDto.getMarketPrice()) {
                                                esGoodsDto.setMarketPrice(skuDto.getMarketPrice());
                                            } else {
                                                esGoodsDto.setMarketPrice(BigDecimal.ZERO);
                                            }
                                            esGoodsDto.setSalesPrice(skuDto.getSalesPrice());
                                            esGoodsDto.setEnterprisePrice(skuDto.getEnterprisePrice());
                                        }
                                        esGoodsDto.setSkuList(setSkuList);
                                        esGoodsIndexDtoList.add(esGoodsDto);
                                    }
                                }
                            }
                        }
                );
            }
        } catch (Exception e) {
            log.error("批量获取根据商品ID和skuCode获取商品异常", e);
        }
        return esGoodsIndexDtoList;
    }


    /**
     * 获取商户商品根据skuCode
     *
     * @param shopId  商户id
     * @param goodsId 商品id
     * @param skuCode skuCode
     * @author: wxf
     * @date: 2020/3/24 13:46
     * @return: {@link EsGoodsDTO}
     **/
    @Override
    public EsGoodsDTO getBySkuCode(String shopId, String goodsId, String skuCode) {
        if (!(BaseUtil.judgeString(goodsId) && BaseUtil.judgeString(skuCode))) {
            log.warn("商品ID或者skuCode为空");
            return null;
        }
        EsGoodsDTO esGoodsDto = null;
        try {
            EsGoodsCommonQueryBuilderDTO queryBuilderDto = new EsGoodsCommonQueryBuilderDTO();
            queryBuilderDto.setShopId(Long.valueOf(shopId));
            queryBuilderDto.setGoodsId(Long.valueOf(goodsId));
            queryBuilderDto.setSkuCode(skuCode);
            BoolQueryBuilder queryBuilder = commonQueryBuilder(queryBuilderDto);
            List<EsGoodsEntity> esGoodsEntityList = elasticsearchTemplate.search(queryBuilder, EsGoodsEntity.class);
            if (BaseUtil.judgeList(esGoodsEntityList)) {
                EsGoodsEntity esGoodsEntity = esGoodsEntityList.get(0);
                esGoodsDto = BaseUtil.objToObj(esGoodsEntity, EsGoodsDTO.class);

                EsGoodsSkuDTO skuDTO = esGoodsEntity.getSkuList().stream().filter(i -> skuCode.equals(i.getSkuCode())).findFirst().get();
                esGoodsDto.setMarketPrice(skuDTO.getMarketPrice());
                esGoodsDto.setSalesPrice(skuDTO.getSalesPrice());
                esGoodsDto.setEnterprisePrice(skuDTO.getEnterprisePrice());
                esGoodsDto.setDiscountLimit(skuDTO.getDiscountLimit());
                esGoodsDto.setSkuCode(skuCode);
                esGoodsDto.setSalesChannels(skuDTO.getSalesChannels());
                esGoodsDto.setLowestBuy(skuDTO.getLowestBuy());
                esGoodsDto.setMinQuantity(skuDTO.getMinQuantity());
                esGoodsDto.setSpecialId(skuDTO.getSpecialId());
                esGoodsDto.setHighestBuy(skuDTO.getHighestBuy());
                esGoodsDto.setPropertyValue(skuDTO.getPropertyValue());
                esGoodsDto.setPackPrice(skuDTO.getPackPrice());
            }
        } catch (Exception e) {
            log.error("根据商品ID和skuCode获取商品异常", e);
        }
        return esGoodsDto;
    }


    private List<EsGoodsDTO> calculateGoods(List<EsGoodsDTO> esGoodsEntities, Boolean isCompanyUser) {
        esGoodsEntities.forEach(goods -> {

            List<EsGoodsSkuDTO> skuDTOList = goods.getSkuList().stream().filter(sku -> BaseUtil.isSupportWx(sku.getSalesChannels())).collect(Collectors.toList());
            goods.setSkuList(skuDTOList);

            skuDTOList.forEach(sku -> {
                BigDecimal marketPrice = sku.getMarketPrice();
                if (isCompanyUser) {
                    BigDecimal enterprisePrice = sku.getEnterprisePrice();
                    if (!BaseUtil.isNullOrNegativeOne(enterprisePrice)) {
                        sku.setSalesPrice(enterprisePrice);
                    } else {
                        sku.setSalesPrice(marketPrice);
                    }
                    sku.setDiscountLimit(BaseUtil.NULL_FLAG.intValue());
                } else {
                    BigDecimal salesPrice = sku.getSalesPrice();
                    if (!BaseUtil.isNullOrNegativeOne(salesPrice)) {
                        sku.setSalesPrice(salesPrice);
                    } else {
                        sku.setSalesPrice(marketPrice);
                    }
                }
            });

            EsGoodsSkuDTO skuDTO = skuDTOList.stream().min(Comparator.comparing(EsGoodsSkuDTO::getSalesPrice)).orElse(null);
            if (skuDTO != null) {
                goods.setMarketPrice(skuDTO.getMarketPrice());
                goods.setSalesPrice(skuDTO.getSalesPrice());
                goods.setEnterprisePrice(skuDTO.getEnterprisePrice());
                if (isCompanyUser) {
                    goods.setDiscountLimit(BaseUtil.NULL_FLAG.intValue());
                } else {
                    goods.setDiscountLimit(skuDTO.getDiscountLimit());
                    goods.setSpecialNumber(skuDTO.getSpecialNumber());
                }
            }
        });
        return esGoodsEntities.stream().filter(goods -> CollectionUtils.isNotEmpty(goods.getSkuList())).collect(Collectors.toList());
    }

    /**
     * 微信首页搜索
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/4/1 14:11
     * @return: {@link List<EsGoodsDTO>}
     **/
    @Override
    public PageData<EsGoodsDTO> wxIndexSearch(EsWxIndexSearchQueryDTO dto) {
        EsGoodsCommonQueryBuilderDTO queryBuilderDto = new EsGoodsCommonQueryBuilderDTO();
        queryBuilderDto.setCityCode(dto.getCityCode());
        queryBuilderDto.setMerchantGoodsFlag(MerchantGoodsFlagEnum.PUSH_GOODS.getFlag());
        if (BaseUtil.judgeString(dto.getName())) {
            queryBuilderDto.setName(dto.getName());
        }
        BoolQueryBuilder queryBuilder = commonQueryBuilder(queryBuilderDto);
        PageData<EsGoodsDTO> pageDataDto = new PageData<>();
        try {
            GeoLimitQueryDTO geoLimitQueryDto = EsUtil.getGeoLimitQueryDTO(queryBuilder, dto.getLat(), dto.getLng(), dto.getPageNo(), dto.getPageSize());
            pageDataDto = geoLimit(geoLimitQueryDto);
        } catch (Exception e) {
            log.error("微信首页搜索异常", e);
        }
        return pageDataDto;
    }

    /**
     * 分页查询商品商家数据
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/20 14:28
     * @return: {@link  PageData<  EsGoodsListDTO >}
     * @version 1.0.1
     **/
    @Override
    public PageData<EsGoodsDTO> limitByGoodsId(EsGoodsMerchantListQueryDTO dto) {
        EsGoodsCommonQueryBuilderDTO queryBuilderDto = new EsGoodsCommonQueryBuilderDTO();
        queryBuilderDto.setCityCode(dto.getCityCode());
        queryBuilderDto.setGoodsId(Long.valueOf(dto.getGoodsId()));

        queryBuilderDto.setGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
        queryBuilderDto.setMerchantGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
        BoolQueryBuilder queryBuilder = commonQueryBuilder(queryBuilderDto);
        PageData<EsGoodsDTO> pageDataDto = new PageData<>();
        try {
            GeoLimitQueryDTO geoLimitQueryDto = EsUtil.getGeoLimitQueryDTO(queryBuilder, dto.getLat(), dto.getLng(), dto.getPageNo(), dto.getPageSize());
            pageDataDto = geoLimit(geoLimitQueryDto);
        } catch (Exception e) {
            log.error("分页查询商品商家数据异常", e);
        }
        return pageDataDto;
    }

    /**
     * 商品对应的推送商家
     *
     * @param goodsIdList 商品id集合
     * @author: wxf
     * @date: 2020/5/29 13:48
     * @return: {@link Map<String,List<EsGoodsDTO>>}
     * @version 1.0.1
     **/
    @Override
    public Map<String, List<EsGoodsDTO>> goodsRelationMerchant(List<String> goodsIdList, String cityCode) {
        if (!BaseUtil.judgeList(goodsIdList)) {
            log.warn("商品id集合为空");
            return null;
        }
        Map<String, List<EsGoodsDTO>> goodsMap = null;
        try {
            EsGoodsCommonQueryBuilderDTO queryBuilderDto = new EsGoodsCommonQueryBuilderDTO();
            List<Long> esGoodsIdList = goodsIdList.stream().map(Long::valueOf).collect(Collectors.toList());
            queryBuilderDto.setGoodsIdList(esGoodsIdList);
            queryBuilderDto.setMerchantGoodsFlag(MerchantGoodsFlagEnum.PUSH_GOODS.getFlag());
            queryBuilderDto.setMerchantGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
            if (null != cityCode) {
                queryBuilderDto.setCityCode(cityCode);
            }
            BoolQueryBuilder queryBuilder = commonQueryBuilder(queryBuilderDto);
            List<EsGoodsEntity> esGoodsEntityList = elasticsearchTemplate.search(queryBuilder, EsGoodsEntity.class);
            if (BaseUtil.judgeList(esGoodsEntityList)) {
                List<EsGoodsDTO> esGoodsDtoList = BaseUtil.objToObj(esGoodsEntityList, EsGoodsDTO.class);
                goodsMap = esGoodsDtoList.stream().collect(Collectors.groupingBy(EsGoodsDTO::getGoodsId));
            }
        } catch (Exception e) {
            log.error("商品对应的推送商家数量异常", e);
        }
        return goodsMap;
    }

    /**
     * 有商品的商户集合
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/29 15:09
     * @return: {@link List<EsGoodsDTO>}
     * @version 1.0.1
     **/
    @Override
    public List<EsGoodsDTO> merchantHaveGoodsList(EsMerchantListParamDTO dto) {
        List<EsGoodsDTO> esGoodsDtoList = Collections.emptyList();
        try {
            EsGoodsCommonQueryBuilderDTO queryBuilderDto = new EsGoodsCommonQueryBuilderDTO();
            queryBuilderDto.setCityCode(dto.getCityCode());
            BoolQueryBuilder queryBuilder = commonQueryBuilder(queryBuilderDto);
            EsGeoSearchDTO<EsGoodsEntity> geo = new EsGeoSearchDTO<>();
            EsUtil.setGeo(dto.getLat(), dto.getLng(), dto.getPageNo(), dto.getPageSize(), geo,
                    EsGoodsEntity.class, queryBuilder, DistanceUnit.KILOMETERS, EsIndexConstant.getGoodsIndex2());
            GeoLimitQueryDTO geoLimitQueryDto = EsUtil.getGeoLimitQueryDTO(queryBuilder, dto.getLat(), dto.getLng(), dto.getPageNo(), dto.getPageSize());
            PageData<EsGoodsDTO> pageDataDto = geoLimit(geoLimitQueryDto);
            if (BaseUtil.judgeList(pageDataDto.getList())) {
                esGoodsDtoList = pageDataDto.getList();
            }
        } catch (Exception e) {
            log.error("有商品的商户集合查询异常", e);
        }
        return esGoodsDtoList;
    }

    /**
     * 批量获取根据商户id集合
     *
     * @param shopId 商户id
     * @author: wxf
     * @date: 2020/6/5 10:27
     * @return: {@link List<EsGoodsDTO>}
     * @version 1.0.1
     **/
    @Override
    public List<EsGoodsDTO> listByShopId(Long shopId) {
        if (null == shopId) {
            return Lists.newArrayList();
        }
        List<EsGoodsDTO> esGoodsDtoList = Collections.emptyList();
        EsGoodsCommonQueryBuilderDTO queryBuilderDto = new EsGoodsCommonQueryBuilderDTO();
        queryBuilderDto.setShopId(shopId);
        BoolQueryBuilder queryBuilder = commonQueryBuilder(queryBuilderDto);
        try {
            List<EsGoodsEntity> esGoodsEntityList = elasticsearchTemplate.search(queryBuilder, EsGoodsEntity.class);
            if (BaseUtil.judgeList(esGoodsEntityList)) {
                esGoodsDtoList = BaseUtil.objToObj(esGoodsEntityList, EsGoodsDTO.class);
            }
        } catch (Exception e) {
            log.error("批量获取根据商户id异常", e);
        }
        return esGoodsDtoList;
    }

    @Override
    public List<EsGoodsDTO> listUpperByShopId(Long shopId, Boolean isCompanyUser) {
        if (null == shopId) {
            return Lists.newArrayList();
        }
        List<EsGoodsDTO> esGoodsDtoList = Collections.emptyList();
        EsGoodsCommonQueryBuilderDTO queryBuilderDto = new EsGoodsCommonQueryBuilderDTO();
        queryBuilderDto.setShopId(shopId);
        queryBuilderDto.setMerchantGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
        queryBuilderDto.setGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
        queryBuilderDto.setSalesChannels(SaleChannelsEnum.TS.getStatus());
        BoolQueryBuilder queryBuilder = commonQueryBuilder(queryBuilderDto);
        try {
            Sort.Order categoryGoodsSort = new Sort.Order(SortOrder.ASC, "categoryGoodsSort");
            Sort sort = new Sort(categoryGoodsSort, categoryGoodsSortOrder);
            List<EsGoodsEntity> esGoodsEntityList = elasticsearchTemplate.merchantGoodsSearch(queryBuilder, EsGoodsEntity.class,
                    sort, EsIndexConstant.getGoodsIndex());
            if (BaseUtil.judgeList(esGoodsEntityList)) {
                esGoodsDtoList = BaseUtil.objToObj(esGoodsEntityList, EsGoodsDTO.class);
                esGoodsDtoList = calculateGoods(esGoodsDtoList, isCompanyUser);
            }
        } catch (Exception e) {
            log.error("批量获取根据商户id异常", e);
        }
        return esGoodsDtoList;
    }

    /**
     * 批量获取根据商品名称和商户id
     *
     * @param goodsName 商品名称
     * @param shopId    商户id
     * @param size      条数
     * @author: wxf
     * @date: 2020/6/8 15:00
     * @return: {@link List<EsGoodsDTO>}
     * @version 1.1.0
     **/
    @Override
    public List<EsGoodsDTO> listByGoodsNameAndMerchantId(String goodsName, String shopId, int size, Boolean isCompanyUser) {
        List<EsGoodsDTO> esGoodsDtoList = Collections.emptyList();
        try {
            EsGoodsCommonQueryBuilderDTO queryBuilderDto = new EsGoodsCommonQueryBuilderDTO();
            queryBuilderDto.setShouldGoodsName(goodsName);
            queryBuilderDto.setShopId(Long.valueOf(shopId));
            //过滤不支持小程序的商品
            queryBuilderDto.setSalesChannels(SaleChannelsEnum.TS.getStatus());
            queryBuilderDto.setGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
            queryBuilderDto.setMerchantGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
            PageData<EsGoodsEntity> pageDataEntity = elasticsearchTemplate.search(commonQueryBuilder(queryBuilderDto), new PageSortHighLight(1, size),
                    EsGoodsEntity.class, EsIndexConstant.GOODS_INDEX);
            if (BaseUtil.judgeList(pageDataEntity.getList())) {
                esGoodsDtoList = BaseUtil.objToObj(pageDataEntity.getList(), EsGoodsDTO.class);
                esGoodsDtoList = calculateGoods(esGoodsDtoList, isCompanyUser);
            }
        } catch (Exception e) {
            log.error("批量获取根据商品名称和商户id异常", e);
        }
        return esGoodsDtoList;
    }

    /**
     * 获取根据商品id和商户id
     *
     * @param goodsId    商品id
     * @param merchantId 商户id
     * @author: wxf
     * @date: 2020/6/9 11:40
     * @return: {@link EsGoodsDTO}
     * @version 1.1.0
     **/
    @Override
    public EsGoodsDTO getByGoodsIdAndMerchantId(Long goodsId, Long merchantId) {
        EsGoodsDTO esGoodsDto = null;
        try {
            EsGoodsCommonQueryBuilderDTO queryBuilderDto = new EsGoodsCommonQueryBuilderDTO();
            queryBuilderDto.setGoodsId(goodsId);
            queryBuilderDto.setMerchantId(merchantId);
            List<EsGoodsEntity> esGoodsEntityList = elasticsearchTemplate.search(commonQueryBuilder(queryBuilderDto), EsGoodsEntity.class);
            if (BaseUtil.judgeList(esGoodsEntityList)) {
                esGoodsDto = BaseUtil.objToObj(esGoodsEntityList.get(0), EsGoodsDTO.class);
            }
        } catch (Exception e) {
            log.error("获取根据商品id和商户id异常", e);
        }
        return esGoodsDto;
    }

    /**
     * 批量获取根据首页分类id
     *
     * @param wxIndexCategoryId 首页分类id
     * @author: wxf
     * @date: 2020/5/20 14:05
     * @return: {@link List<EsGoodsDTO>}
     * @version 1.0.1
     **/
    @Override
    public List<EsGoodsDTO> listByWxIndexCategoryId(String wxIndexCategoryId) {
        if (!BaseUtil.judgeString(wxIndexCategoryId)) {
            log.warn("首页分类id为空");
            return Lists.newArrayList();
        }
        List<EsGoodsDTO> esGoodsDtoList = Collections.emptyList();
        try {
            EsGoodsCommonQueryBuilderDTO queryBuilderDto = new EsGoodsCommonQueryBuilderDTO();
            queryBuilderDto.setMerchantGoodsFlag(MerchantGoodsFlagEnum.NO_PUSH_GOODS.getFlag());
            queryBuilderDto.setWxIndexCategoryId(wxIndexCategoryId);
            BoolQueryBuilder queryBuilder = commonQueryBuilder(queryBuilderDto);
            List<EsGoodsEntity> esGoodsEntityList = elasticsearchTemplate.search(queryBuilder, EsGoodsEntity.class);
            if (BaseUtil.judgeList(esGoodsEntityList)) {
                esGoodsDtoList = BaseUtil.objToObj(esGoodsEntityList, EsGoodsDTO.class);
            }
        } catch (Exception e) {
            log.error("批量获取根据首页分类id异常", e);
        }
        return esGoodsDtoList;
    }


    /**
     * 描述：批量删除推送到门店的商品
     *
     * @param shops
     * @return {@link }
     * @author lhm
     * @date 2020/7/11
     * @version 1.2.0
     **/
    @Override
    public void deleteGoodsToShop(List<Long> shops, boolean flag) {
        if (BaseUtil.judgeList(shops)) {
            try {
                BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                queryBuilder.must(QueryBuilders.termsQuery(CommonConstant.SHOP_ID, shops));
                if (flag) {
                    queryBuilder.must(QueryBuilders.termQuery("goodsAddType", GoodsAddTypeEnum.MERCHANT.getStatus()));
                }
                elasticsearchTemplate.deleteByCondition(queryBuilder, EsGoodsEntity.class);
            } catch (Exception e) {
                log.error("删除门店商品异常", e);
            }
        }
    }

    /**
     * 描述：批量删除推送到门店的商品
     *
     * @param shops
     * @return {@link }
     * @author lhm
     * @date 2020/7/11
     * @version 1.2.0
     **/
    @Override
    public void deleteSkuToShop(List<Long> shops, List<String> removeSkuCode) {
        if (!CollectionUtils.isEmpty(shops)) {
            try {
                BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                queryBuilder.must(QueryBuilders.termsQuery(CommonConstant.SHOP_ID, shops));
                if (!CollectionUtils.isEmpty(removeSkuCode)) {
                    queryBuilder.must(QueryBuilders.termsQuery("skuCode", removeSkuCode));
                }
                elasticsearchTemplate.deleteByCondition(queryBuilder, EsGoodsEntity.class);
            } catch (Exception e) {
                log.error("删除门店商品异常", e);
            }
        }
    }

    @Override
    public void goodsExtUpdate(GoodsExtToEsDTO dto) {
        String shopId = dto.getShopId();
        String merchantId = dto.getMerchantId();
        // 是否是门店修改
        boolean isShop = !StringUtils.isEmpty(shopId);
        // 是否事商户PC修改
        boolean isMerchant = !StringUtils.isEmpty(merchantId);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery(GOODS_ID, dto.getGoodsId()));
        if (isShop) {
            queryBuilder.must(QueryBuilders.termQuery(CommonConstant.SHOP_ID, shopId));
        }
        if (isMerchant) {
            queryBuilder.must(QueryBuilders.termQuery(CommonConstant.MERCHANT_ID, merchantId));
        }
        try {
            List<EsGoodsEntity> list = elasticsearchTemplate.search(queryBuilder, EsGoodsEntity.class);
            if (CollectionUtils.isNotEmpty(list)) {
                // 标签数据组装
                List<Map<String, Object>> labelList = dto.getGoodsLabelList();
                List<EsGoodsCategoryAndLabelDTO> collect = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(labelList)) {
                    collect = dto.getGoodsLabelList().stream().map(e -> {
                        EsGoodsCategoryAndLabelDTO labelDto = new EsGoodsCategoryAndLabelDTO();
                        labelDto.setId(Long.valueOf(e.get("id").toString()));
                        labelDto.setName(e.get("name").toString());
                        labelDto.setType(CategoryLabelTypeEnum.LABEL.getStatus());
                        return labelDto;
                    }).collect(Collectors.toList());
                }
                List<GoodsExtToEsSkuDTO> goodsSkuList = dto.getGoodsSkuList();
                Map<String, GoodsExtToEsSkuDTO> esSkuMap = Maps.newHashMap();
                if (CollectionUtils.isNotEmpty(goodsSkuList)) {
                    esSkuMap = goodsSkuList.stream().collect(Collectors.toMap(GoodsExtToEsSkuDTO::getSkuCode, Function.identity()));
                }
                // V1.4.0 根据goodsId查询出来的特价商品信息
                Map<Long, Map<String, MarketingSpecialSku>> shopSpecialSkuMap = dto.getShopSpecialSkuMap();
                for (EsGoodsEntity entity : list) {
                    // 门店商品更新
                    if (isShop) {
                        if (MapUtils.isNotEmpty(esSkuMap)) {
                            Map<String, MarketingSpecialSku> specialSkuMap = null;
                            if (null != shopSpecialSkuMap && shopSpecialSkuMap.containsKey(Long.valueOf(entity.getShopId()))) {
                                specialSkuMap = shopSpecialSkuMap.get(Long.valueOf(entity.getShopId()));
                            }
                            // 是否是特价商品（需要重新判断，特殊情况-设置了特价商品信息的商品SKU全部被删除了）
                            AtomicReference<Boolean> atomicSpecialState = new AtomicReference<>(Boolean.FALSE);
                            for (EsGoodsSkuDTO skuDTO : entity.getSkuList()) {
                                GoodsExtToEsSkuDTO esSkuDTO = esSkuMap.get(skuDTO.getSkuCode());
                                skuDTO.setMarketPrice(esSkuDTO.getMarketPrice());
                                skuDTO.setSalesPrice(esSkuDTO.getSalesPrice());
                                skuDTO.setEnterprisePrice(esSkuDTO.getEnterprisePrice());
                                skuDTO.setStock(esSkuDTO.getRemainStock());

                                // V1.4.0 设置商品折扣信息
                                if (null != entity.getSpecialState() && entity.getSpecialState()) {
                                    Boolean returnBoolean = setGoodsSpecialInfo(skuDTO, specialSkuMap);
                                    if (Boolean.FALSE.equals(atomicSpecialState.get())) {
                                        atomicSpecialState.set(returnBoolean);
                                    }
                                }

                            }
                            entity.setSpecialState(atomicSpecialState.get());
                        }
                        if (dto.getGoodsStatus() != null) {
                            entity.setGoodsStatus(dto.getGoodsStatus());
                        }
                    } else if (isMerchant) {
                        // 处理商户上下架
                        entity.setMerchantGoodsStatus(dto.getMerchantGoodsStatus());

                    } else {
                        // 平台商品更新
                        entity.setLabelList(collect);
                        entity.setInfoPicture(dto.getInfoPicture());
                        entity.setGoodsDescribeText(dto.getGoodsDescribeText());
                        entity.setGoodsSynopsis(dto.getGoodsSynopsis());
                    }
                }
                elasticsearchTemplate.updateRequest(list);
            }
        } catch (Exception e) {
            log.error("更新商品ES信息失败.", e);
        }
    }

    private static Boolean setGoodsSpecialInfo(EsGoodsSkuDTO sku, Map<String, MarketingSpecialSku> shopSpecialSkuMap) {
        if (null != shopSpecialSkuMap && shopSpecialSkuMap.containsKey(sku.getSkuCode())) {
            MarketingSpecialSku marketingSpecialSku = shopSpecialSkuMap.get(sku.getSkuCode());
            if (MarketingEsSpecialFixTypeEnum.FIXED.getStatus().equals(marketingSpecialSku.getFixType())) {
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

    @Override
    public void delGoods(Long goodsId) {
        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.must(QueryBuilders.termQuery(GOODS_ID, goodsId));
            elasticsearchTemplate.deleteByCondition(queryBuilder, EsGoodsEntity.class);
        } catch (Exception e) {
            log.error("平台删除商品同步Es信息失败，异常。", e);
        }
    }

    @Override
    public void cancelAuthGoods(Long goodsId, Long merchantId) {
        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.must(QueryBuilders.termQuery(GOODS_ID, goodsId));
            if (merchantId != null) {
                queryBuilder.must(QueryBuilders.termQuery(CommonConstant.MERCHANT_ID, merchantId));
            }
            elasticsearchTemplate.deleteByCondition(queryBuilder, EsGoodsEntity.class);
        } catch (Exception e) {
            log.error("平台取消授权商品同步Es信息失败，异常。", e);
        }
    }

    @Override
    public EsGoodsDTO getByGoodsIdAndShopIdId(Long goodsId, Long shopId) {
        EsGoodsDTO esGoodsDto = null;
        try {
            EsGoodsCommonQueryBuilderDTO queryBuilderDto = new EsGoodsCommonQueryBuilderDTO();
            queryBuilderDto.setGoodsId(goodsId);
            queryBuilderDto.setShopId(shopId);
            List<EsGoodsEntity> esGoodsEntityList = elasticsearchTemplate.search(commonQueryBuilder(queryBuilderDto), EsGoodsEntity.class);
            if (BaseUtil.judgeList(esGoodsEntityList)) {
                esGoodsDto = BaseUtil.objToObj(esGoodsEntityList.get(0), EsGoodsDTO.class);
            }
        } catch (Exception e) {
            log.error("获取根据商品id和商户id异常。", e);
        }
        return esGoodsDto;


    }

    @Override
    public void goodsCategoryUpdate(MerchantGoodsToEsDTO dto) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("categoryId", dto.getCategoryId()))
                .should(QueryBuilders.termQuery("shopId", dto.getMerchantId())))
                .should(QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("categoryId", dto.getCategoryId()))
                        .should(QueryBuilders.termQuery("merchantId", dto.getMerchantId())));


        try {
            List<EsGoodsEntity> list = elasticsearchTemplate.search(queryBuilder, EsGoodsEntity.class);

            //这一步非常关键 list 转换成 newList 去修改分类,使用list会导致价格覆盖
            List<EsGoodsEntity> newList = list.stream().map(entity -> BaseUtil.objToObj(entity, EsGoodsEntity.class)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(newList)) {
                // 默认分类id
                String defaultCategoryId = dto.getDefaultCategoryId();
                newList.forEach(e -> {
                    if (!StringUtils.isEmpty(defaultCategoryId)) {
                        e.setCategoryId(defaultCategoryId);
                        e.setCategoryName(dto.getDefaultCategoryName());
                        if (BaseUtil.judgeList(dto.getSorts())) {
                            dto.getSorts().stream().filter(k -> k.getGoodsId().toString().equals(e.getGoodsId())
                                    && k.getShopId().toString().equals(e.getShopId())).forEach(i -> {
                                log.info("门店商品对应的排序号为：{}", i.getSort());
                                e.setCategoryGoodsSort(i.getSort());
                            });
                        }
                    } else {
                        e.setCategoryName(dto.getCategoryName());
                    }
                });
                elasticsearchTemplate.updateRequest(newList);
            }
        } catch (Exception e) {
            log.error("同步ES商户商品分类失败");
        }
    }

    @Override
    public void goodsPresellTaskSyncEs() {
        Long milliSecond = LocalDateTime.now().plusDays(-1L).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("presellFlag", Boolean.TRUE))
                .must(QueryBuilders.rangeQuery(END_SELL_TIME).lt(milliSecond));
        try {
            List<EsGoodsEntity> list = elasticsearchTemplate.search(queryBuilder, EsGoodsEntity.class);
            log.debug("处理ES商户预售商品数据获取ES数据list={}", list);
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(esGoodsEntity -> {
                    if (esGoodsEntity.getGoodsAddType().equals(GoodsAddTypeEnum.MERCHANT.getStatus())) {
                        esGoodsEntity.setMerchantGoodsStatus(GoodsStatusEnum.LOWER_SHELF.getStatus());
                    }
                    esGoodsEntity.setGoodsStatus(GoodsStatusEnum.LOWER_SHELF.getStatus());
                });
                elasticsearchTemplate.updateRequest(list);
            }
        } catch (Exception e) {
            log.error("处理ES商户预售商品下架定时任务失败");
        }


    }

    @Override
    public void delGoodsByShopId(Long shopId) {
        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.must(QueryBuilders.termQuery(CommonConstant.SHOP_ID, shopId));
            elasticsearchTemplate.deleteByCondition(queryBuilder, EsGoodsEntity.class);
        } catch (Exception e) {
            log.error("店铺删除同步删除商品信息失败", e);
        }
    }

    /**
     * 经纬度排序分页
     *
     * @param dto 分页查询参数
     * @author: wxf
     * @date: 2020/6/2 16:08
     * @return: {@link PageData< EsGoodsDTO>}
     * @version 1.0.1
     **/
    private PageData<EsGoodsDTO> geoLimit(GeoLimitQueryDTO dto) {
        PageData<EsGoodsDTO> pageDataDto = new PageData<>();
        try {
            EsGeoSearchDTO<EsGoodsEntity> geo = new EsGeoSearchDTO<>();
            EsUtil.setGeo(dto.getLat(), dto.getLng(), dto.getPageNo(), dto.getPageSize(), geo,
                    EsGoodsEntity.class, dto.getQueryBuilder(), DistanceUnit.KILOMETERS, EsIndexConstant.getGoodsIndex());
            PageData<EsGoodsEntity> pageDataEntity = elasticsearchTemplate.locationSearch(geo);
            if (BaseUtil.judgeList(pageDataEntity.getList())) {
                pageDataDto.setTotal(pageDataEntity.getTotal());
                pageDataDto.setList(BaseUtil.objToObj(pageDataEntity.getList(), EsGoodsDTO.class));
                pageDataDto.setLastPage(EsUtil.lastPages(pageDataEntity.getTotal(), dto.getPageSize().intValue(), dto.getPageNo().intValue()));
            }
        } catch (Exception e) {
            log.error("分页查询商品商家数据异常", e);
        }
        return pageDataDto;
    }


    /**
     * 通用查询条件
     *
     * @param dto 字段参数
     * @author: wxf
     * @date: 2020/6/2 9:50
     * @return: {@link BoolQueryBuilder}
     * @version 1.0.1
     **/
    private BoolQueryBuilder commonQueryBuilder(EsGoodsCommonQueryBuilderDTO dto) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 匹配城市编码
        if (!StringUtils.isEmpty(dto.getCityCode())) {
            queryBuilder.must(QueryBuilders.termQuery("esCityCode", dto.getCityCode()));
        }
        // 匹配商品id
        if (dto.getGoodsId() != null) {
            queryBuilder.must(QueryBuilders.termQuery(GOODS_ID, dto.getGoodsId().toString()));
        }
        // 匹配是否是推送给商户的商品
        if (dto.getMerchantGoodsFlag() != null) {
            queryBuilder.must(QueryBuilders.termQuery("merchantGoodsFlag", dto.getMerchantGoodsFlag()));
        }
        // 商品id集合匹配
        if (BaseUtil.judgeList(dto.getGoodsIdList())) {
            queryBuilder.must(QueryBuilders.termsQuery(GOODS_ID, dto.getGoodsIdList()));
        }
        // 匹配商户id
        if (null != dto.getMerchantId()) {
            queryBuilder.must(QueryBuilders.termQuery(CommonConstant.MERCHANT_ID, dto.getMerchantId().toString()));
        }
        if (null != dto.getShopId()) {
            queryBuilder.must(QueryBuilders.termQuery(CommonConstant.SHOP_ID, dto.getShopId().toString()));
        }
        // 匹配商户id集合
        if (BaseUtil.judgeList(dto.getMerchantIdList())) {
            List<String> merchantIdList = dto.getMerchantIdList().stream().map(Object::toString).collect(Collectors.toList());
            queryBuilder.must(QueryBuilders.termsQuery(CommonConstant.MERCHANT_ID, merchantIdList));
        }
        // 匹配分类id
        if (null != dto.getCategoryOrMenuId()) {
            queryBuilder.must(QueryBuilders.termQuery("categoryId", dto.getCategoryOrMenuId().toString()));
        }
        // 平台上下架状态匹配
        if (null != dto.getGoodsStatus()) {
            queryBuilder.must(QueryBuilders.termQuery("goodsStatus", dto.getGoodsStatus()));
        }
        // 商户上下架状态匹配
        if (null != dto.getMerchantGoodsStatus()) {
            queryBuilder.must(QueryBuilders.termQuery("merchantGoodsStatus", dto.getMerchantGoodsStatus()));
        }
        // 首页搜索 关键字 匹配
        if (BaseUtil.judgeString(dto.getName())) {
            String value = "*" + dto.getName() + "*";
            queryBuilder.should(QueryBuilders.wildcardQuery("merchantName", value));
            queryBuilder.should(QueryBuilders.wildcardQuery("goodsName", value));
        }
        // skuCode 匹配 nestedQuery 嵌套查询
        if (BaseUtil.judgeString(dto.getSkuCode())) {
            GoodsHelper.skuCode(queryBuilder, dto.getSkuCode());
        }
        if (dto.getSalesChannels() != null) {
            GoodsHelper.salesChannels(queryBuilder);
        }
        // 匹配 首页分类 id 对应的 嵌套 集合数据
        if (BaseUtil.judgeString(dto.getWxIndexCategoryId())) {
            EsUtil.setNestedQuery(queryBuilder, "indexCategoryGoodsList",
                    "indexCategoryGoodsList.indexCategoryId.keyword", dto.getWxIndexCategoryId());
        }
        // 或者查询 商品名称
        if (BaseUtil.judgeString(dto.getShouldGoodsName())) {
            String value = "*" + dto.getShouldGoodsName() + "*";
            queryBuilder.should(QueryBuilders.wildcardQuery("goodsName", value));
        }
        sellStatusQueryBuilder(dto, queryBuilder);
        return queryBuilder;
    }

    private void sellStatusQueryBuilder(EsGoodsCommonQueryBuilderDTO dto, BoolQueryBuilder queryBuilder) {
        // 售卖状态：1-进行中；2-未开始；3-已结束
        if (dto.getSellStatus() != null) {
            Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            MarketingStatusEnum statusEnum = MarketingStatusEnum.parse(dto.getSellStatus());
            switch (statusEnum) {
                case ONGOING:
                    BoolQueryBuilder ongoing = QueryBuilders.boolQuery();
                    BoolQueryBuilder between = QueryBuilders.boolQuery();
                    between.must(QueryBuilders.rangeQuery(START_SELL_TIME).lte(milliSecond))
                            .must(QueryBuilders.rangeQuery(END_SELL_TIME).gt(milliSecond));
                    BoolQueryBuilder start = QueryBuilders.boolQuery();
                    start.must(QueryBuilders.rangeQuery(START_SELL_TIME).lte(milliSecond))
                            .mustNot(QueryBuilders.existsQuery(END_SELL_TIME));
                    BoolQueryBuilder end = QueryBuilders.boolQuery();
                    end.mustNot(QueryBuilders.existsQuery(START_SELL_TIME))
                            .must(QueryBuilders.rangeQuery(END_SELL_TIME).gt(milliSecond));
                    BoolQueryBuilder nullTime = QueryBuilders.boolQuery();
                    nullTime.mustNot(QueryBuilders.existsQuery(START_SELL_TIME))
                            .mustNot(QueryBuilders.existsQuery(END_SELL_TIME));
                    ongoing.should(between).should(start).should(end).should(nullTime);
                    queryBuilder.must(ongoing);
                    break;
                case NOT_START:
                    queryBuilder.must(QueryBuilders.rangeQuery(START_SELL_TIME).gt(milliSecond));
                    break;
                case ENDED:
                    queryBuilder.must(QueryBuilders.rangeQuery(END_SELL_TIME).lte(milliSecond));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public List<String> queryDiscountShop(DiscountQuery query) {
        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            if (CollectionUtils.isNotEmpty(query.getAppointShopIds())) {
                queryBuilder.must(QueryBuilders.termsQuery("shopId", query.getAppointShopIds()));
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(query.getCityCode())) {
                queryBuilder.must(QueryBuilders.termQuery("esCityCode", query.getCityCode()));
            }
            //特价标识
            GoodsHelper.specialState(queryBuilder);
            //折扣过滤
            GoodsHelper.special0To10(queryBuilder);

            Attach attach = new Attach("shopId");


            List<EsGoodsEntity> search = elasticsearchTemplate.search(queryBuilder, EsGoodsEntity.class, attach);
            return search.stream().map(EsGoodsEntity::getShopId).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Lists.newArrayList();

    }

    @Override
    public List<EsGoodsEntity> selectListByShopIdAndGoodsIds(Long shopId, Set<Long> goodsIdsSet, Boolean flag) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("shopId", shopId));
        queryBuilder.must(QueryBuilders.termsQuery("goodsId", goodsIdsSet));
        if (!flag) {
            List<Integer> goodsAddTypes = new ArrayList<>();
            goodsAddTypes.add(GoodsAddTypeEnum.PLANTE.getStatus());
            goodsAddTypes.add(GoodsAddTypeEnum.MERCHANT.getStatus());
            queryBuilder.must(QueryBuilders.termsQuery("goodsAddType", goodsAddTypes));
        }
        try {
            return elasticsearchTemplate.search(queryBuilder, EsGoodsEntity.class);
        } catch (Exception e) {
            log.error("根据店铺ID以及商品ID集合查询ES商品数据异常：{}", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public void saveBath(List<EsGoodsEntity> esGoodsList) {
        if (BaseUtil.judgeList(esGoodsList)) {
            try {
                BulkResponse responses = elasticsearchTemplate.save(esGoodsList);
                if (responses.hasFailures()) {
                    log.error("批量修改保存商品特价信息数据失败:{}", responses.buildFailureMessage());
                } else {
                    log.info("批量修改保存商品特价信息数据成功");
                }
            } catch (Exception e) {
                log.error("批量修改保存商品特价信息数据异常：{}", e.getMessage());
            }
        }
    }

    @Override
    public PageData<EsGoodsDTO> queryRecommendList(RecommendDTO dto, List<WxCategoryGoodsDTO> list, Boolean isCompanyUser) {
        try {
            String cityCode = dto.getCityCode();

            Set<Long> ids = list.stream().map(WxCategoryGoodsDTO::getShopGoodsId).collect(Collectors.toSet());
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.must(QueryBuilders.termsQuery(CommonConstant.ID, ids))
                    .must(QueryBuilders.termQuery(GoodsHelper.ES_CITY_CODE, cityCode));
            GoodsHelper.goodsBaseQuery(queryBuilder);

            EsGeoSearchDTO<EsGoodsEntity> searchDTO = new EsGeoSearchDTO<>();
            EsUtil.setGeo(searchDTO, queryBuilder, dto.getLocation(),
                    dto, EsGoodsEntity.class, DistanceUnit.KILOMETERS,
                    EsIndexConstant.getGoodsIndex());

            PageData<EsGoodsEntity> pageData = elasticsearchTemplate.locationSearch33(searchDTO);

            PageData<EsGoodsDTO> pageDto = new PageData<>();
            pageDto.setTotal(pageData.getTotal());
            pageDto.setLastPage(pageData.isLastPage());
            if (BaseUtil.judgeList(pageData.getList())) {
                List<EsGoodsDTO> esGoodsDtoList = BaseUtil.objToObj(pageData.getList(), EsGoodsDTO.class);
                esGoodsDtoList = calculateGoods(esGoodsDtoList, isCompanyUser);
                pageDto.setList(esGoodsDtoList);
            }

            return pageDto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Double queryMinDiscount(Long shopId) {

        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            GoodsHelper.goodsBaseQuery(queryBuilder);
            GoodsHelper.matchShopId(queryBuilder, shopId);
            GoodsHelper.specialState(queryBuilder);
            GoodsHelper.special0To10(queryBuilder);

            String metricName = GoodsHelper.getMetricName();
            return elasticsearchTemplate.nestedAggs(GoodsHelper.SKU_LIST, metricName, AggsType.min, queryBuilder, EsGoodsEntity.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Map<String, Double> queryMinDiscount(Set<String> matchShopIds) {

        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            GoodsHelper.goodsBaseQuery(queryBuilder);
            GoodsHelper.matchShopIds(queryBuilder, matchShopIds);
            GoodsHelper.specialState(queryBuilder);
            GoodsHelper.special0To10(queryBuilder);

            String metricName = GoodsHelper.getMetricName();
            return elasticsearchTemplate.nestedAggs(CommonConstant.SHOP_ID, GoodsHelper.SKU_LIST, metricName, AggsType.min, queryBuilder, EsGoodsEntity.class);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return Maps.newHashMap();
    }
}
