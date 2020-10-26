package com.meiyuan.catering.core.util.dada.domain.merchant;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiyuan.catering.core.util.dada.domain.BaseModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DATE: 18/9/2
 *
 * @author: wan
 */
@Data
public class ShopAddModel extends BaseModel {

    @JSONField(name = "origin_shop_id")
    private String originShopId;

    @JSONField(name = "station_name")
    private String stationName;

    private Integer business;

    @JSONField(name = "city_name")
    private String cityName;

    @JSONField(name = "area_name")
    private String areaName;

    @JSONField(name = "station_address")
    private String stationAddress;

    private BigDecimal lng;

    private BigDecimal lat;

    @JSONField(name = "contact_name")
    private String contactName;

    private String phone;

}
