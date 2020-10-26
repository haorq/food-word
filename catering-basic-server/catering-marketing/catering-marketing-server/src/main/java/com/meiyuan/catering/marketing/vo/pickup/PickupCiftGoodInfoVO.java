package com.meiyuan.catering.marketing.vo.pickup;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author mt
 * @date 2020/3/16
 **/
@Data
@ApiModel("自提活动商品信息VO")
public class PickupCiftGoodInfoVO extends IdEntity {

    @ApiModelProperty("赠品数量")
    private Integer giftQuantity;
    @ApiModelProperty("赠品名字")
    private String giftName;

}
