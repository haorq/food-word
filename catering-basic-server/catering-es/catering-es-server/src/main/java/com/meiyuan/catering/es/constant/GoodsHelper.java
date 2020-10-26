package com.meiyuan.catering.es.constant;


import com.google.common.collect.Lists;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.SaleChannelsEnum;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.es.enums.goods.GoodsStatusEnum;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;
import java.util.Set;

/**
 * @author zengzhangni
 * @date 2020/9/4 15:02
 * @since v1.4.0
 */
public class GoodsHelper {

    public final static String ES_CITY_CODE = "esCityCode";
    public final static String GOODS_ID = "goodsId";
    public final static String SKU_LIST = "skuList";
    public final static String SALES_CHANNELS = "skuList.salesChannels";
    public final static String MERCHANT_GOODS_STATUS = "merchantGoodsStatus";
    public final static String GOODS_STATUS = "goodsStatus";
    public final static String SPECIAL_STATE = "specialState";
    public final static String SPECIAL_NUMBER = "specialNumber";
    public final static String ENTERPRISE_SPECIAL_NUMBER = "enterpriseSpecialNumber";
    public final static List<Integer> WX_SALES_CHANNELS = Lists.newArrayList(
            SaleChannelsEnum.TAKEOUT.getStatus(),
            SaleChannelsEnum.ALL.getStatus());


    /**
     * 描述:活动基础过滤条件
     *
     * @since v1.4.0
     */
    public static void goodsBaseQuery(BoolQueryBuilder queryBuilder) {
        //未删除  商品使用物理删除  不需要此条件
//        noDel(queryBuilder);
        // 品牌上架
        merchantGoodsUpDownStatus(queryBuilder);
        // 门店上架
        goodsUpDownStatus(queryBuilder);
        //支持小程序
        salesChannels(queryBuilder);
    }

    /**
     * 描述:未删除
     *
     * @since v1.4.0
     */
    public static void noDel(BoolQueryBuilder queryBuilder) {
        queryBuilder.must(QueryBuilders.termQuery(CommonConstant.DEL, DelEnum.NOT_DELETE.getFlag()));
    }


    /**
     * 描述:商品上架状态
     *
     * @since v1.4.0
     */
    public static void goodsUpDownStatus(BoolQueryBuilder queryBuilder) {
        queryBuilder.must(QueryBuilders.termQuery(GOODS_STATUS, GoodsStatusEnum.UPPER_SHELF.getStatus()));
    }

    /**
     * 描述:品牌商品上架状态
     *
     * @since v1.4.0
     */
    public static void merchantGoodsUpDownStatus(BoolQueryBuilder queryBuilder) {
        queryBuilder.must(QueryBuilders.termQuery(MERCHANT_GOODS_STATUS, GoodsStatusEnum.UPPER_SHELF.getStatus()));
    }


    /**
     * 描述:商品规格支持小程序的
     *
     * @since v1.4.0
     */
    public static void salesChannels(BoolQueryBuilder queryBuilder) {
        QueryBuilder nestedQuery = QueryBuilders.nestedQuery(SKU_LIST,
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.termsQuery(SALES_CHANNELS, WX_SALES_CHANNELS)),
                ScoreMode.None);
        queryBuilder.must(nestedQuery);
    }

    public static void matchShopIds(BoolQueryBuilder queryBuilder, Set<String> matchShopIds) {
        queryBuilder.must(QueryBuilders.termsQuery(CommonConstant.SHOP_ID, matchShopIds));
    }

    public static void matchShopId(BoolQueryBuilder queryBuilder, Long shopId) {
        queryBuilder.must(QueryBuilders.termQuery(CommonConstant.SHOP_ID, shopId));
    }

    public static void specialState(BoolQueryBuilder queryBuilder, boolean b) {
        queryBuilder.must(QueryBuilders.termQuery(SPECIAL_STATE, b));
    }

    public static void specialState(BoolQueryBuilder queryBuilder) {
        queryBuilder.must(QueryBuilders.termQuery(SPECIAL_STATE, true));
    }


    public static String getMetricName() {
        return SPECIAL_NUMBER;
    }

    public static void skuCode(BoolQueryBuilder queryBuilder, String skuCode) {
        QueryBuilder nestedQuery = QueryBuilders.nestedQuery(SKU_LIST,
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("skuList.skuCode.keyword", skuCode)),
                ScoreMode.None);
        queryBuilder.must(nestedQuery);
    }

    public static void special0To10(BoolQueryBuilder queryBuilder) {
        NestedQueryBuilder nested = QueryBuilders.nestedQuery(SKU_LIST,
                QueryBuilders.rangeQuery("skuList.specialNumber").gte(BaseUtil.D_01).lt(BaseUtil.D_10)
                , ScoreMode.None);
        queryBuilder.must(nested);
    }
}
