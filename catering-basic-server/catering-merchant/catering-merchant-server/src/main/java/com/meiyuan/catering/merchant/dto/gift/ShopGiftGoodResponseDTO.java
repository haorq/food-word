package com.meiyuan.catering.merchant.dto.gift;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @Author MeiTao
 * @Description 店铺赠品信息
 * @Date  2020/3/22 0022 18:15
 */
@Data
@ApiModel("店铺赠品信息")
public class ShopGiftGoodResponseDTO {
    @ApiModelProperty(value = "店铺赠品表id(若id存在则为修改不存在则为添加)")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "赠品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long giftId;
    @ApiModelProperty(value = "自提送赠品活动id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pickupId;

    @ApiModelProperty(value = "赠品名称")
    private String giftName;
    @ApiModelProperty(value = "赠品图片")
    private String giftPicture;
    @ApiModelProperty(value = "赠品价值")
    private BigDecimal giftPrice;

    @ApiModelProperty(value = "赠品库存")
    private Integer giftRemainQuantity;

    @ApiModelProperty(value = "赠送数量")
    private Integer number;

}
