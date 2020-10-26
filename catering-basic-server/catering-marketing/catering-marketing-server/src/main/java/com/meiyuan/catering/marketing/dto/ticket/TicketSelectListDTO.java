package com.meiyuan.catering.marketing.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName TicketSelectListDTO
 * @Description 优惠券选择列表DTO
 * @Author gz
 * @Date 2020/3/19 17:01
 * @Version 1.1
 */
@Data
public class TicketSelectListDTO {

    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String ticketName;
    @ApiModelProperty(value = "编码")
    private String ticketCode;
}
