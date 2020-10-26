package com.meiyuan.catering.goods.dto.category;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/3/11 17:51
 * @description 类目列表分页查询参数DTO
 **/
@Data
@ApiModel("类目列表分页查询参数模型")
public class CategoryLimitQueryDTO extends BasePageDTO {
    @ApiModelProperty("类目名称")
    private String categoryName;

    @ApiModelProperty(value = "商户ID",hidden = true)
    private Long merchantId;

    @ApiModelProperty(value = "类目状态",hidden = true)
    private Integer status;
    @ApiModelProperty("排序字段 sort-排序号")
    private String sortField;
    @ApiModelProperty("排序顺序 desc-降序 asc-升序")
    private String sortOrder;
}
