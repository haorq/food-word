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
@ApiModel("评价回复请求参数——商户")
public class AppraiseReplyParamDTO extends MerchantBaseDTO {
    @ApiModelProperty(value = "评价表Id", required = true)
    @NotNull(message = "评价Id不能为空")
    private Long appraiseId;

    @ApiModelProperty(value = "评价回复内容", required = true)
    @NotNull(message = "评价回复内容")
    private String reply;
}
