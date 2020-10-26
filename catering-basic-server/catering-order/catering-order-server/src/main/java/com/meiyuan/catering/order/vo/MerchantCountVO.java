package com.meiyuan.catering.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author luohuan
 * @date 2020/3/30
 **/
@Data
@ApiModel("评价统计信息VO")
public class MerchantCountVO implements Serializable {
    private static final long serialVersionUID = 1758270445581609805L;
    @ApiModelProperty("月售订单数")
    private Integer ordersNum;

    @ApiModelProperty("商家评分")
    private Double shopGrade;

    @ApiModelProperty("味道评分")
    private Double tasteGrade;

    @ApiModelProperty("服务评分")
    private Double serviceGrade;

    @ApiModelProperty("包装评分")
    private Double packGrade;

    @ApiModelProperty("评论总数量")
    private Integer totalNumber;

    @ApiModelProperty("好评数量")
    private Integer praiseNumber;

    @ApiModelProperty("差评数量")
    private Integer negativeNumber;

    @ApiModelProperty("有图评论数量")
    private Integer pictureNumber;
}
