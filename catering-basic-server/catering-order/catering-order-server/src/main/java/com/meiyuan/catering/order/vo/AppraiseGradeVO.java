package com.meiyuan.catering.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author luohuan
 * @date 2020/3/30
 **/
@Data
@ApiModel("评论-商家评分DTO")
public class AppraiseGradeVO {

    @ApiModelProperty("商家评分")
    private Double shopGrade;

    @ApiModelProperty("味道评分")
    private Double tasteGrade;

    @ApiModelProperty("服务评分")
    private Double serviceGrade;

    @ApiModelProperty("包装评分")
    private Double packGrade;
}
