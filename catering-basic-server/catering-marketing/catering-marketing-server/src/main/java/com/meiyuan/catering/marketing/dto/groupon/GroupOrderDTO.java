package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author luohuan
 * @date 2020/3/27
 **/
@Data
@ApiModel("团单DTO")
public class GroupOrderDTO extends GroupMemberCommonDTO{

    @ApiModelProperty("关联ID")
    private Long ofId;

    @ApiModelProperty("关联ID归属类型:1-秒杀；2-拼团；3-团购；4-优惠券；")
    private Integer ofType;

    @ApiModelProperty("营销商品表主键id")
    private Long mGoodsId;

    @ApiModelProperty("发起拼团时间(订单支付成功的时间)")
    private LocalDateTime groupStartTime;

    @ApiModelProperty(value = "起团数量")
    private Integer minGrouponQuantity;

    @ApiModelProperty("商品购买数量")
    private Integer goodsNumber;

}
