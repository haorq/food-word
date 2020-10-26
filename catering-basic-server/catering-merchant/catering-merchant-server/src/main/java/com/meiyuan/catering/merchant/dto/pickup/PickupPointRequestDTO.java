package com.meiyuan.catering.merchant.dto.pickup;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 自提点列表查询接收参数DTO
 * @Date  2020/3/24 0024 16:24
 */
@Data
@ApiModel("自提点列表查询接收参数DTO")
public class PickupPointRequestDTO {
    @ApiModelProperty(value = "店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "关键字：自提点名称，负责人手机号")
    private String keyWord;

    @ApiModelProperty(value = "类型:1:自提点管理列表查询，2：绑定自提点列表查询")
    private Integer type;

}
