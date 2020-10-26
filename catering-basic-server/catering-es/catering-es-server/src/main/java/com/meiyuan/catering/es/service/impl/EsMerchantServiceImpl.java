package com.meiyuan.catering.es.service.impl;

import com.meiyuan.catering.core.enums.base.ServiceTypeEnum;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.es.constant.CommonConstant;
import com.meiyuan.catering.es.constant.EsIndexConstant;
import com.meiyuan.catering.es.dto.geo.EsGeoSearchDTO;
import com.meiyuan.catering.es.dto.geo.GeoLimitQueryDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantCommonQueryBuilderDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantListParamDTO;
import com.meiyuan.catering.es.dto.wx.EsWxIndexSearchQueryDTO;
import com.meiyuan.catering.es.dto.wx.EsWxMerchantDTO;
import com.meiyuan.catering.es.entity.EsMerchantEntity;
import com.meiyuan.catering.es.enums.merchant.MerchantHaveGoodsEnum;
import com.meiyuan.catering.es.enums.merchant.WxMerchantSearchTypeEnum;
import com.meiyuan.catering.es.repository.ElasticsearchTemplate;
import com.meiyuan.catering.es.repository.Sort;
import com.meiyuan.catering.es.service.EsMerchantService;
import com.meiyuan.catering.es.util.EsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/5/26 11:43
 * @description 简单描述
 **/
@Service
@Slf4j
public class EsMerchantServiceImpl implements EsMerchantService {
    @Resource
    ElasticsearchTemplate<EsMerchantEntity, String> elasticsearchTemplate;

    /**
     * 新增修改数据
     *
     * @param dto 新增修改数据
     * @author: wxf
     * @date: 2020/5/26 11:28
     * @version 1.0.1
     **/
    @Override
    public void saveUpdate(EsMerchantDTO dto) {
        if (null != dto) {
            try {
                EsMerchantEntity esMerchantEntity = BaseUtil.objToObj(dto, EsMerchantEntity.class);
                boolean flag = elasticsearchTemplate.save(esMerchantEntity);
                log.info(flag ? "单个新增/修改ES商户索引数据成功" : "单个新增/修改ES商户索引数据失败");
            } catch (Exception e) {
                log.error("单个新增/修改ES商户索引数据异常", e);
            }
        }
    }

    @Override
    public void update(EsMerchantDTO dto) {
        if (null != dto) {
            try {
                EsMerchantEntity esMerchantEntity = BaseUtil.objToObj(dto, EsMerchantEntity.class);
                boolean flag = elasticsearchTemplate.update(esMerchantEntity);
                log.info(flag ? "单个新增/修改ES商户索引数据成功" : "单个新增/修改ES商户索引数据失败");
            } catch (Exception e) {
                log.error("单个新增/修改ES商户索引数据异常", e);
            }
        }
    }

    /**
     * 新增修改数据
     *
     * @param dtoList 新增修改数据集合
     * @author: wxf
     * @date: 2020/5/26 11:28
     * @version 1.0.1
     **/
    @Override
    public void saveUpdateBatch(List<EsMerchantDTO> dtoList) {
        if (BaseUtil.judgeList(dtoList)) {
            try {
                List<EsMerchantEntity> merchantList = BaseUtil.objToObj(dtoList, EsMerchantEntity.class);
                BulkResponse responses = elasticsearchTemplate.save(merchantList);
                // Bulk响应提供了一种方法来快速检查一个或多个操作是否失败  hasFailures
                // 有一个操作失败，此方法将返回true 全部成功返回false
                if (responses.hasFailures()) {
                    log.info("批量新增/修改商户索引数据失败");
                } else {
                    log.info("批量新增/修改商户索引数据成功");
                }
            } catch (Exception e) {
                log.error("批量新增/修改商户索引数据异常", e);
            }
        }
    }

    @Override
    public void updateBatch(List<EsMerchantDTO> dtoList) {
        if (BaseUtil.judgeList(dtoList)) {
            try {
                List<EsMerchantEntity> merchantList = BaseUtil.objToObj(dtoList, EsMerchantEntity.class);
                BulkResponse responses = elasticsearchTemplate.updateRequest(merchantList);
                // Bulk响应提供了一种方法来快速检查一个或多个操作是否失败  hasFailures
                // 有一个操作失败，此方法将返回true 全部成功返回false
                if (responses.hasFailures()) {
                    log.info("批量新增/修改商户索引数据失败");
                } else {
                    log.info("批量新增/修改商户索引数据成功");
                }
            } catch (Exception e) {
                log.error("批量新增/修改商户索引数据异常", e);
            }
        }
    }

    /**
     * 品牌商户列表分页
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/6 17:33
     * @return: {@link PageData<  EsWxMerchantDTO >}
     **/
    @Override
    public PageData<EsWxMerchantDTO> brandListLimit(EsMerchantListParamDTO dto) {
        EsMerchantCommonQueryBuilderDTO queryBuilderDto = new EsMerchantCommonQueryBuilderDTO();
        queryBuilderDto.setCityCode(dto.getCityCode());
        queryBuilderDto.setShopIdList(dto.getShopIdList());
        EsGeoSearchDTO<EsMerchantEntity> geo = new EsGeoSearchDTO<>();
        if (null != dto.getSearchType()) {
            Sort.Order order = getOrder(dto);
            geo.setSort(new Sort(order));
        }
        return geoLimit(new GeoLimitQueryDTO(dto.getLat(), dto.getLng(), dto.getPageNo(), dto.getPageSize(), commonQueryBuilder(queryBuilderDto)), geo);
    }

    /**
     * 商户列表分页
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/6 17:33
     * @return: {@link PageData<  EsWxMerchantDTO >}
     **/
    @Override
    public PageData<EsWxMerchantDTO> listLimit(EsMerchantListParamDTO dto) {
        EsMerchantCommonQueryBuilderDTO queryBuilderDto = new EsMerchantCommonQueryBuilderDTO();
        queryBuilderDto.setCityCode(dto.getCityCode());
        queryBuilderDto.setShopIdList(dto.getShopIdList());
        EsGeoSearchDTO<EsMerchantEntity> geo = new EsGeoSearchDTO<>();
        if (null != dto.getSearchType()) {
            Sort.Order order = getOrder(dto);
            geo.setSort(new Sort(order));
        }
        return geoLimit(new GeoLimitQueryDTO(dto.getLat(), dto.getLng(), dto.getPageNo(), dto.getPageSize(), commonQueryBuilder(queryBuilderDto)), geo);
    }

    /**
     * 分页
     *
     * @param dto 分页参数
     * @param geo 经纬度参数
     * @author: wxf
     * @date: 2020/6/5 14:03
     * @return: {@link PageData< EsWxMerchantDTO>}
     * @version 1.1.0
     **/
    private PageData<EsWxMerchantDTO> geoLimit(GeoLimitQueryDTO dto, EsGeoSearchDTO<EsMerchantEntity> geo) {
        PageData<EsWxMerchantDTO> pageDataDto = new PageData<>();
        try {
            EsUtil.setGeo(dto.getLat(), dto.getLng(), dto.getPageNo(), dto.getPageSize(), geo,
                    EsMerchantEntity.class, dto.getQueryBuilder(), DistanceUnit.KILOMETERS, EsIndexConstant.getMerchantIndex());
            PageData<EsMerchantEntity> pageDataEntity = elasticsearchTemplate.locationSearch(geo);
            pageDataDto.setTotal(pageDataEntity.getTotal());
            if (null != pageDataEntity.getList()) {
                pageDataDto.setList(BaseUtil.objToObj(pageDataEntity.getList(), EsWxMerchantDTO.class));
            }
            // 处理是否是最后一页
            pageDataDto.setLastPage(EsUtil.lastPages(pageDataEntity.getTotal(), dto.getPageSize().intValue(), dto.getPageNo().intValue()));
        } catch (Exception e) {
            log.error("商户列表分页异常", e);
        }
        return pageDataDto;
    }

    /**
     * 获取根据 商户id
     *
     * @param merchantId 商户id
     * @author: wxf
     * @date: 2020/5/29 15:46
     * @return: {@link EsMerchantDTO}
     * @version 1.0.1
     **/
    @Override
    public EsMerchantDTO getByMerchantId(String merchantId) {
        if (!BaseUtil.judgeString(merchantId)) {
            log.warn("商户id为空");
            return null;
        }
        EsMerchantDTO esMerchantDto = null;
        try {
            EsMerchantEntity esMerchantEntity = elasticsearchTemplate.getById(merchantId, EsMerchantEntity.class);
            esMerchantDto = BaseUtil.objToObj(esMerchantEntity, EsMerchantDTO.class);
        } catch (Exception e) {
            log.error("获取根据商户根据商户id异常", e);
        }
        return esMerchantDto;
    }

    /**
     * 批量获取根据商户id集合
     *
     * @param merchantIdList 商户id集合
     * @author: wxf
     * @date: 2020/6/5 10:44
     * @return: {@link List< EsMerchantDTO>}
     * @version 1.1.0
     **/
    @Override
    public List<EsMerchantDTO> listByMerchantIdList(List<Long> merchantIdList) {
        if (!BaseUtil.judgeList(merchantIdList)) {
            return null;
        }
        List<String> queryMerchantIdList = merchantIdList.stream().map(i -> i.toString()).collect(Collectors.toList());
        List<EsMerchantDTO> esMerchantDtoList = Collections.emptyList();
        try {
            List<EsMerchantEntity> esMerchantEntityList = elasticsearchTemplate.mgetById(EsUtil.stringIdListToArray(queryMerchantIdList), EsMerchantEntity.class);
            if (BaseUtil.judgeList(esMerchantEntityList)) {
                esMerchantDtoList = BaseUtil.objToObj(esMerchantEntityList, EsMerchantDTO.class);
            }
        } catch (Exception e) {
            log.error("批量获取根据商户id集合异常", e);
        }
        return esMerchantDtoList;
    }

    /**
     * 同步商户索引的 评分和月售
     *
     * @param merchantId 商户id
     * @param shopGrade  评分
     * @param ordersNum  月售
     * @author: wxf
     * @date: 2020/6/8 9:55
     * @version 1.1.0
     **/
    @Override
    public void synGradeAndOrderNum(Long merchantId, Double shopGrade, Integer ordersNum) {
        try {
            EsMerchantEntity esMerchantEntity = elasticsearchTemplate.getById(merchantId.toString(), EsMerchantEntity.class);
            if (null != esMerchantEntity) {
                if (null != shopGrade) {
                    esMerchantEntity.setShopGrade(shopGrade);
                }
                if (null != ordersNum) {
                    esMerchantEntity.setOrdersNum(ordersNum);
                }
                saveUpdate(BaseUtil.objToObj(esMerchantEntity, EsMerchantDTO.class));
            }
        } catch (Exception e) {
            log.error("同步商户索引的评分和月售异常", e);
        }
    }

    /**
     * 微信首页搜索
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/6/8 10:34
     * @return: {@link List< EsMerchantDTO>}
     * @version 1.1.0
     **/
    @Override
    public PageData<EsMerchantDTO> wxIndexSearch(EsWxIndexSearchQueryDTO dto) {
        PageData<EsMerchantDTO> pageDataDto = new PageData<>();
        try {
            EsMerchantCommonQueryBuilderDTO queryBuilderDto = new EsMerchantCommonQueryBuilderDTO();
            queryBuilderDto.setCityCode(dto.getCityCode());
            queryBuilderDto.setWxIndexSearchName(dto.getName());
            wxIndexSearchQueryBuilder(queryBuilderDto);

            EsGeoSearchDTO<EsMerchantEntity> geo = new EsGeoSearchDTO<>();
            EsUtil.setGeo(dto.getLat(), dto.getLng(), dto.getPageNo(), dto.getPageSize(), geo,
                    EsMerchantEntity.class, wxIndexSearchQueryBuilder(queryBuilderDto), DistanceUnit.KILOMETERS, EsIndexConstant.getMerchantIndex());
            PageData<EsMerchantEntity> pageDataEntity = elasticsearchTemplate.locationSearch(geo);
            pageDataDto.setTotal(pageDataEntity.getTotal());
            if (null != pageDataEntity.getList()) {
                pageDataDto.setList(BaseUtil.objToObj(pageDataEntity.getList(), EsMerchantDTO.class));
            }
            // 处理是否是最后一页
            pageDataDto.setLastPage(EsUtil.lastPages(pageDataEntity.getTotal(), dto.getPageSize().intValue(), dto.getPageNo().intValue()));
        } catch (Exception e) {
            log.error("微信首页搜索异常", e);
        }
        return pageDataDto;
    }

    /**
     * 批量获取根据商品id
     *
     * @param goodsId 商品id
     * @author: wxf
     * @date: 2020/6/17 16:21
     * @return: {@link List< EsMerchantDTO>}
     * @version 1.1.0
     **/
    @Override
    public List<EsMerchantDTO> listByGoodsId(Long goodsId, Long merchantId, Long shopId) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (null != merchantId) {
            queryBuilder.must(QueryBuilders.termQuery(CommonConstant.MERCHANT_ID, merchantId.toString()));
        }
        if (null != shopId) {
            queryBuilder.must(QueryBuilders.termQuery("shopId", shopId.toString()));
        }
        QueryBuilder nestedQuery = QueryBuilders.nestedQuery("merchantGoodsList",
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("merchantGoodsList.goodsId.keyword", goodsId.toString())),
                ScoreMode.None);
        queryBuilder.must(QueryBuilders.boolQuery().must(nestedQuery));
        List<EsMerchantDTO> esMerchantDtoList = Collections.emptyList();
        try {
            List<EsMerchantEntity> esMerchantEntityList = elasticsearchTemplate.search(queryBuilder, EsMerchantEntity.class);
            if (BaseUtil.judgeList(esMerchantEntityList)) {
                esMerchantDtoList = BaseUtil.objToObj(esMerchantEntityList, EsMerchantDTO.class);
            }
        } catch (Exception e) {
            log.error("批量获取根据商品id异常", e);
        }
        return esMerchantDtoList;
    }

    /**
     * 更新商品嵌套文档
     *
     * @param goodsId     商户id
     * @param goodsStatus 商品上下架状态
     * @param shopId      商品名称
     * @author: wxf
     * @date: 2020/6/17 17:18
     * @version 1.1.0
     **/
    @Override
    public void updateMerchantGoodsList(Long goodsId, Integer goodsStatus, Long merchantId, Long shopId) {
        List<EsMerchantDTO> esMerchantDtoList = listByGoodsId(goodsId, merchantId, shopId);
        if (BaseUtil.judgeList(esMerchantDtoList)) {
            esMerchantDtoList.forEach(
                    i -> i.getMerchantGoodsList().forEach(
                            g -> {
                                if (goodsId.toString().equals(g.getGoodsId())) {
                                    g.setGoodsStatus(goodsStatus);
                                }
                            }
                    )
            );
            saveUpdateBatch(esMerchantDtoList);
        }
    }

    @Override
    public void saveUpdateIndexCategoryGoodsRelation(String indexCategoryId, List<String> merchantIdList, Boolean saveOrUpdate) {
        // TODO yaozou 先评估是否由必要加到ES 中，当前商家很少没有必要
    }

    @Override
    public void deleteById(Long shopId) {
        try {
            elasticsearchTemplate.deleteById(shopId.toString(), EsMerchantEntity.class);
        } catch (Exception e) {
            log.error("门店信息删除失败");
            e.printStackTrace();
        }
    }

    private Sort.Order getOrder(EsMerchantListParamDTO dto) {
        Integer searchType = dto.getSearchType();
        String field = "";
        if (WxMerchantSearchTypeEnum.PRAISE_FIRST.getStatus().equals(searchType)) {
            field = "shopGrade";
        }
        if (WxMerchantSearchTypeEnum.HIGHEST_SALES.getStatus().equals(searchType)) {
            field = "ordersNum";
        }
        return new Sort.Order(SortOrder.DESC, field);
    }

    /**
     * 通用查询条件
     *
     * @param dto 字段参数
     * @author: wxf
     * @date: 2020/6/2 9:50
     * @return: {@link BoolQueryBuilder}
     * @version 1.1.0
     **/
    private BoolQueryBuilder commonQueryBuilder(EsMerchantCommonQueryBuilderDTO dto) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 匹配城市编码
        if (!StringUtils.isEmpty(dto.getCityCode())) {
            queryBuilder.must(QueryBuilders.termQuery("esCityCode", dto.getCityCode()));
        }
        // 匹配商户id
        if (null != dto.getMerchantId()) {
            queryBuilder.must(QueryBuilders.termQuery(CommonConstant.MERCHANT_ID, dto.getMerchantId().toString()));
        }
        // 匹配商户id集合
        if (BaseUtil.judgeList(dto.getMerchantIdList())) {
            queryBuilder.must(QueryBuilders.termsQuery(CommonConstant.MERCHANT_ID, dto.getMerchantIdList()));
        }
        // 匹配商户id集合
        if (BaseUtil.judgeList(dto.getShopIdList())) {
            queryBuilder.must(QueryBuilders.termsQuery("shopId", dto.getShopIdList()));
        }
        // 商家是否有数据
        if (null != dto.getHaveGoodsFlag()) {
            queryBuilder.must(QueryBuilders.termQuery("haveGoodsFlag", dto.getHaveGoodsFlag()));
        }
        // wx只能查出审核通过商家
        queryBuilder.must(QueryBuilders.termQuery("auditStatus", 3));
        // 商家启用禁用状态
        queryBuilder.must(QueryBuilders.termQuery("merchantStatus", StatusEnum.ENABLE.getStatus()));

        //店铺
        queryBuilder.mustNot(QueryBuilders.termQuery("shopService", ServiceTypeEnum.TS.getStatus()));
        queryBuilder.must(QueryBuilders.termQuery("shopStatus", StatusEnum.ENABLE.getStatus()));

        return queryBuilder;
    }

    /**
     * 微信首页搜索查询条件
     *
     * @param dto 参数参数
     * @author: wxf
     * @date: 2020/6/9 16:28
     * @return: {@link BoolQueryBuilder}
     * @version 1.1.0
     **/
    private BoolQueryBuilder wxIndexSearchQueryBuilder(EsMerchantCommonQueryBuilderDTO dto) {
        BoolQueryBuilder filterBuilder = QueryBuilders.boolQuery();
        filterBuilder.filter(QueryBuilders.termQuery("esCityCode", dto.getCityCode()));
        filterBuilder.filter(QueryBuilders.termsQuery("shopService", Arrays.asList(1, 3)));
        filterBuilder.filter(QueryBuilders.termQuery("auditStatus", 3));
        filterBuilder.filter(QueryBuilders.termQuery("merchantStatus", 1));
        filterBuilder.filter(QueryBuilders.termQuery("shopStatus", 1));
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 这里用模糊搜索  分词搜索产品没这个需求
        String value = "*" + dto.getWxIndexSearchName() + "*";
        queryBuilder.should(QueryBuilders.wildcardQuery("merchantName", value));

        // nestedQuery 嵌套查询
        QueryBuilder nestedQuery = QueryBuilders.nestedQuery("merchantGoodsList",
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.wildcardQuery("merchantGoodsList.goodsName.keyword", value)),
                ScoreMode.None);
        queryBuilder.should(QueryBuilders.boolQuery().should(nestedQuery));

        filterBuilder.must(queryBuilder);
        return filterBuilder;
    }

    @Override
    public Long cityShopSum(String cityCode) {
        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                    // 匹配城市编码
                    .must(QueryBuilders.termQuery("esCityCode", cityCode));

            return elasticsearchTemplate.count(queryBuilder, EsMerchantEntity.class);
        } catch (Exception e) {
            log.error("是否有同城门店查询异常：{}", e);
            return 0L;
        }
    }
}
