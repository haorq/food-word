package com.meiyuan.catering.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author luohuan
 * @date 2020/3/30
 **/
@Data
@ApiModel("评论数量统计VO")
public class AppraiseNumberVO {

    @ApiModelProperty("评论总数量")
    private Integer totalNumber;

    @ApiModelProperty("好评数量")
    private Integer praiseNumber;

    @ApiModelProperty("差评数量")
    private Integer negativeNumber;

    @ApiModelProperty("有图评论数量")
    private Integer pictureNumber;
}
