package com.meiyuan.catering.marketing.vo.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName TicketGoodsVO
 * @Description
 * @Author gz
 * @Date 2020/8/10 10:34
 * @Version 1.3.0
 */
@Data
public class TicketGoodsVO {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "商品编码")
    private String code;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
}
