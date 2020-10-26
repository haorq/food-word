package com.meiyuan.catering.order.dto.query.wx;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author luohuan
 * @date 2020/3/30
 **/
@Data
@ApiModel("评论查询DTO")
public class AppraiseQueryDTO extends BasePageDTO {

    @NotNull(message = "商户ID不能为空")
    @ApiModelProperty("商户ID(必填)")
    private Long merchantId;
    @NotNull(message = "门店ID不能为空")
    @ApiModelProperty("门店ID(必填)")
    private Long shopId;

    @ApiModelProperty("查询类型（1：全部，2：好评，3：有图，4：差评）")
    private Integer type;
}
