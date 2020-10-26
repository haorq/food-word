package com.meiyuan.catering.merchant.dto.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 商户删除/恢复 参数DTO
 * @Date  2020/3/12 0012 16:20
 */
@Data
@ApiModel("商户删除/恢复参数DTO")
public class MerchantRemoveDTO {
    @ApiModelProperty("商户ids")
    @NotNull
    private List<Long> ids;
    @ApiModelProperty("1：删除，0：恢复")
    @NotNull
    private Integer type;
}
