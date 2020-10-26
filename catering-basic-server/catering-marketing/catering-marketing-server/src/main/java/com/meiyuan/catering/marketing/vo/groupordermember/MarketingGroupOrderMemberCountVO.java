package com.meiyuan.catering.marketing.vo.groupordermember;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/08/21 15:08
 * @description 团购订单成员统计VO
 **/

@Data
@ApiModel(value = "团购订单成员统计VO")
public class MarketingGroupOrderMemberCountVO {

    @ApiModelProperty(value = "团购单ID")
    private Long groupOrderId;
    @ApiModelProperty(value = "团购单成员统计数量")
    private Integer memberCount;

}
