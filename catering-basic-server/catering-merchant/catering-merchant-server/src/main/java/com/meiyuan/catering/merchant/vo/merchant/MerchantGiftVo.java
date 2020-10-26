package com.meiyuan.catering.merchant.vo.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 查询商户 赠品结果VO
 * @Date 2020/3/12 0012 10:32
 */
@Data
@ApiModel("查询商户 赠品结果VO")
public class MerchantGiftVo{

    @ApiModelProperty("店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty("自提送赠品活动id")
    private Long pickupId;
    @ApiModelProperty("giftId")
    private Long giftId;
    @ApiModelProperty("赠送数量")
    private Integer number;
}
