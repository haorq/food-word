package com.meiyuan.catering.order.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("打印请求参数")
public class PrintPaperDTO {

    @ApiModelProperty(value = "订单id")
    private List<String> orderIds;

    @ApiModelProperty(value = "小票类型:(1:配送单,2:自取单,3:厨打单)")
    private int type;

}
