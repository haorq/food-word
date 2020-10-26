package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhm
 * @date 2020/7/8
 * @description
 **/
@Data
@ApiModel("商户商品上下架")
public class MerchantGoodsUpOrDownDTO {
    @ApiModelProperty(value = "1-下架,2-上架")
    private Integer merchantGoodsStatus;
    @ApiModelProperty("商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String goodsId;
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
}
