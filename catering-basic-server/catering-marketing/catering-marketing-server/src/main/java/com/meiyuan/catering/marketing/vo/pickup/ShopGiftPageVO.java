package com.meiyuan.catering.marketing.vo.pickup;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mt
 * @date 2020/3/16
 **/
@Data
@ApiModel("赠品活动参与商户信息VO")
public class ShopGiftPageVO {
    @ApiModelProperty(value = "店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @ApiModelProperty(value = "赠品名称")
    private String shopName;

    @ApiModelProperty(value = "门店联系电话")
    private String registerPhone;

    @ApiModelProperty(value = "门店联系人/注册电话")
    private String primaryPersonName;

    @ApiModelProperty(value = "赠品库存(赠品活动添加库存)")
    private Integer giftStock;

}
