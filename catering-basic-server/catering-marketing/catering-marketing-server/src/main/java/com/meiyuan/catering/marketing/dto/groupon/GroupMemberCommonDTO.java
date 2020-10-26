package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author luohuan
 * @date 2020/3/27
 **/
@Data
@ApiModel("团单成员公共部分DTO")
public class GroupMemberCommonDTO {
    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("用户昵称")
    private String userNickName;
}
