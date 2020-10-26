package com.meiyuan.catering.user.query.integral;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/3/16
 */
@Data
@ApiModel("积分记录列表查询DTO")
public class IntegralRecordQueryDTO extends BasePageDTO {

    @ApiModelProperty("关键字 用户姓名/昵称/手机号")
    private String keyword;
    @ApiModelProperty("1：企业用户，2：个人用户")
    private Integer userType;
    @ApiModelProperty("积分规则编码")
    private String ruleNo;
}
