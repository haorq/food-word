package com.meiyuan.catering.marketing.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName TicketVerifyActivityNameDTO
 * @Description
 * @Author gz
 * @Date 2020/8/19 11:18
 * @Version 1.3.0
 */
@Data
public class TicketVerifyActivityNameDTO {

    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "名称")
    private String activityName;
    @ApiModelProperty(value = "活动类型：1-店内领券；2-店外发券")
    private Integer activityType;
}
