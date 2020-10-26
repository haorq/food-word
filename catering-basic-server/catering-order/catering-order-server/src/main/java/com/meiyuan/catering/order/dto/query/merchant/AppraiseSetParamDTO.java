package com.meiyuan.catering.order.dto.query.merchant;

import com.meiyuan.catering.order.dto.MerchantBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 评价设置请求参数
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ApiModel("评价设置请求参数——商户")
public class AppraiseSetParamDTO extends MerchantBaseDTO {
    @ApiModelProperty(value = "评价表Id",required = true)
    @NotNull(message = "评价Id不能为空")
    private Long appraiseId;
    @ApiModelProperty(value = "是否展示（0：展示，1：不展示）",required = true)
    @NotNull(message = "是否展示字段不能为空")
    private Boolean show;
}
