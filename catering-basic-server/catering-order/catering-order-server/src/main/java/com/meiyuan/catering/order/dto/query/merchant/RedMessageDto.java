package com.meiyuan.catering.order.dto.query.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lh
 * @date 2020-07-10
 * @version v1.2.0
 */
@Data
@ApiModel("是否有红点消息-商户")
public class RedMessageDto {
    @ApiModelProperty("消息类型。1：未处理订单，2：未处理退款单")
    private Integer messageType;
    @ApiModelProperty("红点数量。0：不展示红点，大于0：红点个数")
    private int showRed;

    public RedMessageDto(Integer messageType, int showRed) {
        this.messageType = messageType;
        this.showRed = showRed;
    }
}
