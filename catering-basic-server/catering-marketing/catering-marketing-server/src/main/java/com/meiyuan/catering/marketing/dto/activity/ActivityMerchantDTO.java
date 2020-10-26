package com.meiyuan.catering.marketing.dto.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/10 13:59
 */
@Data
@ApiModel("活动效果-查询参与品牌参数")
public class ActivityMerchantDTO extends BasePageDTO {

    @ApiModelProperty(value = "id 活动id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
}
