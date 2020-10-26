package com.meiyuan.catering.marketing.vo.pickup;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author mt
 * @date 2020/3/16
 **/
@Data
@ApiModel("自提送赠品活动推送给商户的赠品VO")
public class ShopGiftVO{
    @ApiModelProperty(value = "赠品活动id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pickupId;
    @ApiModelProperty(value = "赠品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long giftId;

    @ApiModelProperty(value = "赠品名称")
    private String giftName;

    @ApiModelProperty(value = "赠品价值")
    private BigDecimal giftPrice;

    @ApiModelProperty(value = "赠品图片")
    private String giftPicture;

    @ApiModelProperty(value = "赠品库存(赠品活动剩余库存)")
    private Long giftStock;

    @ApiModelProperty(value = "赠送数量")
    private Integer number;

    @ApiModelProperty(value = "是否已添加:true:是，false:否")
    private Boolean type;
}
