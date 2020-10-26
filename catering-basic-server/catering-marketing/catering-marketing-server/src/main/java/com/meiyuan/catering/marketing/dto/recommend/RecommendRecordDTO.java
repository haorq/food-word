package com.meiyuan.catering.marketing.dto.recommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author luohuan
 * @date 2020/3/26
 **/
@Data
@ApiModel("推荐记录DTO")
public class RecommendRecordDTO {

    @ApiModelProperty("推荐记录ID")
    private Long id;

    @ApiModelProperty("推荐人ID")
    private Long referrerId;

    @ApiModelProperty(value = "推荐人用户类型")
    private Integer referrerType;

    @ApiModelProperty("被推荐人ID")
    private Long referralId;
}
