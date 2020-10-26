package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author luohuan
 * @date 2020/3/27
 **/
@Data
@ApiModel("团单成员DTO")
public class GroupOrderMemberDTO {
    @ApiModelProperty("团单数据表主键id")
    private Long groupOrderId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("成员id")
    private Long memberId;

    @ApiModelProperty("成员名称")
    private String memberName;

    @ApiModelProperty("成员昵称")
    private String memberNickName;

    @ApiModelProperty("是否是发起人")
    private Boolean isSponsor;
}
