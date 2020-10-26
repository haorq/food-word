package com.meiyuan.catering.marketing.vo.groupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author luohuan
 * @date 2020/3/16
 **/
@Data
@ApiModel("团购列表VO")
public class GrouponListVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("活动ID")
    private Long id;

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("活动状态（1：未开始，2：进行中，3：已结束）")
    private Integer status;

    @ApiModelProperty("上架/下架（1：下架，2：上架）")
    private Integer upDown;

    @ApiModelProperty("商家名称")
    private String merchantName;
    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
