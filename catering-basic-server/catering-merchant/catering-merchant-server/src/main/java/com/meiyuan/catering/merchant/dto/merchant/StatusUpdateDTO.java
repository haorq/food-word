package com.meiyuan.catering.merchant.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 商户/店铺 禁用启用 参数DTO
 * @Date  2020/3/12 0012 16:20
 */
@Data
@ApiModel("商户/店铺 禁用启用 参数DTO")
public class StatusUpdateDTO {
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("1：启用，2：禁用")
    private Integer status;
}
