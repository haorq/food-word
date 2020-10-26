package com.meiyuan.catering.merchant.dto.shop.config;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fql
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeQueryPageDTO  extends BasePageDTO {

    @ApiModelProperty("门店id")
    private Long shopId;
    @ApiModelProperty("角色id")
    private Long roleId;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("关键字：员工名称、账号、手机号；")
    private String keyword;

}
