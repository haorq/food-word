package com.meiyuan.catering.core.dto.base.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Description 设备订单通知
 * @Date 2020/3/24 0024 14:03
 */
@Data
public class DeviceNoticeInfoDTO {

    @ApiModelProperty(value = "是否开启新订单语音提醒：false:否，true：是")
    private Boolean voiceRemind;
    @ApiModelProperty(value = "夜间是否接收语音提示：true：是，false：否")
    private Boolean nightClose;
    @ApiModelProperty(value = "启用禁用状态：1：启用，2：禁用")
    private Integer status;
    @ApiModelProperty(value = "每次打印份数")
    private Integer printingTimes;
    @ApiModelProperty(value = "是否自动打印小票:true:是，false：否")
    private Boolean autoPrint;
    @ApiModelProperty(value = "1-外卖单 2-厨打单 3-全部")
    private Integer ticketType;
    @ApiModelProperty("是否有骑手取消订单：true：是，false：否")
    private Boolean haveCancelOrder;

    @ApiModelProperty(value = "店铺通知订单信息")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> orderIds;
}
