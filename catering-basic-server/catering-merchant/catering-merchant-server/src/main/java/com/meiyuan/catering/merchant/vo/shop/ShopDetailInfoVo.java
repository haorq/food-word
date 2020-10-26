package com.meiyuan.catering.merchant.vo.shop;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 商户店铺信息
 * @Date  2020/3/12 0012 10:32
 */
@Data
@ApiModel("商户店铺信息Vo")
public class ShopDetailInfoVo {

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 经纬度
     */
    private String mapCoordinate;
}
