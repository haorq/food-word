package com.meiyuan.catering.user.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 用户登录信息
 * @date 2020/5/1917:37
 * @since v1.0.0
 */
@Data
public class UserLoginDTO {
    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(hidden = true)
    private String nickname;
    @ApiModelProperty(hidden = true)
    private String avatar;
}
