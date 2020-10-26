package com.meiyuan.catering.marketing.dto.recommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author luohuan
 * @date 2020/3/20
 **/
@Data
@ApiModel("推荐有奖的优惠券DTO")
public class RecommendPrizeTicketDTO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("推荐有奖活动ID")
    private Long recommendPrizeId;

    @ApiModelProperty("推荐人优惠券ID")
    private Long referrerTicketId;

    @ApiModelProperty("被推荐人优惠券ID")
    private Long referralTicketId;

}
