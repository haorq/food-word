package com.meiyuan.catering.marketing.vo.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName TicketActivityShopVO
 * @Description
 * @Author gz
 * @Date 2020/8/5 15:23
 * @Version 1.3.0
 */
@Data
public class TicketActivityShopVO {
    @ApiModelProperty(value = "店铺ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "店铺编码")
    private String shopCode;
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    @ApiModelProperty(value = "店铺负责人")
    private String primaryPersonName;
    @ApiModelProperty(value = "店铺负责人联系电话")
    private String registerPhone;
    @ApiModelProperty(value = "店铺地址")
    private String addressFull;
    private Integer status;
}
