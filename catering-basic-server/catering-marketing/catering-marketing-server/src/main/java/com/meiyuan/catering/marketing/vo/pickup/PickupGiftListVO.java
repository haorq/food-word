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
@ApiModel("自提送赠品列表VO")
public class PickupGiftListVO extends IdEntity {
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("赠品名称")
    private String giftName;

    @ApiModelProperty("赠品id")
    private Long giftId;

    @ApiModelProperty("参与商家")
    private String shopName;
}
