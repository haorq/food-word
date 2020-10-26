package com.meiyuan.catering.marketing.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName MarketingTicketDetailsShopParamDTO
 * @Description
 * @Author gz
 * @Date 2020/8/17 10:37
 * @Version 1.3.0
 */
@Data
public class MarketingTicketDetailsShopParamDTO extends BasePageDTO {
    @ApiModelProperty(value = "详情ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
}
