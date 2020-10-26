package com.meiyuan.catering.merchant.enums;

import lombok.Getter;

/**
 * 品牌属性:1:自营（今天下单，明天送），2：非自营（今天下单，今天送）
 * @author lh
 */
@Getter
public enum MerchantAttribute {
    /**
     * 自营（今天下单，明天送。预售）
     */
    SELF_BUSINESS(Values.SELF_BUSINESS,Values.SELF_BUSINESS_DESC),
    /**
     * 非自营（今天下单，今天送）
     */
    NOT_SELF_BUSINESS(Values.NOT_SELF_BUSINESS,Values.NOT_SELF_BUSINESS_DESC)
    ;


    private Integer businessAttribute;
    private String businessAttributeDesc;

    MerchantAttribute(Integer businessAttribute, String businessAttributeDesc) {
        this.businessAttribute = businessAttribute;
        this.businessAttributeDesc = businessAttributeDesc;
    }

    public static class  Values{
        private static final Integer SELF_BUSINESS = 1;
        private static final String SELF_BUSINESS_DESC = "自营（今天下单，明天送。预售）";
        private static final Integer NOT_SELF_BUSINESS = 2;
        private static final String NOT_SELF_BUSINESS_DESC = "非自营（今天下单，今天送）";
    }

}
