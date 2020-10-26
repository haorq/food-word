package com.meiyuan.catering.es.constant;


import com.google.common.collect.Lists;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.SaleChannelsEnum;
import com.meiyuan.catering.core.enums.base.ServiceTypeEnum;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.es.enums.goods.GoodsStatusEnum;
import com.meiyuan.catering.es.enums.marketing.MarketingStatusEnum;
import com.meiyuan.catering.es.enums.marketing.MarketingUpDownStatusEnum;
import com.meiyuan.catering.es.enums.marketing.MarketingUsingObjectEnum;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Collection;

/**
 * @author zengzhangni
 * @date 2020/9/4 15:02
 * @since v1.4.0
 */
public class MarketingHelper {

    public final static String ES_CITY_CODE = "esCityCode";
    public final static String OBJECT_LIMIT = "objectLimit";
    public final static String SHOP_SERVICE_TYPE = "shopServiceType";
    public final static String MERCHANT_STATE = "merchantState";
    public final static String SHOP_STATE = "shopState";
    public final static String OF_TYPE = "ofType";
    public final static String ACTIVITY_PRICE = "activityPrice";
    public final static String UP_DOWN_STATE = "upDownState";
    public final static String GOODS_UP_DOWN_STATE = "goodsUpDownState";
    public final static String BEGIN_TIME = "beginTime";
    public final static String END_TIME = "endTime";
    public final static String GOODS_SALES_CHANNELS = "goodsSalesChannels";
    public final static String SKILL_EVENT_ID = "seckillEventIds.keyword";

    /**
     * 描述:活动基础过滤条件
     *
     * @since v1.4.0
     */
    public static void marketingBaseQuery(BoolQueryBuilder queryBuilder) {
        // 未删除
        noDel(queryBuilder);
        // 上架
        upDownState(queryBuilder);
        // 商品上架
        goodsUpDownState(queryBuilder);
        //品牌启用
        merchantState(queryBuilder);
        //门店启用
        shopState(queryBuilder);
        //支持小程序
        shopServiceType(queryBuilder);
        //排除堂食
        goodsSaleChannel(queryBuilder);
    }

    public static void goodsSaleChannel(BoolQueryBuilder queryBuilder) {
        queryBuilder.must(QueryBuilders.termsQuery(MarketingHelper.GOODS_SALES_CHANNELS,
                Lists.newArrayList(SaleChannelsEnum.TAKEOUT.getStatus(), SaleChannelsEnum.ALL.getStatus())));
    }

    /**
     * 描述:进行中
     *
     * @since v1.4.0
     */
    public static void ongoing(BoolQueryBuilder queryBuilder, long milliSecond) {
        queryBuilder.must(QueryBuilders.rangeQuery(MarketingHelper.BEGIN_TIME).lte(milliSecond))
                .must(QueryBuilders.rangeQuery(MarketingHelper.END_TIME).gt(milliSecond));
    }

    public static void ongoing(BoolQueryBuilder queryBuilder) {
        Long milliSecond = BaseUtil.milliSecond();
        queryBuilder.must(QueryBuilders.rangeQuery(MarketingHelper.BEGIN_TIME).lte(milliSecond))
                .must(QueryBuilders.rangeQuery(MarketingHelper.END_TIME).gt(milliSecond));
    }

    /**
     * 描述:未开始
     *
     * @since v1.4.0
     */
    public static void notStart(BoolQueryBuilder queryBuilder, long milliSecond) {
        queryBuilder.must(QueryBuilders.rangeQuery(MarketingHelper.BEGIN_TIME).gt(milliSecond));
    }

    /**
     * 描述:已结束
     *
     * @since v1.4.0
     */
    public static void ended(BoolQueryBuilder queryBuilder, long milliSecond) {
        queryBuilder.must(QueryBuilders.rangeQuery(MarketingHelper.END_TIME).lte(milliSecond));
    }

    /**
     * 描述:用户类型过滤
     *
     * @since v1.4.0
     */
    public static void objectLimit(BoolQueryBuilder queryBuilder, Integer objectLimit) {
        queryBuilder.must(QueryBuilders.termsQuery(MarketingHelper.OBJECT_LIMIT,
                Lists.newArrayList(objectLimit, MarketingUsingObjectEnum.ALL.getStatus())));
    }

    /**
     * 描述:活动类型
     *
     * @since v1.4.0
     */
    public static void ofType(BoolQueryBuilder queryBuilder, Integer ofType) {
        queryBuilder.must(QueryBuilders.termQuery(OF_TYPE, ofType));
    }

    /**
     * 描述:品牌状态为启用
     *
     * @since v1.4.0
     */
    public static void merchantState(BoolQueryBuilder queryBuilder) {
        queryBuilder.must(QueryBuilders.termQuery(MERCHANT_STATE, StatusEnum.ENABLE.getStatus()));
    }

    /**
     * 描述:门店状态为启用
     *
     * @since v1.4.0
     */
    public static void shopState(BoolQueryBuilder queryBuilder) {
        queryBuilder.must(QueryBuilders.termQuery(SHOP_STATE, StatusEnum.ENABLE.getStatus()));
    }

    /**
     * 描述:门店id 过滤
     *
     * @since v1.4.0
     */
    public static void shopIds(BoolQueryBuilder queryBuilder, Collection<String> appointShopIds) {
        queryBuilder.must(QueryBuilders.termsQuery(CommonConstant.SHOP_ID, appointShopIds));
    }

    /**
     * 描述:商品是上架状态
     *
     * @since v1.4.0
     */
    public static void goodsUpDownState(BoolQueryBuilder queryBuilder) {
        queryBuilder.must(QueryBuilders.termQuery(MarketingHelper.GOODS_UP_DOWN_STATE, GoodsStatusEnum.UPPER_SHELF.getStatus()));
    }

    /**
     * 描述:活动是上架状态
     *
     * @since v1.4.0
     */
    public static void upDownState(BoolQueryBuilder queryBuilder) {
        queryBuilder.must(QueryBuilders.termQuery(MarketingHelper.UP_DOWN_STATE, MarketingUpDownStatusEnum.UP.getStatus()));
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
     * 描述:门店服务类型有小程序的
     *
     * @since v1.4.0
     */
    public static void shopServiceType(BoolQueryBuilder queryBuilder) {
        queryBuilder.must(QueryBuilders.termsQuery(MarketingHelper.SHOP_SERVICE_TYPE,
                Lists.newArrayList(ServiceTypeEnum.WX_TS.getStatus(), ServiceTypeEnum.WX.getStatus())));
    }

    public static void skillEvent(BoolQueryBuilder queryBuilder, Long eventId) {
        String value = "*" + eventId + "*";
        queryBuilder.must(QueryBuilders.wildcardQuery(MarketingHelper.SKILL_EVENT_ID, value));
    }

    /**
     * 描述:活动状态
     *
     * @param queryBuilder
     * @param status
     * @return void
     * @author zengzhangni
     * @date 2020/9/4 16:09
     * @since v1.4.0
     */
    public static void marketingStatus(BoolQueryBuilder queryBuilder, Integer status) {
        long milliSecond = BaseUtil.milliSecond();
        MarketingStatusEnum statusEnum = MarketingStatusEnum.parse(status);
        switch (statusEnum) {
            case ONGOING:
                // 进行中
                ongoing(queryBuilder, milliSecond);
                break;
            case NOT_START:
                // 未开始
                notStart(queryBuilder, milliSecond);
                break;
            case ENDED:
                // 已结束
                ended(queryBuilder, milliSecond);
                break;
            default:
                break;
        }
    }


}
