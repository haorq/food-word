package com.meiyuan.catering.order.dto.query.wx;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单评价表(CateringOrdersAppraise)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ApiModel("我的评价请求参数——微信")
public class MyAppraiseParamDTO extends BasePageDTO {

    @ApiModelProperty(value = "会员id", hidden = true)
    private Long memberId;

}
