package com.meiyuan.catering.order.dto.splitbill;

import com.meiyuan.catering.order.enums.TradeStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/10/10 09:10
 * @description 分账提现流水状态修改DTO
 **/

@Data
@ApiModel("分账提现流水状态修改DTO")
public class WithdrawFlowStatusUpdateDTO {

    @ApiModelProperty(value = "主键ID")
    private Long id;
    @ApiModelProperty(value = "第三方交易流水编号")
    private String thirdTradeNumber;
    @ApiModelProperty(value = "交易状态（0：未交易；1：交易中；2：交易成功；3：交易失败）")
    private Integer tradeStatus;
    @ApiModelProperty(value = "失败原因")
    private String failMessage;
    @ApiModelProperty(value = "成功时间")
    private LocalDateTime successTime;
    @ApiModelProperty(value = "请求参数信息")
    private String requestMessage;
    @ApiModelProperty(value = "通知参数信息")
    private String notifyMessage;

}
