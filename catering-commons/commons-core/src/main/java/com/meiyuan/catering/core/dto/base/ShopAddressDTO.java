package com.meiyuan.catering.core.dto.base;

import lombok.Data;

/**
 * @Author MeiTao
 * @Description 商户店铺位置信息
 * @Date  2020/3/24 0024 14:03
 */
@Data
public class ShopAddressDTO{
    /**
     * 店铺id
     */
    private Long id;
    /**
     * 经纬度
     */
    private String mapCoordinate;

}
