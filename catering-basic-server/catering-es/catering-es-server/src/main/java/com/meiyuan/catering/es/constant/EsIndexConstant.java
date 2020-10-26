package com.meiyuan.catering.es.constant;


/**
 * @author wxf
 * @date 2019/10/19
 * @description es索引常量
 */
public class EsIndexConstant {
    public final static String GOODS_INDEX = "catering_goods_v2";
    public final static String MARKETING = "catering_marketing_v1";
    public final static String MERCHANT = "catering_merchant_v1";
    public final static String GOODS_INDEX_V2 = "catering_goods_v2";
    public static String[] getMerchantIndex() {
        return new String[]{MERCHANT};
    }

    public static String[] getMarketingIndex() {
        return new String[]{MARKETING};
    }

    public static String[] getGoodsIndex() {
        return new String[]{GOODS_INDEX};
    }

    public static String[] getGoodsIndex2() {
        return new String[]{GOODS_INDEX_V2};
    }

}
