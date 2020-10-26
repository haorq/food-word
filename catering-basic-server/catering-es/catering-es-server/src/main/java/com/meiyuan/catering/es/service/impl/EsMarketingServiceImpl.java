package com.meiyuan.catering.es.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.GoodsAddTypeEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.es.constant.CommonConstant;
import com.meiyuan.catering.es.constant.EsIndexConstant;
import com.meiyuan.catering.es.constant.MarketingHelper;
import com.meiyuan.catering.es.dto.geo.EsGeoSearchDTO;
import com.meiyuan.catering.es.dto.marketing.*;
import com.meiyuan.catering.es.dto.merchant.DiscountQuery;
import com.meiyuan.catering.es.entity.EsMarketingEntity;
import com.meiyuan.catering.es.enums.AggsType;
import com.meiyuan.catering.es.enums.marketing.MarketingMerchantShopTypeEnum;
import com.meiyuan.catering.es.enums.marketing.MarketingStatusEnum;
import com.meiyuan.catering.es.enums.marketing.MarketingTypeEnum;
import com.meiyuan.catering.es.enums.marketing.MarketingUsingObjectEnum;
import com.meiyuan.catering.es.repository.ElasticsearchTemplate;
import com.meiyuan.catering.es.repository.PageSortHighLight;
import com.meiyuan.catering.es.repository.Sort;
import com.meiyuan.catering.es.service.EsMarketingService;
import com.meiyuan.catering.es.util.EsUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/26 15:24
 * @description 简单描述
 **/
@Service
@Slf4j
public class EsMarketingServiceImpl implements EsMarketingService {
    @Resource
    ElasticsearchTemplate<EsMarketingEntity, String> elasticsearchTemplate;

    /**
     * 新增修改数据
     *
     * @param dtoList 新增修改数据集合
     * @author: wxf
     * @date: 2020/3/23 14:04
     **/
    @Override
    public void saveUpdateBatch(List<EsMarketingDTO> dtoList) {
        if (!BaseUtil.judgeList(dtoList)) {
            log.warn("新增/修改数据为空");
        }
        try {
            List<EsMarketingEntity> marketList = BaseUtil.objToObj(dtoList, EsMarketingEntity.class);
            BulkResponse responses = elasticsearchTemplate.save(marketList);
            // Bulk响应提供了一种方法来快速检查一个或多个操作是否失败  hasFailures
            // 有一个操作失败，此方法将返回true 全部成功返回false
            if (responses.hasFailures()) {
                log.info("批量新增/修改活动索引数据失败");
            } else {
                log.info("批量新增/修改活动索引数据成功");
            }
        } catch (Exception e) {
            log.error("批量新增/修改活动索引数据异常:", e);
        }
    }

    /**
     * 获取秒杀/团购 详情
     *
     * @param mGoodsId mGoodsId
     * @author: wxf
     * @date: 2020/3/26 16:53
     * @return: {@link EsMarketingDTO}
     **/
    @Override
    public EsMarketingDTO getBymGoodsId(Long mGoodsId) {
        return getBymGoodsId(mGoodsId, false);
    }

    @Override
    public EsMarketingDTO getBymGoodsId(Long mGoodsId, Boolean isJudgeDel) {
        EsMarketingEntity marketing = null;
        try {
            marketing = elasticsearchTemplate.getById(mGoodsId.toString(), EsMarketingEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == marketing) {
            log.warn("唯一ID对应数据不存在");
            return null;
        }
        if (isJudgeDel && marketing.getDel()) {
            log.warn("活动已逻辑删除");
            return null;
        }

        EsMarketingDTO dto = BaseUtil.objToObj(marketing, EsMarketingDTO.class);
        // 处理活动状态
        handleMarketingStatus(dto);
        dto.setInfoPicture(dto.getGoodsPicture());
        return dto;
    }

    /**
     * 微信首页秒杀/团购
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/3/28 14:52
     * @return: {@link List<   EsMarketingListDTO  >}
     **/
    @Override
    public List<EsMarketingListDTO> wxIndexMarketing(EsWxIndexMarketingQueryDTO dto) {
        List<EsMarketingListDTO> dtoList = new ArrayList<>();
        List<EsMarketingEntity> marketingList = Collections.emptyList();
        BoolQueryBuilder queryBuilder = marketingPublicQueryBuilder(BaseUtil.objToObj(dto, EsMarketingListParamDTO.class));
        // 过滤进行中的活动
        MarketingHelper.ongoing(queryBuilder);
        // 匹配场次
        MarketingHelper.skillEvent(queryBuilder, dto.getEventId());
        // 匹配商品上下架
        MarketingHelper.goodsUpDownState(queryBuilder);
        // 匹配商户状态
        MarketingHelper.merchantState(queryBuilder);
        // 匹配门店状态
        MarketingHelper.shopState(queryBuilder);
        // 匹配门店经营类型
        MarketingHelper.shopServiceType(queryBuilder);
        // 匹配商品销售渠道
        MarketingHelper.goodsSaleChannel(queryBuilder);
        try {
            marketingList =
                    elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class, 1, dto.getShowSize(), EsIndexConstant.getMarketingIndex());
        } catch (Exception e) {
            log.error("微信首页秒杀/团购查询异常", e);
        }
        if (BaseUtil.judgeList(marketingList)) {
//            marketingList = marketingList.stream().limit(dto.getShowSize()).collect(Collectors.toList());
            dtoList = BaseUtil.objToObj(marketingList, EsMarketingListDTO.class);
        }
        return dtoList;
    }


    /**
     * 活动分页
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/3/28 17:11
     * @return: {@link PageData < MarketingListDTO>}
     **/
    @Override
    public PageData<EsMarketingListDTO> marketingLimit(EsMarketingListParamDTO dto) {
        BoolQueryBuilder queryBuilder = marketingPublicQueryBuilder(dto);
        // 匹配活动状态
        if (dto.getStatus() != null) {
            MarketingHelper.marketingStatus(queryBuilder, dto.getStatus());
        }
        PageData<EsMarketingEntity> selectPageData = new PageData<>();
        PageData<EsMarketingListDTO> returnPageData = new PageData<>();
        try {
            selectPageData = pageSortHighLight(queryBuilder, dto.getPageNo().intValue(), dto.getPageSize().intValue(), EsIndexConstant.MARKETING);
        } catch (Exception e) {
            log.error("活动分页查询异常", e);
        }
        if (BaseUtil.judgeList(selectPageData.getList())) {
            returnPageData.setList(BaseUtil.objToObj(selectPageData.getList(), EsMarketingListDTO.class));
        }
        returnPageData.setTotal(selectPageData.getTotal());
        // 处理是否是最后一页
        returnPageData.setLastPage(EsUtil.lastPages(returnPageData.getTotal(), dto.getPageSize().intValue(), dto.getPageNo().intValue()));
        return returnPageData;
    }

    private BoolQueryBuilder marketingPublicQueryBuilder(EsMarketingListParamDTO dto) {
        // 查询语句
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(dto.getCityCode())) {
            queryBuilder.must(QueryBuilders.termQuery(MarketingHelper.ES_CITY_CODE, dto.getCityCode()));
        }
        // 未删除
        MarketingHelper.noDel(queryBuilder);
        // 上架
        MarketingHelper.upDownState(queryBuilder);
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
        if (BaseUtil.judgeList(dto.getShopIds())) {
            List<String> shopIdList = dto.getShopIds().stream().map(Object::toString).collect(Collectors.toList());
            MarketingHelper.shopIds(queryBuilder, shopIdList);
        }
        //匹配用户类型
        MarketingHelper.objectLimit(queryBuilder, null != dto.getUserType() ? dto.getUserType() : MarketingUsingObjectEnum.PERSONAL.getStatus());
        // 匹配活动类型
        if (null != dto.getOfType()) {
            MarketingHelper.ofType(queryBuilder, dto.getOfType());
        }
        return queryBuilder;
    }

    @Override
    public PageData<EsMarketingListDTO> marketingGoodsLimit(EsMarketingListParamDTO dto) {
        EsGeoSearchDTO<EsMarketingEntity> geo = marketingGeoQueryBuilder(dto);
        PageData<EsMarketingEntity> selectPageData = new PageData<>();
        try {
            selectPageData = elasticsearchTemplate.locationSearch33(geo);
        } catch (IOException e) {
            log.error("营销活动商品数据ES查询错误：{}", e.getMessage());
        }
        PageData<EsMarketingListDTO> pageDataDto = new PageData<>();
        // 设置总条数
        pageDataDto.setTotal(selectPageData.getTotal());
        // 设置数据集
        if (BaseUtil.judgeList(selectPageData.getList())) {
            pageDataDto.setList(BaseUtil.objToObj(selectPageData.getList(), EsMarketingListDTO.class));
        }
        // 设置是否是最后一页
        pageDataDto.setLastPage(EsUtil.lastPages(selectPageData.getTotal(), dto.getPageSize().intValue(), dto.getPageNo().intValue()));
        return pageDataDto;
    }

    /**
     * 构建经纬度排序分页查询条件
     *
     * @param dto 查询条件
     * @author: GongJunZheng
     * @date: 2020/8/11 15:57
     * @return: {@link }
     * @version V1.3.0
     **/
    private EsGeoSearchDTO<EsMarketingEntity> marketingGeoQueryBuilder(EsMarketingListParamDTO dto) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        //活动基础过滤
        MarketingHelper.marketingBaseQuery(queryBuilder);

        // 匹配团购活动状态
        if (dto.getStatus() != null && Objects.equals(MarketingTypeEnum.GROUPON.getStatus(), dto.getOfType())) {
            MarketingHelper.marketingStatus(queryBuilder, dto.getStatus());
        }
        // 匹配秒杀状态
        if (Objects.equals(MarketingTypeEnum.SECKILL.getStatus(), dto.getOfType())) {
            // 匹配时间
            long milliSecond = dto.getSeckillTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            MarketingHelper.ongoing(queryBuilder, milliSecond);
            // 匹配场次
            String value = "*" + dto.getSeckillEventId() + "*";
            queryBuilder.must(QueryBuilders.wildcardQuery("seckillEventIds.keyword", value));
        }

        // 城市编码
        if (dto.getCityCode() != null) {
            queryBuilder.must(QueryBuilders.termQuery(MarketingHelper.ES_CITY_CODE, dto.getCityCode()));
        }
        //匹配用户类型
        MarketingHelper.objectLimit(queryBuilder, null != dto.getUserType() ? dto.getUserType() : MarketingUsingObjectEnum.PERSONAL.getStatus());
        // 匹配活动类型
        MarketingHelper.ofType(queryBuilder, dto.getOfType());
        // 经纬度排序
        String location = dto.getLocation();
        String[] locationArr = location.split(BaseUtil.COMMA);
        double lon = Double.parseDouble(locationArr[0]);
        double lat = Double.parseDouble(locationArr[1]);
        // 商品排序
        EsGeoSearchDTO<EsMarketingEntity> geo = new EsGeoSearchDTO<>();

        String shopNameField = "shopName";
        Sort.Order shopNameOrder = new Sort.Order(SortOrder.ASC, shopNameField);

        String goodsSortField = "goodsSort";
        Sort.Order goodsSortOrder = new Sort.Order(SortOrder.ASC, goodsSortField);

        Sort sort = new Sort(shopNameOrder, goodsSortOrder);

        geo.setSort(sort);
        EsUtil.setGeo(lat, lon, dto.getPageNo(), dto.getPageSize(), geo,
                EsMarketingEntity.class, queryBuilder, DistanceUnit.MILLIMETERS, EsIndexConstant.getMarketingIndex());
        return geo;
    }


    @Override
    public void goodsDelSync(Long merchantId, Long goodsId) {
        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.must(QueryBuilders.termQuery("goodsId", goodsId));
            if (null != merchantId) {
                queryBuilder.must(QueryBuilders.termQuery(CommonConstant.MERCHANT_ID, merchantId));
            }
            elasticsearchTemplate.deleteByCondition(queryBuilder, EsMarketingEntity.class);
        } catch (Exception e) {
            log.error("商品被删除/商品被取消授权同步删除ES中的营销活动商品失败：{}", e.getMessage());
        }
    }

    @Override
    public void goodsCancelSync(Long merchantId, Long goodsId) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("goodsId", goodsId));
        queryBuilder.must(QueryBuilders.termQuery(CommonConstant.MERCHANT_ID, merchantId));
        List<EsMarketingEntity> searchList = null;
        try {
            searchList = elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class);
        } catch (Exception e) {
            log.error("通过goodsId以及商户ID查询ES中的营销活动商品数据异常：{}", e.getMessage());
        }
        if (BaseUtil.judgeList(searchList) && searchList != null) {
            searchList.forEach(item -> item.setDel(DelEnum.DELETE.getFlag()));
            try {
                // 批量保存
                BulkResponse responses = elasticsearchTemplate.save(searchList);
                if (responses.hasFailures()) {
                    log.error("批量修改营销活动商品删除状态数据失败，错误：{}", responses.buildFailureMessage());
                } else {
                    log.info("批量修改营销活动商品删除状态数据成功");
                }
            } catch (Exception e) {
                log.error("批量修改营销活动商品删除状态数据异常：{}", e.getMessage());
            }
        }
    }

    @Override
    public void shopDelSync(Long shopId) {
        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.must(QueryBuilders.termQuery(CommonConstant.SHOP_ID, shopId));
            elasticsearchTemplate.deleteByCondition(queryBuilder, EsMarketingEntity.class);
        } catch (Exception e) {
            log.error("门店被删除同步删除ES中的门店营销活动商品失败：{}", e.getMessage());
        }
    }

    @Override
    public void goodsUpDownSync(Long merchantId, Long shopId, Long goodsId, Integer upDown) {
        // 根据goodsId查询ES里面的数据
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("goodsId", goodsId));
        if (null != merchantId) {
            queryBuilder.must(QueryBuilders.termQuery(CommonConstant.MERCHANT_ID, merchantId));
        }
        if (null != shopId) {
            queryBuilder.must(QueryBuilders.termQuery(CommonConstant.SHOP_ID, shopId));
        }
        List<EsMarketingEntity> searchList = null;
        try {
            searchList = elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class);
        } catch (Exception e) {
            log.error("通过goodsId查询ES中的营销活动商品数据异常：{}", e.getMessage());
        }
        if (BaseUtil.judgeList(searchList) && searchList != null) {
            searchList.forEach(item -> item.setGoodsUpDownState(upDown));
            try {
                // 批量保存
                BulkResponse responses = elasticsearchTemplate.save(searchList);
                if (responses.hasFailures()) {
                    log.error("批量修改营销活动商品上下架状态数据失败，错误：{}", responses.buildFailureMessage());
                } else {
                    log.info("批量修改营销活动商品上下架状态数据成功");
                }
            } catch (Exception e) {
                log.error("批量修改营销活动商品上下架状态数据异常：{}", e.getMessage());
            }
        }
    }

    @Override
    public void pcMenuUpdateSync(List<Long> marketingGoodsIdList, Boolean delStatus) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termsQuery("mgoodsId", marketingGoodsIdList));
        List<EsMarketingEntity> searchList = null;
        try {
            searchList = elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class);
        } catch (Exception e) {
            log.error("通过mGoodsId集合查询ES中的营销活动商品数据异常：{}", e.getMessage());
        }
        if (BaseUtil.judgeList(searchList) && searchList != null) {
            searchList.forEach(item -> item.setDel(delStatus));
            try {
                // 批量保存
                BulkResponse responses = elasticsearchTemplate.save(searchList);
                if (responses.hasFailures()) {
                    log.error("批量修改营销活动商品删除状态数据失败，错误：{}", responses.buildFailureMessage());
                } else {
                    log.info("批量修改营销活动商品删除状态数据成功");
                }
            } catch (Exception e) {
                log.error("批量修改营销活动商品删除状态数据异常：{}", e.getMessage());
            }
        }
    }

    @Override
    public void merchantShopUpdateSync(EsStatusUpdateDTO dto) {
        // 根据goodsId查询ES里面的数据
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (MarketingMerchantShopTypeEnum.MERCHANT.getStatus().equals(dto.getType())) {
            // 商户状态改变
            queryBuilder.must(QueryBuilders.termQuery(CommonConstant.MERCHANT_ID, dto.getId()));
        }
        if (MarketingMerchantShopTypeEnum.SHOP.getStatus().equals(dto.getType())) {
            // 商户状态改变
            queryBuilder.must(QueryBuilders.termQuery(CommonConstant.SHOP_ID, dto.getId()));
        }
        List<EsMarketingEntity> searchList = null;
        try {
            searchList = elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class);
        } catch (Exception e) {
            log.error("通过商户ID/门店ID查询ES中的营销活动商品数据，异常。", e);
        }
        if (BaseUtil.judgeList(searchList) && searchList != null) {
            searchList.forEach(item -> {
                if (MarketingMerchantShopTypeEnum.MERCHANT.getStatus().equals(dto.getType())) {
                    item.setMerchantState(dto.getStatus());
                }
                if (MarketingMerchantShopTypeEnum.SHOP.getStatus().equals(dto.getType())) {
                    item.setShopState(dto.getStatus());
                }
            });
            try {
                // 批量保存
                BulkResponse responses = elasticsearchTemplate.save(searchList);
                if (responses.hasFailures()) {
                    log.error("批量修改商户/店铺状态数据失败，错误：{}", responses.buildFailureMessage());
                } else {
                    log.info("批量修改商户/店铺状态数据成功");
                }
            } catch (Exception e) {
                log.error("批量修改商户/店铺状态数据异常。", e);
            }
        }
    }

    @Override
    public void pcMenuShopDelSync(List<Long> shopIds, Boolean flag) {
        // 根据goodsId查询ES里面的数据
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 商户状态改变
        queryBuilder.must(QueryBuilders.termsQuery(CommonConstant.SHOP_ID, shopIds));
        if(!flag) {
            List<Integer> goodsAddTypes = new ArrayList<>();
            goodsAddTypes.add(GoodsAddTypeEnum.PLANTE.getStatus());
            goodsAddTypes.add(GoodsAddTypeEnum.MERCHANT.getStatus());
            queryBuilder.must(QueryBuilders.termsQuery("goodsAddType", goodsAddTypes));
        }
        List<EsMarketingEntity> searchList = null;
        try {
            searchList = elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class);
        } catch (Exception e) {
            log.error("通过商户ID/门店ID查询ES中的营销活动商品数据异常：{}", e.getMessage());
        }
        if (BaseUtil.judgeList(searchList) && searchList != null) {
            searchList.forEach(item ->
                    item.setDel(DelEnum.DELETE.getFlag())
            );
            try {
                // 批量保存
                BulkResponse responses = elasticsearchTemplate.save(searchList);
                if (responses.hasFailures()) {
                    log.error("批量修改移除了门店，将营销商品的状态为删除状态失败，错误：{}", responses.buildFailureMessage());
                } else {
                    log.info("批量修改移除了门店，将营销商品的状态为删除状态成功");
                }
            } catch (Exception e) {
                log.error("批量修改移除了门店，将营销商品的状态为删除状态异常：{}", e.getMessage());
            }
        }
    }

    @Override
    public List<EsMarketingEntity> findAllSeckill() {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery(MarketingHelper.OF_TYPE, MarketingTypeEnum.SECKILL.getStatus()));
        List<EsMarketingEntity> searchList = null;
        try {
            Integer pageNum = 1;
            Integer pageSize = 10000;
            searchList = elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class, pageNum, pageSize);
            log.info("查询出来的秒杀活动数量：{}", searchList.size());
        } catch (Exception e) {
            log.error("查询ES所有的秒杀活动信息失败，出现异常：{}", e.getMessage());
        }
        return searchList;
    }

    @Override
    public void updateBatch(List<EsMarketingEntity> marketingList) {
        try {
            // 批量保存
            BulkResponse responses = elasticsearchTemplate.save(marketingList);
            if (responses.hasFailures()) {
                log.error("批量更新活动数据失败，错误：{}", responses.buildFailureMessage());
            } else {
                log.info("批量更新活动数据成功");
            }
        } catch (Exception e) {
            log.error("批量更新活动数据异常：{}", e.getMessage());
        }
    }

    @Override
    public List<EsMarketingEntity> selectSeckillByDatetime(String cityCode, Integer userType, LocalDateTime dateTime) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        //基础过滤
        MarketingHelper.marketingBaseQuery(queryBuilder);
        // 匹配时间
        Long milliSecond = BaseUtil.milliSecond(dateTime);
        MarketingHelper.ongoing(queryBuilder, milliSecond);

        // 城市编码
        queryBuilder.must(QueryBuilders.termQuery(MarketingHelper.ES_CITY_CODE, cityCode));
        //匹配用户类型
        MarketingHelper.objectLimit(queryBuilder, null != userType ? userType : MarketingUsingObjectEnum.PERSONAL.getStatus());
        // 匹配活动类型
        queryBuilder.must(QueryBuilders.termQuery(MarketingHelper.OF_TYPE, MarketingTypeEnum.SECKILL.getStatus()));

        List<EsMarketingEntity> searchList = null;
        try {
            searchList = elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class);
        } catch (Exception e) {
            log.error("查询指定时间秒杀活动商品失败，错误：{}", e.getMessage());
        }
        return searchList;
    }

    /**
     * 批量获取秒杀/团购
     *
     * @param id 活动id
     * @author: wxf
     * @date: 2020/3/26 16:53
     * @return: {@link EsMarketingDTO}
     **/
    @Override
    public List<EsMarketingDTO> listById(String id) {
        // 查询语句
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 匹配活动id
        queryBuilder.must(QueryBuilders.termQuery("id", id));
        List<EsMarketingEntity> marketingList = Collections.emptyList();
        List<EsMarketingDTO> dtoList = Collections.emptyList();
        try {
            marketingList = elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class, EsIndexConstant.MARKETING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BaseUtil.judgeList(marketingList)) {
            dtoList = BaseUtil.objToObj(marketingList, EsMarketingDTO.class);
        }
        return dtoList;
    }

    @Override
    public Boolean wxVerificationActivityTab(EsMarketingListParamDTO dto) {
        BoolQueryBuilder queryBuilder = this.marketingPublicQueryBuilder(dto);
        Long milliSecond = BaseUtil.milliSecond();
        queryBuilder.must(QueryBuilders.rangeQuery(MarketingHelper.BEGIN_TIME).gt(milliSecond));
        long count = 0;
        try {
            List<EsMarketingEntity> list = elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class);
            count = list.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count > 0;
    }

    /**
     * 删除根据活动id
     *
     * @param marketingId 活动id
     * @author: wxf
     * @date: 2020/5/29 10:28
     * @version 1.0.1
     **/
    @Override
    public void delByMarketingId(Long marketingId) {
        if (null != marketingId) {
            try {
                BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                queryBuilder.must(QueryBuilders.termQuery("id", marketingId));
                elasticsearchTemplate.deleteByCondition(queryBuilder, EsMarketingEntity.class);
            } catch (Exception e) {
                log.error("删除根据活动id异常", e);
            }
        }
    }

    @Override
    public void logicDelByMarketingIds(Set<Long> marketingIds) {
        if (!marketingIds.isEmpty()) {
            try {
                BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                queryBuilder.must(QueryBuilders.termsQuery("id", marketingIds));
                List<EsMarketingEntity> searchList = elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class);
                if (BaseUtil.judgeList(searchList)) {
                    searchList.forEach(item ->
                            item.setDel(DelEnum.DELETE.getFlag())
                    );
                    // 批量保存
                    BulkResponse responses = elasticsearchTemplate.save(searchList);
                    if (responses.hasFailures()) {
                        log.error("根据活动id集合删除营销秒杀/团购活动失败，错误：{}", responses.buildFailureMessage());
                    } else {
                        log.info("根据活动id集合删除营销秒杀/团购活动成功");
                    }
                }
            } catch (Exception e) {
                log.error("根据活动id集合删除营销秒杀/团购活动异常", e);
            }
        }
    }

    @Override
    public void delByMarketingIds(Set<Long> marketingIds) {
        if (!marketingIds.isEmpty()) {
            try {
                BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                queryBuilder.must(QueryBuilders.termsQuery("id", marketingIds));
                elasticsearchTemplate.deleteByCondition(queryBuilder, EsMarketingEntity.class);
            } catch (Exception e) {
                log.error("删除根据活动id集合异常", e);
            }
        }
    }

    /**
     * 批量获取根据商户id
     *
     * @param shopId 店铺id
     * @author: wxf
     * @date: 2020/6/5 10:44
     * @return: {@link List<EsMarketingDTO>}
     * @version 1.1.0
     **/
    @Override
    public List<EsMarketingDTO> listByShopId(Long shopId) {
        return listByShopId(shopId, null, null);
    }

    @Override
    public List<EsMarketingDTO> seckillListByShopId(Long shopId, Integer objectLimit) {
        //todo 枚举  秒杀
        int ofType = 1;
        return listByShopId(shopId, ofType, objectLimit);
    }

    public List<EsMarketingDTO> listByShopId(Long shopId, Integer ofType, Integer objectLimit) {
        if (null == shopId) {
            return null;
        }
        List<EsMarketingDTO> esMarketingDtoList = Collections.emptyList();


        BoolQueryBuilder queryBuilder = marketingCommonBuilder()
                // 匹配商户id
                .must(QueryBuilders.termQuery(CommonConstant.SHOP_ID, shopId.toString()));

        if (ofType != null) {
            //活动类型
            queryBuilder.must(QueryBuilders.termQuery(MarketingHelper.OF_TYPE, ofType));
        }
        if (objectLimit != null) {
            //用户类型
            MarketingHelper.objectLimit(queryBuilder, objectLimit);
        }

        try {
            List<EsMarketingEntity> esMarketingEntityList = elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class);
            if (BaseUtil.judgeList(esMarketingEntityList)) {
                esMarketingDtoList = BaseUtil.objToObj(esMarketingEntityList, EsMarketingDTO.class);
            }
        } catch (Exception e) {
            log.error("批量获取根据商户id异常", e);
        }
        return esMarketingDtoList;
    }

    @Override
    public List<EsMarketingDTO> listByShopIds(EsMarketingShopDTO dto) {
        List<EsMarketingDTO> dtoList = new ArrayList<>();
        List<EsMarketingEntity> marketingList = Collections.emptyList();
        BoolQueryBuilder queryBuilder = marketingPublicQueryBuilder(BaseUtil.objToObj(dto, EsMarketingListParamDTO.class));
        // 过滤 进行中的活动
        MarketingHelper.ongoing(queryBuilder);
        try {
            marketingList =
                    elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class, EsIndexConstant.getMarketingIndex());
        } catch (Exception e) {
            log.error("微信首页秒杀/团购查询异常", e);
        }
        if (BaseUtil.judgeList(marketingList)) {
            dtoList = BaseUtil.objToObj(marketingList, EsMarketingDTO.class);
        }
        return dtoList;
    }

    /**
     * 首页分页查询配置
     *
     * @param queryBuilder 查询参数配置
     * @param pageNum      页码
     * @param pageSize     条数
     * @param index        索引
     * @return {@link PageData< EsMarketingListDTO >}
     * @throws {@link Exception}
     */
    public PageData<EsMarketingEntity> pageSortHighLight(QueryBuilder queryBuilder, Integer pageNum,
                                                         Integer pageSize, String index) throws Exception {
        // 分页
        PageSortHighLight psh = new PageSortHighLight(pageNum, pageSize);
        Sort.Order beginTimeOrder = new Sort.Order(SortOrder.ASC, MarketingHelper.BEGIN_TIME);
        psh.setSort(new Sort(beginTimeOrder));
        return elasticsearchTemplate.search(queryBuilder, psh, EsMarketingEntity.class, index);
    }

    private void handleMarketingStatus(EsMarketingDTO dto) {
        LocalDateTime beginTime = dto.getBeginTime();
        LocalDateTime endTime = dto.getEndTime();
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(beginTime)) {
            // 未开始
            dto.setStatus(MarketingStatusEnum.NOT_START.getStatus());
        } else if (now.isAfter(beginTime) && now.isBefore(endTime)) {
            // 进行中
            dto.setStatus(MarketingStatusEnum.ONGOING.getStatus());
        } else if (now.isAfter(endTime)) {
            // 已结束
            dto.setStatus(MarketingStatusEnum.ENDED.getStatus());
        }
        //计算活动倒计时
        Long countDown = DateTimeUtil.diffSeconds(LocalDateTime.now(), endTime);
        dto.setCountDown(countDown);
    }


    @Override
    public Map<String, Double> queryMarketingDiscount(Set<String> longs, Integer ofType, Integer objectLimit) {

        BoolQueryBuilder queryBuilder = marketingCommonBuilder();

        MarketingHelper.ofType(queryBuilder, ofType);
        MarketingHelper.objectLimit(queryBuilder, objectLimit);
        MarketingHelper.shopIds(queryBuilder, longs);

        //分组字段
        String bucketName = CommonConstant.SHOP_ID;
        //需要统计分析的字段
        String metricName = MarketingHelper.ACTIVITY_PRICE;
        try {
            return elasticsearchTemplate.aggs(metricName, AggsType.min, queryBuilder, EsMarketingEntity.class, bucketName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Maps.newHashMap();
    }


    @Override
    public Double queryMarketingDiscount(String shopId, Integer ofType, Integer objectLimit) {
        Map<String, Double> doubleMap = queryMarketingDiscount(Sets.newHashSet(shopId), ofType, objectLimit);
        return doubleMap.get(shopId);
    }

    @Override
    public List<EsMarketingEntity> queryDiscountMarketing(DiscountQuery query) {

        BoolQueryBuilder queryBuilder = marketingCommonBuilder();

        MarketingHelper.ofType(queryBuilder, query.getOfType());
        MarketingHelper.objectLimit(queryBuilder, query.getObjectLimit());
        MarketingHelper.shopIds(queryBuilder, query.getAppointShopIds());

        try {
            return elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Lists.newArrayList();
    }

    @Override
    public List<EsMarketingEntity> findAll() {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        try{
            Integer pageNum = 1;
            Integer pageSize = 10000;
            return elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class, pageNum, pageSize);
        }catch (Exception e) {
            log.error("查询ES中所有的营销秒杀/团购商品信息异常：{}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private BoolQueryBuilder marketingCommonBuilder() {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        MarketingHelper.marketingBaseQuery(queryBuilder);
        // 进行中的活动
        MarketingHelper.ongoing(queryBuilder);
        return queryBuilder;
    }

    @Override
    public void updateGrouponTime(Long id, LocalDateTime beginTime, LocalDateTime endTime) {
        // 根据团购活动ID查询ES里面的数据
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 商户状态改变
        queryBuilder.must(QueryBuilders.termQuery(CommonConstant.ID, id));
        List<EsMarketingEntity> searchList = null;
        try {
            searchList = elasticsearchTemplate.search(queryBuilder, EsMarketingEntity.class);
        } catch (Exception e) {
            log.error("通过活动ID查询ES中的营销活动商品数据异常：{}", e.getMessage());
        }
        if (BaseUtil.judgeList(searchList) && searchList != null) {
            log.info("修改前的数据：{}", searchList);
            ZoneId zoneId = ZoneId.systemDefault();
            // 开始时间
            ZonedDateTime zonedBeginTime = beginTime.atZone(zoneId);
            Date dateBeginTime = Date.from(zonedBeginTime.toInstant());
            // 结束时间
            ZonedDateTime zonedEndTime = endTime.atZone(zoneId);
            Date dateEndTime = Date.from(zonedEndTime.toInstant());
            searchList.forEach(item ->
                    {
                        item.setBeginTime(dateBeginTime);
                        item.setEndTime(dateEndTime);
                    }
            );
            try {
                // 批量保存
                log.info("修改后的数据：{}", searchList);
                BulkResponse responses = elasticsearchTemplate.save(searchList);
                if (responses.hasFailures()) {
                    log.error("批量修改团购活动时间失败，错误：{}", responses.buildFailureMessage());
                } else {
                    log.info("批量修改团购活动时间成功");
                }
            } catch (Exception e) {
                log.error("批量修改团购活动时间异常。", e);
            }
        }
    }
}
