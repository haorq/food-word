package com.meiyuan.catering.merchant.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Description  商户商品推送店铺下拉查询参数DTO
 * @Date  2020/3/27 0027 0:49
 */
@Data
@ApiModel("商户商品推送店铺下拉查询参数DTO")
public class GoodPushShopPagDTO extends BasePageDTO {

    @ApiModelProperty("省编码")
    private String addressProvinceCode;

    @ApiModelProperty("市编码")
    private String addressCityCode;

    @ApiModelProperty("区编码")
    private String addressAreaCode;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("关键字：店铺名称，店铺编号")
    private String keyword;

    @ApiModelProperty(value = "商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    @ApiModelProperty(value = "shopIds")
    private List<Long> shopIds;

    @ApiModelProperty(value = "移除的门店id")
    private List<Long> removeShopIds;

    @ApiModelProperty("菜单id")
    private Long menuId;

}
