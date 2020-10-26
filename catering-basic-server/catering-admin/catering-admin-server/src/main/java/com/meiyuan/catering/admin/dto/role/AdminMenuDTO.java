package com.meiyuan.catering.admin.dto.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName AdminMenuDTO
 * @Description
 * @Author gz
 * @Date 2020/9/29 9:21
 * @Version 1.5.0
 */
@Data
public class AdminMenuDTO {
    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "菜单名称")
    private String menuName;
    @ApiModelProperty(value = "父ID")
    private Long parentId;
    @ApiModelProperty(value = "标识")
    private String code;
    @ApiModelProperty(value = "类型：0-商户PC；1-app；2-平台")
    private Integer type;
    @ApiModelProperty(value = "链接")
    private String url;
}
