package com.meiyuan.catering.merchant.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author MeiTao
 * @Description 商品推送给店铺接收参数DTO
 * @Date  2020/3/27 0027 10:33
 */
@Data
@ApiModel("商品推送给店铺接收参数DTO")
public class GoodPushShopDTO implements Serializable {
    @ApiModelProperty(value = "推送类型：1：商品推送 2：菜单推送",required = true)
    private Integer type;
    @ApiModelProperty(value = "id: 推送类型为：1则为商品id,2则为菜单id",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "固定商家还是全部商家 1-所有商家 2-指定商家",required = true)
    private Integer fixedOrAll;

    @ApiModelProperty("省编码")
    private String addressProvinceCode;

    @ApiModelProperty("市编码")
    private String addressCityCode;

    @ApiModelProperty("区编码")
    private String addressAreaCode;

    @ApiModelProperty("商家名称")
    private String shopName;

    @ApiModelProperty("关键字：联系人，联系电话")
    private String keyword;
}
