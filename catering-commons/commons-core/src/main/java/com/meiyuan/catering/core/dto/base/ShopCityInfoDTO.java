package com.meiyuan.catering.core.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author MeiTao
 * @Date 2020/6/11 0011 13:56
 * @Description 简单描述 : 店铺所在市DTO
 * @Since version-1.0.0
 */
@Data
@ApiModel("店铺所在市DTO")
public class ShopCityInfoDTO implements Serializable {
    private static final long serialVersionUID = 4133575901582606197L;

    @ApiModelProperty("市名称")
    private String addressCity;
    @ApiModelProperty("省名称")
    private String addressProvince;
    @ApiModelProperty("市编码")
    private String addressCityCode;
    @ApiModelProperty("省编码")
    private String addressProvinceCode;
    @ApiModelProperty("市中心对应经纬度")
    private String mapCoordinate;
}
