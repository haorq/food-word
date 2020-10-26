package com.meiyuan.catering.goods.dto.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/3/31 18:02
 * @description 简单描述
 **/
@Data
@ApiModel("菜单送达时间查询模型")
public class WxMenuServiceTimeQueryDTO {
    @ApiModelProperty("售卖模式 1-菜单售卖模式 2-商品售卖模式 3-团购商品")
    private Integer sellType;
    @ApiModelProperty("对应id")
    private Long id;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("门店id【1.2.0】")
    private Long shopId;

    @ApiModelProperty("品牌属性:1:自营，2:非自营【1.2.0】")
    private Integer merchantAttribute;

    @ApiModelProperty("用户收货地址经纬度[经度，纬度]【1.5.0】")
    private String location;
}
