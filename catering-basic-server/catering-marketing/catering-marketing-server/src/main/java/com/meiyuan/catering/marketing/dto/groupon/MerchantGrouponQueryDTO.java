package com.meiyuan.catering.marketing.dto.groupon;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author luohuan
 * @date 2020/3/16
 **/
@Data
@ApiModel("商户团购查询DTO")
public class MerchantGrouponQueryDTO extends BasePageDTO {

    @ApiModelProperty("活动状态（1：未开始，2：进行中，3：已结束）")
    private Integer status;
}
