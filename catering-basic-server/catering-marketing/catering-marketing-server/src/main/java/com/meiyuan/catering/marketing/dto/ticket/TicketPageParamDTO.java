package com.meiyuan.catering.marketing.dto.ticket;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName TicketPageParamDTO
 * @Description 优惠券分页列表查询参数
 * @Author gz
 * @Date 2020/3/19 10:09
 * @Version 1.1
 */
@Data
public class TicketPageParamDTO extends BasePageDTO {
    @ApiModelProperty(value = "创建时间开始")
    private LocalDateTime createTimeBegin;
    @ApiModelProperty(value = "创建时间结束")
    private LocalDateTime createTimeEnd;
    @ApiModelProperty(value = "优惠券名称")
    private String ticketName;
    @ApiModelProperty(value = "优惠券类型：1-满减券；2-代金券")
    private Integer childType;


}
