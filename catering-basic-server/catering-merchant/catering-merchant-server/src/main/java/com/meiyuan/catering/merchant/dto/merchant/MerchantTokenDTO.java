package com.meiyuan.catering.merchant.dto.merchant;

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
public class MerchantTokenDTO implements Serializable {
    private static final long serialVersionUID = -6430309004461626468L;
    @ApiModelProperty(hidden = true)
    private String token;
    @ApiModelProperty(hidden = true)
    private Long merchantId;
    @ApiModelProperty(value = "门店id",hidden = true)
    private Long shopId;
    @ApiModelProperty(hidden = true)
    private String shopName;
    /**
     * 登录账号类型：1--店铺，2--自提点，3--既是店铺也是自提点，4：员工
     */
    @ApiModelProperty(hidden = true)
    private Integer type;

    @ApiModelProperty(value = "当前登录账号对应id",hidden = true)
    private Long accountTypeId;

    /**
     * 门店类型：1-自营 2-入驻 3-自提点
     */
    @ApiModelProperty(hidden = true)
    private Integer shopType;

    @ApiModelProperty(value = "退出登录类型： 1： 正常登录，2 ：修改密码，3：修改登录手机号",hidden = true)
    private Integer logBackType;
}
