package com.meiyuan.catering.core.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author MeiTao
 * @Description 商户店铺标签信息
 * @Date  2020/3/24 0024 14:03
 */
@Data
public class ShopTagInfoDTO implements Serializable {
    private static final long serialVersionUID = 112533856650454426L;

    @ApiModelProperty("标签名称")
    private String tagName;
    @ApiModelProperty("标签id")
    private Long id;
}
