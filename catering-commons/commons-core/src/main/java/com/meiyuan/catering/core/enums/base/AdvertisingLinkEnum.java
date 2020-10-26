package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * description：广告跳转地址枚举
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/9 16:45
 */
@Getter
public enum AdvertisingLinkEnum {
    /**
     *团购
     **/
    GROUP_BUYING(Values.GROUP_BUYING, "团购"),
    /**
     *推荐有奖
     **/
    RECOMMEND(Values.RECOMMEND, "推荐有奖"),
    /**
     *秒杀
     **/
    SEC_KILL(Values.SEC_KILL, "秒杀"),
    /**
     *指定门店
     **/
    APPOINT_SHOP(Values.APPOINT_SHOP, "指定门店"),
    /**
     *指定商品
     **/
    APPOINT_GOODS(Values.APPOINT_GOODS, "指定商品");

    private String status;
    private String desc;
    AdvertisingLinkEnum(String status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static AdvertisingLinkEnum parse(String status) {
        for (AdvertisingLinkEnum type : values()) {
            if (type.status.equals(status)) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class Values{
        private static final String GROUP_BUYING = "1247406091457794050";
        private static final String RECOMMEND = "1247444003448688642";
        private static final String SEC_KILL = "1247448563881873410";
        private static final String APPOINT_SHOP = "1";
        private static final String APPOINT_GOODS = "2";
    }
}
