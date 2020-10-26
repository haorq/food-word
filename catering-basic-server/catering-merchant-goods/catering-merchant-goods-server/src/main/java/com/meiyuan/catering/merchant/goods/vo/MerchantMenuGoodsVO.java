package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * description：销售菜单列表返回参数
 * @author yy
 * @date 2020/7/7
 */
@Data
@ApiModel("销售菜单-分页列表")
public class MerchantMenuGoodsVO {

    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "适用门店")
    private String shopName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
