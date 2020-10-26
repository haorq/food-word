package com.meiyuan.catering.wx.dto.marketing;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName UserTicketParamDTO
 * @Description
 * @Author gz
 * @Date 2020/3/31 11:23
 * @Version 1.1
 */
@Data
public class UserTicketParamDTO extends BasePageDTO {
    @ApiModelProperty(value = "状态：1-待使用；2-已使用；3-已过期")
    private Integer status;

    @ApiModelProperty(value = "发券方：1-行膳平台；3-品牌")
    private Integer sendTicketParty;
}
