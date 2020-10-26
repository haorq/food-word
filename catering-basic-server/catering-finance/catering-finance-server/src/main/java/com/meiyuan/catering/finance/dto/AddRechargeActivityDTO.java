package com.meiyuan.catering.finance.dto;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-19
 */
@Data
@ApiModel("添加充值活动DTO")
public class AddRechargeActivityDTO extends IdEntity {

    @ApiModelProperty("活动名称")
    private String name;
    @ApiModelProperty("金额列表")
    @Size(max = 9, min = 1, message = "超过最高充值信息设置")
    @Valid
    private List<AccountListDTO> list;
    @ApiModelProperty("活动开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty("活动结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty("活动说明")
    private String remark;
    @ApiModelProperty("状态,0--进行中  1:未开始  2:已结束")
    private Integer status;
    @ApiModelProperty("是否上架 true:是  false:否")
    private Boolean up;
    @ApiModelProperty("活动对象  0:所有 1:企业 2:个人")
    private Integer userType;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


}
