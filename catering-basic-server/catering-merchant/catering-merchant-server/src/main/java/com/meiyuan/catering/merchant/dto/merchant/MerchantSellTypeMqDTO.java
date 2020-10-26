package com.meiyuan.catering.merchant.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 商户添加参数DTO
 * @Date  2020/3/12 0012 16:20
 */
@Data
@ApiModel("商户添加参数DTO")
public class MerchantSellTypeMqDTO {

    @ApiModelProperty("消息中心项目Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("售卖模式 ： 1-菜单售卖模式 2-商品售卖模式")
    private Integer sellType;
}
