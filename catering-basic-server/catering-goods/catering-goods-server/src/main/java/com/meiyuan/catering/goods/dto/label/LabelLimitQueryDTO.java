package com.meiyuan.catering.goods.dto.label;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/3/12 14:58
 * @description 标签列表分页查询参数DTO
 **/
@Data
@ApiModel("标签列表分页查询参数模型")
public class LabelLimitQueryDTO extends BasePageDTO {
    @ApiModelProperty("标签名称")
    private String labelName;
}
