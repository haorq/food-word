package com.meiyuan.catering.wx.dto.marketing;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName H5PullTicketParamDTO
 * @Description
 * @Author gz
 * @Date 2020/9/29 9:55
 * @Version 1.5.0
 */
@Data
public class H5PullTicketParamDTO {
    @NotNull(message = "活动ID不能为空")
    @ApiModelProperty(value = "活动ID")
    private Long id;
    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号")
    private String phone;
    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码")
    private String code;
}
