package com.meiyuan.catering.admin.dto.admin.admin;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商户角色分页查询参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonRolePageListParamsDTO extends BasePageDTO {

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "店铺id",hidden = true)
    private Long shopId;
}
