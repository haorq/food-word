package com.meiyuan.catering.user.dto.integral;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zengzhangni
 * @date 2020/3/18
 */
@Data
@ApiModel("积分活动添加编辑DTO")
public class AddIntegralActivityDTO extends IdEntity {

    @ApiModelProperty("积分活动名称")
    private String name;
    @ApiModelProperty("积分规则编码")
    private String integralNo;
    @ApiModelProperty("积分数量")
    private Integer integral;
    @ApiModelProperty("积分有效期(天) -1:长期")
    private Integer day;
    @ApiModelProperty("积分对象 0:所有 1:企业 2:个人")
    private Integer userType;
    @ApiModelProperty("状态，0为正常，1为禁用")
    private Integer status;
    @ApiModelProperty("积分说明")
    private String remark;
}
