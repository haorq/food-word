package com.meiyuan.catering.merchant.dto.pickup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
  * @Author MeiTao
  * @Date 2020/5/19 0019 9:51
  * @Description 简单描述 : 店铺自提赠品
  * @Since version-1.0.0
  */
 @Data
 @ApiModel("自提点修改参数DTO")
public class SelfMentionGiftDTO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("自提送赠品活动id")
    private Long pickupId;

    @ApiModelProperty("赠品id")
    private Long giftId;

    @ApiModelProperty("数量")
    private Integer number;

    @ApiModelProperty("0-否 1-是")
    private Boolean del;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    private Long updateBy;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}
