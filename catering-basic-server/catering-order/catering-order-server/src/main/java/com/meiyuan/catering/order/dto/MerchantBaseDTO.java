package com.meiyuan.catering.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yaoozu
 * @description 用户token信息
 * @date 2020/3/2013:51
 * @since v1.0.0
 */
@Data
public class MerchantBaseDTO implements Serializable {
    @ApiModelProperty(value = "【非填-系统自行填充】商户ID", hidden = true)
    private Long merchantId;
    @ApiModelProperty(value = "【非填-系统自行填充】门店ID", hidden = true)
    private Long shopId;
    @ApiModelProperty(value = "【非填-系统自行填充】门店名称", hidden = true)
    private String shopName;
    @ApiModelProperty(value = "【非填-系统自行填充】类型：1--店铺，2--自提点，3--既是店铺也是自提点", hidden = true)
    private Integer type;
}
