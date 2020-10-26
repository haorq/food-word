package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/24 15:08
 * @description 团购失败消息DTO
 **/

@Data
@ApiModel(value = "团购失败消息DTO")
public class GrouponFailMsgDTO {

    @ApiModelProperty(value = "失败的订单编号")
    private List<String> failureOrderNumbers;
    @ApiModelProperty(value = "是否是后台操作失败 true-是 false-不是(自动)")
    private Boolean isInitiative;

}
