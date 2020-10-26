package com.meiyuan.catering.admin.dto.wxcategory;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/2 9:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("小程序类目分页查询条件")
public class WxCategoryPageDTO extends BasePageDTO {

    @ApiModelProperty("类目 1:导航栏 2:推荐区 3:爆品推荐")
    private Integer type;

    @ApiModelProperty("类目名称")
    private String name;

    @ApiModelProperty(value = "状态 1:启用 0:禁用")
    private Integer status;
}
