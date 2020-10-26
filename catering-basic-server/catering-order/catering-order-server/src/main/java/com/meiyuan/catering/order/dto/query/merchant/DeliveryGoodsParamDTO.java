package com.meiyuan.catering.order.dto.query.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 待核销商品明细参数——商户端
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("待核销商品明细参数——商户端")
public class DeliveryGoodsParamDTO {
    @ApiModelProperty("时间，0：今日待核销商品；1：明日待核销商品")
    private Integer time;
    @ApiModelProperty(value = "【非填-系统自行填充】商户ID", hidden = true)
    private Long merchantId;
    @ApiModelProperty(value = "【非填-系统自行填充】门店ID", hidden = true)
    private Long shopId;
    @ApiModelProperty(value = "【非填-系统自行填充】类型：1--店铺，2--自提点，3--既是店铺也是自提点", hidden = true)
    private Integer type;
}
